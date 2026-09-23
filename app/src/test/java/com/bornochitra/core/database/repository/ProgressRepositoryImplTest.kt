package com.bornochitra.core.database.repository

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
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

    override suspend fun getSession(sessionId: Long): PracticeSessionEntity? =
        insertedSessions.getOrNull(sessionId.toInt() - 1)?.copy(id = sessionId)

    // Mirrors the real query: newest first, the insertion order breaking ties between equal timestamps.
    override suspend fun getRecentSessions(exerciseId: String, limit: Int): List<PracticeSessionEntity> =
        insertedSessions.withIndex()
            .filter { it.value.exerciseId == exerciseId }
            .sortedWith(compareByDescending<IndexedValue<PracticeSessionEntity>> { it.value.createdAt }.thenByDescending { it.index })
            .take(limit)
            .map { it.value.copy(id = it.index + 1L) }

    override suspend fun getProgress(exerciseId: String): ExerciseProgressEntity? = progress

    override suspend fun upsertProgress(progress: ExerciseProgressEntity) {
        this.progress = progress
        upsertedProgress += progress
    }
}

class ProgressRepositoryImplTest {

    private fun exercise(id: String, type: ExerciseType) = Exercise(
        id = id,
        title = id,
        type = type,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = 1,
    )

    /** Two vowels, one consonant and one drawing, so a category ratio has a known denominator. */
    private val catalogue = listOf(
        exercise("vowel-o", ExerciseType.VOWEL),
        exercise("vowel-a", ExerciseType.VOWEL),
        exercise("consonant-ko", ExerciseType.CONSONANT),
        exercise("drawing-circle", ExerciseType.DRAWING),
    )

    private fun repositoryWith(
        rows: List<ExerciseProgressEntity>,
        practiceSessionDao: PracticeSessionDao = FakePracticeSessionDao(progress = null),
        catalogue: List<Exercise> = this.catalogue,
        masteryRule: MasteryRule = MasteryRule(),
    ): ProgressRepositoryImpl {
        val fakeDao = object : ExerciseProgressDao {
            override fun observeAll(): Flow<List<ExerciseProgressEntity>> = flowOf(rows)
        }
        val exerciseRepository = object : ExerciseRepository {
            override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
                flowOf(catalogue.filter { it.type == type })

            override suspend fun getExercise(id: String): Exercise? = catalogue.find { it.id == id }
        }
        return ProgressRepositoryImpl(fakeDao, practiceSessionDao, exerciseRepository, masteryRule)
    }

    private fun progress(
        exerciseId: String,
        isMastered: Boolean = false,
        completedCount: Int = 1,
        lastPracticedAt: Long = 0L,
    ) = ExerciseProgressEntity(
        exerciseId = exerciseId,
        attemptCount = 1,
        completedCount = completedCount,
        bestScore = 90f,
        lastScore = 90f,
        scoreLevel = "PERFECT",
        lastPracticedAt = lastPracticedAt,
        isMastered = isMastered,
    )

