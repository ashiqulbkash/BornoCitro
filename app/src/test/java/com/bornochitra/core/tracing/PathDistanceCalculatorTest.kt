package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.math.sqrt

class PathDistanceCalculatorTest {

    private val straightPath = listOf(Point(0f, 0f), Point(10f, 0f))
    private val bentPath = listOf(Point(0f, 0f), Point(10f, 0f), Point(10f, 10f))

    @Test
    fun `point exactly on the path has zero distance`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(5f, 0f), straightPath)

        assertEquals(0f, distance, DELTA)
    }

    @Test
    fun `slight deviation returns the small perpendicular distance`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(5f, 1f), straightPath)

        assertEquals(1f, distance, DELTA)
    }

    @Test
    fun `large deviation returns the large perpendicular distance`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(5f, 20f), straightPath)

        assertEquals(20f, distance, DELTA)
    }

    @Test
    fun `point beyond the segment start clamps to the start point`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(-5f, 0f), straightPath)

        assertEquals(5f, distance, DELTA)
    }

    @Test
    fun `point beyond the segment end clamps to the end point`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(15f, 0f), straightPath)

        assertEquals(5f, distance, DELTA)
    }

    @Test
    fun `point exactly on a vertex between two segments has zero distance`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(10f, 0f), bentPath)

        assertEquals(0f, distance, DELTA)
    }

    @Test
    fun `point nearest the second segment picks that segment, not the first`() {
        // (9,5) is 5 units from segment 1 (0,0)-(10,0) but only 1 unit from segment 2 (10,0)-(10,10).
        val distance = PathDistanceCalculator.distanceToPath(Point(9f, 5f), bentPath)

        assertEquals(1f, distance, DELTA)
    }

    @Test
    fun `point beyond the last segment's end clamps to the final point`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(11f, 11f), bentPath)

        assertEquals(sqrt(2f), distance, DELTA)
    }

    @Test
    fun `single point path treats the point as the whole path`() {
        val distance = PathDistanceCalculator.distanceToPath(Point(8f, 9f), listOf(Point(5f, 5f)))

        assertEquals(5f, distance, DELTA)
    }

    @Test
    fun `zero-length segment from a duplicate point does not break distance calculation`() {
        val pathWithDuplicate = listOf(Point(0f, 0f), Point(0f, 0f), Point(10f, 0f))

        val distance = PathDistanceCalculator.distanceToPath(Point(0f, 3f), pathWithDuplicate)

        assertEquals(3f, distance, DELTA)
    }

    @Test
    fun `empty path is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            PathDistanceCalculator.distanceToPath(Point(0f, 0f), emptyList())
        }
    }

    @Test
    fun `trace point overload matches the equivalent point`() {
        val tracePoint = TracePoint(x = 5f, y = 1f, timestampMs = 0L)

        val distance = PathDistanceCalculator.distanceToPath(tracePoint, straightPath)

        assertEquals(1f, distance, DELTA)
    }

    companion object {
        private const val DELTA = 0.0001f
    }
}
