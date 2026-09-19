package com.bornochitra.core.tracing

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

class MultiStrokeTrackerTest {

    private val strokeA = Stroke(id = "a", points = listOf(Point(0f, 0f), Point(10f, 0f)))
    private val strokeB = Stroke(id = "b", points = listOf(Point(10f, 0f), Point(10f, 10f)))
    private val strokeC = Stroke(id = "c", points = listOf(Point(10f, 10f), Point(0f, 10f)))

    private fun exercise(strokes: List<Stroke>) = Exercise(
        id = "exercise-1",
        title = "Test",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.BEGINNER,
        strokes = strokes,
        order = 0,
    )

    private fun tracePoint(x: Float, y: Float) = TracePoint(x, y, timestampMs = 0L)

    /** Traces every 2 units along a straight stroke from [from] to [to], then lifts the finger. */
    private fun traceFully(tracker: MultiStrokeTracker, from: Point, to: Point) =
        tracePart(tracker, from, to, fromFraction = 0f, toFraction = 1f)

    /** Traces the [fromFraction]..[toFraction] part of the straight stroke [from]–[to], then lifts. */
    private fun tracePart(
        tracker: MultiStrokeTracker,
        from: Point,
        to: Point,
        fromFraction: Float,
        toFraction: Float,
    ) {
        fun pointAt(fraction: Float) =
            tracePoint(from.x + (to.x - from.x) * fraction, from.y + (to.y - from.y) * fraction)

        tracker.onStart(pointAt(fromFraction))
        var fraction = fromFraction + STEP
        while (fraction < toFraction) {
            tracker.onMove(pointAt(fraction))
            fraction += STEP
        }
        tracker.onMove(pointAt(toFraction))
        tracker.onEnd()
    }

    /** Traces the whole polyline through [vertices] in one touch, sampling about every unit. */
    private fun traceVertices(tracker: MultiStrokeTracker, vertices: List<Point>) {
        tracker.onStart(tracePoint(vertices.first().x, vertices.first().y))
        vertices.zipWithNext { start, end ->
            val steps = maxOf(1, hypot(end.x - start.x, end.y - start.y).toInt())
            for (step in 1..steps) {
                val fraction = step / steps.toFloat()
                tracker.onMove(
                    tracePoint(
                        start.x + (end.x - start.x) * fraction,
                        start.y + (end.y - start.y) * fraction,
                    ),
                )
            }
        }
        tracker.onEnd()
    }

    private val tightTolerance = TracingTolerance(maxDistance = 0.5f)

    private fun strokeResult(tracker: MultiStrokeTracker, strokeId: String) =
        tracker.strokeResults.single { it.strokeId == strokeId }

