package com.bornochitra.feature.vowels

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
fun VowelsScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VowelsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VowelsContent(
        state = state,
        onBackClick = onBackClick,
        onExerciseClick = onExerciseClick,
        modifier = modifier,
    )
}

@Composable
private fun VowelsContent(
    state: VowelsState,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = ExerciseType.VOWEL.label(), onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.exercises.isEmpty()) {
            BcEmptyState(
                title = stringResource(R.string.empty_vowels),
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
private fun VowelsScreenPreview() {
    BornoChitraTheme {
        VowelsContent(
            state = VowelsState(
                exercises = listOf(
                    VowelListItem(id = "vowel-o", title = "অ", learningState = LearningState.COMPLETED, attemptCount = 1),
                    VowelListItem(id = "vowel-aa", title = "আ", learningState = LearningState.PRACTICING, attemptCount = 2),
                    VowelListItem(id = "vowel-i", title = "ই", learningState = LearningState.MASTERED, attemptCount = 3),
                    VowelListItem(id = "vowel-ii", title = "ঈ"),
                ),
            ),
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun VowelsScreenEmptyPreview() {
    BornoChitraTheme {
        VowelsContent(state = VowelsState(), onBackClick = {}, onExerciseClick = {})
    }
}
