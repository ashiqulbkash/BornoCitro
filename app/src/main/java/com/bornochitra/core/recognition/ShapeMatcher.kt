package com.bornochitra.core.recognition

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Point
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Offline handwriting matcher for when the handwriting model is not available: the $P point-cloud
 * recognizer (Vatavu, Anthony and Wobbrock, 2012). Ink and every candidate's strokes are resampled
 * into [CLOUD_SIZE] points, scaled into a unit box and centred, then compared as unordered clouds,
 * so where the letter was written, how big, in which stroke order and in which direction do not
 * matter — only the shape does.
 */
object ShapeMatcher {

    const val CLOUD_SIZE = 64

    /**
     * Largest cloud distance still read as the letter. Calibrated on the shipped catalog: every
     * item redrawn at another size and place, stretched sideways, wobbling and in reverse stroke
     * order and direction lands at most 1.9 from its own guide, while random scribbles land 2.0 or
     * more from their nearest item 95% of the time, and must also be nearest the expected one.
     */
    const val DEFAULT_MAX_DISTANCE = 2.4f

    /** Whether [ink] is closest to [expected] of all of [category], and close enough to be it. */
    fun isRecognisedAs(
        ink: List<List<Point>>,
        expected: Exercise,
        category: List<Exercise>,
        maxDistance: Float = DEFAULT_MAX_DISTANCE,
    ): Boolean {
        val inkCloud = cloudOf(ink) ?: return false
        val nearest = (category + expected).distinctBy { it.id }
            .minBy { candidate -> cloudOf(candidate.strokes.map { it.points })?.let { cloudMatch(inkCloud, it) } ?: Float.MAX_VALUE }
        if (nearest.id != expected.id) return false
        val expectedCloud = cloudOf(expected.strokes.map { it.points }) ?: return false
        return cloudMatch(inkCloud, expectedCloud) <= maxDistance
    }

    /** Cloud distance between two drawings; 0 for the same shape, growing as they differ. */
    fun distance(first: List<List<Point>>, second: List<List<Point>>): Float {
        val firstCloud = cloudOf(first) ?: return Float.MAX_VALUE
        val secondCloud = cloudOf(second) ?: return Float.MAX_VALUE
        return cloudMatch(firstCloud, secondCloud)
    }

    private fun cloudMatch(first: List<Point>, second: List<Point>): Float {
        val step = floor(CLOUD_SIZE.toDouble().pow(0.5)).toInt()
        var best = Float.MAX_VALUE
        for (start in 0 until CLOUD_SIZE step step) {
            best = minOf(best, cloudDistance(first, second, start), cloudDistance(second, first, start))
        }
        return best
    }

    /** Greedy matching from [start], weighting the earliest matches most as $P does. */
    private fun cloudDistance(from: List<Point>, to: List<Point>, start: Int): Float {
        val matched = BooleanArray(to.size)
        var sum = 0f
        var index = start
        do {
            var nearest = -1
            var nearestDistance = Float.MAX_VALUE
            for (candidate in to.indices) {
                if (matched[candidate]) continue
                val d = distanceBetween(from[index], to[candidate])
                if (d < nearestDistance) {
                    nearestDistance = d
                    nearest = candidate
                }
            }
            matched[nearest] = true
            val weight = 1f - ((index - start + CLOUD_SIZE) % CLOUD_SIZE).toFloat() / CLOUD_SIZE
            sum += weight * nearestDistance
            index = (index + 1) % CLOUD_SIZE
        } while (index != start)
        return sum
    }

    /** The drawing as a normalised cloud, or null when it has no length to resample. */
    private fun cloudOf(strokes: List<List<Point>>): List<Point>? {
        val resampled = resample(strokes.filter { it.size >= 2 }) ?: return null
        return centre(scale(resampled))
    }

    /** [CLOUD_SIZE] points spread evenly along every stroke, never bridging the gap between two. */
    private fun resample(strokes: List<List<Point>>): List<Point>? {
        val totalLength = strokes.sumOf { pathLength(it).toDouble() }.toFloat()
        if (totalLength <= 0f) return null
        val interval = totalLength / (CLOUD_SIZE - 1)
        val result = mutableListOf(strokes.first().first())
        var carried = 0f
        for (stroke in strokes) {
            var previous = stroke.first()
            for (next in stroke.drop(1)) {
                var from = previous
                var segment = distanceBetween(from, next)
                while (carried + segment >= interval && result.size < CLOUD_SIZE) {
                    val t = (interval - carried) / segment
                    from = Point(from.x + t * (next.x - from.x), from.y + t * (next.y - from.y))
                    result += from
                    segment = distanceBetween(from, next)
                    carried = 0f
                }
                carried += segment
                previous = next
            }
        }
        // Float rounding can leave the last point short of the end.
        while (result.size < CLOUD_SIZE) result += strokes.last().last()
        return result
    }

    private fun scale(points: List<Point>): List<Point> {
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val size = max(points.maxOf { it.x } - minX, points.maxOf { it.y } - minY).takeIf { it > 0f } ?: 1f
        return points.map { Point((it.x - minX) / size, (it.y - minY) / size) }
    }

    private fun centre(points: List<Point>): List<Point> {
        val cx = points.sumOf { it.x.toDouble() }.toFloat() / points.size
        val cy = points.sumOf { it.y.toDouble() }.toFloat() / points.size
        return points.map { Point(it.x - cx, it.y - cy) }
    }

    private fun pathLength(points: List<Point>): Float =
        points.zipWithNext().sumOf { (a, b) -> distanceBetween(a, b).toDouble() }.toFloat()

    private fun distanceBetween(a: Point, b: Point): Float {
        val dx = b.x - a.x
        val dy = b.y - a.y
        return sqrt(dx * dx + dy * dy)
    }
}
