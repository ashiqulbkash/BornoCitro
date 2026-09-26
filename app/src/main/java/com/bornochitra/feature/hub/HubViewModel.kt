package com.bornochitra.feature.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HubState(
    /** Each category's finished share, the same value as its bar on Progress. */
    val categoryProgress: Map<ExerciseType, Float> = emptyMap(),
) {
    fun progressOf(type: ExerciseType): Float = categoryProgress[type] ?: 0f
}

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/** The progress on a hub's category rows. Both hubs use it; Math shows the same progress in each. */
@HiltViewModel
class HubViewModel @Inject constructor(
    progressRepository: ProgressRepository,
) : ViewModel() {

    val uiState: StateFlow<HubState> = progressRepository.observeProgress()
        .map { progress -> HubState(categoryProgress = ExerciseType.entries.associateWith { progress.progressOf(it) }) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = HubState(),
        )
}
