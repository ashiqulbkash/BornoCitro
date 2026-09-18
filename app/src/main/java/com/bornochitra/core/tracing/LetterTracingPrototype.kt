package com.bornochitra.core.tracing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.content.consonantExercises
import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * End-to-end tracing prototype for a single [Exercise]: renders the dotted guide, accepts finger
 * tracing via [TracingInputCanvas], advances through the exercise's strokes with
 * [MultiStrokeTracker], and shows coverage/score feedback via [TraceScoreCalculator] once every
 * stroke is completed. This is the isolated prototype from plan.md Step 10.8, exercised first
 * against the real Bengali "অ" content. It is intentionally not wired into navigation or the real
 * Practice feature yet — that integration is plan.md Step 10.11.
 */
@Composable
fun LetterTracingPrototype(exercise: Exercise, modifier: Modifier = Modifier) {
    var attemptId by remember(exercise) { mutableIntStateOf(0) }
    val tracker = remember(exercise, attemptId) { MultiStrokeTracker(exercise) }
    val scoreCalculator = remember { TraceScoreCalculator() }

    var currentStroke by remember(tracker) { mutableStateOf(tracker.currentStroke) }
    var strokeNumber by remember(tracker) { mutableIntStateOf(tracker.currentStrokeIndex + 1) }
    var feedback by remember(tracker) { mutableStateOf<String?>(null) }

    fun handleStrokeEnd() {
        tracker.onEnd()
        val attempt = tracker.lastAttemptResult
        currentStroke = tracker.currentStroke
        strokeNumber = (tracker.currentStrokeIndex + 1).coerceAtMost(exercise.strokes.size)
        feedback = when {
            tracker.isSequenceCompleted -> {
                val score = scoreCalculator.scoreTrace(tracker.toTraceResult())
                "Score ${score.toInt()} — ${scoreCalculator.scoreLevel(score)}"
            }
            attempt != null && !attempt.isCompleted -> "Try again — follow the dots closely"
            else -> null
        }
    }

    Column(
        modifier = modifier.padding(BcSpacing.md),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = exercise.title, style = MaterialTheme.typography.displayMedium)

        val strokeToTrace = currentStroke
        if (strokeToTrace != null) {
            Text(text = "Stroke $strokeNumber of ${exercise.strokes.size}", style = MaterialTheme.typography.bodyLarge)
            TracingInputCanvas(
                stroke = strokeToTrace,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                onPointerEvent = { event ->
                    when (event) {
                        is TracingPointerEvent.Start -> tracker.onStart(event.point)
                        is TracingPointerEvent.Move -> tracker.onMove(event.point)
                        is TracingPointerEvent.End -> handleStrokeEnd()
                        TracingPointerEvent.Cancel -> tracker.onCancel()
                    }
                },
            )
        }

        feedback?.let { message -> Text(text = message, style = MaterialTheme.typography.titleMedium) }

        BcPrimaryButton(text = "Reset", onClick = { attemptId += 1 })
    }
}

@Preview(showBackground = true, name = "letter tracing prototype — অ")
@Composable
private fun LetterTracingPrototypeVowelOPreview() {
    BornoChitraTheme {
        Surface {
            LetterTracingPrototype(exercise = vowelExercises.first { it.id == "vowel-o" })
        }
    }
}

/** plan.md Step 10.9 — ক has straight/angular strokes, structurally different from অ's curved loop. */
@Preview(showBackground = true, name = "letter tracing prototype — ক")
@Composable
private fun LetterTracingPrototypeConsonantKoPreview() {
    BornoChitraTheme {
        Surface {
            LetterTracingPrototype(exercise = consonantExercises.first { it.id == "consonant-ko" })
        }
    }
}
