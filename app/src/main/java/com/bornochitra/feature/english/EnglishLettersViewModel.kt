package com.bornochitra.feature.english

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class EnglishLettersState(
    val type: ExerciseType,
    val grid: CategoryGridState = CategoryGridState(),
)

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/** Must match [com.bornochitra.app.navigation.BcDestination.EnglishLetters.ARG_TYPE]. */
private const val ARG_TYPE = "type"

/**
 * The English small letters a-z or capitals A-Z, as the route asks, each with how far the child has
 * got with it (plan.md Step 3).
 */
@HiltViewModel
class EnglishLettersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    exerciseRepository: ExerciseRepository,
    progressRepository: ProgressRepository,
) : ViewModel() {

    private val type: ExerciseType = ExerciseType.valueOf(checkNotNull(savedStateHandle[ARG_TYPE]))

    val uiState: StateFlow<EnglishLettersState> = observeCategoryGrid(type, exerciseRepository, progressRepository)
        .map { EnglishLettersState(type = type, grid = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
            initialValue = EnglishLettersState(type = type),
        )
}
