package com.bornochitra.core.tracing

import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Validates plan.md Step 10.8's checklist for the real Bengali "অ" exercise content by running it
 * through the actual tracing engine (stroke path, starting point, stroke order, coverage, score).
 * Dotted rendering and live finger interaction can only be checked visually/manually via
 * [LetterTracingPrototype]'s preview and a physical device — see the post-task brief.
 */
class LetterTracingPrototypeTest {

    private val vowelO = vowelExercises.first { it.id == "vowel-o" }

    private fun tracePoint(point: Point) = TracePoint(point.x, point.y, timestampMs = 0L)

    @Test
    fun `vowel-o has exactly one stroke starting at its documented point`() {
        assertEquals(1, vowelO.strokes.size)
        assertEquals(Point(65f, 15f), vowelO.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing the real stroke path completes the sequence with a perfect score`() {
        val tracker = MultiStrokeTracker(vowelO)
        val scoreCalculator = TraceScoreCalculator()
        val stroke = vowelO.strokes.single()

        val points = stroke.points
        tracker.onStart(tracePoint(points.first()))
        points.drop(1).forEach { tracker.onMove(tracePoint(it)) }
        tracker.onEnd()

        assertTrue(tracker.isSequenceCompleted)
        val attempt = tracker.lastAttemptResult!!
        assertTrue(attempt.isCompleted)
        assertTrue("coverage was ${attempt.coverage}", attempt.coverage >= 0.95f)

        val score = scoreCalculator.scoreTrace(tracker.toTraceResult())
        assertTrue("score was $score", score >= 90f)
        assertEquals(ScoreLevel.PERFECT, scoreCalculator.scoreLevel(score))
    }

    @Test
    fun `tracing far from the guide path neither advances nor scores well`() {
        val tracker = MultiStrokeTracker(vowelO)
        val scoreCalculator = TraceScoreCalculator()

        tracker.onStart(tracePoint(Point(0f, 0f)))
        tracker.onMove(tracePoint(Point(5f, 5f)))
        tracker.onEnd()

        val attempt = tracker.lastAttemptResult!!
        assertFalse(attempt.isCompleted)
        assertFalse(tracker.isSequenceCompleted)

        val score = scoreCalculator.scoreStroke(attempt)
        assertEquals(ScoreLevel.LOW, scoreCalculator.scoreLevel(score))
    }
}