    private fun practiceResult(
        score: Float,
        scoreLevel: ScoreLevel = ScoreLevel.PERFECT,
        completed: Boolean = true,
        completedAtMs: Long = 0L,
    ) = PracticeResult(
        exerciseId = "vowel-o",
        score = score,
        scoreLevel = scoreLevel,
        completed = completed,
        durationMs = 5_000L,
        completedAtMs = completedAtMs,
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
    fun `progress is the share of each category's exercises completed at least once`() = runTest {
        val rows = listOf(
            progress("vowel-o"),
            progress("consonant-ko"),
            progress("drawing-circle", completedCount = 0),
        )

        val result = repositoryWith(rows).observeProgress().first()

        assertEquals(0.5f, result.vowelProgress)
        assertEquals(1f, result.consonantProgress)
        assertEquals(0f, result.drawingProgress)
        assertEquals(0.5f, result.overallProgress)
    }

    @Test
    fun `an attempt that was never completed does not count towards progress`() = runTest {
        val rows = listOf(progress("vowel-o", completedCount = 0))

        val result = repositoryWith(rows).observeProgress().first()

        assertEquals(0f, result.vowelProgress)
        assertEquals(0f, result.overallProgress)
    }

    @Test
    fun `progress is measured against the whole catalogue, not only what was attempted`() = runTest {
        val rows = listOf(progress("vowel-o"), progress("vowel-a"))

        val result = repositoryWith(rows).observeProgress().first()

        assertEquals(1f, result.vowelProgress)
        assertEquals(0.5f, result.overallProgress)
    }

    @Test
    fun `english small letters have their own ratio and count towards overall progress`() = runTest {
        val catalogue = this@ProgressRepositoryImplTest.catalogue + listOf(
            exercise("english-small-a", ExerciseType.ENGLISH_SMALL),
            exercise("english-small-b", ExerciseType.ENGLISH_SMALL),
        )
        val rows = listOf(progress("english-small-a"), progress("vowel-o"))

        val result = repositoryWith(rows, catalogue = catalogue).observeProgress().first()

        assertEquals(0.5f, result.englishSmallProgress)
        assertEquals(0.5f, result.vowelProgress)
        assertEquals(2f / 6f, result.overallProgress)
    }

    @Test
    fun `english capital letters have their own ratio and count towards overall progress`() = runTest {
        val catalogue = this@ProgressRepositoryImplTest.catalogue + listOf(
            exercise("english-small-a", ExerciseType.ENGLISH_SMALL),
            exercise("english-capital-a", ExerciseType.ENGLISH_CAPITAL),
            exercise("english-capital-b", ExerciseType.ENGLISH_CAPITAL),
            exercise("english-capital-c", ExerciseType.ENGLISH_CAPITAL),
            exercise("english-capital-d", ExerciseType.ENGLISH_CAPITAL),
        )
        val rows = listOf(progress("english-capital-a"), progress("english-small-a"))

        val result = repositoryWith(rows, catalogue = catalogue).observeProgress().first()

        assertEquals(0.25f, result.englishCapitalProgress)
        assertEquals(1f, result.englishSmallProgress)
        assertEquals(2f / 9f, result.overallProgress)
    }

    @Test
    fun `math has its own ratio and counts towards overall progress`() = runTest {
        val catalogue = this@ProgressRepositoryImplTest.catalogue + listOf(
            exercise("math-1", ExerciseType.MATH),
            exercise("math-12", ExerciseType.MATH),
            exercise("math-plus", ExerciseType.MATH),
            exercise("math-equals", ExerciseType.MATH),
        )
        val rows = listOf(progress("math-12"), progress("math-plus"), progress("vowel-o"))

        val result = repositoryWith(rows, catalogue = catalogue).observeProgress().first()

        assertEquals(0.5f, result.mathProgress)
        assertEquals(3f / 8f, result.overallProgress)
    }

    @Test
    fun `an empty category is zero rather than undefined`() = runTest {
        val result = repositoryWith(rows = emptyList(), catalogue = emptyList()).observeProgress().first()

        assertEquals(0f, result.vowelProgress)
        assertEquals(0f, result.overallProgress)
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

    @Test
    fun `a saved practice result can be read back by its session id`() = runTest {
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = FakePracticeSessionDao(progress = null))
        val saved = PracticeResult(
            exerciseId = "vowel-o",
            score = 92f,
            scoreLevel = ScoreLevel.PERFECT,
            completed = true,
            durationMs = 5_000L,
            completedAtMs = 1_000L,
        )
        val sessionId = repository.savePracticeResult(saved)

        assertEquals(saved, repository.getPracticeResult(sessionId))
    }

    @Test
    fun `mastery is awarded once the rule is met, not before`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        repeat(3) { attempt ->
            repository.savePracticeResult(practiceResult(score = 92f, completedAtMs = attempt.toLong()))
        }

        assertEquals(
            listOf(false, false, true),
            practiceSessionDao.upsertedProgress.map { it.isMastered },
        )
    }

