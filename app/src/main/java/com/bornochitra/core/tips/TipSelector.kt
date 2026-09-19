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
     * After the finger was lifted with the letter or drawing still unfinished. Stopping is allowed
     * and the trace is kept, so this only says what is left to do (plan.md Step 2).
     */
    fun afterUnfinishedTrace(): ContextualTip = ContextualTip.UNFINISHED_TRACE

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
