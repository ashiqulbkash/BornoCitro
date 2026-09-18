package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel

/**
 * Configurable score boundaries used to classify a 0..100 score into a [ScoreLevel], so the
 * thresholds are not hardcoded throughout the UI, per plan.md section 2. Defaults match the
 * plan's example: 0-59 LOW, 60-89 MEDIUM, 90-100 PERFECT.
 */
data class ScoreThresholds(
    val mediumMinScore: Float = DEFAULT_MEDIUM_MIN_SCORE,
    val perfectMinScore: Float = DEFAULT_PERFECT_MIN_SCORE,
) {

    init {
        require(mediumMinScore in 0f..100f) { "mediumMinScore must be within 0..100" }
        require(perfectMinScore in 0f..100f) { "perfectMinScore must be within 0..100" }
        require(mediumMinScore <= perfectMinScore) { "mediumMinScore must not exceed perfectMinScore" }
    }

    fun classify(score: Float): ScoreLevel = when {
        score >= perfectMinScore -> ScoreLevel.PERFECT
        score >= mediumMinScore -> ScoreLevel.MEDIUM
        else -> ScoreLevel.LOW
    }

    companion object {
        const val DEFAULT_MEDIUM_MIN_SCORE = 60f
        const val DEFAULT_PERFECT_MIN_SCORE = 90f
    }
}
