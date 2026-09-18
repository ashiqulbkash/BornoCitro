package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel

/** Outcome of ending one stroke attempt via [TracingEngine.onEnd]. */
sealed interface TracingAttemptOutcome {

    /** [TracingEngine.onEnd] was called with no active attempt to score. */
    data object NoAttempt : TracingAttemptOutcome

    /** A stroke attempt finished; [isCompleted] says whether it met the threshold and advanced. */
    data class StrokeAttempted(val isCompleted: Boolean) : TracingAttemptOutcome

    /** Every stroke is now complete, with the exercise's overall [score] (0..100) and [level]. */
    data class ExerciseCompleted(val score: Float, val level: ScoreLevel) : TracingAttemptOutcome
}
