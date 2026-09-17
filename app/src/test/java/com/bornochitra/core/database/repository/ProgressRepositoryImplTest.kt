package com.bornochitra.core.database.repository

import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProgressRepositoryImplTest {

    private fun repositoryWith(rows: List<ExerciseProgressEntity>): ProgressRepositoryImpl {
        val fakeDao = object : ExerciseProgressDao {
            override fun observeAll(): Flow<List<ExerciseProgressEntity>> = flowOf(rows)
        }
        return ProgressRepositoryImpl(fakeDao)
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
}
