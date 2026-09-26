package com.bornochitra.feature.home

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.Stroke
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

    private fun exercise(id: String, type: ExerciseType, title: String = id) = Exercise(
        id = id,
        title = title,
        type = type,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = 0,
    )

    private fun progress(exerciseId: String, completedCount: Int, bestScore: Float, attemptCount: Int = completedCount) =
        ExerciseProgress(
            exerciseId = exerciseId,
            attemptCount = attemptCount,
            completedCount = completedCount,
            bestScore = bestScore,
            lastScore = bestScore,
            lastPracticedAt = 0L,
            isMastered = false,
        )

    private val exercises = listOf(
        exercise("vowel-o", ExerciseType.VOWEL, "অ"),
        exercise("vowel-aa", ExerciseType.VOWEL, "আ"),
        exercise("consonant-ko", ExerciseType.CONSONANT, "ক"),
        exercise("math-1", ExerciseType.MATH, "1"),
        exercise("english-small-a", ExerciseType.ENGLISH_SMALL, "a"),
        exercise("drawing-line", ExerciseType.DRAWING, "Line"),
        exercise("drawing-circle", ExerciseType.DRAWING, "Circle"),
    )

    private fun viewModelOf(
        learningProgress: Flow<LearningProgress>,
        progressById: Map<String, ExerciseProgress>,
        masteryRule: MasteryRule = MasteryRule(),
    ): HomeViewModel {
        val exerciseRepository = object : ExerciseRepository {
            override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> = flowOf(exercises.filter { it.type == type })
            override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
        }
        val progressRepository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = learningProgress
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(progressById.filterKeys { it in exerciseIds })
            override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
            override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
        }
        return HomeViewModel(exerciseRepository, progressRepository, masteryRule)
    }

    private fun TestScope.collect(viewModel: HomeViewModel) {
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `the continue card shows the repository's exercise and follows changes`() = runTest(dispatcher) {
        val learningProgress = MutableStateFlow(LearningProgress(continueExerciseId = "vowel-aa"))
        val viewModel = viewModelOf(learningProgress, mapOf("vowel-aa" to progress("vowel-aa", completedCount = 1, bestScore = 95f)))
        collect(viewModel)

        val item = viewModel.uiState.value.continueItem!!
        assertEquals("আ", item.title)
        assertEquals(ExerciseType.VOWEL, item.type)
        assertEquals(3, item.stars)
        assertEquals(1, item.completedSegments)
        assertEquals(2, item.remainingCompletions)
        assertEquals(3, item.requiredCompletions)

        learningProgress.value = LearningProgress(continueExerciseId = "consonant-ko")
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals("consonant-ko", viewModel.uiState.value.continueItem?.id)
    }

    @Test
    fun `an unfinished continue exercise has no stars and needs every finish`() = runTest(dispatcher) {
        val viewModel = viewModelOf(
            MutableStateFlow(LearningProgress(continueExerciseId = "vowel-o")),
            mapOf("vowel-o" to progress("vowel-o", completedCount = 0, bestScore = 30f, attemptCount = 2)),
        )
        collect(viewModel)

        val item = viewModel.uiState.value.continueItem!!
        assertEquals(0, item.stars)
        assertEquals(0, item.completedSegments)
        assertEquals(3, item.remainingCompletions)
    }

    @Test
    fun `finished often enough but below the mastery score, only the score is still needed`() = runTest(dispatcher) {
        val viewModel = viewModelOf(
            MutableStateFlow(LearningProgress(continueExerciseId = "vowel-o")),
            mapOf("vowel-o" to progress("vowel-o", completedCount = 5, bestScore = 70f)),
            masteryRule = MasteryRule(requiredCompletions = 4, minBestScore = 85f),
        )
        collect(viewModel)

        val item = viewModel.uiState.value.continueItem!!
        assertEquals(4, item.completedSegments)
        assertEquals(4, item.requiredCompletions)
        assertEquals(0, item.remainingCompletions)
        assertEquals(85, item.minBestScorePercent)
    }

    @Test
    fun `subject progress counts finished items, with math in both bangla and english`() = runTest(dispatcher) {
        val viewModel = viewModelOf(
            MutableStateFlow(LearningProgress()),
            mapOf(
                "vowel-o" to progress("vowel-o", completedCount = 1, bestScore = 90f),
                "consonant-ko" to progress("consonant-ko", completedCount = 0, bestScore = 20f, attemptCount = 1),
                "math-1" to progress("math-1", completedCount = 2, bestScore = 70f),
                "drawing-line" to progress("drawing-line", completedCount = 1, bestScore = 80f),
            ),
        )
        collect(viewModel)

        val state = viewModel.uiState.value
        assertEquals(2 / 4f, state.banglaProgress)
        assertEquals(1 / 2f, state.englishProgress)
        assertEquals(1 / 2f, state.drawingProgress)
        assertNull(state.continueItem)
    }

    @Test
    fun `default ui state has nothing to continue and no progress`() = runTest(dispatcher) {
        val viewModel = viewModelOf(MutableStateFlow(LearningProgress()), emptyMap())

        assertEquals(HomeState(), viewModel.uiState.value)
    }
}
