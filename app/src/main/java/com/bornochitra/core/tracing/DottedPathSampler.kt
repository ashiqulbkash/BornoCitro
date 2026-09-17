package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import kotlin.math.sqrt

/**
 * Places evenly spaced dots along a polyline by walking its cumulative arc length. Works for both
 * straight strokes (two points) and curved strokes (many points) since both are plain point
 * sequences. See plan.md section 26.
 */
object DottedPathSampler {

    fun sample(points: List<Point>, spacing: Float): List<Point> {
        require(spacing > 0f) { "spacing must be positive" }
        if (points.size < 2) return points

        val dots = mutableListOf(points.first())
        var distanceSinceLastDot = 0f

        for (index in 0 until points.size - 1) {
            val start = points[index]
            val end = points[index + 1]
            val segmentLength = distanceBetween(start, end)
            if (segmentLength <= 0f) continue

            var walked = 0f
            while (distanceSinceLastDot + (segmentLength - walked) >= spacing) {
                walked += spacing - distanceSinceLastDot
                val t = walked / segmentLength
                dots += Point(
                    x = start.x + (end.x - start.x) * t,
                    y = start.y + (end.y - start.y) * t,
                )
                distanceSinceLastDot = 0f
            }
            distanceSinceLastDot += segmentLength - walked
        }
        return dots
    }

    private fun distanceBetween(a: Point, b: Point): Float {
        val dx = b.x - a.x
        val dy = b.y - a.y
        return sqrt(dx * dx + dy * dy)
    }
}
