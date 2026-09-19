package com.bornochitra.core.tracing

import kotlin.math.abs

/**
 * Configurable weighting of the four metrics plan.md section 38 requires a learning score to be
 * built from, so the balance is not hardcoded. Weights must sum to 1.
 *
 * - [coverageWeight]   how much of each guide path was traced
 * - [accuracyWeight]   how close the finger stayed to the guide path
 * - [completionWeight] how many of the exercise's strokes were finished
 * - [orderWeight]      how often the expected stroke was traced rather than another one
 *
 * Tracing speed is intentionally not a scoring input at all — plan.md sections 31 and 38 say
 * accuracy and completion should matter more than speed for young children. The default split
 * keeps the original 60:40 balance between coverage and accuracy within their combined share.
 */
data class ScoreWeights(
    val coverageWeight: Float = DEFAULT_COVERAGE_WEIGHT,
    val accuracyWeight: Float = DEFAULT_ACCURACY_WEIGHT,
    val completionWeight: Float = DEFAULT_COMPLETION_WEIGHT,
    val orderWeight: Float = DEFAULT_ORDER_WEIGHT,
) {

    init {
        require(coverageWeight in 0f..1f) { "coverageWeight must be within 0..1" }
        require(accuracyWeight in 0f..1f) { "accuracyWeight must be within 0..1" }
        require(completionWeight in 0f..1f) { "completionWeight must be within 0..1" }
        require(orderWeight in 0f..1f) { "orderWeight must be within 0..1" }
        require(abs(total() - 1f) < WEIGHT_SUM_TOLERANCE) { "weights must sum to 1" }
    }

    /** Coverage's share of the coverage/accuracy pair alone, used when scoring a single stroke. */
    val coverageShareOfStroke: Float = strokeShare(coverageWeight)

    /** Accuracy's share of the coverage/accuracy pair alone, used when scoring a single stroke. */
    val accuracyShareOfStroke: Float = strokeShare(accuracyWeight)

    private fun total(): Float = coverageWeight + accuracyWeight + completionWeight + orderWeight

    private fun strokeShare(weight: Float): Float {
        val pairTotal = coverageWeight + accuracyWeight
        return if (pairTotal <= 0f) 0f else weight / pairTotal
    }

    companion object {
        const val DEFAULT_COVERAGE_WEIGHT = 0.45f
        const val DEFAULT_ACCURACY_WEIGHT = 0.30f
        const val DEFAULT_COMPLETION_WEIGHT = 0.15f
        const val DEFAULT_ORDER_WEIGHT = 0.10f
        private const val WEIGHT_SUM_TOLERANCE = 0.0001f
    }
}
