package com.bornochitra.core.tracing

import com.bornochitra.core.content.consonantExercises
import com.bornochitra.core.content.drawingExercises
import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Validates plan.md Steps 10.8-10.10's checklist for real exercise content — the Bengali letters
 * "অ" and "ক", plus the non-letter "Circle" drawing — by running each through [TracingEngine],
 * the same reusable component [LetterTracingPrototype] drives (plan.md Step 10.11). "ক" has
 * straight/angular strokes structurally different from "অ"'s curved loop, and "Circle" is not a
 * letter at all, so passing all three proves the engine is not accidentally specialized for one
 * shape or for letters specifically. Dotted rendering and live finger interaction can only be
 * checked visually/manually via [LetterTracingPrototype]'s previews and a physical device — see
 * the post-task brief.
 */
class LetterTracingPrototypeTest {

    private val vowelO = vowelExercises.first { it.id == "vowel-o" }
    private val consonantKo = consonantExercises.first { it.id == "consonant-ko" }
    private val drawingCircle = drawingExercises.first { it.id == "drawing-circle" }

    private fun tracePoint(point: Point) = TracePoint(point.x, point.y, timestampMs = 0L)

    @Test
    fun `vowel-o has exactly one stroke starting at its documented point`() {
        assertEquals(1, vowelO.strokes.size)
        assertEquals(Point(65f, 15f), vowelO.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing vowel-o's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelO)
    }

    @Test
    fun `tracing far from vowel-o's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelO)
    }

    @Test
    fun `consonant-ko has exactly one stroke starting at its documented point`() {
        assertEquals(1, consonantKo.strokes.size)
        assertEquals(Point(30f, 15f), consonantKo.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing consonant-ko's real stroke path completes the sequence with a perfect score`() {
        // consonant-ko is built from straight diagonal/vertical segments, unlike vowel-o's curved
        // loop, so this proves the engine is not accidentally specialized for one stroke shape.
        assertFaithfulTraceIsPerfect(consonantKo)
    }

    @Test
    fun `tracing far from consonant-ko's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(consonantKo)
    }

    @Test
    fun `drawing-circle has exactly one stroke starting at its documented point`() {
        assertEquals(1, drawingCircle.strokes.size)
        assertEquals(Point(85f, 50f), drawingCircle.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing drawing-circle's real stroke path completes the sequence with a perfect score`() {
        // A closed arc loop, not a letter at all - proves the engine generalizes beyond handwriting.
        assertFaithfulTraceIsPerfect(drawingCircle)
    }

    @Test
    fun `tracing far from drawing-circle's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(drawingCircle)
    }

    private fun assertFaithfulTraceIsPerfect(exercise: Exercise) {
        val engine = TracingEngine(exercise)
        val points = exercise.strokes.single().points

        engine.onStart(tracePoint(points.first()))
        points.drop(1).forEach { engine.onMove(tracePoint(it)) }
        val outcome = engine.onEnd()

        assertTrue(engine.isExerciseCompleted)
        require(outcome is TracingAttemptOutcome.ExerciseCompleted) { "expected ExerciseCompleted, was $outcome" }
        assertTrue("score was ${outcome.score}", outcome.score >= 90f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
    }

    private fun assertOffPathTraceDoesNotComplete(exercise: Exercise) {
        val engine = TracingEngine(exercise)

        engine.onStart(tracePoint(Point(0f, 0f)))
        engine.onMove(tracePoint(Point(5f, 5f)))
        val outcome = engine.onEnd()

        assertEquals(TracingAttemptOutcome.StrokeAttempted(isCompleted = false), outcome)
        assertFalse(engine.isExerciseCompleted)
    }
}
