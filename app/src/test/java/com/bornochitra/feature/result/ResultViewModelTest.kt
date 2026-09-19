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

    private fun exercise(id: String, title: String, order: Int, type: ExerciseType = ExerciseType.VOWEL) = Exercise(
        id = id,
        title = title,
        type = type,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = order,
    )

    private val exercises = listOf(
        exercise(id = "vowel-o", title = "অ", order = 1),
        exercise(id = "vowel-aa", title = "আ", order = 2),
        exercise(id = "consonant-ko", title = "ক", order = 1, type = ExerciseType.CONSONANT),
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
    ) = ResultViewModel(
        savedStateHandle = SavedStateHandle(mapOf("sessionId" to sessionId)),
        exerciseRepository = exerciseRepositoryOf(exercises),
        progressRepository = progressRepositoryOf(results),
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
            ),
            state.attempt,
        )
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
