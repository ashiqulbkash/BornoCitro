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
import com.bornochitra.core.content.drawingExercises
import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * End-to-end tracing prototype for a single [Exercise]: renders the complete dotted guide, accepts
 * finger tracing via [TracingInputCanvas], and shows score feedback, all driven through
 * [TracingEngine] — the reusable tracing component from plan.md Step 10.11. This Composable holds
 * no tracing business logic itself; it only translates [TracingEngine]'s state and
 * [TracingAttemptOutcome] into UI, the same shape a future PracticeViewModel (plan.md Step 11)
 * would use. It is intentionally not wired into navigation or the real Practice feature yet.
 */
@Composable
fun LetterTracingPrototype(exercise: Exercise, modifier: Modifier = Modifier) {
    var attemptId by remember(exercise) { mutableIntStateOf(0) }
    val engine = remember(exercise, attemptId) { TracingEngine(exercise) }

    var currentStroke by remember(engine) { mutableStateOf(engine.currentStroke) }
    var guideStrokes by remember(engine) { mutableStateOf(engine.guideStrokes) }
    var feedback by remember(engine) { mutableStateOf<String?>(null) }

    fun handleStrokeEnd() {
        feedback = when (val outcome = engine.onEnd()) {
            is TracingAttemptOutcome.ExerciseCompleted -> "Score ${outcome.score.toInt()} — ${outcome.level}"
            is TracingAttemptOutcome.StrokeAttempted ->
                if (outcome.isCompleted) null else "Try again — follow the dots closely"
            TracingAttemptOutcome.NoAttempt -> feedback
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

/** plan.md Step 10.10 — validates the same engine also supports non-letter drawing exercises. */
@Preview(showBackground = true, name = "letter tracing prototype — Circle")
@Composable
private fun LetterTracingPrototypeDrawingCirclePreview() {
    BornoChitraTheme {
        Surface {
            LetterTracingPrototype(exercise = drawingExercises.first { it.id == "drawing-circle" })
        }
    }
}
