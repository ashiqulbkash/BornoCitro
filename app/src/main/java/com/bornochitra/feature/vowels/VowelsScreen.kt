package com.bornochitra.feature.vowels

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
fun VowelsScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VowelsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LetterGridScreen(
        title = ExerciseType.VOWEL.label(),
        state = state,
        emptyTitle = stringResource(R.string.empty_vowels),
        emptyMessage = stringResource(R.string.empty_letters_message),
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}
