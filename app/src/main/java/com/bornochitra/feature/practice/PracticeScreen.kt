package com.bornochitra.feature.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.tracing.ExerciseTracingCanvas
import com.bornochitra.core.tracing.TracingEngine
import com.bornochitra.core.ui.components.BcChip
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseShape
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcOutlineButton
import com.bornochitra.core.ui.components.BcRestartDialog
import com.bornochitra.core.ui.components.BcTip
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * Runs a real [Exercise] through [TracingEngine]. The engine is owned locally here rather than
 * routed through [PracticeViewModel]'s intents: per-touch pointer events need to stay cheap and
 * in-memory (plan.md sections 22 and 27), so only the exercise-completed outcome — a rare event —
 * is dispatched to the ViewModel to be scored/persisted.
 */
@Composable
fun PracticeScreen(
    exerciseId: String,
    onBackClick: () -> Unit,
    onCompleteClick: (sessionId: String, sessionScores: List<Float>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PracticeContent(
        state = state,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )

    val sessionId = state.sessionId
    LaunchedEffect(sessionId) {
        if (sessionId != null) onCompleteClick(sessionId.toString(), state.sessionScores)
    }
}

@Composable
private fun PracticeContent(
    state: PracticeState,
    onBackClick: () -> Unit,
    onEvent: (PracticeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BcTopAppBar(
                title = stringResource(R.string.title_practice),
                onBackClick = onBackClick,
                trailing = state.exercise?.let { exercise ->
                    {
                        BcChip(
                            text = stringResource(
                                R.string.practice_category_position,
                                exercise.type.label(),
                                state.categoryPosition,
                                state.categorySize,
                            ),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            state.error != null -> BcEmptyState(
                title = stringResource(R.string.practice_unavailable),
                message = stringResource(state.error),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            state.isLoading || state.exercise == null -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }

            else -> ExerciseTracingContent(
                exercise = state.exercise,
                attemptId = state.attemptId,
                tip = state.tip,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }

    if (state.isRestartConfirmationShown) {
        BcRestartDialog(
            onConfirm = { onEvent(PracticeEvent.RestartConfirmed) },
            onDismiss = { onEvent(PracticeEvent.RestartDismissed) },
        )
    }
}

@Composable
private fun ExerciseTracingContent(
    exercise: Exercise,
    attemptId: Int,
    tip: ContextualTip?,
    onEvent: (PracticeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = exerciseTitle(exercise.id, exercise.title)
    val canvas: @Composable (Modifier) -> Unit = { canvasModifier ->
        ExerciseTracingCanvas(
            exercise = exercise,
            attemptId = attemptId,
            contentDescription = stringResource(R.string.practice_canvas_description, title),
            onExerciseCompleted = { score, level -> onEvent(PracticeEvent.ExerciseCompleted(score, level)) },
            onTraceUnfinished = { onEvent(PracticeEvent.TraceUnfinished) },
            onStrokeStarted = { onEvent(PracticeEvent.StrokeStarted) },
            modifier = canvasModifier
                .clip(BcShapes.xxl)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .border(BcDimens.tileBorder, MaterialTheme.colorScheme.outlineVariant, BcShapes.xxl),
        )
    }
    val reset: @Composable () -> Unit = {
        BcOutlineButton(
            text = stringResource(R.string.action_reset),
            onClick = { onEvent(PracticeEvent.RestartRequested) },
            icon = R.drawable.bc_ic_reset,
            modifier = Modifier.widthIn(min = BcDimens.practiceResetMinWidth),
        )
    }
    val header: @Composable () -> Unit = { PracticeHeader(exercise = exercise, title = title) }

    BoxWithConstraints(
        modifier = modifier.padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.l),
    ) {
        if (maxWidth > maxHeight) {
            // Side by side, so the canvas takes the full height instead of what a heading, tip and
            // button leave above and below it.
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                canvas(Modifier.fillMaxHeight().aspectRatio(1f))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(BcSpacing.m, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    header()
                    tip?.let { BcTip(tip = it) }
                    reset()
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    header()
                    // Square, but never taller than what the header, tip and button leave, so a short
                    // screen shrinks the canvas rather than pushing Reset off screen.
                    canvas(Modifier.weight(1f, fill = false).aspectRatio(1f))
                    tip?.let { BcTip(tip = it) }
                }
                reset()
            }
        }
    }
}

/** The model to copy on a tonal tile, and what to do with it. */
@Composable
private fun PracticeHeader(exercise: Exercise, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ModelTile(exercise = exercise, title = title)
        Text(
            text = stringResource(R.string.practice_tracing_instruction),
            modifier = Modifier.weight(1f),
            style = BcType.instruction,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** The letter to copy; a drawing shows its shape, since its name would not fit. */
@Composable
private fun ModelTile(exercise: Exercise, title: String) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(BcDimens.practiceTile)
            .background(scheme.primaryContainer, BcShapes.xl),
        contentAlignment = Alignment.Center,
    ) {
        if (exercise.type == ExerciseType.DRAWING) {
            BcExerciseShape(strokes = exercise.strokes, color = scheme.onPrimaryContainer)
        } else {
            BcLetterText(text = title, style = BcType.letterPractice, color = scheme.onPrimaryContainer)
        }
    }
}

private val previewExercise = Exercise(
    id = "vowel-o",
    title = "অ",
    type = ExerciseType.VOWEL,
    difficulty = Difficulty.BEGINNER,
    strokes = listOf(
        Stroke(
            id = "vowel-o-stroke-1",
            points = listOf(Point(20f, 80f), Point(20f, 30f), Point(50f, 15f), Point(80f, 30f), Point(80f, 80f)),
        ),
    ),
    order = 1,
)

@Preview(showBackground = true, name = "Tracing")
@Composable
private fun PracticeScreenTracingPreview() {
    BornoChitraTheme {
        PracticeContent(
            state = PracticeState(
                exercise = previewExercise,
                isLoading = false,
                tip = ContextualTip.FIRST_ATTEMPT,
                categoryPosition = 1,
                categorySize = 11,
            ),
            onBackClick = {},
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun PracticeScreenLoadingPreview() {
    BornoChitraTheme {
        PracticeContent(state = PracticeState(isLoading = true), onBackClick = {}, onEvent = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun PracticeScreenErrorPreview() {
    BornoChitraTheme {
        PracticeContent(
            state = PracticeState(isLoading = false, error = R.string.error_exercise_not_found),
            onBackClick = {},
            onEvent = {},
        )
    }
}
