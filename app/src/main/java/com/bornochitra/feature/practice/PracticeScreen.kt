package com.bornochitra.feature.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseHeading
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTip
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
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
        onExerciseCompleted = { score, level -> viewModel.onEvent(PracticeEvent.ExerciseCompleted(score, level)) },
        onTraceUnfinished = { viewModel.onEvent(PracticeEvent.TraceUnfinished) },
        onRestart = { viewModel.onEvent(PracticeEvent.Restarted) },
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
    onExerciseCompleted: (score: Float, level: ScoreLevel) -> Unit,
    onTraceUnfinished: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "Practice", onBackClick = onBackClick) },
    ) { innerPadding ->
        when {
            state.error != null -> BcEmptyState(
                title = "Exercise unavailable",
                message = state.error,
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
                onExerciseCompleted = onExerciseCompleted,
                onTraceUnfinished = onTraceUnfinished,
                onRestart = onRestart,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}

@Composable
private fun ExerciseTracingContent(
    exercise: Exercise,
    attemptId: Int,
    tip: ContextualTip?,
    onExerciseCompleted: (score: Float, level: ScoreLevel) -> Unit,
    onTraceUnfinished: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canvas: @Composable (Modifier) -> Unit = { canvasModifier ->
        ExerciseTracingCanvas(
            exercise = exercise,
            attemptId = attemptId,
            contentDescription = "Tracing area for ${exercise.title}. Follow the dots with your finger.",
            onExerciseCompleted = onExerciseCompleted,
            onTraceUnfinished = onTraceUnfinished,
            modifier = canvasModifier,
        )
    }
    val reset: @Composable () -> Unit = {
        BcPrimaryButton(text = "Reset", onClick = onRestart)
    }

    BoxWithConstraints(modifier = modifier.padding(BcSpacing.md)) {
        if (maxWidth > maxHeight) {
            // Side by side, so the canvas takes the full height instead of what a heading, tip and
            // button leave above and below it.
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                canvas(Modifier.fillMaxHeight().aspectRatio(1f))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(BcSpacing.sm, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BcExerciseHeading(title = exercise.title)
                    TracingInstruction()
                    tip?.let { BcTip(tip = it) }
                    reset()
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BcExerciseHeading(title = exercise.title)
                TracingInstruction()
                // Square, but never taller than what the heading, tip and button leave, so a short
                // screen shrinks the canvas rather than pushing Reset off screen.
                canvas(Modifier.weight(1f, fill = false).aspectRatio(1f))
                tip?.let { BcTip(tip = it) }
                reset()
            }
        }
    }
}

/** Tells the child what to do on the canvas, in place of a marker drawn on it (plan.md step 3). */
@Composable
private fun TracingInstruction(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.practice_tracing_instruction),
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
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
            state = PracticeState(exercise = previewExercise, isLoading = false, tip = ContextualTip.FIRST_ATTEMPT),
            onBackClick = {},
            onExerciseCompleted = { _, _ -> },
            onTraceUnfinished = {},
            onRestart = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun PracticeScreenLoadingPreview() {
    BornoChitraTheme {
        PracticeContent(
            state = PracticeState(isLoading = true),
            onBackClick = {},
            onExerciseCompleted = { _, _ -> },
            onTraceUnfinished = {},
            onRestart = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun PracticeScreenErrorPreview() {
    BornoChitraTheme {
        PracticeContent(
            state = PracticeState(isLoading = false, error = "We couldn't find that exercise."),
            onBackClick = {},
            onExerciseCompleted = { _, _ -> },
            onTraceUnfinished = {},
            onRestart = {},
        )
    }
}
