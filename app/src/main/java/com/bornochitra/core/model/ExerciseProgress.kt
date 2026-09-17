package com.bornochitra.core.model

/** Aggregate learning progress for a single exercise. See plan.md section 10-11. */
data class ExerciseProgress(
    val exerciseId: String,
    val attemptCount: Int,
    val completedCount: Int,
    val bestScore: Float,
    val lastScore: Float,
    val lastPracticedAt: Long,
    val isMastered: Boolean,
)
