package com.bornochitra.feature.fillblanks

import androidx.lifecycle.SavedStateHandle
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.recognition.FreehandChecker
import com.bornochitra.core.recognition.FreehandScorer
import com.bornochitra.core.recognition.InkRecognizer
import com.bornochitra.core.recognition.WritingScript
import com.bornochitra.core.tracing.TracePoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FillBlanksViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun exercise(id: String, title: String, order: Int, type: ExerciseType = ExerciseType.MATH) = Exercise(
        id = id,
        title = title,
        type = type,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(Stroke(id = "$id-stroke", points = listOf(Point(50f, 10f), Point(50f, 90f)))),
        order = order,
    )

    private val numbers = (1..20).map { exercise("math-$it", "$it", order = it) }
    private val mathCatalog = numbers + listOf(
        exercise("math-plus", "+", order = 21),
        exercise("math-minus", "−", order = 22),
    )

    private fun exerciseRepositoryOf(exercises: List<Exercise>) = object : ExerciseRepository {
        override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
            flowOf(exercises.filter { it.type == type })

        override suspend fun getExercise(id: String): Exercise? = exercises.find { it.id == id }
    }

    private class FakeProgressRepository : ProgressRepository {
        val savedResults = mutableListOf<PracticeResult>()

        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(emptyMap())

        override suspend fun savePracticeResult(result: PracticeResult): Long {
            savedResults += result
            return savedResults.size.toLong()
        }

        override suspend fun getPracticeResult(sessionId: Long): PracticeResult? = null
        override suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult> = emptyList()
    }

    private val progressRepository = FakeProgressRepository()

    /** Reads every ink as [reading]; null stands for a model that is not downloaded yet. */
    private class FakeRecognizer(var reading: String? = null) : InkRecognizer {
        override suspend fun isModelReady(script: WritingScript) = true

        override suspend fun downloadModel(script: WritingScript) = true

        override suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String> =
            listOfNotNull(reading)
    }

    private val recognizer = FakeRecognizer()

    /** A vertical line written small and off to one side. */
    private val ink = listOf(listOf(TracePoint(20f, 30f, 0L), TracePoint(20f, 50f, 16L), TracePoint(20f, 70f, 32L)))

    private fun viewModel(
        type: String? = ExerciseType.MATH.name,
        difficulty: String = Difficulty.BEGINNER.name,
        seed: Long = SEED,
        exercises: List<Exercise> = mathCatalog,
    ): Pair<FillBlanksViewModel, SavedStateHandle> {
        val handle = SavedStateHandle(
            buildMap {
                if (type != null) put("type", type)
                put("difficulty", difficulty)
                put(KEY_SEED, seed)
            },
        )
        val checker = FreehandChecker(recognizer, dispatcher)
        return FillBlanksViewModel(handle, exerciseRepositoryOf(exercises), progressRepository, checker) to handle
    }

    private fun expectedSequence(seed: Long = SEED, difficulty: Difficulty = Difficulty.BEGINNER) =
        BlankSequenceGenerator.generate(numbers, difficulty, seed)

    @Test
    fun `starts loading`() {
        val (viewModel, _) = viewModel()
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loads the seeded sequence with the first blank active`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()

        val sequence = expectedSequence()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(ExerciseType.MATH, state.type)
        assertEquals(sequence.items.map { it.id }, state.cells.map { it.exerciseId })
        state.cells.forEachIndexed { index, cell ->
            val expected = when (index) {
                !in sequence.blankIndices -> CellStatus.SHOWN
                sequence.blankIndices.first() -> CellStatus.ACTIVE
                else -> CellStatus.BLANK
            }
            assertEquals(expected, cell.status)
        }
        assertEquals(sequence.items[sequence.blankIndices.first()], state.activeExercise)
        assertFalse(state.isHintShown)
        assertFalse(state.isSequenceFinished)
    }

    @Test
    fun `math sequences leave out the operators`() = runTest(dispatcher) {
        (0L until 50L).forEach { seed ->
            val (viewModel, _) = viewModel(seed = seed)
            dispatcher.scheduler.advanceUntilIdle()
            assertTrue(viewModel.uiState.value.cells.all { cell -> cell.title.all { it.isDigit() } })
        }
    }

    @Test
    fun `completing a blank saves it against the missing item and moves to the next blank`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val sequence = expectedSequence()
        val attemptBefore = viewModel.uiState.value.attemptId

        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 95f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        val firstBlank = sequence.items[sequence.blankIndices.first()]
        val saved = progressRepository.savedResults.single()
        assertEquals(firstBlank.id, saved.exerciseId)
        assertEquals(95f, saved.score)
        assertEquals(ScoreLevel.PERFECT, saved.scoreLevel)
        assertTrue(saved.completed)

        val state = viewModel.uiState.value
        assertEquals(CellStatus.FILLED, state.cells[sequence.blankIndices.first()].status)
        assertEquals(listOf(BlankResult(firstBlank.id, 95f, ScoreLevel.PERFECT, hintUsed = false)), state.results)
        assertNotEquals(attemptBefore, state.attemptId)
        val secondBlank = sequence.blankIndices.getOrNull(1)
        if (secondBlank != null) {
            assertEquals(sequence.items[secondBlank], state.activeExercise)
            assertEquals(CellStatus.ACTIVE, state.cells[secondBlank].status)
        } else {
            assertNull(state.activeExercise)
        }
    }

    @Test
    fun `a hint shows the guide, caps that blank's score and is cleared for the next blank`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel(seed = seedWithBlanks(2))
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(FillBlanksEvent.HintUsed)
        assertTrue(viewModel.uiState.value.isHintShown)
        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 98f, scoreLevel = ScoreLevel.PERFECT))
        dispatcher.scheduler.advanceUntilIdle()

        val saved = progressRepository.savedResults.single()
        assertEquals(HintRule.HINTED_SCORE_CAP, saved.score)
        assertEquals(ScoreLevel.MEDIUM, saved.scoreLevel)
        val state = viewModel.uiState.value
        assertTrue(state.results.single().hintUsed)
        assertFalse(state.isHintShown)
    }

    @Test
    fun `restarting a blank gives a new attempt on the same blank`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.uiState.value

        viewModel.onEvent(FillBlanksEvent.BlankRestarted)

        val after = viewModel.uiState.value
        assertEquals(before.activeExercise, after.activeExercise)
        assertNotEquals(before.attemptId, after.attemptId)
        assertTrue(progressRepository.savedResults.isEmpty())
    }

    @Test
    fun `help asks in the hint sheet before the guide is shown`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(FillBlanksEvent.HintRequested)
        assertTrue(viewModel.uiState.value.isHintSheetShown)
        assertFalse(viewModel.uiState.value.isHintShown)

        viewModel.onEvent(FillBlanksEvent.HintUsed)
        assertFalse(viewModel.uiState.value.isHintSheetShown)
        assertTrue(viewModel.uiState.value.isHintShown)
    }

    @Test
    fun `closing the hint sheet leaves the guide hidden`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(FillBlanksEvent.HintRequested)
        viewModel.onEvent(FillBlanksEvent.HintDismissed)

        assertFalse(viewModel.uiState.value.isHintSheetShown)
        assertFalse(viewModel.uiState.value.isHintShown)
    }

    @Test
    fun `reset with no ink starts the blank over at once`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.uiState.value.attemptId

        viewModel.onEvent(FillBlanksEvent.BlankRestartRequested)

        assertFalse(viewModel.uiState.value.isRestartConfirmationShown)
        assertNotEquals(before, viewModel.uiState.value.attemptId)
    }

    @Test
    fun `reset with ink asks first and a yes wipes it`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.uiState.value.attemptId
        viewModel.onEvent(FillBlanksEvent.StrokeStarted)

        viewModel.onEvent(FillBlanksEvent.BlankRestartRequested)
        assertTrue(viewModel.uiState.value.isRestartConfirmationShown)
        assertEquals(before, viewModel.uiState.value.attemptId)

        viewModel.onEvent(FillBlanksEvent.BlankRestarted)
        val state = viewModel.uiState.value
        assertFalse(state.isRestartConfirmationShown)
        assertFalse(state.hasInk)
        assertNotEquals(before, state.attemptId)
    }

    @Test
    fun `reset with ink keeps it when the child says no`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.uiState.value.attemptId
        viewModel.onEvent(FillBlanksEvent.StrokeStarted)

        viewModel.onEvent(FillBlanksEvent.BlankRestartRequested)
        viewModel.onEvent(FillBlanksEvent.BlankRestartDismissed)

        val state = viewModel.uiState.value
        assertFalse(state.isRestartConfirmationShown)
        assertTrue(state.hasInk)
        assertEquals(before, state.attemptId)
    }

    @Test
    fun `a filled blank leaves the next one without ink`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel(seed = seedWithBlanks(2))
        dispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(FillBlanksEvent.StrokeStarted)

        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 90f, scoreLevel = ScoreLevel.PERFECT))
        viewModel.onEvent(FillBlanksEvent.BlankRestartRequested)

        assertFalse(viewModel.uiState.value.hasInk)
        assertFalse(viewModel.uiState.value.isRestartConfirmationShown)
    }

    @Test
    fun `filling the last blank finishes the sequence with the average score`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel(seed = seedWithBlanks(2))
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 90f, scoreLevel = ScoreLevel.PERFECT))
        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 71f, scoreLevel = ScoreLevel.MEDIUM))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSequenceFinished)
        assertNull(state.activeExercise)
        assertEquals(81, state.averagePercent)
        assertEquals(2, state.stars)
        assertTrue(state.cells.none { it.status == CellStatus.BLANK || it.status == CellStatus.ACTIVE })
        assertEquals(2, progressRepository.savedResults.size)

        // A late completion with nothing left to fill is ignored.
        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 50f, scoreLevel = ScoreLevel.LOW))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(2, progressRepository.savedResults.size)
    }

    @Test
    fun `next sequence moves to the next seed and starts over`() = runTest(dispatcher) {
        val (viewModel, handle) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(FillBlanksEvent.BlankCompleted(score = 90f, scoreLevel = ScoreLevel.PERFECT))

        viewModel.onEvent(FillBlanksEvent.NextSequence)

        val next = expectedSequence(seed = SEED + 1)
        val state = viewModel.uiState.value
        assertEquals(SEED + 1, handle.get<Long>(KEY_SEED))
        assertEquals(next.items.map { it.id }, state.cells.map { it.exerciseId })
        assertEquals(next.items[next.blankIndices.first()], state.activeExercise)
        assertTrue(state.results.isEmpty())
        assertFalse(state.isSequenceFinished)
    }

    @Test
    fun `hard difficulty is read from the route`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel(difficulty = Difficulty.ADVANCED.name)
        dispatcher.scheduler.advanceUntilIdle()

        val sequence = expectedSequence(difficulty = Difficulty.ADVANCED)
        assertEquals(sequence.items[sequence.blankIndices.first()], viewModel.uiState.value.activeExercise)
        assertEquals(Difficulty.ADVANCED, viewModel.uiState.value.difficulty)
    }

    @Test
    fun `a category with too few items is an error`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel(exercises = numbers.take(4))
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.error != null)
        assertNull(state.activeExercise)
        assertFalse(state.isSequenceFinished)
    }

    @Test
    fun `an unknown or drawing category is an error`() = runTest(dispatcher) {
        listOf("NOT_A_TYPE", ExerciseType.DRAWING.name, null).forEach { type ->
            val (viewModel, _) = viewModel(type = type)
            dispatcher.scheduler.advanceUntilIdle()
            assertTrue(viewModel.uiState.value.error != null)
        }
    }

    @Test
    fun `writing read as the missing item fills the blank once the finger has stayed up`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val blank = checkNotNull(viewModel.uiState.value.activeExercise)
        recognizer.reading = blank.title

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceTimeBy(RECOGNITION_PAUSE_MS - 1)
        dispatcher.scheduler.runCurrent()
        assertTrue(progressRepository.savedResults.isEmpty())

        dispatcher.scheduler.advanceUntilIdle()
        val saved = progressRepository.savedResults.single()
        assertEquals(blank.id, saved.exerciseId)
        assertEquals(FreehandScorer.score(ink, blank), saved.score)
        assertEquals(blank.id, viewModel.uiState.value.results.single().exerciseId)
    }

    @Test
    fun `writing read as another item leaves the blank to fill`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.uiState.value
        recognizer.reading = "0"

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(progressRepository.savedResults.isEmpty())
        assertEquals(before.copy(isNotRecognized = true), viewModel.uiState.value)
    }

    /** Writes ink the model reads as another item, so the check fails and the message shows. */
    private fun kotlinx.coroutines.test.TestScope.writeWrongLetter(viewModel: FillBlanksViewModel) {
        recognizer.reading = "0"
        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isNotRecognized)
    }

    @Test
    fun `the try again message goes once writing starts again`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()

        writeWrongLetter(viewModel)
        viewModel.onEvent(FillBlanksEvent.StrokeStarted)
        assertFalse(viewModel.uiState.value.isNotRecognized)

        writeWrongLetter(viewModel)
        viewModel.onEvent(FillBlanksEvent.BlankRestarted)
        assertFalse(viewModel.uiState.value.isNotRecognized)

        writeWrongLetter(viewModel)
        viewModel.onEvent(FillBlanksEvent.HintUsed)
        assertFalse(viewModel.uiState.value.isNotRecognized)
    }

    @Test
    fun `the right letter after a wrong one fills the blank without the message`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        val blank = checkNotNull(viewModel.uiState.value.activeExercise)

        writeWrongLetter(viewModel)
        viewModel.onEvent(FillBlanksEvent.BlankRestarted)
        recognizer.reading = blank.title
        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(blank.id, progressRepository.savedResults.single().exerciseId)
        assertFalse(viewModel.uiState.value.isNotRecognized)
    }

    @Test
    fun `a check for ink that was reset shows no message`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        recognizer.reading = "0"

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceTimeBy(RECOGNITION_PAUSE_MS - 1)
        viewModel.onEvent(FillBlanksEvent.BlankRestarted)
        dispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isNotRecognized)
    }

    @Test
    fun `starting another stroke during the pause waits for it`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        recognizer.reading = checkNotNull(viewModel.uiState.value.activeExercise).title

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceTimeBy(RECOGNITION_PAUSE_MS / 2)
        viewModel.onEvent(FillBlanksEvent.StrokeStarted)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(progressRepository.savedResults.isEmpty())

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, progressRepository.savedResults.size)
    }

    @Test
    fun `restarting during the pause discards the ink`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        recognizer.reading = checkNotNull(viewModel.uiState.value.activeExercise).title

        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        viewModel.onEvent(FillBlanksEvent.BlankRestarted)
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(progressRepository.savedResults.isEmpty())
        assertTrue(viewModel.uiState.value.results.isEmpty())
    }

    @Test
    fun `a hint caps a blank filled freehand too`() = runTest(dispatcher) {
        val (viewModel, _) = viewModel()
        dispatcher.scheduler.advanceUntilIdle()
        recognizer.reading = checkNotNull(viewModel.uiState.value.activeExercise).title

        viewModel.onEvent(FillBlanksEvent.HintUsed)
        viewModel.onEvent(FillBlanksEvent.InkChanged(ink))
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(progressRepository.savedResults.single().score <= HintRule.HINTED_SCORE_CAP)
        assertTrue(viewModel.uiState.value.results.single().hintUsed)
    }

    /** The first seed whose sequence has exactly [count] blanks, so a test can fill them all. */
    private fun seedWithBlanks(count: Int): Long =
        generateSequence(0L) { it + 1 }.first { expectedSequence(seed = it).blankIndices.size == count }

    private companion object {
        const val SEED = 42L
    }
}
