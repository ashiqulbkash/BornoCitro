package com.bornochitra.core.tracing

/** Aggregate tracing measurement data for an exercise attempt. See plan.md section 25. */
data class TraceResult(
    val exerciseId: String,
    val strokeResults: List<StrokeTraceResult>,
    val isCompleted: Boolean,
)
