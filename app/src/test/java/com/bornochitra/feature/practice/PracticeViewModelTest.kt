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

    private class FakeProgressRepository : ProgressRepository {
        val savedResults = mutableListOf<PracticeResult>()
        var nextSessionId = 1L

        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(emptyMap())

        override suspend fun savePracticeResult(result: PracticeResult): Long {
            savedResults += result
            return nextSessionId
        }
    }

    private fun viewModel(
        exerciseId: String = "vowel-o",
        exerciseRepository: ExerciseRepository = exerciseRepositoryOf(listOf(exercise)),
        progressRepository: ProgressRepository = FakeProgressRepository(),
    ) = PracticeViewModel(
        savedStateHandle = SavedStateHandle(mapOf("exerciseId" to exerciseId)),
        exerciseRepository = exerciseRepository,
        progressRepository = progressRepository,
    )

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
