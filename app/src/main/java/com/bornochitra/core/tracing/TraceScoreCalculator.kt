package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel
import kotlin.math.roundToInt

/**
 * Turns raw tracing measurement data ([TraceResult]/[StrokeTraceResult]) into a 0..100 learning
 * score and [ScoreLevel] — the score calculator from plan.md Step 10.7, completed in Step 12.
 *
 * A whole attempt is scored from the four metrics plan.md section 38 requires, combined with
 * [weights]:
 *
 * ```text
 * coverage    how much of each guide path was traced
 * accuracy    how close the finger stayed to the guide path, against [tolerance]
 * completion  finished strokes out of the exercise's own stroke count
 * order       attempts that traced the expected stroke rather than another one
 * ```
 *
 * Coverage and accuracy are per-stroke measurements, averaged across every stroke the exercise
 * expects using [strokeWeighting] (equal weight by default), so a stroke that was never traced
 * counts as zero rather than being left out of the average. Completion and order describe the
 * attempt as a whole. Only the attempt that finally completed a stroke contributes its coverage
 * and accuracy, so retrying a stroke never drags the score below what the final attempt earned.
 *
 * Tracing speed is deliberately not a factor at all, per the plan's guidance that accuracy and
 * completion should matter more than speed for young children. The same logic scores letters and
 * drawings — nothing here is aware of which it is.
 */
class TraceScoreCalculator(
    private val tolerance: TracingTolerance = TracingTolerance(),
    private val weights: ScoreWeights = ScoreWeights(),
    private val thresholds: ScoreThresholds = ScoreThresholds(),
    private val strokeWeighting: StrokeWeighting = StrokeWeighting.Equal,
) {

    /** Score (0..100) for a single stroke attempt, from its coverage and average path distance. */
    fun scoreStroke(strokeResult: StrokeTraceResult): Float {
        val combined = strokeResult.coverage * weights.coverageShareOfStroke +
            accuracyOf(strokeResult.averageDistance) * weights.accuracyShareOfStroke
        return toScore(combined)
    }

    /** Score (0..100) for a whole exercise attempt across all four metrics. */
    fun scoreTrace(traceResult: TraceResult): Float {
        val strokeResults = traceResult.strokeResults
        if (strokeResults.isEmpty()) return 0f

        val combined = weightedAverage(traceResult) { it.coverage } * weights.coverageWeight +
            weightedAverage(traceResult) { accuracyOf(it.averageDistance) } * weights.accuracyWeight +
            completionOf(traceResult) * weights.completionWeight +
            orderAccuracyOf(strokeResults) * weights.orderWeight
        return toScore(combined)
    }

    fun scoreLevel(score: Float): ScoreLevel = thresholds.classify(score)

    /**
     * Turns a 0..1 fraction into the 0..100 score, rounded to two decimals so accumulated float
     * noise cannot push a score that lands exactly on a [ScoreThresholds] boundary to the level
     * below it.
     */
    private fun toScore(fraction: Float): Float =
        ((fraction * 100f).coerceIn(0f, 100f) * 100f).roundToInt() / 100f

    /** Converts a raw distance into a 0..1 closeness fraction: 0 at/beyond [TracingTolerance.maxDistance], 1 at zero distance. */
    private fun accuracyOf(averageDistance: Float): Float {
        val clampedDistance = averageDistance.coerceIn(0f, tolerance.maxDistance)
        return 1f - (clampedDistance / tolerance.maxDistance)
    }

    /** Fraction (0..1) of the exercise's strokes the child actually finished. */
    private fun completionOf(traceResult: TraceResult): Float {
        val expected = traceResult.expectedStrokeCount
        if (expected <= 0) return 0f
        val completed = traceResult.strokeResults.count { it.isCompleted }
        return (completed.toFloat() / expected).coerceIn(0f, 1f)
    }

    /**
     * Fraction (0..1) of attempts that went to the expected stroke. Ordinary retries of the same
     * stroke are not order mistakes and do not lower it; tracing a different stroke of the
     * exercise does.
     */
    private fun orderAccuracyOf(strokeResults: List<StrokeTraceResult>): Float {
        val attempts = strokeResults.sumOf { it.attemptCount }
        if (attempts <= 0) return 1f
        val outOfOrder = strokeResults.sumOf { it.outOfOrderAttempts }
        return (1f - outOfOrder.toFloat() / attempts).coerceIn(0f, 1f)
    }

    /** Averages [metric] over every expected stroke, counting a stroke that was never traced as zero. */
    private fun weightedAverage(
        traceResult: TraceResult,
        metric: (StrokeTraceResult) -> Float,
    ): Float {
        val strokeCount = maxOf(traceResult.strokeResults.size, traceResult.expectedStrokeCount)
        var weightedTotal = 0f
        var weightSum = 0f
        for (index in 0 until strokeCount) {
            val weight = strokeWeighting.weightOf(index, strokeCount)
            weightedTotal += (traceResult.strokeResults.getOrNull(index)?.let(metric) ?: 0f) * weight
            weightSum += weight
        }
        return if (weightSum <= 0f) 0f else weightedTotal / weightSum
    }
}
