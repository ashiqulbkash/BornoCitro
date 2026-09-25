package com.bornochitra.feature.vowels

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
class VowelsViewModelTest {

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
        type = ExerciseType.VOWEL,
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
            exercise("vowel-o", "অ", order = 1),
            exercise("vowel-aa", "আ", order = 2),
            exercise("vowel-i", "ই", order = 3),
            exercise("vowel-ii", "ঈ", order = 4),
        )
        val progressByExerciseId = mapOf(
            "vowel-o" to progress("vowel-o", attemptCount = 1, completedCount = 1, isMastered = false),
            "vowel-aa" to progress("vowel-aa", attemptCount = 2, completedCount = 0, isMastered = false),
            "vowel-i" to progress("vowel-i", attemptCount = 5, completedCount = 3, isMastered = true),
        )

        val viewModel = VowelsViewModel(
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val items = viewModel.uiState.value.exercises
        assertEquals(4, items.size)
        assertEquals(VowelListItem(id = "vowel-o", title = "অ", learningState = LearningState.COMPLETED, attemptCount = 1), items[0])
        assertEquals(VowelListItem(id = "vowel-aa", title = "আ", learningState = LearningState.PRACTICING, attemptCount = 2), items[1])
        assertEquals(VowelListItem(id = "vowel-i", title = "ই", learningState = LearningState.MASTERED, attemptCount = 5), items[2])
        assertEquals(VowelListItem(id = "vowel-ii", title = "ঈ"), items[3])
    }

    @Test
    fun `default ui state has no exercises`() = runTest(dispatcher) {
        val viewModel = VowelsViewModel(
            exerciseRepository = exerciseRepositoryOf(emptyList()),
            progressRepository = progressRepositoryOf(emptyMap()),
        )

        assertEquals(VowelsState(), viewModel.uiState.value)
    }
}
