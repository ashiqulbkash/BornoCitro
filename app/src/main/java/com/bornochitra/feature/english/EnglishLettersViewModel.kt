package com.bornochitra.feature.english

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.toLearningState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class EnglishLetterListItem(
    val id: String,
    val title: String,
    val statusText: String? = null,
)

data class EnglishLettersState(
    val type: ExerciseType,
    val exercises: List<EnglishLetterListItem> = emptyList(),
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/** Must match [com.bornochitra.app.navigation.BcDestination.EnglishLetters.ARG_TYPE]. */
private const val ARG_TYPE = "type"

/**
 * The English small letters a-z or capitals A-Z, as the route asks, each with how far the child has
 * got with it (plan.md Step 3).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EnglishLettersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    private val type: ExerciseType = ExerciseType.valueOf(checkNotNull(savedStateHandle[ARG_TYPE]))

    val uiState: StateFlow<EnglishLettersState> = exerciseRepository.observeExercises(type)
        .flatMapLatest { exercises ->
            progressRepository.observeExerciseProgress(exercises.map { it.id }).map { progressByExerciseId ->
                EnglishLettersState(
                    type = type,
                    exercises = exercises.map { exercise ->
                        EnglishLetterListItem(
                            id = exercise.id,
                            title = exercise.title,
                            statusText = progressByExerciseId[exercise.id].toStatusText(),
                        )
                    },
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = EnglishLettersState(type = type),
        )
}

private fun ExerciseProgress?.toStatusText(): String? {
    val progress = this ?: return null
    return when (progress.toLearningState()) {
        LearningState.NOT_STARTED -> null
        LearningState.STARTED -> "1 attempt"
        LearningState.PRACTICING -> "${progress.attemptCount} attempts"
        LearningState.COMPLETED -> "Completed"
        LearningState.MASTERED -> "Mastered"
    }
}
