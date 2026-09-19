package com.bornochitra.core.tips

import com.bornochitra.core.model.Difficulty

/**
 * When a [ContextualTip] appears, kept configurable so the balance can be moved in one place.
 *
 * "Low" is a score below [lowScoreBelow] rather than [com.bornochitra.core.model.ScoreLevel.LOW]:
 * an attempt only finishes once every stroke is traced well enough, which keeps its score well above
 * the LOW band, so a rule keyed to that band would practically never fire. The default matches the
 * score a child needs for mastery, so the tip appears when attempts keep falling short of it.
 */
data class TipRules(
    val repeatedLowScoreCount: Int = DEFAULT_REPEATED_LOW_SCORE_COUNT,
    val lowScoreBelow: Float = DEFAULT_LOW_SCORE_BELOW,
    val difficultFrom: Difficulty = DEFAULT_DIFFICULT_FROM,
) {

    init {
        require(repeatedLowScoreCount > 0) { "repeatedLowScoreCount must be positive" }
        require(lowScoreBelow in 0f..100f) { "lowScoreBelow must be within 0..100" }
    }

    companion object {
        const val DEFAULT_REPEATED_LOW_SCORE_COUNT = 2
        const val DEFAULT_LOW_SCORE_BELOW = 80f
        val DEFAULT_DIFFICULT_FROM = Difficulty.ADVANCED
    }
}
