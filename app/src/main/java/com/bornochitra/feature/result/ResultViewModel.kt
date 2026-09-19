package com.bornochitra.feature.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.tips.TipSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

/** Must match [com.bornochitra.app.navigation.BcDestination.Result.ARG_SESSION_ID]. */
private const val ARG_SESSION_ID = "sessionId"

/** The finished attempt this screen reports on, as the UI needs it. */
data class ResultAttempt(
    val exerciseId: String,
    val title: String,
    val scorePercent: Int,
    val scoreLevel: ScoreLevel,
    val nextExerciseId: String?,
    val tip: ContextualTip?,
)

data class ResultState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val attempt: ResultAttempt? = null,
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
    private val progressRepository: ProgressRepository,
    private val tipSelector: TipSelector,
) : ViewModel() {

    private val sessionId: Long? = savedStateHandle.get<String>(ARG_SESSION_ID)?.toLongOrNull()

    private val mutableState = MutableStateFlow(ResultState())
    val uiState: StateFlow<ResultState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            mutableState.value = loadResult()
        }
    }

    private suspend fun loadResult(): ResultState {
        val result = sessionId?.let { progressRepository.getPracticeResult(it) }
            ?: return ResultState(isLoading = false, error = "We couldn't find that practice result.")
        val exercise = exerciseRepository.getExercise(result.exerciseId)
            ?: return ResultState(isLoading = false, error = "We couldn't find that exercise.")

        return ResultState(
            isLoading = false,
            attempt = ResultAttempt(
                exerciseId = exercise.id,
                title = exercise.title,
                scorePercent = result.score.roundToInt(),
                scoreLevel = result.scoreLevel,
                nextExerciseId = nextExerciseIdAfter(exercise),
                tip = tipSelector.afterAttempt(
                    difficulty = exercise.difficulty,
                    recentScores = progressRepository
                        .getRecentResults(exercise.id, tipSelector.recentResultsNeeded)
                        .map { it.score },
                ),
            ),
        )
    }

    /** The following exercise in the same category, so "Next" keeps the child in one learning set. */
    private suspend fun nextExerciseIdAfter(exercise: Exercise): String? =
        exerciseRepository.observeExercises(exercise.type).first()
            .filter { it.order > exercise.order }
            .minByOrNull { it.order }
            ?.id
}
