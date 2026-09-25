package com.bornochitra.feature.home

import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.LearningProgress
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
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ui state shows the repository's exercise to continue`() = runTest(dispatcher) {
        val progressFlow = MutableStateFlow(LearningProgress(overallProgress = 0.5f, continueExerciseId = "vowel-a"))
        val repository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = progressFlow
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(emptyMap())
            override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? =
                null
            override suspend fun getRecentResults(
                exerciseId: String,
                limit: Int,
            ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
        }

        val viewModel = HomeViewModel(repository)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals("vowel-a", viewModel.uiState.value.continueExerciseId)

        progressFlow.value = LearningProgress(overallProgress = 0.6f, continueExerciseId = "vowel-aa")
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals("vowel-aa", viewModel.uiState.value.continueExerciseId)
    }

    @Test
    fun `default ui state has no continue exercise`() = runTest(dispatcher) {
        val repository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(emptyMap())
            override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? =
                null
            override suspend fun getRecentResults(
                exerciseId: String,
                limit: Int,
            ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
        }

        val viewModel = HomeViewModel(repository)

        assertEquals(HomeState(), viewModel.uiState.value)
    }
}
