package com.bornochitra.feature.progress

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcLabeledProgress
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.components.letterFontFamily
import com.bornochitra.core.ui.theme.BcFeedbackGood
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val FILLED_STAR = "★"
private const val EMPTY_STAR = "☆"

/**
 * Shows how much has been learned overall and, behind a category button per section, how well each
 * exercise has gone (plan.md sections 40 and 5). Everything here comes from persisted progress, so
 * it survives a restart.
 */
@Composable
fun ProgressScreen(
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProgressContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onMenuClick = onMenuClick,
        modifier = modifier,
    )
}

@Composable
private fun ProgressContent(
    state: ProgressState,
    onEvent: (ProgressEvent) -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openCategory = state.categories.firstOrNull { it.type == state.openCategory }

    // Inside a section, back closes the section rather than leaving the screen.
    BackHandler(enabled = openCategory != null) { onEvent(ProgressEvent.CategoryClosed) }

    Scaffold(
        modifier = modifier,
        topBar = {
            BcTopAppBar(
                title = openCategory?.type?.label() ?: stringResource(R.string.title_progress),
                onBackClick = {
                    if (openCategory != null) onEvent(ProgressEvent.CategoryClosed) else onBackClick()
                },
                onMenuClick = onMenuClick,
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when {
            state.error != null -> BcEmptyState(
                title = stringResource(R.string.progress_unavailable),
                message = stringResource(state.error),
                modifier = contentModifier,
            )

            state.isLoading -> Column(
                modifier = contentModifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }

            openCategory != null -> CategoryProgressList(
                category = openCategory,
                innerPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
            )

            state.categories.isEmpty() -> BcEmptyState(
                title = stringResource(R.string.progress_empty),
                message = stringResource(R.string.progress_empty_message),
                modifier = contentModifier,
            )

            else -> CategoryButtons(
                state = state,
                onCategoryClick = { onEvent(ProgressEvent.CategoryOpened(it)) },
                modifier = contentModifier,
            )
        }
    }
}

/** The way into each section, following the Home screen's category buttons. */
@Composable
private fun CategoryButtons(
    state: ProgressState,
    onCategoryClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(BcSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
    ) {
        BcLabeledProgress(label = stringResource(R.string.overall), progress = state.overallProgress)

        state.categories.forEach { category ->
            BcPrimaryButton(
                text = category.type.label(),
                onClick = { onCategoryClick(category.type) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** How far the category has got, then how well each of its exercises has gone. */
@Composable
private fun CategoryProgressList(
    category: ProgressCategory,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    if (category.exercises.isEmpty()) {
        BcEmptyState(
            title = stringResource(R.string.empty_exercises),
            message = stringResource(R.string.empty_letters_message),
            modifier = modifier.padding(innerPadding),
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = BcSpacing.md,
            end = BcSpacing.md,
            top = innerPadding.calculateTopPadding() + BcSpacing.md,
            bottom = innerPadding.calculateBottomPadding() + BcSpacing.md,
        ),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
    ) {
        item(key = "category-${category.type}") {
            BcLabeledProgress(label = category.type.label(), progress = category.progress)
        }

        items(category.exercises, key = { it.id }) { exercise ->
            ExerciseStarsRow(
                title = exerciseTitle(exercise.id, exercise.title),
                stars = exercise.stars,
                stateText = exercise.state.label(),
            )
        }
    }
}

@Composable
private fun ExerciseStarsRow(
    title: String,
    stars: Int,
    stateText: String?,
    modifier: Modifier = Modifier,
) {
    val starsDescription = stringResource(R.string.progress_stars_description, stars, MAX_EXERCISE_STARS)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontFamily = title.letterFontFamily())
            // Stars rate how well the tracing went; this says how far the exercise has got,
            // which is what mastery is about (plan.md section 41).
            if (stateText != null) {
                Text(
                    text = stateText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Row(
            // The star glyphs read as punctuation to a screen reader, so say the rating instead.
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = starsDescription
            },
        ) {
            // Earned stars are filled as well as coloured, so the rating still reads without colour.
            Text(
                text = FILLED_STAR.repeat(stars),
                style = MaterialTheme.typography.titleLarge,
                color = BcFeedbackGood,
            )
            Text(
                text = EMPTY_STAR.repeat(MAX_EXERCISE_STARS - stars),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun LearningState.label(): String? = when (this) {
    LearningState.NOT_STARTED -> null
    LearningState.STARTED -> stringResource(R.string.status_started)
    LearningState.PRACTICING -> stringResource(R.string.status_practicing)
    LearningState.COMPLETED -> stringResource(R.string.status_completed)
    LearningState.MASTERED -> stringResource(R.string.status_mastered)
}

private val previewState = ProgressState(
    isLoading = false,
    overallProgress = 0.5f,
    categories = listOf(
        ProgressCategory(
            type = ExerciseType.VOWEL,
            progress = 0.75f,
            exercises = listOf(
                ProgressExerciseItem(id = "vowel-o", title = "অ", stars = 3, state = LearningState.MASTERED),
                ProgressExerciseItem(id = "vowel-aa", title = "আ", stars = 2, state = LearningState.COMPLETED),
                ProgressExerciseItem(id = "vowel-i", title = "ই", stars = 1, state = LearningState.PRACTICING),
                ProgressExerciseItem(id = "vowel-ii", title = "ঈ", stars = 0, state = LearningState.NOT_STARTED),
            ),
        ),
        ProgressCategory(
            type = ExerciseType.CONSONANT,
            progress = 0.2f,
            exercises = listOf(
                ProgressExerciseItem(id = "consonant-ko", title = "ক", stars = 2, state = LearningState.COMPLETED),
            ),
        ),
        ProgressCategory(
            type = ExerciseType.DRAWING,
            progress = 0f,
            exercises = listOf(
                ProgressExerciseItem(id = "drawing-line", title = "Line", stars = 0, state = LearningState.NOT_STARTED),
            ),
        ),
    ),
)

@Preview(showBackground = true, name = "Categories")
@Composable
private fun ProgressScreenPreview() {
    BornoChitraTheme {
        ProgressContent(state = previewState, onEvent = {}, onBackClick = {}, onMenuClick = {})
    }
}

@Preview(showBackground = true, name = "Vowel section")
@Composable
private fun ProgressScreenCategoryPreview() {
    BornoChitraTheme {
        ProgressContent(
            state = previewState.copy(openCategory = ExerciseType.VOWEL),
            onEvent = {},
            onBackClick = {},
            onMenuClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ProgressScreenLoadingPreview() {
    BornoChitraTheme {
        ProgressContent(state = ProgressState(), onEvent = {}, onBackClick = {}, onMenuClick = {})
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ProgressScreenEmptyPreview() {
    BornoChitraTheme {
        ProgressContent(state = ProgressState(isLoading = false), onEvent = {}, onBackClick = {}, onMenuClick = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ProgressScreenErrorPreview() {
    BornoChitraTheme {
        ProgressContent(
            state = ProgressState(isLoading = false, error = R.string.error_progress_load),
            onEvent = {},
            onBackClick = {},
            onMenuClick = {},
        )
    }
}
