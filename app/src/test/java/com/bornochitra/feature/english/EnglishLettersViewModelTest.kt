package com.bornochitra.feature.english

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
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
class EnglishLettersViewModelTest {

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
        type = ExerciseType.ENGLISH_SMALL,
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
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
            flowOf(exercises.filter { it.type == type })
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
            exercise("english-small-a", "a", order = 1),
            exercise("english-small-b", "b", order = 2),
            exercise("english-small-c", "c", order = 3),
            exercise("english-small-d", "d", order = 4),
        )
        val progressByExerciseId = mapOf(
            "english-small-a" to progress("english-small-a", attemptCount = 1, completedCount = 1, isMastered = false),
            "english-small-b" to progress("english-small-b", attemptCount = 2, completedCount = 0, isMastered = false),
            "english-small-c" to progress("english-small-c", attemptCount = 5, completedCount = 3, isMastered = true),
        )

        val viewModel = EnglishLettersViewModel(
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val items = viewModel.uiState.value.exercises
        assertEquals(4, items.size)
        assertEquals(EnglishLetterListItem(id = "english-small-a", title = "a", statusText = "Completed"), items[0])
        assertEquals(EnglishLetterListItem(id = "english-small-b", title = "b", statusText = "2 attempts"), items[1])
        assertEquals(EnglishLetterListItem(id = "english-small-c", title = "c", statusText = "Mastered"), items[2])
        assertEquals(EnglishLetterListItem(id = "english-small-d", title = "d", statusText = null), items[3])
    }

    @Test
    fun `default ui state has no exercises`() = runTest(dispatcher) {
        val viewModel = EnglishLettersViewModel(
            exerciseRepository = exerciseRepositoryOf(emptyList()),
            progressRepository = progressRepositoryOf(emptyMap()),
        )

        assertEquals(EnglishLettersState(), viewModel.uiState.value)
    }
}
