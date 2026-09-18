package com.bornochitra.feature.practice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.PracticeResult
import com.bornochitra.core.model.ScoreLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Must match [com.bornochitra.app.navigation.BcDestination.Practice.ARG_EXERCISE_ID]. */
private const val ARG_EXERCISE_ID = "exerciseId"

data class PracticeState(
    val exercise: Exercise? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isExerciseCompleted: Boolean = false,
    val score: Float? = null,
    val scoreLevel: ScoreLevel? = null,
    val sessionId: Long? = null,
)

/**
 * Only the low-frequency lifecycle event the tracing engine reports crosses into MVI; per-touch
 * pointer events stay local to the Composable (see plan.md section 24 and [PracticeScreen]).
 */
sealed interface PracticeEvent {
    data class ExerciseCompleted(val score: Float, val scoreLevel: ScoreLevel) : PracticeEvent
}

@HiltViewModel
class PracticeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val exerciseId: String = checkNotNull(savedStateHandle[ARG_EXERCISE_ID])
    private var startedAtMs: Long? = null

    private val mutableState = MutableStateFlow(PracticeState())
    val uiState: StateFlow<PracticeState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExercise(exerciseId)
            mutableState.value = if (exercise != null) {
                startedAtMs = System.currentTimeMillis()
                PracticeState(exercise = exercise, isLoading = false)
            } else {
                PracticeState(isLoading = false, error = "We couldn't find that exercise.")
            }
        }
    }

    fun onEvent(event: PracticeEvent) {
        when (event) {
            is PracticeEvent.ExerciseCompleted -> onExerciseCompleted(event.score, event.scoreLevel)
        }
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
            mutableState.value = mutableState.value.copy(
                isExerciseCompleted = true,
                score = score,
                scoreLevel = scoreLevel,
                sessionId = sessionId,
            )
        }
    }
}
