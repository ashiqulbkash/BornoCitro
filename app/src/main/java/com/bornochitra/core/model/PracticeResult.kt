package com.bornochitra.core.model

/** One finished exercise attempt, ready to be persisted. See plan.md sections 10-11. */
data class PracticeResult(
    val exerciseId: String,
    val score: Float,
    val scoreLevel: ScoreLevel,
    val completed: Boolean,
    val durationMs: Long,
    val completedAtMs: Long,
)
