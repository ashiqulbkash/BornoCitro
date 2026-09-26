package com.bornochitra.feature.drawing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseTile
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.components.learningStatusText
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val GRID_COLUMNS = 3

@Composable
fun DrawingScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DrawingContent(
        state = state,
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}

@Composable
private fun DrawingContent(
    state: DrawingState,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = ExerciseType.DRAWING.label(), onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.exercises.isEmpty()) {
            BcEmptyState(
                title = stringResource(R.string.empty_drawings),
                message = stringResource(R.string.empty_drawings_message),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_COLUMNS),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = BcSpacing.m,
                    end = BcSpacing.m,
                    top = innerPadding.calculateTopPadding() + BcSpacing.m,
                    bottom = innerPadding.calculateBottomPadding() + BcSpacing.m,
                ),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
            ) {
                items(state.exercises, key = { it.id }) { item ->
                    BcExerciseTile(
                        label = exerciseTitle(item.id, item.title),
                        statusText = learningStatusText(item.learningState, item.attemptCount),
                        onClick = { onExerciseClick(item.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "With progress")
@Composable
private fun DrawingScreenPreview() {
    BornoChitraTheme {
        DrawingContent(
            state = DrawingState(
                exercises = listOf(
                    DrawingListItem(id = "drawing-line", title = "Line", learningState = LearningState.COMPLETED, attemptCount = 1),
                    DrawingListItem(id = "drawing-circle", title = "Circle", learningState = LearningState.PRACTICING, attemptCount = 2),
                    DrawingListItem(id = "drawing-square", title = "Square", learningState = LearningState.MASTERED, attemptCount = 3),
                    DrawingListItem(id = "drawing-triangle", title = "Triangle"),
                ),
            ),
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun DrawingScreenEmptyPreview() {
    BornoChitraTheme {
        DrawingContent(state = DrawingState(), onBackClick = {}, onExerciseClick = {})
    }
}
