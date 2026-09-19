package com.bornochitra.feature.practice

import androidx.lifecycle.SavedStateHandle
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.tips.TipRules
import com.bornochitra.core.tips.TipSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PracticeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val exercise = Exercise(
        id = "vowel-o",
        title = "অ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "vowel-o-stroke", points = emptyList())),
        order = 1,
    )

    private fun exerciseRepositoryOf(exercises: List<Exercise>) = object : ExerciseRepository {
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> = flowOf(exercises)
        override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
    }

    private class FakeProgressRepository(
        private val progressById: Map<String, ExerciseProgress> = emptyMap(),
    ) : ProgressRepository {
        val savedResults = mutableListOf<PracticeResult>()
        var nextSessionId = 1L

        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(progressById.filterKeys { it in exerciseIds })

        override suspend fun savePracticeResult(result: PracticeResult): Long {
            savedResults += result
            return nextSessionId
        }

        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = savedResults.firstOrNull()
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> =
            savedResults.filter { it.exerciseId == exerciseId }.takeLast(limit).reversed()
    }

    private fun previousProgress(attemptCount: Int) = mapOf(
        "vowel-o" to ExerciseProgress(
            exerciseId = "vowel-o",
            attemptCount = attemptCount,
            completedCount = attemptCount,
            bestScore = 90f,
            lastScore = 90f,
            lastPracticedAt = 0L,
            isMastered = false,
        ),
    )

    private fun viewModel(
        exerciseId: String = "vowel-o",
        exerciseRepository: ExerciseRepository = exerciseRepositoryOf(listOf(exercise)),
        progressRepository: ProgressRepository = FakeProgressRepository(),
    ) = PracticeViewModel(
        savedStateHandle = SavedStateHandle(mapOf("exerciseId" to exerciseId)),
        exerciseRepository = exerciseRepository,
        progressRepository = progressRepository,
        tipSelector = TipSelector(TipRules()),
    )

    /** Loads the exercise so the ViewModel is ready to receive events. */
    private fun TestScope.startedViewModel(
        progressRepository: ProgressRepository = FakeProgressRepository(),
    ): PracticeViewModel {
        val viewModel = viewModel(progressRepository = progressRepository)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
        return viewModel
    }

    @Test
    fun `loads the requested exercise on start`() = runTest(dispatcher) {
        val viewModel = viewModel()
        assertTrue(viewModel.uiState.value.isLoading)

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(exercise, state.exercise)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `a drawing runs through the same practice lifecycle as a letter`() = runTest(dispatcher) {
        // plan.md Step 16 — drawings use the same infrastructure; nothing here is letter-specific.
        val house = Exercise(
            id = "drawing-house",
            title = "House",
            type = ExerciseType.DRAWING,
            difficulty = Difficulty.ADVANCED,
            strokes = listOf(
                Stroke(id = "drawing-house-roof", points = emptyList()),
                Stroke(id = "drawing-house-body", points = emptyList()),
            ),
            order = 5,
        )
        val progressRepository = FakeProgressRepository()
        val viewModel = viewModel(
            exerciseId = "drawing-house",
            exerciseRepository = exerciseRepositoryOf(listOf(house)),
            progressRepository = progressRepository,
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(house, viewModel.uiState.value.exercise)

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 88f, scoreLevel = ScoreLevel.MEDIUM))
        dispatcher.scheduler.advanceUntilIdle()

        val savedResult = progressRepository.savedResults.single()
        assertEquals("drawing-house", savedResult.exerciseId)
        assertEquals(88f, savedResult.score)
        assertTrue(savedResult.completed)
        assertEquals(ScoreLevel.MEDIUM, viewModel.uiState.value.scoreLevel)
    }

    @Test
    fun `a first-time exercise starts with the first-attempt tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        assertEquals(ContextualTip.FIRST_ATTEMPT, viewModel.uiState.value.tip)
    }

    @Test
    fun `an exercise practised before starts without a tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeProgressRepository(previousProgress(attemptCount = 2)))

        assertNull(viewModel.uiState.value.tip)
    }

    @Test
    fun `finishing a stroke well clears the first-attempt tip and shows none`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = true))

        assertNull(viewModel.uiState.value.tip)
    }

    @Test
    fun `missing a stroke once shows the try-again tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false))

        assertEquals(ContextualTip.MISSED_STROKE, viewModel.uiState.value.tip)
    }

    @Test
    fun `missing the same stroke repeatedly escalates the tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        repeat(2) { viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false)) }
        assertEquals(ContextualTip.MISSED_STROKE, viewModel.uiState.value.tip)

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false))
        assertEquals(ContextualTip.REPEATED_MISSES, viewModel.uiState.value.tip)
    }

    @Test
    fun `completing the stroke resets the miss count for the next one`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        repeat(3) { viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false)) }

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = true))
        assertNull(viewModel.uiState.value.tip)

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false))
        assertEquals(ContextualTip.MISSED_STROKE, viewModel.uiState.value.tip)
    }

    @Test
    fun `starting over restores the opening tip and forgets earlier misses`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        repeat(3) { viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false)) }

        viewModel.onEvent(PracticeEvent.Restarted)
        assertEquals(ContextualTip.FIRST_ATTEMPT, viewModel.uiState.value.tip)

        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false))
        assertEquals(ContextualTip.MISSED_STROKE, viewModel.uiState.value.tip)
    }

    @Test
    fun `starting over an exercise practised before leaves no tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeProgressRepository(previousProgress(attemptCount = 1)))
        viewModel.onEvent(PracticeEvent.StrokeAttempted(isCompleted = false))

        viewModel.onEvent(PracticeEvent.Restarted)

        assertNull(viewModel.uiState.value.tip)
    }

    @Test
    fun `unknown exercise id surfaces an error instead of a stuck loading state`() = runTest(dispatcher) {
        val viewModel = viewModel(exerciseId = "missing", exerciseRepository = exerciseRepositoryOf(emptyList()))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.exercise)
        assertEquals(false, state.isLoading)
        assertTrue(state.error != null)
    }

    @Test
    fun `exercise completed event saves the result and exposes the session id`() = runTest(dispatcher) {
        val progressRepository = FakeProgressRepository().apply { nextSessionId = 42L }
        val viewModel = viewModel(progressRepository = progressRepository)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 92f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        val savedResult = progressRepository.savedResults.single()
        assertEquals("vowel-o", savedResult.exerciseId)
        assertEquals(92f, savedResult.score)
        assertEquals(ScoreLevel.PERFECT, savedResult.scoreLevel)
        assertTrue(savedResult.completed)

        val state = viewModel.uiState.value
        assertTrue(state.isExerciseCompleted)
        assertEquals(92f, state.score)
        assertEquals(ScoreLevel.PERFECT, state.scoreLevel)
        assertEquals(42L, state.sessionId)
    }
}
