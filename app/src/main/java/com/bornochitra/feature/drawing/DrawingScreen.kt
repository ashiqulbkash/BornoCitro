package com.bornochitra.feature.drawing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.label
import com.bornochitra.feature.category.DrawingGridScreen

@Composable
fun DrawingScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DrawingGridScreen(
        title = ExerciseType.DRAWING.label(),
        state = state,
        emptyTitle = stringResource(R.string.empty_drawings),
        emptyMessage = stringResource(R.string.empty_drawings_message),
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}
