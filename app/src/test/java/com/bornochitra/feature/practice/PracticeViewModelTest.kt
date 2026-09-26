package com.bornochitra.feature.practice

import androidx.lifecycle.SavedStateHandle
import com.bornochitra.core.analytics.AnalyticsEvent
import com.bornochitra.core.analytics.AnalyticsTracker
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
        /** What the stored progress becomes once a result has been saved, as the real repository would update it. */
        private val progressAfterSave: Map<String, ExerciseProgress> = progressById,
    ) : ProgressRepository {
        val savedResults = mutableListOf<PracticeResult>()
        var nextSessionId = 1L

        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf((if (savedResults.isEmpty()) progressById else progressAfterSave).filterKeys { it in exerciseIds })

        override suspend fun savePracticeResult(result: PracticeResult): Long {
            savedResults += result
            return nextSessionId
        }

        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = savedResults.firstOrNull()
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> =
            savedResults.filter { it.exerciseId == exerciseId }.takeLast(limit).reversed()
    }

    private fun previousProgress(attemptCount: Int, isMastered: Boolean = false) = mapOf(
        "vowel-o" to ExerciseProgress(
            exerciseId = "vowel-o",
            attemptCount = attemptCount,
            completedCount = attemptCount,
            bestScore = 90f,
            lastScore = 90f,
            lastPracticedAt = 0L,
            isMastered = isMastered,
        ),
    )

    private val trackedEvents = mutableListOf<AnalyticsEvent>()

    private val analytics = object : AnalyticsTracker {
        override fun track(event: AnalyticsEvent) {
            trackedEvents += event
        }
    }

    private fun viewModel(
        exerciseId: String = "vowel-o",
        exerciseRepository: ExerciseRepository = exerciseRepositoryOf(listOf(exercise)),
        progressRepository: ProgressRepository = FakeProgressRepository(),
        sessionScores: String = "",
    ) = PracticeViewModel(
        savedStateHandle = SavedStateHandle(mapOf("exerciseId" to exerciseId, "scores" to sessionScores)),
        exerciseRepository = exerciseRepository,
        progressRepository = progressRepository,
        tipSelector = TipSelector(TipRules()),
        analytics = analytics,
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
    fun `stopping with the character unfinished says it is not finished yet`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(PracticeEvent.TraceUnfinished)

        assertEquals(ContextualTip.UNFINISHED_TRACE, viewModel.uiState.value.tip)
    }

    @Test
    fun `stopping again keeps saying the character is unfinished`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        repeat(3) { viewModel.onEvent(PracticeEvent.TraceUnfinished) }

        assertEquals(ContextualTip.UNFINISHED_TRACE, viewModel.uiState.value.tip)
    }

    @Test
    fun `starting over restores the opening tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        viewModel.onEvent(PracticeEvent.TraceUnfinished)

        viewModel.onEvent(PracticeEvent.RestartRequested)

        assertEquals(ContextualTip.FIRST_ATTEMPT, viewModel.uiState.value.tip)
    }

    @Test
    fun `starting over begins a new attempt so the drawn path and its progress are cleared`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        assertEquals(0, viewModel.uiState.value.attemptId)

        viewModel.onEvent(PracticeEvent.RestartRequested)
        assertEquals(1, viewModel.uiState.value.attemptId)

        viewModel.onEvent(PracticeEvent.RestartRequested)
        assertEquals(2, viewModel.uiState.value.attemptId)
    }

    @Test
    fun `tracing without starting over keeps the same attempt`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        repeat(2) { viewModel.onEvent(PracticeEvent.TraceUnfinished) }

        assertEquals(0, viewModel.uiState.value.attemptId)
    }

    @Test
    fun `resetting with ink on the canvas asks first and keeps the attempt`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        viewModel.onEvent(PracticeEvent.StrokeStarted)
        trackedEvents.clear()

        viewModel.onEvent(PracticeEvent.RestartRequested)

        val state = viewModel.uiState.value
        assertTrue(state.isRestartConfirmationShown)
        assertEquals(0, state.attemptId)
        assertTrue(state.hasInk)
        assertTrue(trackedEvents.isEmpty())
    }

    @Test
    fun `confirming the reset wipes the ink and starts a new attempt`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        viewModel.onEvent(PracticeEvent.StrokeStarted)
        viewModel.onEvent(PracticeEvent.RestartRequested)

        viewModel.onEvent(PracticeEvent.RestartConfirmed)

        val state = viewModel.uiState.value
        assertEquals(false, state.isRestartConfirmationShown)
        assertEquals(1, state.attemptId)
        assertEquals(false, state.hasInk)
        assertEquals(AnalyticsEvent.PracticeRepeated("vowel-o"), trackedEvents.last())
    }

    @Test
    fun `keeping on writing closes the question and keeps the ink`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        viewModel.onEvent(PracticeEvent.StrokeStarted)
        viewModel.onEvent(PracticeEvent.RestartRequested)

        viewModel.onEvent(PracticeEvent.RestartDismissed)

        val state = viewModel.uiState.value
        assertEquals(false, state.isRestartConfirmationShown)
        assertEquals(0, state.attemptId)
        assertTrue(state.hasInk)
    }

    @Test
    fun `resetting an empty canvas starts over without asking`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(PracticeEvent.RestartRequested)

        assertEquals(false, viewModel.uiState.value.isRestartConfirmationShown)
        assertEquals(1, viewModel.uiState.value.attemptId)
    }

    @Test
    fun `the exercise's place in its category is exposed for the top bar`() = runTest(dispatcher) {
        val vowels = listOf(
            exercise.copy(id = "vowel-i", title = "ই", order = 3),
            exercise,
            exercise.copy(id = "vowel-aa", title = "আ", order = 2),
        )
        val viewModel = viewModel(exerciseId = "vowel-aa", exerciseRepository = exerciseRepositoryOf(vowels))
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.categoryPosition)
        assertEquals(3, viewModel.uiState.value.categorySize)
    }

    @Test
    fun `starting over an exercise practised before leaves no tip`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeProgressRepository(previousProgress(attemptCount = 1)))
        viewModel.onEvent(PracticeEvent.TraceUnfinished)

        viewModel.onEvent(PracticeEvent.RestartRequested)

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

    @Test
    fun `a fresh visit starts a session with no tries`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        assertEquals(emptyList<Float>(), viewModel.uiState.value.sessionScores)
    }

    @Test
    fun `each completed try is added to the session in order`() = runTest(dispatcher) {
        val viewModel = viewModel(sessionScores = "70.0,80.0")
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(listOf(70f, 80f), viewModel.uiState.value.sessionScores)

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 90f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(70f, 80f, 90f), viewModel.uiState.value.sessionScores)
    }

    @Test
    fun `every try in a session is saved individually`() = runTest(dispatcher) {
        val progressRepository = FakeProgressRepository()
        val viewModel = viewModel(progressRepository = progressRepository, sessionScores = "60.0")
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 85f, scoreLevel = ScoreLevel.MEDIUM))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(85f), progressRepository.savedResults.map { it.score })
    }

    @Test
    fun `opening an exercise for the first time tracks it as started, not repeated`() = runTest(dispatcher) {
        startedViewModel()

        assertEquals(listOf<AnalyticsEvent>(AnalyticsEvent.ExerciseStarted("vowel-o", ExerciseType.VOWEL)), trackedEvents)
    }

    @Test
    fun `opening an exercise practised before tracks it as started and repeated`() = runTest(dispatcher) {
        startedViewModel(FakeProgressRepository(previousProgress(attemptCount = 2)))

        assertEquals(
            listOf(AnalyticsEvent.ExerciseStarted("vowel-o", ExerciseType.VOWEL), AnalyticsEvent.PracticeRepeated("vowel-o")),
            trackedEvents,
        )
    }

    @Test
    fun `resetting mid-attempt tracks a repeat`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(PracticeEvent.RestartRequested)

        assertEquals(AnalyticsEvent.PracticeRepeated("vowel-o"), trackedEvents.last())
    }

    @Test
    fun `finishing a letter tracks completion and the rounded score`() = runTest(dispatcher) {
        val viewModel = startedViewModel()
        trackedEvents.clear()

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 93.6f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, trackedEvents.size)
        assertTrue(trackedEvents[0] is AnalyticsEvent.ExerciseCompleted)
        assertEquals(AnalyticsEvent.ScoreReceived("vowel-o", score = 94, level = ScoreLevel.PERFECT), trackedEvents[1])
    }

    @Test
    fun `finishing a drawing also tracks a drawing completed`() = runTest(dispatcher) {
        val circle = exercise.copy(id = "drawing-circle", title = "Circle", type = ExerciseType.DRAWING)
        val viewModel = viewModel(exerciseId = "drawing-circle", exerciseRepository = exerciseRepositoryOf(listOf(circle)))
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 90f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(AnalyticsEvent.DrawingCompleted("drawing-circle") in trackedEvents)
    }

    @Test
    fun `mastery is tracked once, on the attempt that earns it`() = runTest(dispatcher) {
        val progress = FakeProgressRepository(
            progressById = previousProgress(attemptCount = 2),
            progressAfterSave = previousProgress(attemptCount = 3, isMastered = true),
        )
        val viewModel = startedViewModel(progress)

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 95f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, trackedEvents.count { it is AnalyticsEvent.ExerciseMastered })
    }

    @Test
    fun `an exercise that was already mastered is not tracked as mastered again`() = runTest(dispatcher) {
        val mastered = previousProgress(attemptCount = 5, isMastered = true)
        val viewModel = startedViewModel(FakeProgressRepository(progressById = mastered))

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 95f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(trackedEvents.none { it is AnalyticsEvent.ExerciseMastered })
    }

    @Test
    fun `an attempt that does not earn mastery is not tracked as mastered`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeProgressRepository(progressById = previousProgress(attemptCount = 1)))

        viewModel.onEvent(PracticeEvent.ExerciseCompleted(score = 70f, scoreLevel = ScoreLevel.MEDIUM))
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(trackedEvents.none { it is AnalyticsEvent.ExerciseMastered })
    }
}
