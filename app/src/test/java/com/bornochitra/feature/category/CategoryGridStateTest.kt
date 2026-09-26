package com.bornochitra.feature.category

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.Stroke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryGridStateTest {

    private fun exercise(id: String, title: String = id) = Exercise(
        id = id,
        title = title,
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = listOf(Point(10f, 10f), Point(90f, 90f)))),
        order = 0,
    )

    private fun progress(
        exerciseId: String,
        attemptCount: Int,
        completedCount: Int,
        bestScore: Float,
        isMastered: Boolean = false,
    ) = ExerciseProgress(
        exerciseId = exerciseId,
        attemptCount = attemptCount,
        completedCount = completedCount,
        bestScore = bestScore,
        lastScore = bestScore,
        lastPracticedAt = 0L,
        isMastered = isMastered,
    )

    private val exercises = listOf(
        exercise("o", "অ"),
        exercise("aa", "আ"),
        exercise("i", "ই"),
        exercise("ii", "ঈ"),
        exercise("u", "উ"),
    )

    private val progressById = mapOf(
        "o" to progress("o", attemptCount = 4, completedCount = 3, bestScore = 95f, isMastered = true),
        "aa" to progress("aa", attemptCount = 3, completedCount = 2, bestScore = 92f),
        "i" to progress("i", attemptCount = 2, completedCount = 0, bestScore = 40f),
        "ii" to progress("ii", attemptCount = 1, completedCount = 1, bestScore = 70f),
    )

    @Test
    fun `each tile shows its state, attempts and best-score stars`() {
        val items = categoryGridState(exercises, progressById, continueExerciseId = null).items

        assertEquals(
            listOf(LearningState.MASTERED, LearningState.COMPLETED, LearningState.PRACTICING, LearningState.COMPLETED, LearningState.NOT_STARTED),
            items.map { it.learningState },
        )
        assertEquals(listOf(4, 3, 2, 1, 0), items.map { it.attemptCount })
        assertEquals(listOf(3, 3, 1, 2, 0), items.map { it.stars })
        assertEquals(exercises.map { it.strokes }, items.map { it.strokes })
    }

    @Test
    fun `only the continue exercise is marked continue here`() {
        val items = categoryGridState(exercises, progressById, continueExerciseId = "aa").items

        assertEquals(listOf("aa"), items.filter { it.isContinueHere }.map { it.id })
    }

    @Test
    fun `a continue exercise from another category marks no tile`() {
        val items = categoryGridState(exercises, progressById, continueExerciseId = "consonant-ko").items

        assertFalse(items.any { it.isContinueHere })
    }

    @Test
    fun `the header counts learned, done and running tiles and measures finished ones`() {
        val state = categoryGridState(exercises, progressById, continueExerciseId = null)

        assertEquals(1, state.learnedCount)
        assertEquals(2, state.doneCount)
        assertEquals(1, state.runningCount)
        assertEquals(3 / 5f, state.progress)
    }

    @Test
    fun `a single unfinished attempt counts as running`() {
        val state = categoryGridState(
            exercises = listOf(exercise("o")),
            progressByExerciseId = mapOf("o" to progress("o", attemptCount = 1, completedCount = 0, bestScore = 20f)),
            continueExerciseId = null,
        )

        assertEquals(LearningState.STARTED, state.items.single().learningState)
        assertEquals(1, state.runningCount)
        assertEquals(0f, state.progress)
    }

    @Test
    fun `an empty category has no progress`() {
        assertEquals(CategoryGridState(), categoryGridState(emptyList(), emptyMap(), continueExerciseId = null))
    }

    @Test
    fun `the grid follows Home's continue exercise`() = runTest {
        val learningProgress = MutableStateFlow(LearningProgress(continueExerciseId = "i"))
        val exerciseRepository = object : ExerciseRepository {
            override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> = flowOf(exercises)
            override suspend fun getExercise(id: String): Exercise? = null
        }
        val progressRepository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = learningProgress
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> = flowOf(progressById)
            override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
            override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
        }

        val grid = observeCategoryGrid(ExerciseType.VOWEL, exerciseRepository, progressRepository).first()

        assertTrue(grid.items.single { it.id == "i" }.isContinueHere)
    }
}
