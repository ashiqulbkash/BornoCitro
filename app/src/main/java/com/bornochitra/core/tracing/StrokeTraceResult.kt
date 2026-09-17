package com.bornochitra.core.tracing

/**
 * Raw measurement data produced while tracing a single [com.bornochitra.core.model.Stroke].
 * Holds coverage/distance data only; converting it into a score is the Score Calculator's job
 * (plan.md section 31).
 */
data class StrokeTraceResult(
    val strokeId: String,
    val tracePoints: List<TracePoint>,
    val coverage: Float,
    val averageDistance: Float,
    val isCompleted: Boolean,
)
