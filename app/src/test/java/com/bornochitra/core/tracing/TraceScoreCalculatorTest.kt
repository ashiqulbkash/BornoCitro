package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class TraceScoreCalculatorTest {

    private val tolerance = TracingTolerance(maxDistance = 8f)
    private val calculator = TraceScoreCalculator(tolerance = tolerance)

    private fun strokeResult(coverage: Float, averageDistance: Float, id: String = "s") = StrokeTraceResult(
        strokeId = id,
        tracePoints = emptyList(),
        coverage = coverage,
        averageDistance = averageDistance,
        isCompleted = true,
    )

    @Test
    fun `full coverage and zero distance scores 100 and is PERFECT`() {
        val score = calculator.scoreStroke(strokeResult(coverage = 1f, averageDistance = 0f))

        assertEquals(100f, score, DELTA)
        assertEquals(ScoreLevel.PERFECT, calculator.scoreLevel(score))
    }

    @Test
    fun `no coverage and distance at the tolerance boundary scores 0 and is LOW`() {
        val score = calculator.scoreStroke(strokeResult(coverage = 0f, averageDistance = 8f))

        assertEquals(0f, score, DELTA)
        assertEquals(ScoreLevel.LOW, calculator.scoreLevel(score))
    }

    @Test
    fun `partial coverage and moderate distance blend into a MEDIUM score`() {
        // accuracy = 1 - 4/8 = 0.5; score = 0.8*60 + 0.5*40 = 48 + 20 = 68
        val score = calculator.scoreStroke(strokeResult(coverage = 0.8f, averageDistance = 4f))

        assertEquals(68f, score, DELTA)
        assertEquals(ScoreLevel.MEDIUM, calculator.scoreLevel(score))
    }

    @Test
    fun `distance beyond the tolerance is clamped instead of going negative`() {
        // accuracy clamps to 0 once distance exceeds tolerance; score = 1*60 + 0*40 = 60
        val score = calculator.scoreStroke(strokeResult(coverage = 1f, averageDistance = 100f))

        assertEquals(60f, score, DELTA)
        assertEquals(ScoreLevel.MEDIUM, calculator.scoreLevel(score))
    }

    @Test
    fun `trace score is the average of its strokes' scores`() {
        val traceResult = TraceResult(
            exerciseId = "exercise-1",
            strokeResults = listOf(
                strokeResult(coverage = 1f, averageDistance = 0f, id = "a"), // 100
                strokeResult(coverage = 0f, averageDistance = 8f, id = "b"), // 0
            ),
            isCompleted = true,
        )

        val score = calculator.scoreTrace(traceResult)

        assertEquals(50f, score, DELTA)
        assertEquals(ScoreLevel.LOW, calculator.scoreLevel(score))
    }

    @Test
    fun `trace score with no strokes is zero`() {
        val traceResult = TraceResult(exerciseId = "exercise-1", strokeResults = emptyList(), isCompleted = false)

        assertEquals(0f, calculator.scoreTrace(traceResult), DELTA)
    }

    @Test
    fun `custom weights change how coverage and accuracy contribute`() {
        val accuracyOnly = TraceScoreCalculator(tolerance = tolerance, weights = ScoreWeights(coverageWeight = 0f, accuracyWeight = 1f))

        // Coverage is ignored entirely; only the zero distance (accuracy = 1) matters.
        val score = accuracyOnly.scoreStroke(strokeResult(coverage = 0f, averageDistance = 0f))

        assertEquals(100f, score, DELTA)
    }

    @Test
    fun `custom thresholds are used for level classification`() {
        val strictCalculator = TraceScoreCalculator(
            tolerance = tolerance,
            thresholds = ScoreThresholds(mediumMinScore = 80f, perfectMinScore = 95f),
        )

        assertEquals(ScoreLevel.MEDIUM, strictCalculator.scoreLevel(85f))
    }

    companion object {
        private const val DELTA = 0.01f
    }
}
