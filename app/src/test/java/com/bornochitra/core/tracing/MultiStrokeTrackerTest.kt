package com.bornochitra.core.tracing

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

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
    private fun traceFully(tracker: MultiStrokeTracker, from: Point, to: Point) {
        tracker.onStart(tracePoint(from.x, from.y))
        var t = 0.2f
        while (t < 1f) {
            val x = from.x + (to.x - from.x) * t
            val y = from.y + (to.y - from.y) * t
            tracker.onMove(tracePoint(x, y))
            t += 0.2f
        }
        tracker.onMove(tracePoint(to.x, to.y))
        tracker.onEnd()
    }

    private val tightTolerance = TracingTolerance(maxDistance = 0.5f)

    @Test
    fun `completing a stroke advances to the next stroke`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())

        assertEquals(1, tracker.currentStrokeIndex)
        assertEquals(strokeB, tracker.currentStroke)
        assertEquals(listOf("a"), tracker.completedResults.map { it.strokeId })
        assertTrue(tracker.completedResults.single().isCompleted)
        assertFalse(tracker.isSequenceCompleted)
    }

    @Test
    fun `completing all strokes marks the sequence complete`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        assertTrue(tracker.isSequenceCompleted)
        assertNull(tracker.currentStroke)
        assertEquals(listOf("a", "b"), tracker.completedResults.map { it.strokeId })
    }

    @Test
    fun `toTraceResult after completion returns the aggregated result`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        val result = tracker.toTraceResult()

        assertEquals("exercise-1", result.exerciseId)
        assertEquals(tracker.completedResults, result.strokeResults)
        assertTrue(result.isCompleted)
        assertEquals(2, result.expectedStrokeCount)
    }

    @Test
    fun `toTraceResult before the sequence is complete throws`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        assertThrows(IllegalStateException::class.java) { tracker.toTraceResult() }
    }

    @Test
    fun `an attempt below the completion threshold does not advance and can be retried`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        // Only trace a small portion near the start of strokeA - well short of its full path.
        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(1f, 0f))
        tracker.onEnd()

        assertFalse(tracker.lastAttemptResult!!.isCompleted)
        assertEquals(0, tracker.currentStrokeIndex)
        assertTrue(tracker.completedResults.isEmpty())

        // Retrying by starting again succeeds.
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())

        assertEquals(1, tracker.currentStrokeIndex)
        assertEquals(listOf("a"), tracker.completedResults.map { it.strokeId })
    }

    @Test
    fun `cancelling a stroke discards points without scoring`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(2f, 0f))
        tracker.onCancel()

        assertNull(tracker.lastAttemptResult)
        assertEquals(0, tracker.currentStrokeIndex)
        assertTrue(tracker.completedResults.isEmpty())
    }

    @Test
    fun `input is ignored once the sequence is already complete`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA)), tolerance = tightTolerance)
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        assertTrue(tracker.isSequenceCompleted)
        val resultBefore = tracker.lastAttemptResult

        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(5f, 5f))
        tracker.onEnd()

        assertEquals(resultBefore, tracker.lastAttemptResult)
        assertTrue(tracker.isSequenceCompleted)
    }

    @Test
    fun `an exercise without strokes is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            MultiStrokeTracker(exercise(emptyList()))
        }
    }

    @Test
    fun `a custom completion threshold changes what counts as complete`() {
        val lenientTracker = MultiStrokeTracker(
            exercise(listOf(strokeA, strokeB)),
            tolerance = tightTolerance,
            completionThreshold = 0.3f,
        )

        // Partial trace: covers only the first checkpoint of a 3-checkpoint straight stroke.
        lenientTracker.onStart(tracePoint(0f, 0f))
        lenientTracker.onMove(tracePoint(1f, 0f))
        lenientTracker.onEnd()

        assertTrue(lenientTracker.lastAttemptResult!!.isCompleted)
        assertEquals(1, lenientTracker.currentStrokeIndex)
    }

    @Test
    fun `strokes are tracked in the given order across three strokes`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB, strokeC)), tolerance = tightTolerance)

        assertEquals(strokeA, tracker.currentStroke)

        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        assertEquals(strokeB, tracker.currentStroke)

        traceFully(tracker, strokeB.points.first(), strokeB.points.last())
        assertEquals(strokeC, tracker.currentStroke)

        traceFully(tracker, strokeC.points.first(), strokeC.points.last())
        assertTrue(tracker.isSequenceCompleted)
        assertEquals(listOf("a", "b", "c"), tracker.completedResults.map { it.strokeId })
    }

    @Test
    fun `a completed stroke records how many attempts it took`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        // A first attempt that falls short of the completion threshold, then a full trace.
        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(1f, 0f))
        tracker.onEnd()
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())

        val result = tracker.completedResults.single()
        assertEquals(2, result.attemptCount)
        assertEquals(0, result.outOfOrderAttempts)
    }

    @Test
    fun `tracing another stroke while a different one is expected is recorded as out of order`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        val attempt = tracker.lastAttemptResult!!
        assertFalse(attempt.isCompleted)
        assertEquals("a", attempt.strokeId)
        assertEquals(1, attempt.outOfOrderAttempts)
        assertEquals(0, tracker.currentStrokeIndex)
    }

    @Test
    fun `a sloppy retry of the expected stroke is not counted as out of order`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(1f, 0f))
        tracker.onEnd()

        assertEquals(0, tracker.lastAttemptResult!!.outOfOrderAttempts)
    }

    @Test
    fun `attempt counts are per stroke and reset once a stroke is completed`() {
        val tracker = MultiStrokeTracker(exercise(listOf(strokeA, strokeB)), tolerance = tightTolerance)

        tracker.onStart(tracePoint(0f, 0f))
        tracker.onMove(tracePoint(1f, 0f))
        tracker.onEnd()
        traceFully(tracker, strokeA.points.first(), strokeA.points.last())
        traceFully(tracker, strokeB.points.first(), strokeB.points.last())

        assertEquals(listOf(2, 1), tracker.completedResults.map { it.attemptCount })
    }
}
