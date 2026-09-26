package com.bornochitra.core.model

import com.bornochitra.core.tracing.ScoreThresholds

/**
 * How many stars a score earns: one per [ScoreLevel] band, so the rating follows the same configurable
 * thresholds as the score's level. An exercise's stars come from its best score, so a good attempt is never
 * taken away by a worse one.
 */
object StarRule {

    const val MAX_STARS = 3

    fun starsFor(score: Float, thresholds: ScoreThresholds = ScoreThresholds()): Int = when (thresholds.classify(score)) {
        ScoreLevel.PERFECT -> 3
        ScoreLevel.MEDIUM -> 2
        ScoreLevel.LOW -> 1
    }

    /** No stars before the first attempt; afterwards the stars of the best score so far. */
    fun starsOf(progress: ExerciseProgress?, thresholds: ScoreThresholds = ScoreThresholds()): Int =
        if (progress == null || progress.attemptCount == 0) 0 else starsFor(progress.bestScore, thresholds)
}
