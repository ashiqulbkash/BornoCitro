package com.bornochitra.core.tracing

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.model.Stroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TracingEngineTest {

    private val strokeA = Stroke(id = "a", points = listOf(Point(0f, 0f), Point(10f, 0f)))
    private val strokeB = Stroke(id = "b", points = listOf(Point(10f, 0f), Point(10f, 10f)))

    private val exercise = Exercise(
        id = "exercise-1",
        title = "Test",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(strokeA, strokeB),
        order = 0,
    )

    private fun tracePoint(x: Float, y: Float) = TracePoint(x, y, timestampMs = 0L)

    private fun traceFully(engine: TracingEngine, from: Point, to: Point): TracingAttemptOutcome {
        engine.onStart(tracePoint(from.x, from.y))
        var t = 0.2f
        while (t < 1f) {
            engine.onMove(tracePoint(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t))
            t += 0.2f
        }
        engine.onMove(tracePoint(to.x, to.y))
        return engine.onEnd()
    }

    @Test
    fun `ending without ever starting reports no attempt`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        assertEquals(TracingAttemptOutcome.NoAttempt, engine.onEnd())
    }

    @Test
    fun `completing a stroke advances the current stroke and reports it as completed`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        val outcome = traceFully(engine, strokeA.points.first(), strokeA.points.last())

        assertEquals(TracingAttemptOutcome.StrokeAttempted(isCompleted = true), outcome)
        assertEquals(strokeB, engine.currentStroke)
        assertFalse(engine.isExerciseCompleted)
    }

    @Test
    fun `every stroke's guide is shown from the start so the whole letter is visible`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        assertEquals(listOf(strokeA, strokeB), engine.guideStrokes)

        traceFully(engine, strokeA.points.first(), strokeA.points.last())

        assertEquals(listOf(strokeA, strokeB), engine.guideStrokes)
    }

    @Test
    fun `the exercise completes with a perfect score once every stroke is written`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        traceFully(engine, strokeA.points.first(), strokeA.points.last())

        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertTrue("score was ${outcome.score}", outcome.score >= 90f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
        assertTrue(engine.isExerciseCompleted)
        assertEquals(null, engine.currentStroke)
        assertEquals(emptyList<Stroke>(), engine.guideStrokes)
    }

    @Test
    fun `a single-stroke exercise completes on its only stroke`() {
        val singleStroke = exercise.copy(strokes = listOf(strokeA))
        val engine = TracingEngine(singleStroke, tolerance = TracingTolerance(maxDistance = 0.5f))

        val outcome = traceFully(engine, strokeA.points.first(), strokeA.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertTrue(engine.isExerciseCompleted)
    }

    @Test
    fun `an attempt below the completion threshold does not advance`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(0f, 0f))
        engine.onMove(tracePoint(1f, 0f)) // well short of strokeA's full path
        val outcome = engine.onEnd()

        assertEquals(TracingAttemptOutcome.StrokeAttempted(isCompleted = false), outcome)
        assertEquals(strokeA, engine.currentStroke)
        assertFalse(engine.isExerciseCompleted)
    }

    @Test
    fun `cancelling discards the attempt without reporting an outcome`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(0f, 0f))
        engine.onMove(tracePoint(2f, 0f))
        engine.onCancel()

        assertEquals(strokeA, engine.currentStroke)
        assertFalse(engine.isExerciseCompleted)
    }

    /** Traces the whole exercise cleanly and returns the score it earns, as a reference point. */
    private fun cleanScore(): Float {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        traceFully(engine, strokeA.points.first(), strokeA.points.last())
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())
        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        return outcome.score
    }

    @Test
    fun `retrying a stroke does not lower the final score`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(0f, 0f))
        engine.onMove(tracePoint(1f, 0f)) // a first attempt well short of strokeA
        engine.onEnd()
        traceFully(engine, strokeA.points.first(), strokeA.points.last())
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertEquals(cleanScore(), outcome.score, 0.01f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
    }

    @Test
    fun `tracing the wrong stroke lowers the final score through the order metric`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        traceFully(engine, strokeB.points.first(), strokeB.points.last()) // strokeA was expected
        traceFully(engine, strokeA.points.first(), strokeA.points.last())
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertTrue("score was ${outcome.score}", outcome.score < cleanScore())
    }

    @Test
    fun `a letter and a drawing with the same strokes score identically`() {
        val letter = exercise.copy(id = "vowel-a", type = ExerciseType.VOWEL)
        val engine = TracingEngine(letter, tolerance = TracingTolerance(maxDistance = 0.5f))

        traceFully(engine, strokeA.points.first(), strokeA.points.last())
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertEquals(cleanScore(), outcome.score, 0.01f)
    }
}
