package com.bornochitra.feature.banglanumbers

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseTile
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val GRID_COLUMNS = 3

@Composable
fun BanglaNumbersScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BanglaNumbersViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BanglaNumbersContent(
        state = state,
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}

@Composable
private fun BanglaNumbersContent(
    state: BanglaNumbersState,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = ExerciseType.BANGLA_NUMBER.label(), onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.exercises.isEmpty()) {
            BcEmptyState(
                title = "No numbers yet",
                message = "Check back soon for numbers to practice.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_COLUMNS),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = BcSpacing.md,
                    end = BcSpacing.md,
                    top = innerPadding.calculateTopPadding() + BcSpacing.md,
                    bottom = innerPadding.calculateBottomPadding() + BcSpacing.md,
                ),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
            ) {
                items(state.exercises, key = { it.id }) { item ->
                    BcExerciseTile(
                        label = item.title,
                        statusText = item.statusText,
                        onClick = { onExerciseClick(item.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "With progress")
@Composable
private fun BanglaNumbersScreenPreview() {
    BornoChitraTheme {
        BanglaNumbersContent(
            state = BanglaNumbersState(
                exercises = listOf(
                    BanglaNumberListItem(id = "bangla-number-1", title = "১", statusText = "Completed"),
                    BanglaNumberListItem(id = "bangla-number-2", title = "২", statusText = "2 attempts"),
                    BanglaNumberListItem(id = "bangla-number-3", title = "৩", statusText = null),
                ),
            ),
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun BanglaNumbersScreenEmptyPreview() {
    BornoChitraTheme {
        BanglaNumbersContent(state = BanglaNumbersState(), onBackClick = {}, onExerciseClick = {})
    }
}
