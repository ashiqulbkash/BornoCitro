package com.bornochitra.core.model

/**
 * When an exercise counts as learned rather than merely practised, per plan.md section 41:
 * finished [requiredCompletions] times **and** a best score of at least [minBestScore]. Both are
 * configurable so the bar can be moved without touching the code that applies it.
 */
data class MasteryRule(
    val requiredCompletions: Int = DEFAULT_REQUIRED_COMPLETIONS,
    val minBestScore: Float = DEFAULT_MIN_BEST_SCORE,
) {

    init {
        require(requiredCompletions > 0) { "requiredCompletions must be positive" }
        require(minBestScore in 0f..100f) { "minBestScore must be within 0..100" }
    }

    fun isMastered(completedCount: Int, bestScore: Float): Boolean =
        completedCount >= requiredCompletions && bestScore >= minBestScore

    companion object {
        const val DEFAULT_REQUIRED_COMPLETIONS = 3
        const val DEFAULT_MIN_BEST_SCORE = 80f
    }
}
