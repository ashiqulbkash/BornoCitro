package com.bornochitra.feature.fillblanks

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.R
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.recognition.FreehandChecker
import com.bornochitra.core.tracing.TracePoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import javax.inject.Inject

/** Must match [com.bornochitra.app.navigation.BcDestination.FillBlanksSequence.ARG_TYPE]. */
private const val ARG_TYPE = "type"

/** Must match [com.bornochitra.app.navigation.BcDestination.FillBlanksSequence.ARG_DIFFICULTY]. */
private const val ARG_DIFFICULTY = "difficulty"

/** Kept in the saved state so a recreated screen rebuilds the same sequence. */
internal const val KEY_SEED = "seed"

/**
 * How long the writer's finger has to stay up before the ink is read. Long enough to reach for the
 * next stroke of a letter, so a half-written letter is not read on its own.
 */
internal const val RECOGNITION_PAUSE_MS = 800L

enum class CellStatus {
    /** An item of the sequence the child is shown. */
    SHOWN,

    /** A blank still to be filled in. */
    BLANK,

    /** The blank being traced now. */
    ACTIVE,

    /** A blank the child has filled in. */
    FILLED,
}

data class SequenceCell(
    val exerciseId: String,
    val title: String,
    val status: CellStatus,
)

data class BlankResult(
    val exerciseId: String,
    val score: Float,
    val scoreLevel: ScoreLevel,
    val hintUsed: Boolean,
)

data class FillBlanksState(
    val type: ExerciseType? = null,
    val isLoading: Boolean = true,
    @StringRes val error: Int? = null,
    val cells: List<SequenceCell> = emptyList(),
    /** The missing item being traced, or null once every blank is filled. */
    val activeExercise: Exercise? = null,
    /** Which blank attempt is on the canvas; a new value clears the canvas's ink. */
    val attemptId: Int = 0,
    val isHintShown: Boolean = false,
    /** The last check read the writing as something other than the missing item; cleared by the next stroke. */
    val isNotRecognized: Boolean = false,
    val results: List<BlankResult> = emptyList(),
) {
    val isSequenceFinished: Boolean get() = !isLoading && error == null && activeExercise == null && results.isNotEmpty()

    val averagePercent: Int get() = if (results.isEmpty()) 0 else results.map { it.score }.average().roundToInt()
}

sealed interface FillBlanksEvent {
    /** The active blank was traced to completion with the engine's [score] and [scoreLevel]. */
    data class BlankCompleted(val score: Float, val scoreLevel: ScoreLevel) : FillBlanksEvent

    /** The child asked to see the active blank's dotted guide. */
    data object HintUsed : FillBlanksEvent

    /** A new stroke began on the active blank, so the writing is not finished yet. */
    data object StrokeStarted : FillBlanksEvent

    /** A stroke was lifted; [ink] is everything written on the active blank so far, one list per stroke. */
    data class InkChanged(val ink: List<List<TracePoint>>) : FillBlanksEvent

    /** The child started the active blank over. */
    data object BlankRestarted : FillBlanksEvent

    /** The child asked for a new sequence from the same category. */
    data object NextSequence : FillBlanksEvent
}

/**
 * Runs fill-in-the-blanks for one category (plan.md Step 6): builds a sequence with
 * [BlankSequenceGenerator], steps through its blanks in order, applies [HintRule], and saves each
 * filled blank as a practice result for the missing item so it counts towards that item's progress.
 *
 * A blank is filled either by covering its hidden guide, as in tracing, or by writing it freehand
 * in any style: once the finger has stayed up for [RECOGNITION_PAUSE_MS], [FreehandChecker] reads
 * the ink, and writing that reads as the missing item completes the blank with its shape score.
 */
