package com.bornochitra.core.tracing

/** A raw finger-input lifecycle event captured while tracing a stroke. See plan.md section 27. */
sealed interface TracingPointerEvent {
    data class Start(val point: TracePoint) : TracingPointerEvent
    data class Move(val point: TracePoint) : TracingPointerEvent
    data class End(val point: TracePoint) : TracingPointerEvent
    data object Cancel : TracingPointerEvent
}
