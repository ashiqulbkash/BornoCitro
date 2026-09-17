package com.bornochitra.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single practice attempt at an exercise (see plan.md Section 10).
 * DAOs/repositories are added when the progress-tracking feature is implemented.
 */
@Entity(tableName = "practice_session")
data class PracticeSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: String,
    val score: Float,
    val scoreLevel: String,
    val duration: Long,
    val completed: Boolean,
    val createdAt: Long,
)
