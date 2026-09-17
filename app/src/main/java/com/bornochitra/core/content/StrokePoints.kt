package com.bornochitra.core.content

import com.bornochitra.core.model.Point
import kotlin.math.cos
import kotlin.math.sin

/**
 * Samples points along basic path primitives so exercise content can be authored as simple
 * shapes instead of hand-listing every coordinate. Points live on a 0..100 normalized square
 * canvas (x right, y down).
 */
internal object StrokePoints {

    fun line(from: Point, to: Point, samples: Int = 10): List<Point> =
        (0..samples).map { step ->
            val t = step / samples.toFloat()
            Point(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t)
        }

    fun polyline(vertices: List<Point>, samplesPerSegment: Int = 8): List<Point> {
        require(vertices.size >= 2) { "polyline needs at least two vertices" }
        return vertices.zipWithNext { start, end -> line(start, end, samplesPerSegment) }
            .flatMapIndexed { index, segment -> if (index == 0) segment else segment.drop(1) }
    }

    fun arc(
        center: Point,
        radius: Float,
        startDeg: Float,
        sweepDeg: Float,
        samples: Int = 24,
    ): List<Point> =
        (0..samples).map { step ->
            val t = step / samples.toFloat()
            val angleRad = Math.toRadians((startDeg + sweepDeg * t).toDouble())
            Point(
                x = center.x + (radius * cos(angleRad)).toFloat(),
                y = center.y + (radius * sin(angleRad)).toFloat(),
            )
        }
}
