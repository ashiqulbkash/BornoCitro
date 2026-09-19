package com.bornochitra.core.tips

import com.bornochitra.core.model.Difficulty
import javax.inject.Inject

/**
 * Picks the one tip that fits the moment, or none. Pure and UI-free, so what the child is told is
 * decided by [TipRules] and easy to test, not scattered through the screens (plan.md section 43).
 * Each moment offers at most one tip, and where more than one could apply the most specific and
 * actionable one wins.
 */
class TipSelector @Inject constructor(private val rules: TipRules) {

    /** How many of an exercise's latest results [afterAttempt] looks at. */
    val recentResultsNeeded: Int get() = rules.repeatedLowScoreCount

    /** On opening an exercise, [previousAttempts] being how many attempts at it were already saved. */
    fun beforeFirstAttempt(previousAttempts: Int): ContextualTip? =
        ContextualTip.FIRST_ATTEMPT.takeIf { previousAttempts == 0 }

    /**
     * After a stroke was lifted. [consecutiveMisses] is how many times in a row the stroke being
     * traced was not traced well enough; zero means it was, and no tip is needed.
     */
    fun afterStrokeAttempt(consecutiveMisses: Int): ContextualTip? = when {
        consecutiveMisses >= rules.repeatedMissesThreshold -> ContextualTip.REPEATED_MISSES
        consecutiveMisses > 0 -> ContextualTip.MISSED_STROKE
        else -> null
    }

    /**
     * After an exercise was finished. [recentScores] are its latest scores, newest first,
     * including the attempt just finished.
     */
    fun afterAttempt(difficulty: Difficulty, recentScores: List<Float>): ContextualTip? = when {
        hasRepeatedLowScores(recentScores) -> ContextualTip.REPEATED_LOW_SCORES
        difficulty.ordinal >= rules.difficultFrom.ordinal -> ContextualTip.DIFFICULT_COMPLETED
        else -> null
    }

    private fun hasRepeatedLowScores(recentScores: List<Float>): Boolean {
        val latest = recentScores.take(rules.repeatedLowScoreCount)
        return latest.size == rules.repeatedLowScoreCount && latest.all { it < rules.lowScoreBelow }
    }
}
