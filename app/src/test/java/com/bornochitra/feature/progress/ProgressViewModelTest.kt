package com.bornochitra.feature.progress

import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.Stroke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun exercise(id: String, title: String, type: ExerciseType, order: Int = 1) = Exercise(
        id = id,
        title = title,
        type = type,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = emptyList())),
        order = order,
    )

    private val catalogue = listOf(
        exercise("vowel-o", "অ", ExerciseType.VOWEL, order = 1),
        exercise("vowel-aa", "আ", ExerciseType.VOWEL, order = 2),
        exercise("consonant-ko", "ক", ExerciseType.CONSONANT),
        exercise("drawing-line", "Line", ExerciseType.DRAWING),
    )

    private fun exerciseProgress(
        exerciseId: String,
        bestScore: Float,
        attemptCount: Int = 1,
        isMastered: Boolean = false,
    ) = ExerciseProgress(
        exerciseId = exerciseId,
        attemptCount = attemptCount,
        completedCount = 1,
        bestScore = bestScore,
        lastScore = bestScore,
        lastPracticedAt = 0L,
        isMastered = isMastered,
    )

    private fun viewModel(
        learningProgress: LearningProgress = LearningProgress(),
        progressByExerciseId: Map<String, ExerciseProgress> = emptyMap(),
        catalogue: List<Exercise> = this.catalogue,
        progressFails: Boolean = false,
    ): ProgressViewModel {
        val exerciseRepository = object : ExerciseRepository {
            override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
                flowOf(catalogue.filter { it.type == type })

            override suspend fun getExercise(id: String): Exercise? = catalogue.find { it.id == id }
        }
        val progressRepository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> =
                if (progressFails) flow { throw IllegalStateException("progress unavailable") } else flowOf(learningProgress)

            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(progressByExerciseId.filterKeys { it in exerciseIds })

            override suspend fun savePracticeResult(result: PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
            override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
        }
        return ProgressViewModel(exerciseRepository, progressRepository)
    }

    @Test
    fun `the screen starts loading, with no categories and zero overall progress`() {
        val viewModel = viewModel()

        assertEquals(ProgressState(), viewModel.uiState.value)
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `the first read ends the loading state`() = runTest(dispatcher) {
        val viewModel = viewModel()
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `a failing read reports an error instead of empty progress`() = runTest(dispatcher) {
        val viewModel = viewModel(progressFails = true)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertEquals(emptyList<ProgressCategory>(), state.categories)
    }

    @Test
    fun `no category is open until one is chosen`() = runTest(dispatcher) {
        val viewModel = viewModel()
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.openCategory)
    }

    @Test
    fun `opening a category keeps its own exercises, and closing returns to the buttons`() = runTest(dispatcher) {
        val viewModel = viewModel()
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.VOWEL))
        dispatcher.scheduler.advanceUntilIdle()
        val opened = viewModel.uiState.value
        assertEquals(ExerciseType.VOWEL, opened.openCategory)
        assertEquals(
            listOf("vowel-o", "vowel-aa"),
            opened.categories.first { it.type == opened.openCategory }.exercises.map { it.id },
        )

        viewModel.onEvent(ProgressEvent.CategoryClosed)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.openCategory)
    }

    @Test
    fun `opening another category replaces the open one`() = runTest(dispatcher) {
        val viewModel = viewModel()
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.VOWEL))
        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.CONSONANT))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(ExerciseType.CONSONANT, state.openCategory)
        assertEquals(
            listOf("consonant-ko"),
            state.categories.first { it.type == ExerciseType.CONSONANT }.exercises.map { it.id },
        )
    }

    @Test
    fun `each category carries its own ratio and its exercises in order`() = runTest(dispatcher) {
        val viewModel = viewModel(
            learningProgress = LearningProgress(
                overallProgress = 0.5f,
                vowelProgress = 0.5f,
                consonantProgress = 1f,
                drawingProgress = 0f,
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0.5f, state.overallProgress)
        assertEquals(
            listOf(
                ExerciseType.VOWEL,
                ExerciseType.CONSONANT,
                ExerciseType.ENGLISH_SMALL,
                ExerciseType.ENGLISH_CAPITAL,
                ExerciseType.MATH,
                ExerciseType.BANGLA_NUMBER,
                ExerciseType.DRAWING,
            ),
            state.categories.map { it.type },
        )
        assertEquals(0.5f, state.categories.first { it.type == ExerciseType.VOWEL }.progress)
        assertEquals(1f, state.categories.first { it.type == ExerciseType.CONSONANT }.progress)
        assertEquals(
            listOf("অ", "আ"),
            state.categories.first { it.type == ExerciseType.VOWEL }.exercises.map { it.title },
        )
    }

    @Test
    fun `the english small letters section carries its own ratio and letters`() = runTest(dispatcher) {
        val viewModel = viewModel(
            learningProgress = LearningProgress(englishSmallProgress = 0.5f),
            progressByExerciseId = mapOf("english-small-a" to exerciseProgress("english-small-a", bestScore = 94f)),
            catalogue = catalogue + listOf(
                exercise("english-small-a", "a", ExerciseType.ENGLISH_SMALL, order = 1),
                exercise("english-small-b", "b", ExerciseType.ENGLISH_SMALL, order = 2),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.ENGLISH_SMALL))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val section = state.categories.first { it.type == ExerciseType.ENGLISH_SMALL }
        assertEquals(ExerciseType.ENGLISH_SMALL, state.openCategory)
        assertEquals(0.5f, section.progress)
        assertEquals(listOf("a", "b"), section.exercises.map { it.title })
        assertEquals(listOf(3, 0), section.exercises.map { it.stars })
    }

    @Test
    fun `the english capital letters section carries its own ratio and letters`() = runTest(dispatcher) {
        val viewModel = viewModel(
            learningProgress = LearningProgress(englishCapitalProgress = 0.5f),
            progressByExerciseId = mapOf("english-capital-b" to exerciseProgress("english-capital-b", bestScore = 72f)),
            catalogue = catalogue + listOf(
                exercise("english-small-a", "a", ExerciseType.ENGLISH_SMALL, order = 1),
                exercise("english-capital-a", "A", ExerciseType.ENGLISH_CAPITAL, order = 1),
                exercise("english-capital-b", "B", ExerciseType.ENGLISH_CAPITAL, order = 2),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.ENGLISH_CAPITAL))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val section = state.categories.first { it.type == ExerciseType.ENGLISH_CAPITAL }
        assertEquals(ExerciseType.ENGLISH_CAPITAL, state.openCategory)
        assertEquals(0.5f, section.progress)
        assertEquals(listOf("A", "B"), section.exercises.map { it.title })
        assertEquals(listOf(0, 2), section.exercises.map { it.stars })
    }

    @Test
    fun `the math section carries its own ratio and items in order`() = runTest(dispatcher) {
        val viewModel = viewModel(
            learningProgress = LearningProgress(mathProgress = 0.5f),
            progressByExerciseId = mapOf("math-1" to exerciseProgress("math-1", bestScore = 94f)),
            catalogue = catalogue + listOf(
                exercise("math-1", "1", ExerciseType.MATH, order = 1),
                exercise("math-plus", "+", ExerciseType.MATH, order = 21),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.MATH))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val section = state.categories.first { it.type == ExerciseType.MATH }
        assertEquals(ExerciseType.MATH, state.openCategory)
        assertEquals(0.5f, section.progress)
        assertEquals(listOf("1", "+"), section.exercises.map { it.title })
        assertEquals(listOf(3, 0), section.exercises.map { it.stars })
    }

    @Test
    fun `the bengali numbers section carries its own ratio and items in order`() = runTest(dispatcher) {
        val viewModel = viewModel(
            learningProgress = LearningProgress(banglaNumberProgress = 0.5f),
            progressByExerciseId = mapOf("bangla-number-2" to exerciseProgress("bangla-number-2", bestScore = 94f)),
            catalogue = catalogue + listOf(
                exercise("bangla-number-1", "১", ExerciseType.BANGLA_NUMBER, order = 1),
                exercise("bangla-number-2", "২", ExerciseType.BANGLA_NUMBER, order = 2),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ProgressEvent.CategoryOpened(ExerciseType.BANGLA_NUMBER))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val section = state.categories.first { it.type == ExerciseType.BANGLA_NUMBER }
        assertEquals(ExerciseType.BANGLA_NUMBER, state.openCategory)
        assertEquals(0.5f, section.progress)
        assertEquals(listOf("১", "২"), section.exercises.map { it.title })
        assertEquals(listOf(0, 3), section.exercises.map { it.stars })
    }

    @Test
    fun `stars come from the best score band, and an untried exercise has none`() = runTest(dispatcher) {
        val viewModel = viewModel(
            progressByExerciseId = mapOf(
                "vowel-o" to exerciseProgress("vowel-o", bestScore = 94f),
                "vowel-aa" to exerciseProgress("vowel-aa", bestScore = 72f),
                "consonant-ko" to exerciseProgress("consonant-ko", bestScore = 41f),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val starsById = viewModel.uiState.value.categories
            .flatMap { it.exercises }
            .associate { it.id to it.stars }

        assertEquals(3, starsById["vowel-o"])
        assertEquals(2, starsById["vowel-aa"])
        assertEquals(1, starsById["consonant-ko"])
        assertEquals(0, starsById["drawing-line"])
    }

    @Test
    fun `a recorded row with no attempts yet shows no stars`() = runTest(dispatcher) {
        val viewModel = viewModel(
            progressByExerciseId = mapOf(
                "vowel-o" to exerciseProgress("vowel-o", bestScore = 0f, attemptCount = 0),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val vowels = viewModel.uiState.value.categories.first { it.type == ExerciseType.VOWEL }
        assertEquals(0, vowels.exercises.first { it.id == "vowel-o" }.stars)
    }

    @Test
    fun `each exercise carries its learning state, so mastery is visible`() = runTest(dispatcher) {
        val viewModel = viewModel(
            progressByExerciseId = mapOf(
                "vowel-o" to exerciseProgress("vowel-o", bestScore = 94f, attemptCount = 4, isMastered = true),
                "vowel-aa" to exerciseProgress("vowel-aa", bestScore = 72f),
            ),
        )
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val stateById = viewModel.uiState.value.categories
            .flatMap { it.exercises }
            .associate { it.id to it.state }

        assertEquals(LearningState.MASTERED, stateById["vowel-o"])
        assertEquals(LearningState.COMPLETED, stateById["vowel-aa"])
        assertEquals(LearningState.NOT_STARTED, stateById["drawing-line"])
    }

    @Test
    fun `an empty catalogue yields empty categories rather than missing ones`() = runTest(dispatcher) {
        val viewModel = viewModel(catalogue = emptyList())
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(ExerciseType.entries.size, state.categories.size)
        assertEquals(emptyList<ProgressExerciseItem>(), state.categories.flatMap { it.exercises })
    }
}
