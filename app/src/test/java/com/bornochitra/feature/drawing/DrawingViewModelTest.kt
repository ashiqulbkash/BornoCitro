package com.bornochitra.feature.drawing

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
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
class DrawingViewModelTest {

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
        type = ExerciseType.DRAWING,
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

    private fun exerciseRepositoryOf(exercises: List<Exercise>) = object : ExerciseRepository {
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> = flowOf(exercises)
        override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
    }

    private fun progressRepositoryOf(progressByExerciseId: Map<String, ExerciseProgress>) = object : ProgressRepository {
        override fun observeProgress() = MutableStateFlow(com.bornochitra.core.model.LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(progressByExerciseId.filterKeys { it in exerciseIds })
        override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
        override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? = null
        override suspend fun getRecentResults(
            exerciseId: String,
            limit: Int,
        ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
    }

    @Test
    fun `ui state maps each exercise to a status derived from its progress`() = runTest(dispatcher) {
        val exercises = listOf(
            exercise("drawing-line", "Line", order = 1),
            exercise("drawing-circle", "Circle", order = 2),
            exercise("drawing-square", "Square", order = 3),
            exercise("drawing-triangle", "Triangle", order = 4),
        )
        val progressByExerciseId = mapOf(
            "drawing-line" to progress("drawing-line", attemptCount = 1, completedCount = 1, isMastered = false),
            "drawing-circle" to progress("drawing-circle", attemptCount = 2, completedCount = 0, isMastered = false),
            "drawing-square" to progress("drawing-square", attemptCount = 5, completedCount = 3, isMastered = true),
        )

        val viewModel = DrawingViewModel(
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val items = viewModel.uiState.value.exercises
        assertEquals(4, items.size)
        assertEquals(DrawingListItem(id = "drawing-line", title = "Line", learningState = LearningState.COMPLETED, attemptCount = 1), items[0])
        assertEquals(DrawingListItem(id = "drawing-circle", title = "Circle", learningState = LearningState.PRACTICING, attemptCount = 2), items[1])
        assertEquals(DrawingListItem(id = "drawing-square", title = "Square", learningState = LearningState.MASTERED, attemptCount = 5), items[2])
        assertEquals(DrawingListItem(id = "drawing-triangle", title = "Triangle"), items[3])
    }

    @Test
    fun `default ui state has no exercises`() = runTest(dispatcher) {
        val viewModel = DrawingViewModel(
            exerciseRepository = exerciseRepositoryOf(emptyList()),
            progressRepository = progressRepositoryOf(emptyMap()),
        )

        assertEquals(DrawingState(), viewModel.uiState.value)
    }
}
