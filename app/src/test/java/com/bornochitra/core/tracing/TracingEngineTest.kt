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
        assertEquals(2, engine.currentStrokeNumber)
        assertFalse(engine.isExerciseCompleted)
    }

    @Test
    fun `completing every stroke reports the exercise complete with a perfect score`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        traceFully(engine, strokeA.points.first(), strokeA.points.last())

        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertTrue("score was ${outcome.score}", outcome.score >= 90f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
        assertTrue(engine.isExerciseCompleted)
        assertEquals(null, engine.currentStroke)
    }

    @Test
    fun `an attempt below the completion threshold does not advance`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(0f, 0f))
        engine.onMove(tracePoint(1f, 0f)) // well short of strokeA's full path
        val outcome = engine.onEnd()

        assertEquals(TracingAttemptOutcome.StrokeAttempted(isCompleted = false), outcome)
        assertEquals(strokeA, engine.currentStroke)
        assertEquals(1, engine.currentStrokeNumber)
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
}
