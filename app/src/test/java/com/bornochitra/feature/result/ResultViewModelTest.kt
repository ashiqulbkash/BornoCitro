package com.bornochitra.feature.result

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
class ResultViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun exercise(
        id: String,
        title: String,
        order: Int,
        type: ExerciseType = ExerciseType.VOWEL,
        difficulty: Difficulty = Difficulty.BEGINNER,
    ) = Exercise(
        id = id,
        title = title,
        type = type,
        difficulty = difficulty,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = order,
    )

    private val exercises = listOf(
        exercise(id = "vowel-o", title = "অ", order = 1),
        exercise(id = "vowel-aa", title = "আ", order = 2),
        exercise(id = "consonant-ko", title = "ক", order = 1, type = ExerciseType.CONSONANT),
        exercise(id = "drawing-circle", title = "Circle", order = 2, type = ExerciseType.DRAWING),
        exercise(id = "drawing-square", title = "Square", order = 3, type = ExerciseType.DRAWING),
        exercise(
            id = "drawing-house",
            title = "House",
            order = 5,
            type = ExerciseType.DRAWING,
            difficulty = Difficulty.ADVANCED,
        ),
    )

    private fun exerciseRepositoryOf(exercises: List<Exercise>) = object : ExerciseRepository {
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
            flowOf(exercises.filter { it.type == type })

        override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
    }

    private fun progressRepositoryOf(resultsBySessionId: Map<Long, PracticeResult>) = object : ProgressRepository {
        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(emptyMap())

        override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = resultsBySessionId[sessionId]

        // Newest session first, as the database returns them.
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> =
            resultsBySessionId.entries
                .filter { it.value.exerciseId == exerciseId }
                .sortedByDescending { it.key }
                .map { it.value }
                .take(limit)
    }

    private fun practiceResult(
        exerciseId: String = "vowel-o",
        score: Float = 94f,
        scoreLevel: ScoreLevel = ScoreLevel.PERFECT,
    ) = PracticeResult(
        exerciseId = exerciseId,
        score = score,
        scoreLevel = scoreLevel,
        completed = true,
        durationMs = 5_000L,
        completedAtMs = 1_000L,
    )

    private fun viewModel(
        sessionId: String = "1",
        results: Map<Long, PracticeResult> = mapOf(1L to practiceResult()),
        exercises: List<Exercise> = this.exercises,
        sessionScores: String = "",
    ) = ResultViewModel(
        savedStateHandle = SavedStateHandle(mapOf("sessionId" to sessionId, "scores" to sessionScores)),
        exerciseRepository = exerciseRepositoryOf(exercises),
        progressRepository = progressRepositoryOf(results),
        tipSelector = TipSelector(TipRules()),
    )

    @Test
    fun `saved session is reported with its letter, score and next exercise`() = runTest(dispatcher) {
        val viewModel = viewModel()
        assertTrue(viewModel.uiState.value.isLoading)

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals(
            ResultAttempt(
                exerciseId = "vowel-o",
                title = "অ",
                scorePercent = 94,
                scoreLevel = ScoreLevel.PERFECT,
                nextExerciseId = "vowel-aa",
                tip = null,
            ),
            state.attempt,
        )
    }

    @Test
    fun `a single try reports one try and its own score as the overall progress`() = runTest(dispatcher) {
        val viewModel = viewModel()

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val attempt = viewModel.uiState.value.attempt
        assertEquals(1, attempt?.sessionAttempts)
        assertEquals(94, attempt?.sessionAveragePercent)
    }

    @Test
    fun `several tries report how many and their average as the overall progress`() = runTest(dispatcher) {
        val viewModel = viewModel(sessionScores = "60.0,80.0,94.0")

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val attempt = viewModel.uiState.value.attempt
        assertEquals(3, attempt?.sessionAttempts)
        assertEquals(78, attempt?.sessionAveragePercent)
    }

    @Test
    fun `session summary says how many times the child tried and the overall progress`() {
        assertEquals("You tried 1 time. Your overall progress is 94%.", sessionSummary(1, 94))
        assertEquals("You tried 3 times. Your overall progress is 78%.", sessionSummary(3, 78))
    }

    @Test
    fun `score is rounded to a whole percentage`() = runTest(dispatcher) {
        val viewModel = viewModel(results = mapOf(1L to practiceResult(score = 75.6f, scoreLevel = ScoreLevel.MEDIUM)))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(76, viewModel.uiState.value.attempt?.scorePercent)
    }

    @Test
    fun `the last exercise in a category has no next exercise`() = runTest(dispatcher) {
        val viewModel = viewModel(results = mapOf(1L to practiceResult(exerciseId = "vowel-aa")))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val attempt = viewModel.uiState.value.attempt
        assertEquals("vowel-aa", attempt?.exerciseId)
        assertNull(attempt?.nextExerciseId)
    }

    @Test
    fun `next exercise stays inside the attempt's own category`() = runTest(dispatcher) {
        val viewModel = viewModel(results = mapOf(1L to practiceResult(exerciseId = "consonant-ko")))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.attempt?.nextExerciseId)
    }

    @Test
    fun `a drawing's result reports the drawing and offers the next one`() = runTest(dispatcher) {
        // plan.md Step 16 — a drawing ends in the same result as a letter.
        val viewModel = viewModel(
            results = mapOf(
                1L to practiceResult(exerciseId = "drawing-circle", score = 88f, scoreLevel = ScoreLevel.MEDIUM),
            ),
        )

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            ResultAttempt(
                exerciseId = "drawing-circle",
                title = "Circle",
                scorePercent = 88,
                scoreLevel = ScoreLevel.MEDIUM,
                nextExerciseId = "drawing-square",
                tip = null,
            ),
            viewModel.uiState.value.attempt,
        )
    }

    @Test
    fun `repeated attempts below the bar earn the slow-down tip`() = runTest(dispatcher) {
        val viewModel = viewModel(
            sessionId = "2",
            results = mapOf(
                1L to practiceResult(score = 70f, scoreLevel = ScoreLevel.MEDIUM),
                2L to practiceResult(score = 72f, scoreLevel = ScoreLevel.MEDIUM),
            ),
        )

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(ContextualTip.REPEATED_LOW_SCORES, viewModel.uiState.value.attempt?.tip)
    }

    @Test
    fun `a single weak attempt is not enough for a tip`() = runTest(dispatcher) {
        val viewModel = viewModel(
            results = mapOf(1L to practiceResult(score = 65f, scoreLevel = ScoreLevel.MEDIUM)),
        )

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.attempt?.tip)
    }

    @Test
    fun `an earlier weak attempt followed by a good one earns no tip`() = runTest(dispatcher) {
        val viewModel = viewModel(
            sessionId = "2",
            results = mapOf(
                1L to practiceResult(score = 60f, scoreLevel = ScoreLevel.MEDIUM),
                2L to practiceResult(score = 92f),
            ),
        )

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.attempt?.tip)
    }

    @Test
    fun `finishing a difficult exercise earns the well-done tip`() = runTest(dispatcher) {
        val viewModel = viewModel(results = mapOf(1L to practiceResult(exerciseId = "drawing-house", score = 95f)))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(ContextualTip.DIFFICULT_COMPLETED, viewModel.uiState.value.attempt?.tip)
    }

    @Test
    fun `unknown session id surfaces an error instead of a stuck loading state`() = runTest(dispatcher) {
        val viewModel = viewModel(sessionId = "404", results = emptyMap())

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.attempt)
        assertTrue(state.error != null)
    }

    @Test
    fun `a session id that is not a number surfaces an error`() = runTest(dispatcher) {
        val viewModel = viewModel(sessionId = "not-a-session")

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertTrue(state.error != null)
    }

    @Test
    fun `a session whose exercise is missing surfaces an error`() = runTest(dispatcher) {
        val viewModel = viewModel(results = mapOf(1L to practiceResult(exerciseId = "vowel-gone")))

        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.attempt)
        assertTrue(state.error != null)
    }
}
