package com.bornochitra.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.database.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeState(
    val overallProgress: Float = 0f,
    val vowelProgress: Float = 0f,
    val consonantProgress: Float = 0f,
    val englishSmallProgress: Float = 0f,
    val englishCapitalProgress: Float = 0f,
    val drawingProgress: Float = 0f,
    val continueExerciseId: String? = null,
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

@HiltViewModel
class HomeViewModel @Inject constructor(
    progressRepository: ProgressRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeState> = progressRepository.observeProgress()
        .map { progress ->
            HomeState(
                overallProgress = progress.overallProgress,
                vowelProgress = progress.vowelProgress,
                consonantProgress = progress.consonantProgress,
                englishSmallProgress = progress.englishSmallProgress,
                englishCapitalProgress = progress.englishCapitalProgress,
                drawingProgress = progress.drawingProgress,
                continueExerciseId = progress.continueExerciseId,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = HomeState(),
        )
}
