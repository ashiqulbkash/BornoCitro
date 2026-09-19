package com.bornochitra.core.analytics

import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.ScoreLevel

/**
 * Product-interaction events from plan.md Step 24. They describe what happened to a piece of
 * content, never who did it: no user, device or account identifiers, and no free text.
 */
sealed interface AnalyticsEvent {

    val name: String

    val params: Map<String, String>

    data class ExerciseStarted(val exerciseId: String, val type: ExerciseType) : AnalyticsEvent {
        override val name = "exercise_started"
        override val params = mapOf("exercise_id" to exerciseId, "type" to type.name)
    }

    data class ExerciseCompleted(
        val exerciseId: String,
        val type: ExerciseType,
        val durationMs: Long,
    ) : AnalyticsEvent {
        override val name = "exercise_completed"
        override val params = mapOf(
            "exercise_id" to exerciseId,
            "type" to type.name,
            "duration_ms" to durationMs.toString(),
        )
    }

    /** The child is practising an exercise again: reopened after an earlier attempt, or reset mid-attempt. */
    data class PracticeRepeated(val exerciseId: String) : AnalyticsEvent {
        override val name = "practice_repeated"
        override val params = mapOf("exercise_id" to exerciseId)
    }

    data class ScoreReceived(val exerciseId: String, val score: Int, val level: ScoreLevel) : AnalyticsEvent {
        override val name = "score_received"
        override val params = mapOf(
            "exercise_id" to exerciseId,
            "score" to score.toString(),
            "level" to level.name,
        )
    }

    data class ExerciseMastered(val exerciseId: String) : AnalyticsEvent {
        override val name = "exercise_mastered"
        override val params = mapOf("exercise_id" to exerciseId)
    }

    data class DrawingCompleted(val exerciseId: String) : AnalyticsEvent {
        override val name = "drawing_completed"
        override val params = mapOf("exercise_id" to exerciseId)
    }
}
