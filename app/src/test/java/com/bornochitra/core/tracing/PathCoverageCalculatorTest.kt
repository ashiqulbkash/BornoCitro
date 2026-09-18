package com.bornochitra.core.tracing

import com.bornochitra.core.model.Point
import org.junit.Assert.assertEquals
import org.junit.Test

class PathCoverageCalculatorTest {

    // Checkpoints at x = 0, 5, 10, 15, 20 with checkpointSpacing = 5f.
    private val straightPath = listOf(Point(0f, 0f), Point(20f, 0f))
    private val tightTolerance = TracingTolerance(maxDistance = 0.5f)

    private fun tracePoint(x: Float, y: Float = 0f) = TracePoint(x, y, timestampMs = 0L)

    @Test
    fun `tracing every checkpoint gives full coverage`() {
        val tracedPoints = listOf(0f, 5f, 10f, 15f, 20f).map { tracePoint(it) }

        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(1f, coverage, DELTA)
    }

    @Test
    fun `tracing only half the path gives partial coverage`() {
        val tracedPoints = (0..10).map { tracePoint(it.toFloat()) } // covers x = 0..10 only

        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        // Reaches checkpoints 0, 5, 10 out of 0, 5, 10, 15, 20.
        assertEquals(0.6f, coverage, DELTA)
    }

    @Test
    fun `no traced points gives zero coverage`() {
        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = emptyList(),
        )

        assertEquals(0f, coverage, DELTA)
    }

    @Test
    fun `traced points far from the path give zero coverage`() {
        val tracedPoints = listOf(tracePoint(x = 10f, y = 100f))

        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(0f, coverage, DELTA)
    }

    @Test
    fun `coverage does not depend on how many touch samples were recorded`() {
        val tolerance = TracingTolerance(maxDistance = 10f)
        val sparseTrace = listOf(tracePoint(0f), tracePoint(20f)) // just start and end
        val denseTrace = (0..20).map { tracePoint(it.toFloat()) } // one sample per unit

        val sparseCoverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = sparseTrace,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        )
        val denseCoverage = PathCoverageCalculator.coverage(
            expectedPathPoints = straightPath,
            tracedPoints = denseTrace,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(1f, sparseCoverage, DELTA)
        assertEquals(sparseCoverage, denseCoverage, DELTA)
    }

    @Test
    fun `distance exactly at the tolerance boundary counts as reached`() {
        val tracedPoints = listOf(tracePoint(x = 0f, y = 0.5f))
        val tolerance = TracingTolerance(maxDistance = 0.5f)

        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = listOf(Point(0f, 0f), Point(0.0001f, 0f)),
            tracedPoints = tracedPoints,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(1f, coverage, DELTA)
    }

    @Test
    fun `coverage works for a multi-segment curved path`() {
        val bentPath = listOf(Point(0f, 0f), Point(10f, 0f), Point(10f, 10f))
        // Checkpoints at spacing 5 along the bent path: (0,0) (5,0) (10,0) (10,5) (10,10).
        val tracedPoints = listOf(tracePoint(0f, 0f), tracePoint(5f, 0f), tracePoint(10f, 0f))

        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = bentPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(0.6f, coverage, DELTA)
    }

    @Test
    fun `a degenerate single-point path gives zero coverage`() {
        val coverage = PathCoverageCalculator.coverage(
            expectedPathPoints = listOf(Point(5f, 5f)),
            tracedPoints = listOf(tracePoint(5f, 5f)),
        )

        assertEquals(0f, coverage, DELTA)
    }

    companion object {
        private const val DELTA = 0.0001f
    }
}
