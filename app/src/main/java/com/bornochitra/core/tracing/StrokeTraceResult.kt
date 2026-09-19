package com.bornochitra.core.tracing

/**
 * Raw measurement data produced while tracing a single [com.bornochitra.core.model.Stroke].
 * Holds coverage/distance data only; converting it into a score is the Score Calculator's job
 * (plan.md section 31).
 *
 * [attemptCount] and [outOfOrderAttempts] describe how the stroke was reached, not how well it
 * was traced: an ordinary retry only raises [attemptCount], while an attempt that traced a
 * different stroke of the exercise instead of the expected one also raises [outOfOrderAttempts].
 * That separation lets the Score Calculator measure stroke order without charging a child for
 * simply trying the same stroke again (plan.md section 38).
 */
data class StrokeTraceResult(
    val strokeId: String,
    val tracePoints: List<TracePoint>,
    val coverage: Float,
    val averageDistance: Float,
    val isCompleted: Boolean,
    val attemptCount: Int = 1,
    val outOfOrderAttempts: Int = 0,
)
