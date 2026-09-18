package com.bornochitra.feature.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.tracing.TracingAttemptOutcome
import com.bornochitra.core.tracing.TracingEngine
import com.bornochitra.core.tracing.TracingInputCanvas
import com.bornochitra.core.tracing.TracingPointerEvent
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcPrimaryButton
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
    onCompleteClick: (sessionId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PracticeContent(
        state = state,
        onBackClick = onBackClick,
        onExerciseCompleted = { score, level -> viewModel.onEvent(PracticeEvent.ExerciseCompleted(score, level)) },
        modifier = modifier,
    )

    val sessionId = state.sessionId
    LaunchedEffect(sessionId) {
        if (sessionId != null) onCompleteClick(sessionId.toString())
    }
}

@Composable
private fun PracticeContent(
    state: PracticeState,
    onBackClick: () -> Unit,
    onExerciseCompleted: (score: Float, level: ScoreLevel) -> Unit,
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
                onExerciseCompleted = onExerciseCompleted,
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
    onExerciseCompleted: (score: Float, level: ScoreLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var attemptId by remember(exercise) { mutableIntStateOf(0) }
    val engine = remember(exercise, attemptId) { TracingEngine(exercise) }

    var currentStroke by remember(engine) { mutableStateOf(engine.currentStroke) }
    var guideStrokes by remember(engine) { mutableStateOf(engine.guideStrokes) }
    var feedback by remember(engine) { mutableStateOf<String?>(null) }

    fun handleStrokeEnd() {
        when (val outcome = engine.onEnd()) {
            is TracingAttemptOutcome.ExerciseCompleted -> onExerciseCompleted(outcome.score, outcome.level)
            is TracingAttemptOutcome.StrokeAttempted ->
                feedback = if (outcome.isCompleted) null else "Try again — follow the dots closely"
            TracingAttemptOutcome.NoAttempt -> Unit
        }
        currentStroke = engine.currentStroke
        guideStrokes = engine.guideStrokes
    }

    Column(
        modifier = modifier.padding(BcSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = exercise.title, style = MaterialTheme.typography.displayMedium)

        val strokeToTrace = currentStroke
        if (strokeToTrace != null) {
            TracingInputCanvas(
                stroke = strokeToTrace,
                guideStrokes = guideStrokes,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                onPointerEvent = { event ->
                    when (event) {
                        is TracingPointerEvent.Start -> engine.onStart(event.point)
                        is TracingPointerEvent.Move -> engine.onMove(event.point)
                        is TracingPointerEvent.End -> handleStrokeEnd()
                        TracingPointerEvent.Cancel -> engine.onCancel()
                    }
                },
            )
        }

        feedback?.let { message -> Text(text = message, style = MaterialTheme.typography.titleMedium) }

        BcPrimaryButton(text = "Reset", onClick = { attemptId += 1 })
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
            state = PracticeState(exercise = previewExercise, isLoading = false),
            onBackClick = {},
            onExerciseCompleted = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun PracticeScreenLoadingPreview() {
    BornoChitraTheme {
        PracticeContent(state = PracticeState(isLoading = true), onBackClick = {}, onExerciseCompleted = { _, _ -> })
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
        )
    }
}
