package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import kotlin.math.sqrt

/**
 * How much of an expected stroke path a trace covered, and where the biggest hole in it is.
 *
 * [longestUntracedGap] is what tells a wobbly-but-whole trace apart from one that left a piece of
 * the shape out: the same missing fraction may be scattered as harmless specks along the path or
 * sit in one continuous stretch that never got drawn. See [PathCoverageCalculator].
 */
data class PathCoverage(
    /** Fraction (0f..1f) of the path's checkpoints reached by the trace. */
    val fraction: Float,
    /** Length, in canvas units, of the longest unbroken run of checkpoints no traced point reached. */
    val longestUntracedGap: Float,
)

/**
 * Measures how much of an expected stroke path a child has successfully traced — the path
 * coverage from plan.md Step 10.5. Lays evenly spaced checkpoints along the path's arc length
 * (reusing [DottedPathSampler]) and reports the fraction reached by at least one traced point,
 * so the result depends on how much of the path was covered, not on how many touch samples the
 * device happened to report (which varies with finger speed/device sampling rate).
 *
 * The fraction alone cannot say whether the shape was actually drawn. Because a checkpoint counts
 * as reached from up to [TracingTolerance.maxDistance] away, the traced neighbours of a missing
 * segment bleed over its ends and hide most of what is missing: three sides of a square measure
 * 0.84 covered, well above the completion threshold. So the longest untraced gap is measured too,
 * and completion asks for both (see [MultiStrokeTracker.DEFAULT_MAX_UNTRACED_GAP]).
 */
object PathCoverageCalculator {

    /** Spacing between coverage checkpoints, finer than the default visual dot spacing for a more precise measurement. */
    const val DEFAULT_CHECKPOINT_SPACING = 4f

    /** Coverage of [expectedPathPoints] by [tracedPoints], within [tolerance]. */
    fun measure(
        expectedPathPoints: List<Point>,
        tracedPoints: List<TracePoint>,
        tolerance: TracingTolerance = TracingTolerance(),
        checkpointSpacing: Float = DEFAULT_CHECKPOINT_SPACING,
    ): PathCoverage {
        if (expectedPathPoints.size < 2) return PathCoverage(fraction = 0f, longestUntracedGap = 0f)

        val checkpoints = DottedPathSampler.sample(expectedPathPoints, checkpointSpacing)
        if (checkpoints.isEmpty()) return PathCoverage(fraction = 0f, longestUntracedGap = 0f)

        var reachedCount = 0
        var currentGap = 0
        var longestGap = 0
        for (checkpoint in checkpoints) {
            val isReached = tracedPoints.any { traced -> tolerance.isWithinTolerance(distance(traced, checkpoint)) }
            if (isReached) {
                reachedCount++
                currentGap = 0
            } else {
                currentGap++
                if (currentGap > longestGap) longestGap = currentGap
            }
        }
        return PathCoverage(
            fraction = reachedCount.toFloat() / checkpoints.size,
            longestUntracedGap = longestGap * checkpointSpacing,
        )
    }

    private fun distance(tracePoint: TracePoint, point: Point): Float {
        val dx = point.x - tracePoint.x
        val dy = point.y - tracePoint.y
        return sqrt(dx * dx + dy * dy)
    }
}
