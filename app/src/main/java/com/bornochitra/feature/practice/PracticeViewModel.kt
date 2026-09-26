package com.bornochitra.feature.practice

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.R
import com.bornochitra.core.analytics.AnalyticsEvent
import com.bornochitra.core.analytics.AnalyticsTracker
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.SessionScores
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.tips.TipSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import javax.inject.Inject

/** Must match [com.bornochitra.app.navigation.BcDestination.Practice.ARG_EXERCISE_ID]. */
private const val ARG_EXERCISE_ID = "exerciseId"

data class PracticeState(
    val exercise: Exercise? = null,
    val isLoading: Boolean = true,
    @StringRes val error: Int? = null,
    val isExerciseCompleted: Boolean = false,
    val score: Float? = null,
    val scoreLevel: ScoreLevel? = null,
    val sessionId: Long? = null,
    val tip: ContextualTip? = null,
    /**
     * Which attempt is being traced. A restart reuses the same exercise and stroke instances, so
     * this is what tells the tracing UI that the ink and progress on screen belong to an attempt
     * that is over and must be cleared.
     */
    val attemptId: Int = 0,
    /** The score of every try completed in this session, oldest first. */
    val sessionScores: List<Float> = emptyList(),
    /** Where the exercise sits in its category, from 1, for the top bar's "২/১১". */
    val categoryPosition: Int = 0,
    val categorySize: Int = 0,
    /** Whether the child has written anything in this attempt, so starting over would wipe it. */
    val hasInk: Boolean = false,
    val isRestartConfirmationShown: Boolean = false,
)

/**
 * Only the low-frequency lifecycle event the tracing engine reports crosses into MVI; per-touch
 * pointer events stay local to the Composable (see plan.md section 24 and [PracticeScreen]).
 */
sealed interface PracticeEvent {
    data class ExerciseCompleted(val score: Float, val scoreLevel: ScoreLevel) : PracticeEvent

    /** The finger was lifted with the letter or drawing still unfinished; the trace so far is kept. */
    data object TraceUnfinished : PracticeEvent

    /** The finger touched the canvas: this attempt now has ink. */
    data object StrokeStarted : PracticeEvent

    /** The child tapped Reset. With ink on the canvas this asks first; without, it starts over at once. */
    data object RestartRequested : PracticeEvent

    /** The child agreed to wipe the ink and start over. */
    data object RestartConfirmed : PracticeEvent

    /** The child chose to keep writing. */
    data object RestartDismissed : PracticeEvent
}

@HiltViewModel
class PracticeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val progressRepository: ProgressRepository,
    private val tipSelector: TipSelector,
    private val analytics: AnalyticsTracker,
) : ViewModel() {

    private val exerciseId: String = checkNotNull(savedStateHandle[ARG_EXERCISE_ID])
    private val earlierSessionScores = SessionScores.decode(savedStateHandle[SessionScores.ARG])
    private var startedAtMs: Long? = null
    private var previousAttempts = 0
    private var wasMastered = false

    private val mutableState = MutableStateFlow(PracticeState())
    val uiState: StateFlow<PracticeState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExercise(exerciseId)
            mutableState.value = if (exercise != null) {
                startedAtMs = System.currentTimeMillis()
                val progress = progressRepository.observeExerciseProgress(listOf(exerciseId)).first()[exerciseId]
                previousAttempts = progress?.attemptCount ?: 0
                wasMastered = progress?.isMastered == true
                analytics.track(AnalyticsEvent.ExerciseStarted(exercise.id, exercise.type))
                if (previousAttempts > 0) analytics.track(AnalyticsEvent.PracticeRepeated(exercise.id))
                val category = exerciseRepository.observeExercises(exercise.type).first().sortedBy { it.order }
                PracticeState(
                    exercise = exercise,
                    isLoading = false,
                    tip = tipSelector.beforeFirstAttempt(previousAttempts),
                    sessionScores = earlierSessionScores,
                    categoryPosition = category.indexOfFirst { it.id == exercise.id } + 1,
                    categorySize = category.size,
                )
            } else {
                PracticeState(isLoading = false, error = R.string.error_exercise_not_found)
            }
        }
    }

    fun onEvent(event: PracticeEvent) {
        when (event) {
            is PracticeEvent.ExerciseCompleted -> onExerciseCompleted(event.score, event.scoreLevel)
            PracticeEvent.TraceUnfinished -> onTraceUnfinished()
            PracticeEvent.StrokeStarted -> mutableState.value = mutableState.value.copy(hasInk = true)
            PracticeEvent.RestartRequested -> onRestartRequested()
            PracticeEvent.RestartConfirmed -> restart()
            PracticeEvent.RestartDismissed -> mutableState.value = mutableState.value.copy(isRestartConfirmationShown = false)
        }
    }

    private fun onRestartRequested() {
        if (mutableState.value.hasInk) {
            mutableState.value = mutableState.value.copy(isRestartConfirmationShown = true)
        } else {
            restart()
        }
    }

    private fun onTraceUnfinished() {
        mutableState.value = mutableState.value.copy(tip = tipSelector.afterUnfinishedTrace())
    }

    private fun restart() {
        analytics.track(AnalyticsEvent.PracticeRepeated(exerciseId))
        mutableState.value = mutableState.value.copy(
            tip = tipSelector.beforeFirstAttempt(previousAttempts),
            attemptId = mutableState.value.attemptId + 1,
            hasInk = false,
            isRestartConfirmationShown = false,
        )
    }

    private fun onExerciseCompleted(score: Float, scoreLevel: ScoreLevel) {
        viewModelScope.launch {
            val completedAtMs = System.currentTimeMillis()
            val sessionId = progressRepository.savePracticeResult(
                PracticeResult(
                    exerciseId = exerciseId,
                    score = score,
                    scoreLevel = scoreLevel,
                    completed = true,
                    durationMs = completedAtMs - (startedAtMs ?: completedAtMs),
                    completedAtMs = completedAtMs,
                ),
            )
            trackCompletion(score, scoreLevel, durationMs = completedAtMs - (startedAtMs ?: completedAtMs))
            mutableState.value = mutableState.value.copy(
                isExerciseCompleted = true,
                score = score,
                scoreLevel = scoreLevel,
                sessionId = sessionId,
                sessionScores = mutableState.value.sessionScores + score,
            )
        }
    }

    private suspend fun trackCompletion(score: Float, scoreLevel: ScoreLevel, durationMs: Long) {
        val type = mutableState.value.exercise?.type ?: return
        analytics.track(AnalyticsEvent.ExerciseCompleted(exerciseId, type, durationMs))
        analytics.track(AnalyticsEvent.ScoreReceived(exerciseId, score.roundToInt(), scoreLevel))
        if (type == ExerciseType.DRAWING) analytics.track(AnalyticsEvent.DrawingCompleted(exerciseId))

        val isMastered = progressRepository.observeExerciseProgress(listOf(exerciseId)).first()[exerciseId]?.isMastered == true
        if (isMastered && !wasMastered) analytics.track(AnalyticsEvent.ExerciseMastered(exerciseId))
        wasMastered = isMastered
    }
}
