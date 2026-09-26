package com.bornochitra.feature.fillblanks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.tracing.DottedPathCanvas
import com.bornochitra.core.tracing.DottedPathStyle
import com.bornochitra.core.tracing.ExerciseTracingCanvas
import com.bornochitra.core.ui.components.BcBanner
import com.bornochitra.core.ui.components.BcBannerKind
import com.bornochitra.core.ui.components.BcBottomSheet
import com.bornochitra.core.ui.components.BcChip
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcOutlineButton
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcRestartDialog
import com.bornochitra.core.ui.components.BcResultStars
import com.bornochitra.core.ui.components.BcSheetHeader
import com.bornochitra.core.ui.components.BcTextButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlin.math.roundToInt
import com.bornochitra.core.model.Stroke as ExerciseStroke

/** Where the writing area's two dashed lines sit, as a share of its height (design/DESIGN_SPEC.md 4, Tracing canvas). */
private val WritingLines = listOf(0.29f, 0.78f)

/** The hint preview's dots are primary at 55%. */
private const val HINT_DOT_ALPHA = 0.55f

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
    Scaffold(
        modifier = modifier,
        topBar = {
            BcTopAppBar(
                title = stringResource(R.string.title_fill_blanks),
                onBackClick = onBackClick,
                trailing = { BcChip(text = state.difficulty.label()) },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when {
            state.error != null -> BcEmptyState(
                title = stringResource(R.string.fill_blanks_no_sequence),
                message = stringResource(state.error),
                modifier = contentModifier,
            )

            state.isLoading -> Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            else -> Column(
                modifier = contentModifier.padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.l),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
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
                        stars = state.stars,
                        onNextSequence = { onEvent(FillBlanksEvent.NextSequence) },
                    )
                }
            }
        }
    }

    val activeExercise = state.activeExercise
    if (state.isHintSheetShown && activeExercise != null) {
        HintSheet(exercise = activeExercise, onEvent = onEvent)
    }
    if (state.isRestartConfirmationShown) {
        BcRestartDialog(
            onConfirm = { onEvent(FillBlanksEvent.BlankRestarted) },
            onDismiss = { onEvent(FillBlanksEvent.BlankRestartDismissed) },
        )
    }
}

@Composable
private fun Difficulty.label(): String = when (this) {
    // The sequences have two levels: only the hard ones put blanks side by side (BlankSequenceGenerator).
    Difficulty.BEGINNER, Difficulty.INTERMEDIATE -> stringResource(R.string.fill_blanks_easy)
    Difficulty.ADVANCED -> stringResource(R.string.fill_blanks_hard)
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

/** One cell of the sequence (design/DESIGN_SPEC.md 4, Sequence cell). */
@Composable
private fun SequenceCellBox(cell: SequenceCell, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val description = when (cell.status) {
        CellStatus.SHOWN -> cell.title
        CellStatus.BLANK -> stringResource(R.string.fill_blanks_cell_blank)
        CellStatus.ACTIVE -> stringResource(R.string.fill_blanks_cell_active)
        CellStatus.FILLED -> stringResource(R.string.fill_blanks_cell_filled, cell.title)
    }
    val shape = BcShapes.md
    val cellModifier = modifier
        .height(BcDimens.sequenceCellHeight)
        .clearAndSetSemantics { contentDescription = description }
    val styled = when (cell.status) {
        CellStatus.SHOWN -> cellModifier
            .background(scheme.surfaceContainerLowest, shape)
            .border(BcDimens.tileBorder, scheme.outlineVariant, shape)
        CellStatus.ACTIVE -> cellModifier
            .background(scheme.primaryContainer, shape)
            .border(BcDimens.selectedBorder, scheme.primary, shape)
        CellStatus.BLANK -> cellModifier.dashedBorder(BcDimens.tileBorder, scheme.outline, BcDimens.sequenceCellCorner)
        CellStatus.FILLED -> cellModifier
            .background(scheme.secondaryContainer, shape)
            .border(BcDimens.tileBorder, BcTheme.colors.accent, shape)
    }
    Box(modifier = styled, contentAlignment = Alignment.Center) {
        when (cell.status) {
            CellStatus.SHOWN -> BcLetterText(text = cell.title, style = BcType.sequenceCell, color = scheme.onSurface)
            CellStatus.FILLED -> BcLetterText(text = cell.title, style = BcType.sequenceCell, color = scheme.onSecondaryContainer)
            CellStatus.ACTIVE -> Text(text = "?", style = BcType.sequenceCell, color = scheme.primary)
            CellStatus.BLANK -> Unit
        }
    }
}

private fun Modifier.dashedBorder(width: Dp, color: Color, corner: Dp): Modifier = drawBehind {
    val stroke = width.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(stroke / 2, stroke / 2),
        size = Size(size.width - stroke, size.height - stroke),
        cornerRadius = CornerRadius(corner.toPx()),
        style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(BcDimens.dashLength.toPx(), BcDimens.dashGap.toPx()))),
    )
}

