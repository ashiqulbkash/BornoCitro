package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import kotlin.math.sqrt

/**
 * Measures how far a finger point is from the expected stroke path by projecting it onto the
 * nearest point of the path's polyline segments — the point-to-path distance from plan.md
 * Step 10.4. Produces raw distance only; deciding what counts as "close enough" is
 * [TracingTolerance]'s job, and turning distance into a score is the Score Calculator's job
 * (plan.md section 31).
 */
object PathDistanceCalculator {

    /** Shortest distance from [point] to the polyline formed by [pathPoints]. */
    fun distanceToPath(point: Point, pathPoints: List<Point>): Float {
        require(pathPoints.isNotEmpty()) { "pathPoints must not be empty" }
        if (pathPoints.size == 1) return distanceBetween(point, pathPoints.first())

        var minDistance = Float.MAX_VALUE
        for (index in 0 until pathPoints.size - 1) {
            val segmentDistance = distanceToSegment(point, pathPoints[index], pathPoints[index + 1])
            if (segmentDistance < minDistance) minDistance = segmentDistance
        }
        return minDistance
    }

    /** Convenience overload for a live finger sample. */
    fun distanceToPath(tracePoint: TracePoint, pathPoints: List<Point>): Float =
        distanceToPath(Point(tracePoint.x, tracePoint.y), pathPoints)

    /** Distance from [point] to its perpendicular projection onto segment [start]–[end], clamped to the segment. */
    private fun distanceToSegment(point: Point, start: Point, end: Point): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val lengthSquared = dx * dx + dy * dy
        if (lengthSquared == 0f) return distanceBetween(point, start)

        val t = (((point.x - start.x) * dx) + ((point.y - start.y) * dy)) / lengthSquared
        val clampedT = t.coerceIn(0f, 1f)
        val nearest = Point(start.x + clampedT * dx, start.y + clampedT * dy)
        return distanceBetween(point, nearest)
    }

    private fun distanceBetween(a: Point, b: Point): Float {
        val dx = b.x - a.x
        val dy = b.y - a.y
        return sqrt(dx * dx + dy * dy)
    }
}
