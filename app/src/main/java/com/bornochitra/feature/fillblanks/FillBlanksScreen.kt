package com.bornochitra.feature.fillblanks

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.tracing.ExerciseTracingCanvas
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.components.letterFontFamily
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun FillBlanksScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FillBlanksViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FillBlanksContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
private fun FillBlanksContent(
    state: FillBlanksState,
    onEvent: (FillBlanksEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = state.type?.let { "$FILL_BLANKS_TITLE · ${it.label()}" } ?: FILL_BLANKS_TITLE
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = title, onBackClick = onBackClick) },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.error != null -> BcEmptyState(
                title = "No sequence",
                message = state.error,
                modifier = contentModifier,
            )

            state.isLoading -> Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            else -> Column(
                modifier = contentModifier.padding(BcSpacing.md),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SequenceRow(cells = state.cells)
                val activeExercise = state.activeExercise
                if (activeExercise != null) {
                    BlankTracing(
                        exercise = activeExercise,
                        attemptId = state.attemptId,
                        isHintShown = state.isHintShown,
                        isNotRecognized = state.isNotRecognized,
                        onEvent = onEvent,
                    )
                } else {
                    SequenceSummary(
                        blankCount = state.results.size,
                        averagePercent = state.averagePercent,
                        onNextSequence = { onEvent(FillBlanksEvent.NextSequence) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SequenceRow(cells: List<SequenceCell>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs),
    ) {
        cells.forEach { cell ->
            SequenceCellBox(cell = cell, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SequenceCellBox(cell: SequenceCell, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val borderColor = when (cell.status) {
        CellStatus.SHOWN -> colors.outlineVariant
        CellStatus.BLANK -> colors.outline
        CellStatus.ACTIVE -> colors.primary
        CellStatus.FILLED -> colors.secondary
    }
    val borderWidth = if (cell.status == CellStatus.ACTIVE) 3.dp else 1.dp
    val (text, description) = when (cell.status) {
        CellStatus.SHOWN -> cell.title to cell.title
        CellStatus.BLANK -> "_" to "Blank"
        CellStatus.ACTIVE -> "?" to "Blank to fill in now"
        CellStatus.FILLED -> cell.title to "Filled in: ${cell.title}"
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(borderWidth, borderColor, MaterialTheme.shapes.medium)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = if (cell.status == CellStatus.SHOWN || cell.status == CellStatus.FILLED) {
                cell.title.letterFontFamily()
            } else {
                null
            },
            color = when (cell.status) {
                CellStatus.FILLED -> colors.secondary
                CellStatus.ACTIVE -> colors.primary
                CellStatus.SHOWN, CellStatus.BLANK -> colors.onSurface
            },
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
private fun ColumnScope.BlankTracing(
    exercise: Exercise,
    attemptId: Int,
    isHintShown: Boolean,
    isNotRecognized: Boolean,
    onEvent: (FillBlanksEvent) -> Unit,
) {
    // A failed check takes the instruction's place rather than a line of its own, so the writing
    // area does not move under the ink.
    Text(
        text = when {
            isNotRecognized -> "Not quite. Tap Reset and try again."
            isHintShown -> "Trace over the dots"
            else -> "Write the missing one in the box"
        },
        style = MaterialTheme.typography.titleMedium,
        color = if (isNotRecognized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
    )
    // Square, but never taller than what the row, instruction and buttons leave.
    ExerciseTracingCanvas(
        exercise = exercise,
        attemptId = attemptId,
        contentDescription = "Writing area for the missing item.",
        onExerciseCompleted = { score, level -> onEvent(FillBlanksEvent.BlankCompleted(score, level)) },
        onTraceUnfinished = {},
        showGuide = isHintShown,
        onStrokeStarted = { onEvent(FillBlanksEvent.StrokeStarted) },
        onInkChanged = { onEvent(FillBlanksEvent.InkChanged(it)) },
        modifier = Modifier
            .weight(1f, fill = false)
            .aspectRatio(1f)
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium),
    )
    Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
        BcSecondaryButton(
            text = "Hint",
            onClick = { onEvent(FillBlanksEvent.HintUsed) },
            enabled = !isHintShown,
        )
        BcPrimaryButton(text = "Reset", onClick = { onEvent(FillBlanksEvent.BlankRestarted) })
    }
}

@Composable
private fun SequenceSummary(
    blankCount: Int,
    averagePercent: Int,
    onNextSequence: () -> Unit,
) {
    Text(
        text = "You filled $blankCount blank${if (blankCount == 1) "" else "s"}. Your score is $averagePercent%.",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
    )
    BcPrimaryButton(text = "Next sequence", onClick = onNextSequence, modifier = Modifier.fillMaxWidth())
}

private val previewCells = listOf(
    SequenceCell("english-small-a", "a", CellStatus.SHOWN),
    SequenceCell("english-small-b", "b", CellStatus.FILLED),
    SequenceCell("english-small-c", "c", CellStatus.SHOWN),
    SequenceCell("english-small-d", "d", CellStatus.ACTIVE),
    SequenceCell("english-small-e", "e", CellStatus.SHOWN),
    SequenceCell("english-small-f", "f", CellStatus.BLANK),
)

@Preview(showBackground = true, name = "Finished")
@Composable
private fun FillBlanksFinishedPreview() {
    BornoChitraTheme {
        FillBlanksContent(
            state = FillBlanksState(
                isLoading = false,
                cells = previewCells.map { if (it.status == CellStatus.SHOWN) it else it.copy(status = CellStatus.FILLED) },
                results = listOf(
                    BlankResult("english-small-b", 92f, ScoreLevel.PERFECT, hintUsed = false),
                    BlankResult("english-small-d", 70f, ScoreLevel.MEDIUM, hintUsed = true),
                ),
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Sequence row")
@Composable
private fun SequenceRowPreview() {
    BornoChitraTheme {
        SequenceRow(cells = previewCells, modifier = Modifier.padding(BcSpacing.md))
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun FillBlanksErrorPreview() {
    BornoChitraTheme {
        FillBlanksContent(
            state = FillBlanksState(isLoading = false, error = "This category has too few items yet."),
            onEvent = {},
            onBackClick = {},
        )
    }
}
