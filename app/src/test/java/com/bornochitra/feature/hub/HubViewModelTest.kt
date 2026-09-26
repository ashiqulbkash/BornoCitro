package com.bornochitra.feature.hub

import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.PracticeResult
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
class HubViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun repositoryOf(progress: Flow<LearningProgress>) = object : ProgressRepository {
        override fun observeProgress(): Flow<LearningProgress> = progress
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> = flowOf(emptyMap())
        override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
    }

    @Test
    fun `each category row shows its category's progress and follows changes`() = runTest(dispatcher) {
        val progress = MutableStateFlow(LearningProgress(vowelProgress = 0.27f, mathProgress = 0.1f, englishSmallProgress = 0.08f))
        val viewModel = HubViewModel(repositoryOf(progress))
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0.27f, state.progressOf(ExerciseType.VOWEL))
        assertEquals(0.1f, state.progressOf(ExerciseType.MATH))
        assertEquals(0.08f, state.progressOf(ExerciseType.ENGLISH_SMALL))
        assertEquals(0f, state.progressOf(ExerciseType.CONSONANT))

        progress.value = LearningProgress(consonantProgress = 0.5f)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(0.5f, viewModel.uiState.value.progressOf(ExerciseType.CONSONANT))
    }

    @Test
    fun `before progress loads every row is empty`() = runTest(dispatcher) {
        val viewModel = HubViewModel(repositoryOf(MutableStateFlow(LearningProgress())))

        assertEquals(0f, viewModel.uiState.value.progressOf(ExerciseType.VOWEL))
    }
}
