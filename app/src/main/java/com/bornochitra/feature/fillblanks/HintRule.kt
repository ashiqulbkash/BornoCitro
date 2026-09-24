package com.bornochitra.feature.fillblanks

import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.tracing.ScoreThresholds

/**
 * What a hint costs in fill-in-the-blanks (plan.md Step 6). A blank is meant to be recalled and
 * written freehand; revealing its dotted guide turns it back into plain tracing, so a hinted blank
 * scores at most [HINTED_SCORE_CAP]. The cap sits below [ScoreThresholds.perfectMinScore], so a
 * hinted blank is never PERFECT, and its level is re-classified from the capped score.
 */
object HintRule {

    const val HINTED_SCORE_CAP = 70f

    fun score(score: Float, hintUsed: Boolean): Float = if (hintUsed) minOf(score, HINTED_SCORE_CAP) else score

    fun level(
        score: Float,
        level: ScoreLevel,
        hintUsed: Boolean,
        thresholds: ScoreThresholds = ScoreThresholds(),
    ): ScoreLevel = if (hintUsed) thresholds.classify(score(score, hintUsed = true)) else level
}
