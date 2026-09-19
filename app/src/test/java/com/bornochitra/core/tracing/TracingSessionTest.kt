package com.bornochitra.core.tracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TracingSessionTest {

    private val events = mutableListOf<TracingPointerEvent>()
    private lateinit var session: TracingSession

    @Before
    fun setUp() {
        events.clear()
        session = TracingSession(onEvent = { events += it })
    }

    private fun point(x: Float, y: Float) = TracePoint(x, y, timestampMs = 0L)

    @Test
    fun `start seeds traced points and emits Start`() {
        val start = point(1f, 1f)

        session.onStart(start)

        assertEquals(listOf(start), session.tracedPoints)
        assertEquals(listOf<TracingPointerEvent>(TracingPointerEvent.Start(start)), events)
    }

    @Test
    fun `move after start appends the point and emits Move`() {
        val start = point(0f, 0f)
        val move = point(5f, 5f)

        session.onStart(start)
        session.onMove(move)

        assertEquals(listOf(start, move), session.tracedPoints)
        assertEquals(
            listOf<TracingPointerEvent>(TracingPointerEvent.Start(start), TracingPointerEvent.Move(move)),
            events,
        )
    }

    @Test
    fun `move without a preceding start is ignored`() {
        session.onMove(point(5f, 5f))

        assertTrue(session.tracedPoints.isEmpty())
        assertTrue(events.isEmpty())
    }

    @Test
    fun `end emits the last traced point`() {
        val start = point(0f, 0f)
        val move = point(5f, 5f)
        session.onStart(start)
        session.onMove(move)

        session.onEnd()

        assertEquals(listOf<TracingPointerEvent>(
            TracingPointerEvent.Start(start),
            TracingPointerEvent.Move(move),
            TracingPointerEvent.End(move),
        ), events)
    }

    @Test
    fun `end without a preceding start does nothing`() {
        session.onEnd()

        assertTrue(events.isEmpty())
    }

    @Test
    fun `cancel clears traced points and emits Cancel`() {
        session.onStart(point(0f, 0f))
        session.onMove(point(5f, 5f))

        session.onCancel()

        assertTrue(session.tracedPoints.isEmpty())
        assertEquals(TracingPointerEvent.Cancel, events.last())
    }

    @Test
    fun `starting again discards points from a previous attempt`() {
        session.onStart(point(0f, 0f))
        session.onMove(point(5f, 5f))

        val newStart = point(9f, 9f)
        session.onStart(newStart)

        assertEquals(listOf(newStart), session.tracedPoints)
    }

    @Test
    fun `live points follow the finger without copying, while traced points are a snapshot`() {
        session.onStart(point(0f, 0f))
        val snapshot = session.tracedPoints
        val live = session.livePoints

        session.onMove(point(5f, 5f))

        assertEquals(1, snapshot.size)
        assertEquals(2, live.size)
        assertEquals(session.tracedPoints, live)
    }
}
