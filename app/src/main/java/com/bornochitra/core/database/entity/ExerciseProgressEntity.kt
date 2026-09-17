package com.bornochitra.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Aggregate learning progress for a single exercise (see plan.md Section 10).
 * DAOs/repositories are added when the progress-tracking feature is implemented.
 */
@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey val exerciseId: String,
    val attemptCount: Int,
    val completedCount: Int,
    val bestScore: Float,
    val lastScore: Float,
    val scoreLevel: String,
    val lastPracticedAt: Long,
    val isMastered: Boolean,
)
