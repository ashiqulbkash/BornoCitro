package com.bornochitra.feature.consonants

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
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.components.learningStatusText
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val GRID_COLUMNS = 3

@Composable
fun ConsonantsScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConsonantsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ConsonantsContent(
        state = state,
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}

@Composable
private fun ConsonantsContent(
    state: ConsonantsState,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = ExerciseType.CONSONANT.label(), onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.exercises.isEmpty()) {
            BcEmptyState(
                title = stringResource(R.string.empty_consonants),
                message = stringResource(R.string.empty_letters_message),
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
private fun ConsonantsScreenPreview() {
    BornoChitraTheme {
        ConsonantsContent(
            state = ConsonantsState(
                exercises = listOf(
                    ConsonantListItem(id = "consonant-ko", title = "ক", learningState = LearningState.COMPLETED, attemptCount = 1),
                    ConsonantListItem(id = "consonant-kho", title = "খ", learningState = LearningState.PRACTICING, attemptCount = 2),
                    ConsonantListItem(id = "consonant-go", title = "গ", learningState = LearningState.MASTERED, attemptCount = 3),
                    ConsonantListItem(id = "consonant-gho", title = "ঘ"),
                ),
            ),
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ConsonantsScreenEmptyPreview() {
    BornoChitraTheme {
        ConsonantsContent(state = ConsonantsState(), onBackClick = {}, onExerciseClick = {})
    }
}
