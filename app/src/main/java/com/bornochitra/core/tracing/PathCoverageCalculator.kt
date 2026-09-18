package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import kotlin.math.sqrt

/**
 * Measures how much of an expected stroke path a child has successfully traced — the path
 * coverage from plan.md Step 10.5. Lays evenly spaced checkpoints along the path's arc length
 * (reusing [DottedPathSampler]) and reports the fraction reached by at least one traced point,
 * so the result depends on how much of the path was covered, not on how many touch samples the
 * device happened to report (which varies with finger speed/device sampling rate).
 */
object PathCoverageCalculator {

    /** Spacing between coverage checkpoints, finer than the default visual dot spacing for a more precise measurement. */
    const val DEFAULT_CHECKPOINT_SPACING = 4f

    /** Fraction (0f..1f) of [expectedPathPoints] reached by [tracedPoints], within [tolerance]. */
    fun coverage(
        expectedPathPoints: List<Point>,
        tracedPoints: List<TracePoint>,
        tolerance: TracingTolerance = TracingTolerance(),
        checkpointSpacing: Float = DEFAULT_CHECKPOINT_SPACING,
    ): Float {
        if (expectedPathPoints.size < 2 || tracedPoints.isEmpty()) return 0f

        val checkpoints = DottedPathSampler.sample(expectedPathPoints, checkpointSpacing)
        if (checkpoints.isEmpty()) return 0f

        val reachedCount = checkpoints.count { checkpoint ->
            tracedPoints.any { traced -> tolerance.isWithinTolerance(distance(traced, checkpoint)) }
        }
        return reachedCount.toFloat() / checkpoints.size
    }

    private fun distance(tracePoint: TracePoint, point: Point): Float {
        val dx = point.x - tracePoint.x
        val dy = point.y - tracePoint.y
        return sqrt(dx * dx + dy * dy)
    }
}
