package com.bornochitra.core.tracing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ScoreLevel

/** Draws nothing for the guides, so the child writes freehand while they are still scored against. */
private val HiddenGuideStyle = DottedPathStyle(dotColor = Color.Transparent, pathColor = Color.Transparent)

/**
 * Runs one attempt at [exercise] through [TracingEngine] on a [TracingInputCanvas]. The engine is
 * owned here rather than routed through a ViewModel's intents: per-touch pointer events need to stay
 * cheap and in-memory (plan.md sections 22 and 27), so only the rare outcomes — the exercise
 * completed, or a lift with it still unfinished — are reported to the caller.
 *
 * [showGuide] false hides the dotted guide without changing what is scored, and switching it back
 * on keeps the ink already drawn.
 *
 * [onStrokeStarted] and [onInkChanged] are for a caller that reads the writing itself rather than
 * relying on the guide being covered: each lift that leaves the exercise unfinished reports all the
 * ink so far, one list of points per stroke.
 */
@Composable
fun ExerciseTracingCanvas(
    exercise: Exercise,
    attemptId: Int,
    contentDescription: String,
    onExerciseCompleted: (score: Float, level: ScoreLevel) -> Unit,
    onTraceUnfinished: () -> Unit,
    modifier: Modifier = Modifier,
    showGuide: Boolean = true,
    onStrokeStarted: () -> Unit = {},
    onInkChanged: (List<List<TracePoint>>) -> Unit = {},
) {
    val engine = remember(exercise, attemptId) { TracingEngine(exercise) }

    var guideStrokes by remember(engine) { mutableStateOf(engine.guideStrokes) }

    val haptics = LocalHapticFeedback.current

    // Finishing the whole letter is felt; stopping part way through is not, since pausing is
    // allowed and a child who stops should never be buzzed for it.
    fun handleTraceEnd() {
        when (val outcome = engine.onEnd()) {
            is TracingAttemptOutcome.ExerciseCompleted -> {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                onExerciseCompleted(outcome.score, outcome.level)
            }

            is TracingAttemptOutcome.Unfinished -> {
                onTraceUnfinished()
                onInkChanged(engine.ink)
            }

            TracingAttemptOutcome.NoAttempt -> Unit
        }
        guideStrokes = engine.guideStrokes
    }

    if (guideStrokes.isNotEmpty()) {
        // A restart hands the canvas the same stroke instances it already holds ink for, so it
        // cannot tell the new attempt from the old one. Keying it on the attempt discards that
        // ink along with the progress drawn on top of the guides.
        key(attemptId) {
            TracingInputCanvas(
                guideStrokes = guideStrokes,
                modifier = modifier.semantics { this.contentDescription = contentDescription },
                style = if (showGuide) DottedPathStyle() else HiddenGuideStyle,
                onPointerEvent = { event ->
                    when (event) {
                        is TracingPointerEvent.Start -> {
                            engine.onStart(event.point)
                            onStrokeStarted()
                        }
                        is TracingPointerEvent.Move -> engine.onMove(event.point)
                        is TracingPointerEvent.End -> handleTraceEnd()
                        TracingPointerEvent.Cancel -> engine.onCancel()
                    }
                },
            )
        }
    }
}
