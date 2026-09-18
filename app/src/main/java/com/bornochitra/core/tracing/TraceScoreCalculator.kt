package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel

/**
 * Turns raw tracing measurement data ([TraceResult]/[StrokeTraceResult]) into a 0..100 learning
 * score and [ScoreLevel] — the score calculator from plan.md Step 10.7. Combines path coverage
 * and accuracy (average distance from the guide path, normalized against [tolerance]) using
 * [weights]; tracing speed is deliberately not a factor, per the plan's guidance that accuracy
 * and completion should matter more than speed for young children.
 */
class TraceScoreCalculator(
    private val tolerance: TracingTolerance = TracingTolerance(),
    private val weights: ScoreWeights = ScoreWeights(),
    private val thresholds: ScoreThresholds = ScoreThresholds(),
) {

    /** Score (0..100) for a single stroke attempt, from its coverage and average path distance. */
    fun scoreStroke(strokeResult: StrokeTraceResult): Float {
        val accuracy = accuracyOf(strokeResult.averageDistance)
        val combined = strokeResult.coverage * weights.coverageWeight + accuracy * weights.accuracyWeight
        return (combined * 100f).coerceIn(0f, 100f)
    }

    /** Score (0..100) for a whole exercise attempt: the average of its strokes' scores. */
    fun scoreTrace(traceResult: TraceResult): Float {
        val strokeResults = traceResult.strokeResults
        if (strokeResults.isEmpty()) return 0f
        return strokeResults.map(::scoreStroke).average().toFloat()
    }

    fun scoreLevel(score: Float): ScoreLevel = thresholds.classify(score)

    /** Converts a raw distance into a 0..1 closeness fraction: 0 at/beyond [TracingTolerance.maxDistance], 1 at zero distance. */
    private fun accuracyOf(averageDistance: Float): Float {
        val clampedDistance = averageDistance.coerceIn(0f, tolerance.maxDistance)
        return 1f - (clampedDistance / tolerance.maxDistance)
    }
}