    @Test
    fun `completing often but never well enough is not mastery`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        repeat(5) { attempt ->
            repository.savePracticeResult(
                practiceResult(score = 70f, scoreLevel = ScoreLevel.MEDIUM, completedAtMs = attempt.toLong()),
            )
        }

        assertTrue(practiceSessionDao.upsertedProgress.none { it.isMastered })
    }

    @Test
    fun `attempts that were not completed do not count towards mastery`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)

        repeat(4) { attempt ->
            repository.savePracticeResult(
                practiceResult(score = 95f, completed = false, completedAtMs = attempt.toLong()),
            )
        }

        assertTrue(practiceSessionDao.upsertedProgress.none { it.isMastered })
    }

    @Test
    fun `mastery once earned is not taken away by a weaker attempt`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = practiceSessionDao)
        repeat(3) { attempt ->
            repository.savePracticeResult(practiceResult(score = 92f, completedAtMs = attempt.toLong()))
        }

        repository.savePracticeResult(
            practiceResult(score = 20f, scoreLevel = ScoreLevel.LOW, completedAtMs = 10L),
        )

        assertTrue(practiceSessionDao.upsertedProgress.last().isMastered)
    }

    @Test
    fun `a gentler mastery rule awards mastery sooner`() = runTest {
        val practiceSessionDao = FakePracticeSessionDao(progress = null)
        val repository = repositoryWith(
            rows = emptyList(),
            practiceSessionDao = practiceSessionDao,
            masteryRule = MasteryRule(requiredCompletions = 1, minBestScore = 60f),
        )

        repository.savePracticeResult(practiceResult(score = 65f, scoreLevel = ScoreLevel.MEDIUM))

        assertTrue(practiceSessionDao.upsertedProgress.single().isMastered)
    }

    @Test
    fun `recent results are the latest attempts, newest first, up to the limit`() = runTest {
        val repository = repositoryWith(rows = emptyList())
        listOf(50f, 60f, 70f, 80f).forEachIndexed { index, score ->
            repository.savePracticeResult(practiceResult(score = score, completedAtMs = index * 1_000L))
        }

        val recent = repository.getRecentResults("vowel-o", limit = 3)

        assertEquals(listOf(80f, 70f, 60f), recent.map { it.score })
    }

    @Test
    fun `recent results only cover the requested exercise`() = runTest {
        val repository = repositoryWith(rows = emptyList())
        repository.savePracticeResult(practiceResult(score = 90f, completedAtMs = 1L))
        repository.savePracticeResult(practiceResult(score = 40f, completedAtMs = 2L).copy(exerciseId = "vowel-a"))

        assertEquals(listOf(90f), repository.getRecentResults("vowel-o", limit = 5).map { it.score })
    }

    @Test
    fun `attempts saved at the same moment still come back in the order they were saved`() = runTest {
        val repository = repositoryWith(rows = emptyList())
        repository.savePracticeResult(practiceResult(score = 61f, completedAtMs = 5L))
        repository.savePracticeResult(practiceResult(score = 62f, completedAtMs = 5L))

        assertEquals(listOf(62f, 61f), repository.getRecentResults("vowel-o", limit = 2).map { it.score })
    }

    @Test
    fun `an exercise with no attempts has no recent results`() = runTest {
        assertEquals(emptyList<PracticeResult>(), repositoryWith(rows = emptyList()).getRecentResults("vowel-o", limit = 3))
    }

    @Test
    fun `an unknown session id has no practice result`() = runTest {
        val repository = repositoryWith(rows = emptyList(), practiceSessionDao = FakePracticeSessionDao(progress = null))

        assertNull(repository.getPracticeResult(404L))
    }
}
