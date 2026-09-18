package com.bornochitra.core.tracing

import kotlin.math.abs

/**
 * Configurable weighting between a stroke's path coverage and its accuracy (closeness to the
 * guide path) when computing a score, so the balance is not hardcoded. Weights must sum to 1.
 * Tracing speed is intentionally not a scoring input at all — plan.md Step 10.7 says accuracy
 * and completion should matter more than speed for young children.
 */
data class ScoreWeights(
    val coverageWeight: Float = DEFAULT_COVERAGE_WEIGHT,
    val accuracyWeight: Float = DEFAULT_ACCURACY_WEIGHT,
) {

    init {
        require(coverageWeight in 0f..1f) { "coverageWeight must be within 0..1" }
        require(accuracyWeight in 0f..1f) { "accuracyWeight must be within 0..1" }
        require(abs((coverageWeight + accuracyWeight) - 1f) < WEIGHT_SUM_TOLERANCE) {
            "coverageWeight and accuracyWeight must sum to 1"
        }
    }

    companion object {
        const val DEFAULT_COVERAGE_WEIGHT = 0.6f
        const val DEFAULT_ACCURACY_WEIGHT = 0.4f
        private const val WEIGHT_SUM_TOLERANCE = 0.0001f
    }
}
