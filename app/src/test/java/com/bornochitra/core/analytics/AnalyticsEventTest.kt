package com.bornochitra.core.analytics

import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsEventTest {

    private val allEvents = listOf(
        AnalyticsEvent.ExerciseStarted("vowel-o", ExerciseType.VOWEL),
        AnalyticsEvent.ExerciseCompleted("vowel-o", ExerciseType.VOWEL, durationMs = 4_000),
        AnalyticsEvent.PracticeRepeated("vowel-o"),
        AnalyticsEvent.ScoreReceived("vowel-o", score = 94, level = ScoreLevel.PERFECT),
        AnalyticsEvent.ExerciseMastered("vowel-o"),
        AnalyticsEvent.DrawingCompleted("drawing-circle"),
    )

    @Test
    fun `events use the names from the plan`() {
        assertEquals(
            listOf(
                "exercise_started", "exercise_completed", "practice_repeated",
                "score_received", "exercise_mastered", "drawing_completed",
            ),
            allEvents.map { it.name },
        )
    }

    @Test
    fun `events describe content and results only, never the child`() {
        val allowedKeys = setOf("exercise_id", "type", "duration_ms", "score", "level")

        allEvents.forEach { event ->
            assertTrue("${event.name} carries ${event.params.keys - allowedKeys}", event.params.keys.all { it in allowedKeys })
        }
    }
}
