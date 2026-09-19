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

        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        ).fraction

        assertEquals(1f, coverage, DELTA)
    }

    @Test
    fun `tracing only half the path gives partial coverage`() {
        val tracedPoints = (0..10).map { tracePoint(it.toFloat()) } // covers x = 0..10 only

        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        ).fraction

        // Reaches checkpoints 0, 5, 10 out of 0, 5, 10, 15, 20.
        assertEquals(0.6f, coverage, DELTA)
    }

    @Test
    fun `no traced points gives zero coverage`() {
        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = emptyList(),
        ).fraction

        assertEquals(0f, coverage, DELTA)
    }

    @Test
    fun `traced points far from the path give zero coverage`() {
        val tracedPoints = listOf(tracePoint(x = 10f, y = 100f))

        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        ).fraction

        assertEquals(0f, coverage, DELTA)
    }

    @Test
    fun `coverage does not depend on how many touch samples were recorded`() {
        val tolerance = TracingTolerance(maxDistance = 10f)
        val sparseTrace = listOf(tracePoint(0f), tracePoint(20f)) // just start and end
        val denseTrace = (0..20).map { tracePoint(it.toFloat()) } // one sample per unit

        val sparseCoverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = sparseTrace,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        ).fraction
        val denseCoverage = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = denseTrace,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        ).fraction

        assertEquals(1f, sparseCoverage, DELTA)
        assertEquals(sparseCoverage, denseCoverage, DELTA)
    }

    @Test
    fun `distance exactly at the tolerance boundary counts as reached`() {
        val tracedPoints = listOf(tracePoint(x = 0f, y = 0.5f))
        val tolerance = TracingTolerance(maxDistance = 0.5f)

        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = listOf(Point(0f, 0f), Point(0.0001f, 0f)),
            tracedPoints = tracedPoints,
            tolerance = tolerance,
            checkpointSpacing = 5f,
        ).fraction

        assertEquals(1f, coverage, DELTA)
    }

    @Test
    fun `coverage works for a multi-segment curved path`() {
        val bentPath = listOf(Point(0f, 0f), Point(10f, 0f), Point(10f, 10f))
        // Checkpoints at spacing 5 along the bent path: (0,0) (5,0) (10,0) (10,5) (10,10).
        val tracedPoints = listOf(tracePoint(0f, 0f), tracePoint(5f, 0f), tracePoint(10f, 0f))

        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = bentPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        ).fraction

        assertEquals(0.6f, coverage, DELTA)
    }

    @Test
    fun `a degenerate single-point path gives zero coverage`() {
        val coverage = PathCoverageCalculator.measure(
            expectedPathPoints = listOf(Point(5f, 5f)),
            tracedPoints = listOf(tracePoint(5f, 5f)),
        ).fraction

        assertEquals(0f, coverage, DELTA)
    }

    @Test
    fun `a fully traced path leaves no untraced gap`() {
        val tracedPoints = (0..20).map { tracePoint(it.toFloat()) }

        val measured = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(1f, measured.fraction, DELTA)
        assertEquals(0f, measured.longestUntracedGap, DELTA)
    }

    @Test
    fun `the untraced gap measures the longest unbroken stretch that was missed`() {
        // Reaches checkpoints 0 and 20 only, so 5, 10 and 15 are missed in one run.
        val tracedPoints = listOf(tracePoint(0f), tracePoint(20f))

        val measured = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(0.4f, measured.fraction, DELTA)
        assertEquals(15f, measured.longestUntracedGap, DELTA)
    }

    @Test
    fun `scattered misses do not add up into one gap`() {
        // Misses checkpoints 5 and 15, but they sit either side of a checkpoint that was reached.
        val tracedPoints = listOf(tracePoint(0f), tracePoint(10f), tracePoint(20f))

        val measured = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = tracedPoints,
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(0.6f, measured.fraction, DELTA)
        assertEquals(5f, measured.longestUntracedGap, DELTA)
    }

    @Test
    fun `a path nobody traced is one gap from end to end`() {
        val measured = PathCoverageCalculator.measure(
            expectedPathPoints = straightPath,
            tracedPoints = emptyList(),
            tolerance = tightTolerance,
            checkpointSpacing = 5f,
        )

        assertEquals(0f, measured.fraction, DELTA)
        assertEquals(25f, measured.longestUntracedGap, DELTA)
    }

    companion object {
        private const val DELTA = 0.0001f
    }
}
