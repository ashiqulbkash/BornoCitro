package com.bornochitra.core.tracing

/**
 * In-memory accumulator for one finger's touch lifecycle while tracing a single stroke. Only
 * appends points and emits [TracingPointerEvent]s — no distance/coverage math and no persistence,
 * per plan.md section 27 ("do not perform heavy calculations on every event", "do not write
 * anything to Room"). Independent of Compose/ViewModel/Room so it stays unit-testable on its own.
 */
class TracingSession(private val onEvent: (TracingPointerEvent) -> Unit) {

    private val mutablePoints = mutableListOf<TracePoint>()

    /** A copy that stays valid after the session moves on. Use it once per stroke, not per event. */
    val tracedPoints: List<TracePoint> get() = mutablePoints.toList()

    /**
     * The points so far without copying them, for drawing on every pointer event. It changes as the
     * finger moves, so read it right away and never keep it.
     */
    val livePoints: List<TracePoint> get() = mutablePoints

    /** Starts a new trace, discarding any points left over from a previous attempt. */
    fun onStart(point: TracePoint) {
        mutablePoints.clear()
        mutablePoints += point
        onEvent(TracingPointerEvent.Start(point))
    }

    /** Ignored if a trace was never started, e.g. a stray move event without a preceding down. */
    fun onMove(point: TracePoint) {
        if (mutablePoints.isEmpty()) return
        mutablePoints += point
        onEvent(TracingPointerEvent.Move(point))
    }

    /** Ignored if a trace was never started. */
    fun onEnd() {
        val last = mutablePoints.lastOrNull() ?: return
        onEvent(TracingPointerEvent.End(last))
    }

    /** The system cancelled the gesture (e.g. an ancestor intercepted it); discard the attempt. */
    fun onCancel() {
        mutablePoints.clear()
        onEvent(TracingPointerEvent.Cancel)
    }
}
