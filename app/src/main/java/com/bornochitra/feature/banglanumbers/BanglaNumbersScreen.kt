package com.bornochitra.feature.banglanumbers

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
fun BanglaNumbersScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BanglaNumbersViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LetterGridScreen(
        title = ExerciseType.BANGLA_NUMBER.label(),
        state = state,
        emptyTitle = stringResource(R.string.empty_numbers),
        emptyMessage = stringResource(R.string.empty_numbers_message),
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}
