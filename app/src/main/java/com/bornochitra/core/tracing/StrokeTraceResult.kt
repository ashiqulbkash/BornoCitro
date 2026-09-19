package com.bornochitra.core.tracing

/**
 * Raw measurement data produced while tracing a single [com.bornochitra.core.model.Stroke].
 * Holds coverage/distance data only; converting it into a score is the Score Calculator's job
 * (plan.md section 31).
 *
 * [attemptCount] and [outOfOrderAttempts] describe how the stroke was reached, not how well it
 * was traced. A whole letter or drawing is now traced as one attempt in any stroke order
 * (plan.md Step 2), so nothing the child does counts as out of order and both keep their
 * defaults; the Score Calculator's order metric (plan.md section 38) is satisfied by every
 * attempt as a result.
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
