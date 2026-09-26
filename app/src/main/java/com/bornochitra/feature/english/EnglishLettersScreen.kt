package com.bornochitra.feature.english

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.feature.category.LetterGridScreen

@Composable
fun EnglishLettersScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EnglishLettersViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LetterGridScreen(
        title = state.type.label(),
        state = state.grid,
        emptyTitle = stringResource(R.string.empty_letters),
        emptyMessage = stringResource(R.string.empty_letters_message),
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        progressColor = BcTheme.colors.english,
        modifier = modifier,
    )
}