@HiltViewModel
class FillBlanksViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val progressRepository: ProgressRepository,
    private val freehandChecker: FreehandChecker,
) : ViewModel() {

    private val type: ExerciseType? = savedStateHandle.get<String>(ARG_TYPE)
        ?.let { name -> ExerciseType.entries.find { it.name == name } }
    private val difficulty: Difficulty = savedStateHandle.get<String>(ARG_DIFFICULTY)
        ?.let { name -> Difficulty.entries.find { it.name == name } }
        ?: Difficulty.BEGINNER

    private var sequenceItems: List<Exercise> = emptyList()
    private var sequence: BlankSequence? = null
    private var blankStartedAtMs: Long = 0L
    private var recognitionJob: Job? = null

    private val mutableState = MutableStateFlow(FillBlanksState(type = type))
    val uiState: StateFlow<FillBlanksState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch { load() }
    }

    fun onEvent(event: FillBlanksEvent) {
        when (event) {
            is FillBlanksEvent.BlankCompleted -> {
                cancelRecognition()
                onBlankCompleted(event.score, event.scoreLevel)
            }
            FillBlanksEvent.HintUsed ->
                mutableState.value = mutableState.value.copy(isHintShown = true, isNotRecognized = false)
            FillBlanksEvent.StrokeStarted -> {
                cancelRecognition()
                mutableState.value = mutableState.value.copy(isNotRecognized = false)
            }
            is FillBlanksEvent.InkChanged -> onInkChanged(event.ink)
            FillBlanksEvent.BlankRestarted -> {
                cancelRecognition()
                mutableState.value = mutableState.value.copy(
                    attemptId = mutableState.value.attemptId + 1,
                    isNotRecognized = false,
                )
            }
            FillBlanksEvent.NextSequence -> {
                cancelRecognition()
                onNextSequence()
            }
        }
    }

    private suspend fun load() {
        if (type == null || type == ExerciseType.DRAWING) {
            mutableState.value = FillBlanksState(type = type, isLoading = false, error = R.string.error_no_sequences)
            return
        }
        sequenceItems = exerciseRepository.observeExercises(type).first().sequenceItems(type)
        if (sequenceItems.size < BlankSequenceGenerator.MIN_WINDOW) {
            mutableState.value = FillBlanksState(type = type, isLoading = false, error = R.string.error_too_few_items)
            return
        }
        val seed = savedStateHandle.get<Long>(KEY_SEED) ?: System.currentTimeMillis().also { savedStateHandle[KEY_SEED] = it }
        startSequence(seed)
    }

    private fun startSequence(seed: Long) {
        val next = BlankSequenceGenerator.generate(sequenceItems, difficulty, seed)
        sequence = next
        blankStartedAtMs = System.currentTimeMillis()
        mutableState.value = FillBlanksState(
            type = type,
            isLoading = false,
            cells = next.cells(filledCount = 0),
            activeExercise = next.items[next.blankIndices.first()],
            attemptId = mutableState.value.attemptId + 1,
        )
    }

    private fun onNextSequence() {
        if (sequence == null) return
        val seed = (savedStateHandle.get<Long>(KEY_SEED) ?: 0L) + 1
        savedStateHandle[KEY_SEED] = seed
        startSequence(seed)
    }

    private fun onInkChanged(ink: List<List<TracePoint>>) {
        cancelRecognition()
        val current = mutableState.value
        val exercise = current.activeExercise ?: return
        recognitionJob = viewModelScope.launch {
            delay(RECOGNITION_PAUSE_MS)
            val result = freehandChecker.check(ink, exercise, sequenceItems)
            // The ink belongs to the attempt it was drawn in; a reset or a finished blank replaces it.
            if (mutableState.value.attemptId != current.attemptId) return@launch
            if (result == null) {
                mutableState.value = mutableState.value.copy(isNotRecognized = true)
            } else {
                onBlankCompleted(result.score, result.level)
            }
        }
    }

    private fun cancelRecognition() {
        recognitionJob?.cancel()
        recognitionJob = null
    }

    private fun onBlankCompleted(score: Float, scoreLevel: ScoreLevel) {
        val current = mutableState.value
        val exercise = current.activeExercise ?: return
        val blanks = sequence?.blankIndices ?: return
        val result = BlankResult(
            exerciseId = exercise.id,
            score = HintRule.score(score, current.isHintShown),
            scoreLevel = HintRule.level(score, scoreLevel, current.isHintShown),
            hintUsed = current.isHintShown,
        )
        val results = current.results + result
        val nextBlank = blanks.getOrNull(results.size)
        val completedAtMs = System.currentTimeMillis()
        val durationMs = completedAtMs - blankStartedAtMs
        blankStartedAtMs = completedAtMs
        mutableState.value = current.copy(
            cells = checkNotNull(sequence).cells(filledCount = results.size),
            activeExercise = nextBlank?.let { checkNotNull(sequence).items[it] },
            attemptId = current.attemptId + 1,
            isHintShown = false,
            isNotRecognized = false,
            results = results,
        )
        viewModelScope.launch {
            progressRepository.savePracticeResult(
                PracticeResult(
                    exerciseId = result.exerciseId,
                    score = result.score,
                    scoreLevel = result.scoreLevel,
                    completed = true,
                    durationMs = durationMs,
                    completedAtMs = completedAtMs,
                ),
            )
        }
    }
}

/** The math category also holds the operators, which are not part of the 1-20 sequence. */
private fun List<Exercise>.sequenceItems(type: ExerciseType): List<Exercise> = when (type) {
    ExerciseType.MATH -> filter { exercise -> exercise.title.all { it.isDigit() } }
    else -> this
}

private fun BlankSequence.cells(filledCount: Int): List<SequenceCell> = items.mapIndexed { index, item ->
    val blankNumber = blankIndices.indexOf(index)
    val status = when {
        blankNumber == -1 -> CellStatus.SHOWN
        blankNumber < filledCount -> CellStatus.FILLED
        blankNumber == filledCount -> CellStatus.ACTIVE
        else -> CellStatus.BLANK
    }
    SequenceCell(exerciseId = item.id, title = item.title, status = status)
}
