package com.bornochitra.feature.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcLabeledProgress
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcFeedbackGood
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val FILLED_STAR = "★"
private const val EMPTY_STAR = "☆"

/**
 * Shows how much of each category has been learned and how well each exercise has gone
 * (plan.md section 40). Everything here comes from persisted progress, so it survives a restart.
 */
@Composable
fun ProgressScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProgressContent(state = state, onBackClick = onBackClick, modifier = modifier)
}

@Composable
private fun ProgressContent(
    state: ProgressState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "My Progress", onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.categories.isEmpty()) {
            BcEmptyState(
                title = "No progress yet",
                message = "Practice a letter and your stars will show up here.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = BcSpacing.md,
                    end = BcSpacing.md,
                    top = innerPadding.calculateTopPadding() + BcSpacing.md,
                    bottom = innerPadding.calculateBottomPadding() + BcSpacing.md,
                ),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
            ) {
                item(key = "overall") {
                    BcLabeledProgress(label = "Overall", progress = state.overallProgress)
                }

                state.categories.forEach { category ->
                    item(key = "category-${category.type}") {
                        BcLabeledProgress(label = category.type.label(), progress = category.progress)
                    }

                    items(category.exercises, key = { it.id }) { exercise ->
                        ExerciseStarsRow(
                            title = exercise.title,
                            stars = exercise.stars,
                            stateText = exercise.state.label(),
                        )
                    }
                }
            }
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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
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
                contentDescription = "$stars of $MAX_EXERCISE_STARS stars"
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

private fun LearningState.label(): String? = when (this) {
    LearningState.NOT_STARTED -> null
    LearningState.STARTED -> "Started"
    LearningState.PRACTICING -> "Practicing"
    LearningState.COMPLETED -> "Completed"
    LearningState.MASTERED -> "Mastered"
}

private fun ExerciseType.label(): String = when (this) {
    ExerciseType.VOWEL -> "স্বরবর্ণ"
    ExerciseType.CONSONANT -> "ব্যঞ্জনবর্ণ"
    ExerciseType.DRAWING -> "আঁকা"
}

private val previewState = ProgressState(
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

@Preview(showBackground = true, name = "With progress")
@Composable
private fun ProgressScreenPreview() {
    BornoChitraTheme {
        ProgressContent(state = previewState, onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ProgressScreenEmptyPreview() {
    BornoChitraTheme {
        ProgressContent(state = ProgressState(), onBackClick = {})
    }
}
