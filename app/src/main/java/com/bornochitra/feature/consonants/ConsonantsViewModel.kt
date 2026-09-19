package com.bornochitra.feature.consonants

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

data class ConsonantListItem(
    val id: String,
    val title: String,
    val statusText: String? = null,
)

data class ConsonantsState(
    val exercises: List<ConsonantListItem> = emptyList(),
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ConsonantsViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    val uiState: StateFlow<ConsonantsState> = exerciseRepository.observeExercises(ExerciseType.CONSONANT)
        .flatMapLatest { exercises ->
            progressRepository.observeExerciseProgress(exercises.map { it.id }).map { progressByExerciseId ->
                ConsonantsState(
                    exercises = exercises.map { exercise ->
                        ConsonantListItem(
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
            initialValue = ConsonantsState(),
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
