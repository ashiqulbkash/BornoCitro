package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel

/** Outcome of lifting the finger once, via [TracingEngine.onEnd]. */
sealed interface TracingAttemptOutcome {

    /** [TracingEngine.onEnd] was called with no active attempt to score. */
    data object NoAttempt : TracingAttemptOutcome

    /**
     * The finger was lifted with part of the shape still untraced. The attempt keeps everything
     * traced so far, so the child can carry on from where they stopped; [coverage] (0..1) is how
     * much of the whole shape they have covered.
     */
    data class Unfinished(val coverage: Float) : TracingAttemptOutcome

    /** The whole shape is now traced, with the exercise's overall [score] (0..100) and [level]. */
    data class ExerciseCompleted(val score: Float, val level: ScoreLevel) : TracingAttemptOutcome
}
