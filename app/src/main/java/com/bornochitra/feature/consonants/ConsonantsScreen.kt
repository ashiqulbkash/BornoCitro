package com.bornochitra.feature.consonants

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.label
import com.bornochitra.feature.category.LetterGridScreen

@Composable
fun ConsonantsScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConsonantsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LetterGridScreen(
        title = ExerciseType.CONSONANT.label(),
        state = state,
        emptyTitle = stringResource(R.string.empty_consonants),
        emptyMessage = stringResource(R.string.empty_letters_message),
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}