    @Test
    fun `the shape is only complete once every stroke is covered`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())

        assertFalse(tracker.isCompleted)
        assertTrue(strokeResult(tracker, "a").isCompleted)
        assertFalse(strokeResult(tracker, "b").isCompleted)

        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        assertTrue(tracker.isCompleted)
        assertEquals(listOf("a", "b"), tracker.strokeResults.map { it.strokeId })
    }

    @Test
    fun `lifting the finger part way keeps the progress and a new touch continues it`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA)), tolerance = tightTolerance)

        tracePart(tracker, strokeA.points.first(), strokeA.points.last(), fromFraction = 0f, toFraction = 0.5f)
        val halfway = strokeResult(tracker, "a").coverage
        assertFalse(tracker.isCompleted)
        assertTrue("coverage was $halfway", halfway > 0.4f && halfway < 0.7f)

        tracePart(tracker, strokeA.points.first(), strokeA.points.last(), fromFraction = 0.5f, toFraction = 1f)

        assertTrue("coverage was ${strokeResult(tracker, "a").coverage}", tracker.isCompleted)
    }

    @Test
    fun `strokes may be traced in any order`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB, strokeC)), tolerance = tightTolerance)

        traceFully(tracker, strokeC.points.first(), strokeC.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())
        assertFalse(tracker.isCompleted)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())

        assertTrue(tracker.isCompleted)
        assertTrue(tracker.strokeResults.all { it.outOfOrderAttempts == 0 })
    }

    @Test
    fun `a stroke traced backwards still completes it`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA)), tolerance = tightTolerance)

        traceFully(tracker, strokeA.points.last(), strokeA.points.first())

        assertTrue(tracker.isCompleted)
    }

    @Test
    fun `toTraceResult after completion returns the aggregated result`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        val result = tracker.toTraceResult()

        assertEquals("exercise-1", result.exerciseId)
        assertEquals(tracker.strokeResults, result.strokeResults)
        assertTrue(result.isCompleted)
        assertEquals(2, result.expectedStrokeCount)
    }

    @Test
    fun `toTraceResult before the shape is fully traced throws`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        assertThrows(IllegalStateException::class.java) { tracker.toTraceResult() }
    }

    @Test
    fun `tracing off the guide path covers nothing`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        tracker.onStart(tracePoint(50f, 50f))
        tracker.onMove(tracePoint(55f, 55f))
        tracker.onEnd()

        assertFalse(tracker.isCompleted)
        assertTrue(tracker.strokeResults.all { it.coverage == 0f })
    }

    @Test
    fun `each point is measured against the stroke it falls closest to`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        assertTrue(tracker.strokeResults.all { it.averageDistance < 0.01f })
        assertEquals(tracker.tracedPoints.size, tracker.strokeResults.sumOf { it.tracePoints.size })
    }

    @Test
    fun `cancelling discards the touch in progress but keeps earlier ones`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        val pointsBefore = tracker.tracedPoints

        tracker.onStart(tracePoint(50f, 50f))
        tracker.onMove(tracePoint(55f, 55f))
        tracker.onCancel()

        assertEquals(pointsBefore, tracker.tracedPoints)
        assertTrue(strokeResult(tracker, "a").isCompleted)
    }

    @Test
    fun `ending without a touch in progress measures nothing`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA)), tolerance = tightTolerance)

        assertFalse(tracker.onEnd())
        assertTrue(tracker.strokeResults.isEmpty())
        assertTrue(tracker.tracedPoints.isEmpty())
    }

    @Test
    fun `input is ignored once the shape is already complete`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA)), tolerance = tightTolerance)
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        val resultsBefore = tracker.strokeResults

        tracker.onStart(tracePoint(50f, 50f))
        tracker.onMove(tracePoint(55f, 55f))
        tracker.onEnd()

        assertEquals(resultsBefore, tracker.strokeResults)
        assertTrue(tracker.isCompleted)
    }

    @Test
    fun `an exercise without strokes is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            MultiStrokeTracker(exercise(emptyList()))
        }
    }

    /**
     * A skipped side used to pass as drawn: the traced sides beside it reach a whole tolerance into
     * it from each corner, leaving only 0.84 of the square's outline missing — inside the coverage
     * threshold's slack. The untraced gap it leaves behind is what rules it out.
     */
    @Test
    fun `a square with one side missing is not complete`() {
        val corners = listOf(Point(20f, 20f), Point(80f, 20f), Point(80f, 80f), Point(20f, 80f))
        val square = Stroke(id = "square", points = corners + corners.first())
        val tracker = MultiStrokeTracker(exercise(listOf(square)))

        traceVertices(tracker, corners) // every side but the last one, back up to the start

        val threeSides = strokeResult(tracker, "square")
        assertTrue("coverage was ${threeSides.coverage}", threeSides.coverage > 0.8f)
        assertFalse(tracker.isCompleted)

        traceVertices(tracker, listOf(corners.last(), corners.first()))

        assertTrue(tracker.isCompleted)
    }

    @Test
    fun `a house with a wall missing is not complete`() {
        val roof = Stroke(id = "roof", points = listOf(Point(15f, 45f), Point(50f, 15f), Point(85f, 45f)))
        val body = Stroke(
            id = "body",
            points = listOf(Point(15f, 45f), Point(15f, 85f), Point(85f, 85f), Point(85f, 45f)),
        )
        val tracker = MultiStrokeTracker(exercise(listOf(roof, body)))

        traceVertices(tracker, roof.points)
        traceVertices(tracker, body.points.drop(1)) // the left wall is left out

        assertTrue(strokeResult(tracker, "roof").isCompleted)
        assertFalse(strokeResult(tracker, "body").isCompleted)
        assertFalse(tracker.isCompleted)

        traceVertices(tracker, body.points.take(2))

        assertTrue(tracker.isCompleted)
    }

    @Test
    fun `a custom completion threshold changes what counts as complete`() {
        val lenientTracker = MultiStrokeTracker(
            exercise(listOf(strokeA)),
            tolerance = tightTolerance,
            completionThreshold = 0.3f,
        )

        tracePart(lenientTracker, strokeA.points.first(), strokeA.points.last(), fromFraction = 0f, toFraction = 0.4f)

        assertTrue(lenientTracker.isCompleted)
    }

    private companion object {
        /** Fraction of a stroke's length between traced samples, fine enough to cover its checkpoints. */
        const val STEP = 0.05f
    }
}
