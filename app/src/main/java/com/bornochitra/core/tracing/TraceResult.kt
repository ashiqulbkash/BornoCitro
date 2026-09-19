package com.bornochitra.core.tracing

/**
 * Aggregate tracing measurement data for an exercise attempt. See plan.md section 25.
 *
 * [expectedStrokeCount] is the exercise's own stroke count, so stroke completion can be measured
 * against what the letter or shape actually requires rather than against however many strokes the
 * child happened to finish.
 */
data class TraceResult(
    val exerciseId: String,
    val strokeResults: List<StrokeTraceResult>,
    val isCompleted: Boolean,
    val expectedStrokeCount: Int = strokeResults.size,
)
