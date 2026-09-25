package com.bornochitra.feature.math

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.Stroke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MathViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun exercise(id: String, title: String, order: Int) = Exercise(
        id = id,
        title = title,
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = order,
    )

    private fun progress(exerciseId: String, attemptCount: Int, completedCount: Int, isMastered: Boolean) =
        ExerciseProgress(
            exerciseId = exerciseId,
            attemptCount = attemptCount,
            completedCount = completedCount,
            bestScore = 0f,
            lastScore = 0f,
            lastPracticedAt = 0L,
            isMastered = isMastered,
        )

    private var requestedType: ExerciseType? = null

    private fun exerciseRepositoryOf(exercises: List<Exercise>) = object : ExerciseRepository {
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> {
            requestedType = type
            return flowOf(exercises)
        }

        override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
    }

    private fun progressRepositoryOf(progressByExerciseId: Map<String, ExerciseProgress>) = object : ProgressRepository {
        override fun observeProgress() = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(progressByExerciseId.filterKeys { it in exerciseIds })
        override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
    }

    @Test
    fun `ui state lists the math exercises with a status derived from their progress`() = runTest(dispatcher) {
        val exercises = listOf(
            exercise("math-1", "1", order = 1),
            exercise("math-12", "12", order = 12),
            exercise("math-20", "20", order = 20),
            exercise("math-plus", "+", order = 21),
        )
        val progressByExerciseId = mapOf(
            "math-1" to progress("math-1", attemptCount = 1, completedCount = 1, isMastered = false),
            "math-12" to progress("math-12", attemptCount = 2, completedCount = 0, isMastered = false),
            "math-20" to progress("math-20", attemptCount = 5, completedCount = 3, isMastered = true),
        )

        val viewModel = MathViewModel(
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(ExerciseType.MATH, requestedType)
        assertEquals(
            listOf(
                MathListItem(id = "math-1", title = "1", learningState = LearningState.COMPLETED, attemptCount = 1),
                MathListItem(id = "math-12", title = "12", learningState = LearningState.PRACTICING, attemptCount = 2),
                MathListItem(id = "math-20", title = "20", learningState = LearningState.MASTERED, attemptCount = 5),
                MathListItem(id = "math-plus", title = "+"),
            ),
            viewModel.uiState.value.exercises,
        )
    }

    @Test
    fun `default ui state has no exercises`() = runTest(dispatcher) {
        val viewModel = MathViewModel(
            exerciseRepository = exerciseRepositoryOf(emptyList()),
            progressRepository = progressRepositoryOf(emptyMap()),
        )

        assertEquals(MathState(), viewModel.uiState.value)
    }
}
