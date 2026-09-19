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

    private fun traceFully(engine: TracingEngine, from: Point, to: Point): TracingAttemptOutcome =
        tracePart(engine, from, to, fromFraction = 0f, toFraction = 1f)

    /** Traces the [fromFraction]..[toFraction] part of the straight stroke [from]–[to], then lifts. */
    private fun tracePart(
        engine: TracingEngine,
        from: Point,
        to: Point,
        fromFraction: Float,
        toFraction: Float,
    ): TracingAttemptOutcome {
        fun pointAt(fraction: Float) =
            tracePoint(from.x + (to.x - from.x) * fraction, from.y + (to.y - from.y) * fraction)

        engine.onStart(pointAt(fromFraction))
        var fraction = fromFraction + STEP
        while (fraction < toFraction) {
            engine.onMove(pointAt(fraction))
            fraction += STEP
        }
        engine.onMove(pointAt(toFraction))
        return engine.onEnd()
    }

    @Test
    fun `ending without ever starting reports no attempt`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        assertEquals(TracingAttemptOutcome.NoAttempt, engine.onEnd())
    }

    @Test
    fun `lifting the finger with the shape unfinished reports how much is traced`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        val outcome = traceFully(engine, strokeA.points.first(), strokeA.points.last())

        require(outcome is TracingAttemptOutcome.Unfinished)
        assertTrue("coverage was ${outcome.coverage}", outcome.coverage > 0f && outcome.coverage < 1f)
        assertFalse(engine.isExerciseCompleted)
    }

    @Test
    fun `every stroke's guide is shown until the whole shape is traced`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        assertEquals(listOf(strokeA, strokeB), engine.guideStrokes)

        traceFully(engine, strokeA.points.first(), strokeA.points.last())

        assertEquals(listOf(strokeA, strokeB), engine.guideStrokes)
    }

    @Test
    fun `the exercise completes with a perfect score once the whole shape is written`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        traceFully(engine, strokeA.points.first(), strokeA.points.last())

        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        assertTrue("score was ${outcome.score}", outcome.score >= 90f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
        assertTrue(engine.isExerciseCompleted)
        assertEquals(emptyList<Stroke>(), engine.guideStrokes)
    }

    @Test
    fun `a stroke stopped part way through is resumed rather than restarted`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        tracePart(engine, strokeA.points.first(), strokeA.points.last(), fromFraction = 0f, toFraction = 0.5f)
        tracePart(engine, strokeA.points.first(), strokeA.points.last(), fromFraction = 0.5f, toFraction = 1f)
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted) { "expected completion, was $outcome" }
        assertEquals(cleanScore(), outcome.score, 0.01f)
    }

    @Test
    fun `the shape completes whatever order its strokes are traced in`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        traceFully(engine, strokeB.points.first(), strokeB.points.last())
        val outcome = traceFully(engine, strokeA.points.first(), strokeA.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted) { "expected completion, was $outcome" }
        assertEquals(cleanScore(), outcome.score, 0.01f)
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
    fun `tracing away from the guide path does not complete the exercise`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(50f, 50f))
        engine.onMove(tracePoint(55f, 55f))
        val outcome = engine.onEnd()

        assertEquals(TracingAttemptOutcome.Unfinished(coverage = 0f), outcome)
        assertFalse(engine.isExerciseCompleted)
    }

    @Test
    fun `cancelling discards the touch without reporting an outcome`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))

        engine.onStart(tracePoint(0f, 0f))
        engine.onMove(tracePoint(2f, 0f))
        engine.onCancel()

        assertEquals(TracingAttemptOutcome.NoAttempt, engine.onEnd())
        assertFalse(engine.isExerciseCompleted)
    }

    /** Traces the whole exercise cleanly in one touch per stroke, as a score reference point. */
    private fun cleanScore(): Float {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 0.5f))
        traceFully(engine, strokeA.points.first(), strokeA.points.last())
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())
        require(outcome is TracingAttemptOutcome.ExerciseCompleted)
        return outcome.score
    }

    @Test
    fun `sloppy tracing off the path lowers the score without blocking completion`() {
        val engine = TracingEngine(exercise, tolerance = TracingTolerance(maxDistance = 4f))

        engine.onStart(tracePoint(0f, 1.5f))
        var x = 0f
        while (x <= 10f) {
            engine.onMove(tracePoint(x, 1.5f))
            x += 0.5f
        }
        engine.onEnd()
        val outcome = traceFully(engine, strokeB.points.first(), strokeB.points.last())

        require(outcome is TracingAttemptOutcome.ExerciseCompleted) { "expected completion, was $outcome" }
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

    private companion object {
        /** Fraction of a stroke's length between traced samples, fine enough to cover its checkpoints. */
        const val STEP = 0.05f
    }
}
