package com.bornochitra.core.database.repository

import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Records every session/progress row a [FakePracticeSessionDao] was asked to write, for assertions. */
private class FakePracticeSessionDao(private var progress: ExerciseProgressEntity?) : PracticeSessionDao {
    val insertedSessions = mutableListOf<PracticeSessionEntity>()
    val upsertedProgress = mutableListOf<ExerciseProgressEntity>()

    override suspend fun insertSession(session: PracticeSessionEntity): Long {
        insertedSessions += session
        return insertedSessions.size.toLong()
    }

    override suspend fun getProgress(exerciseId: String): ExerciseProgressEntity? = progress

    override suspend fun upsertProgress(progress: ExerciseProgressEntity) {
        this.progress = progress
        upsertedProgress += progress
    }
}

class ProgressRepositoryImplTest {

    private fun repositoryWith(
        rows: List<ExerciseProgressEntity>,
        practiceSessionDao: PracticeSessionDao = FakePracticeSessionDao(progress = null),
    ): ProgressRepositoryImpl {
        val fakeDao = object : ExerciseProgressDao {
            override fun observeAll(): Flow<List<ExerciseProgressEntity>> = flowOf(rows)
        }
        return ProgressRepositoryImpl(fakeDao, practiceSessionDao)
    }

    private fun progress(
        exerciseId: String,
        isMastered: Boolean,
        lastPracticedAt: Long = 0L,
    ) = ExerciseProgressEntity(
        exerciseId = exerciseId,
        attemptCount = 1,
        completedCount = 1,
        bestScore = 90f,
        lastScore = 90f,
        scoreLevel = "PERFECT",
        lastPracticedAt = lastPracticedAt,
        isMastered = isMastered,
    )

    @Test
    fun `no progress rows yields all zero progress and no continue exercise`() = runTest {
        val result = repositoryWith(emptyList()).observeProgress().first()

        assertEquals(0f, result.overallProgress)
        assertEquals(0f, result.vowelProgress)
        assertEquals(0f, result.consonantProgress)
        assertEquals(0f, result.drawingProgress)
        assertNull(result.continueExerciseId)
    }

    @Test
    fun `progress is grouped by exerciseId category prefix`() = runTest {
        val rows = listOf(
            progress("vowel-o", isMastered = true),
            progress("vowel-a", isMastered = false),
            progress("consonant-ko", isMastered = true),
            progress("drawing-circle", isMastered = false),
        )

        val result = repositoryWith(rows).observeProgress().first()

        assertEquals(0.5f, result.vowelProgress)
        assertEquals(1f, result.consonantProgress)
        assertEquals(0f, result.drawingProgress)
        assertEquals(0.5f, result.overallProgress)
    }

    @Test
    fun `continue exercise is the most recently practiced unmastered exercise`() = runTest {
        val rows = listOf(
            progress("vowel-o", isMastered = false, lastPracticedAt = 100L),
            progress("vowel-a", isMastered = false, lastPracticedAt = 200L),
            progress("consonant-ko", isMastered = true, lastPracticedAt = 300L),
        )

        val result = repositoryWith(rows).observeProgress().first()

        assertEquals("vowel-a", result.continueExerciseId)
    }

    @Test
    fun `fully mastered exercises never appear as the continue exercise`() = runTest {
        val rows = listOf(progress("vowel-o", isMastered = true, lastPracticedAt = 100L))

        val result = repositoryWith(rows).observeProgress().first()

        assertNull(result.continueExerciseId)
    }

    @Test
    fun `exercise progress is keyed by id and filtered to the requested ids`() = runTest {
        val rows = listOf(
            progress("vowel-o", isMastered = true),
            progress("vowel-aa", isMastered = false),
            progress("consonant-ko", isMastered = true),
        )

        val result = repositoryWith(rows).observeExerciseProgress(listOf("vowel-o", "vowel-aa")).first()

        assertEquals(setOf("vowel-o", "vowel-aa"), result.keys)
        assertEquals(true, result["vowel-o"]?.isMastered)
        assertEquals(false, result["vowel-aa"]?.isMastered)
    }

    @Test
    fun `exercise progress omits ids with no recorded progress`() = runTest {
        val result = repositoryWith(emptyList()).observeExerciseProgress(listOf("vowel-o")).first()

        assertEquals(emptyMap<String, Any>(), result)
    }

    @Test
    fun `first practice result starts a fresh progress row`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        repository.savePracticeResult(
            PracticeResult(
                exerciseId = "vowel-o",
                score = 92f,
                scoreLevel = ScoreLevel.PERFECT,
                completed = true,
                durationMs = 5_000L,
                completedAtMs = 1_000L,
            ),
        )

        val savedProgress = practiceSessionDao.upsertedProgress.single()
        assertEquals(1, savedProgress.attemptCount)
        assertEquals(1, savedProgress.completedCount)
        assertEquals(92f, savedProgress.bestScore)
        assertEquals(92f, savedProgress.lastScore)
        assertEquals("PERFECT", savedProgress.scoreLevel)
        assertEquals(1_000L, savedProgress.lastPracticedAt)
        assertTrue(practiceSessionDao.insertedSessions.single().exerciseId == "vowel-o")
    }

    @Test
    fun `later practice result accumulates attempts and keeps the best score`() = runTest {
        val existing = progress("vowel-o", isMastered = false, lastPracticedAt = 500L).copy(
            attemptCount = 2,
            completedCount = 1,
            bestScore = 95f,
        )
        val practiceSessionDao = FakePracticeSessionDao(progress = existing)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        repository.savePracticeResult(
            PracticeResult(
                exerciseId = "vowel-o",
                score = 70f,
                scoreLevel = ScoreLevel.MEDIUM,
                completed = true,
                durationMs = 4_000L,
                completedAtMs = 1_500L,
            ),
        )

        val savedProgress = practiceSessionDao.upsertedProgress.single()
        assertEquals(3, savedProgress.attemptCount)
        assertEquals(2, savedProgress.completedCount)
        assertEquals(95f, savedProgress.bestScore)
        assertEquals(70f, savedProgress.lastScore)
        assertEquals("MEDIUM", savedProgress.scoreLevel)
    }

    @Test
    fun `save practice result returns the new session id`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        val sessionId = repository.savePracticeResult(
            PracticeResult(
                exerciseId = "vowel-o",
                score = 92f,
                scoreLevel = ScoreLevel.PERFECT,
                completed = true,
                durationMs = 5_000L,
                completedAtMs = 1_000L,
            ),
        )

        assertEquals(1L, sessionId)
    }
}
