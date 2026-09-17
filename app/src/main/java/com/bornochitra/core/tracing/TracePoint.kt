package com.bornochitra.core.tracing

/** A single sampled finger position during tracing. See plan.md section 25. */
data class TracePoint(val x: Float, val y: Float, val timestampMs: Long)