@Composable
private fun ColumnScope.BlankTracing(
    exercise: Exercise,
    attemptId: Int,
    isHintShown: Boolean,
    isNotRecognized: Boolean,
    onEvent: (FillBlanksEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if (isHintShown) stringResource(R.string.fill_blanks_trace_hint) else stringResource(R.string.fill_blanks_write_missing),
            style = BcType.instruction,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        val scheme = MaterialTheme.colorScheme
        val lineColor = BcTheme.colors.guide
        // Square, but never taller than what the row, instruction, tip and buttons leave.
        ExerciseTracingCanvas(
            exercise = exercise,
            attemptId = attemptId,
            contentDescription = stringResource(R.string.fill_blanks_canvas_description),
            onExerciseCompleted = { score, level -> onEvent(FillBlanksEvent.BlankCompleted(score, level)) },
            onTraceUnfinished = {},
            showGuide = isHintShown,
            onStrokeStarted = { onEvent(FillBlanksEvent.StrokeStarted) },
            onInkChanged = { onEvent(FillBlanksEvent.InkChanged(it)) },
            modifier = Modifier
                .weight(1f, fill = false)
                .aspectRatio(1f)
                .clip(BcShapes.xxl)
                .background(scheme.surfaceContainerLowest)
                .drawBehind {
                    val stroke = BcDimens.tileBorder.toPx()
                    val dash = PathEffect.dashPathEffect(floatArrayOf(BcDimens.dashLength.toPx(), BcDimens.dashGap.toPx()))
                    WritingLines.forEach { share ->
                        val y = size.height * share
                        drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = stroke, pathEffect = dash)
                    }
                }
                .border(BcDimens.tileBorder, scheme.outlineVariant, BcShapes.xxl),
        )
        if (isNotRecognized) {
            BcBanner(
                kind = BcBannerKind.TIP,
                text = stringResource(R.string.fill_blanks_not_recognized),
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
        BcOutlineButton(
            text = stringResource(R.string.fill_blanks_hint),
            onClick = { onEvent(FillBlanksEvent.HintRequested) },
            enabled = !isHintShown,
            icon = R.drawable.bc_ic_bulb,
            compactPadding = true,
            modifier = Modifier.weight(1f),
        )
        BcOutlineButton(
            text = stringResource(R.string.action_reset),
            onClick = { onEvent(FillBlanksEvent.BlankRestartRequested) },
            icon = R.drawable.bc_ic_reset,
            compactPadding = true,
            modifier = Modifier.weight(1f),
        )
    }
}

/** Asks before the guide is shown, since it caps the blank's score (design/DESIGN_SPEC.md 5.16). */
@Composable
private fun HintSheet(exercise: Exercise, onEvent: (FillBlanksEvent) -> Unit) {
    val scheme = MaterialTheme.colorScheme
    BcBottomSheet(onDismissRequest = { onEvent(FillBlanksEvent.HintDismissed) }) {
        BcSheetHeader(
            icon = R.drawable.bc_ic_bulb,
            iconContainerColor = scheme.secondaryContainer,
            iconTint = scheme.onSecondaryContainer,
            title = stringResource(R.string.fill_blanks_hint_title),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HintPreview(strokes = exercise.strokes)
            Text(
                text = stringResource(R.string.fill_blanks_hint_message, HintRule.HINTED_SCORE_CAP.roundToInt()),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
        }
        BcPrimaryButton(
            text = stringResource(R.string.fill_blanks_hint_confirm),
            onClick = { onEvent(FillBlanksEvent.HintUsed) },
            modifier = Modifier.fillMaxWidth(),
        )
        BcTextButton(
            text = stringResource(R.string.fill_blanks_hint_dismiss),
            onClick = { onEvent(FillBlanksEvent.HintDismissed) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** The dots the hint would show, small. Decorative; the message says what they are. */
@Composable
private fun HintPreview(strokes: List<ExerciseStroke>) {
    val scheme = MaterialTheme.colorScheme
    val style = DottedPathStyle(dotColor = scheme.primary.copy(alpha = HINT_DOT_ALPHA), pathColor = Color.Transparent)
    Box(
        modifier = Modifier
            .size(BcDimens.hintPreview)
            .clip(BcShapes.lg)
            .background(scheme.surfaceContainerLowest)
            .border(BcDimens.tileBorder, scheme.outlineVariant, BcShapes.lg)
            .clearAndSetSemantics {},
    ) {
        strokes.forEach { stroke ->
            DottedPathCanvas(stroke = stroke, style = style, modifier = Modifier.matchParentSize())
        }
    }
}

/** The finished sequence: its stars and score, and the way on to a new one (design/DESIGN_SPEC.md 5.17). */
@Composable
private fun ColumnScope.SequenceSummary(
    blankCount: Int,
    averagePercent: Int,
    stars: Int,
    onNextSequence: () -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(top = BcSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.s),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BcResultStars(count = stars, sideSize = BcDimens.summaryStarSide, middleSize = BcDimens.summaryStarMiddle)
        Text(
            text = stringResource(R.string.percent, averagePercent),
            style = BcType.scoreXL,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clearAndSetSemantics {},
        )
        Text(
            text = pluralStringResource(R.plurals.fill_blanks_summary, blankCount, blankCount, averagePercent),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
    BcPrimaryButton(
        text = stringResource(R.string.fill_blanks_next_sequence),
        onClick = onNextSequence,
        trailingIcon = R.drawable.bc_ic_arrow_forward,
        modifier = Modifier.fillMaxWidth(),
    )
}

private val previewCells = listOf(
    SequenceCell("vowel-o", "অ", CellStatus.SHOWN),
    SequenceCell("vowel-aa", "আ", CellStatus.ACTIVE),
    SequenceCell("vowel-i", "ই", CellStatus.SHOWN),
    SequenceCell("vowel-ii", "ঈ", CellStatus.BLANK),
    SequenceCell("vowel-u", "উ", CellStatus.SHOWN),
)

private val previewExercise = Exercise(
    id = "vowel-aa",
    title = "আ",
    type = ExerciseType.VOWEL,
    difficulty = Difficulty.BEGINNER,
    strokes = listOf(ExerciseStroke("vowel-aa-stroke-1", listOf(Point(20f, 80f), Point(20f, 30f), Point(80f, 30f), Point(80f, 80f)))),
    order = 2,
)

@Preview(showBackground = true, name = "Sequence")
@Composable
private fun FillBlanksSequencePreview() {
    BornoChitraTheme {
        FillBlanksContent(
            state = FillBlanksState(isLoading = false, cells = previewCells, activeExercise = previewExercise, isNotRecognized = true),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Finished")
@Composable
private fun FillBlanksFinishedPreview() {
    BornoChitraTheme {
        FillBlanksContent(
            state = FillBlanksState(
                isLoading = false,
                cells = previewCells.map { if (it.status == CellStatus.SHOWN) it else it.copy(status = CellStatus.FILLED) },
                results = listOf(
                    BlankResult("vowel-aa", 76f, ScoreLevel.MEDIUM, hintUsed = false),
                    BlankResult("vowel-ii", 70f, ScoreLevel.MEDIUM, hintUsed = true),
                ),
            ),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun FillBlanksErrorPreview() {
    BornoChitraTheme {
        FillBlanksContent(
            state = FillBlanksState(isLoading = false, error = R.string.error_too_few_items),
            onEvent = {},
            onBackClick = {},
        )
    }
}
