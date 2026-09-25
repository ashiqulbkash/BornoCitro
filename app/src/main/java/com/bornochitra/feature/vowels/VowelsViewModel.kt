package com.bornochitra.feature.vowels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
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

data class VowelListItem(
    val id: String,
    val title: String,
    val learningState: LearningState = LearningState.NOT_STARTED,
    val attemptCount: Int = 0,
)

data class VowelsState(
    val exercises: List<VowelListItem> = emptyList(),
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VowelsViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    val uiState: StateFlow<VowelsState> = exerciseRepository.observeExercises(ExerciseType.VOWEL)
        .flatMapLatest { exercises ->
            progressRepository.observeExerciseProgress(exercises.map { it.id }).map { progressByExerciseId ->
                VowelsState(
                    exercises = exercises.map { exercise ->
                        VowelListItem(
                            id = exercise.id,
                            title = exercise.title,
                            learningState = progressByExerciseId[exercise.id].toLearningState(),
                            attemptCount = progressByExerciseId[exercise.id]?.attemptCount ?: 0,
                        )
                    },
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = VowelsState(),
        )
}
