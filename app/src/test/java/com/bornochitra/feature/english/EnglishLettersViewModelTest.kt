package com.bornochitra.feature.english

import androidx.lifecycle.SavedStateHandle
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

    private fun exercise(id: String, title: String, order: Int, type: ExerciseType = ExerciseType.ENGLISH_SMALL) = Exercise(
        id = id,
        title = title,
        type = type,
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
            savedStateHandle = savedStateHandleFor(ExerciseType.ENGLISH_SMALL),
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val items = viewModel.uiState.value.exercises
        assertEquals(4, items.size)
        assertEquals(EnglishLetterListItem(id = "english-small-a", title = "a", learningState = LearningState.COMPLETED, attemptCount = 1), items[0])
        assertEquals(EnglishLetterListItem(id = "english-small-b", title = "b", learningState = LearningState.PRACTICING, attemptCount = 2), items[1])
        assertEquals(EnglishLetterListItem(id = "english-small-c", title = "c", learningState = LearningState.MASTERED, attemptCount = 5), items[2])
        assertEquals(EnglishLetterListItem(id = "english-small-d", title = "d"), items[3])
    }

    @Test
    fun `default ui state has no exercises`() = runTest(dispatcher) {
        val viewModel = EnglishLettersViewModel(
            savedStateHandle = savedStateHandleFor(ExerciseType.ENGLISH_SMALL),
            exerciseRepository = exerciseRepositoryOf(emptyList()),
            progressRepository = progressRepositoryOf(emptyMap()),
        )

        assertEquals(EnglishLettersState(type = ExerciseType.ENGLISH_SMALL), viewModel.uiState.value)
    }

    @Test
    fun `the route's letter type picks which letters are listed`() = runTest(dispatcher) {
        val exercises = listOf(
            exercise("english-small-a", "a", order = 1),
            exercise("english-capital-a", "A", order = 1, type = ExerciseType.ENGLISH_CAPITAL),
            exercise("english-capital-b", "B", order = 2, type = ExerciseType.ENGLISH_CAPITAL),
        )
        val progressByExerciseId = mapOf(
            "english-small-a" to progress("english-small-a", attemptCount = 1, completedCount = 1, isMastered = false),
            "english-capital-b" to progress("english-capital-b", attemptCount = 1, completedCount = 1, isMastered = false),
        )

        val viewModel = EnglishLettersViewModel(
            savedStateHandle = savedStateHandleFor(ExerciseType.ENGLISH_CAPITAL),
            exerciseRepository = exerciseRepositoryOf(exercises),
            progressRepository = progressRepositoryOf(progressByExerciseId),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(ExerciseType.ENGLISH_CAPITAL, state.type)
        assertEquals(
            listOf(
                EnglishLetterListItem(id = "english-capital-a", title = "A"),
                EnglishLetterListItem(id = "english-capital-b", title = "B", learningState = LearningState.COMPLETED, attemptCount = 1),
            ),
            state.exercises,
        )
    }

    private fun savedStateHandleFor(type: ExerciseType) = SavedStateHandle(mapOf("type" to type.name))
}
