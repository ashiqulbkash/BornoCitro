package com.bornochitra.feature.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.content.ExerciseRepository
import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.feature.category.CategoryGridState
import com.bornochitra.feature.category.observeCategoryGrid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/** The drawings, each with how far the child has got with it. */
@HiltViewModel
class DrawingViewModel @Inject constructor(
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    val uiState: StateFlow<CategoryGridState> = observeCategoryGrid(ExerciseType.DRAWING, exerciseRepository, progressRepository)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = CategoryGridState(),
        )
}
