package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TraceScoreCalculatorTest {

    private val tolerance = TracingTolerance(maxDistance = 8f)
    private val calculator = TraceScoreCalculator(tolerance = tolerance)

    private fun strokeResult(
        coverage: Float,
        averageDistance: Float,
        id: String = "s",
        isCompleted: Boolean = true,
        attemptCount: Int = 1,
        outOfOrderAttempts: Int = 0,
    ) = StrokeTraceResult(
        strokeId = id,
        tracePoints = emptyList(),
        coverage = coverage,
        averageDistance = averageDistance,
        isCompleted = isCompleted,
        attemptCount = attemptCount,
        outOfOrderAttempts = outOfOrderAttempts,
    )

    private fun traceResult(
        strokeResults: List<StrokeTraceResult>,
        expectedStrokeCount: Int = strokeResults.size,
        isCompleted: Boolean = true,
    ) = TraceResult(
        exerciseId = "exercise-1",
        strokeResults = strokeResults,
        isCompleted = isCompleted,
        expectedStrokeCount = expectedStrokeCount,
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
    fun `a clean whole-letter attempt scores 100 and is PERFECT`() {
        val score = calculator.scoreTrace(
            traceResult(
                listOf(
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "b"),
                ),
            ),
        )

        assertEquals(100f, score, DELTA)
        assertEquals(ScoreLevel.PERFECT, calculator.scoreLevel(score))
    }

    @Test
    fun `a single-stroke exercise scores from its one stroke`() {
        val score = calculator.scoreTrace(
            traceResult(listOf(strokeResult(coverage = 0.8f, averageDistance = 4f))),
        )

        // coverage 0.8*45 + accuracy 0.5*30 + completion 1*15 + order 1*10 = 36 + 15 + 25 = 76
        assertEquals(76f, score, DELTA)
        assertEquals(ScoreLevel.MEDIUM, calculator.scoreLevel(score))
    }

    @Test
    fun `a whole attempt blends per-stroke metrics with completion and order`() {
        val score = calculator.scoreTrace(
            traceResult(
                listOf(
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
                    strokeResult(coverage = 0f, averageDistance = 8f, id = "b"),
                ),
            ),
        )

        // coverage 0.5*45 + accuracy 0.5*30 + completion 1*15 + order 1*10 = 22.5 + 15 + 25 = 62.5
        assertEquals(62.5f, score, DELTA)
        assertEquals(ScoreLevel.MEDIUM, calculator.scoreLevel(score))
    }

    @Test
    fun `a stroke that was never traced counts as missing, not as absent from the average`() {
        val score = calculator.scoreTrace(
            traceResult(
                strokeResults = listOf(strokeResult(coverage = 1f, averageDistance = 0f, id = "a")),
                expectedStrokeCount = 2,
                isCompleted = false,
            ),
        )

        // coverage 0.5*45 + accuracy 0.5*30 + completion 0.5*15 + order 1*10 = 22.5 + 7.5 + 10 = 55
        assertEquals(55f, score, DELTA)
        assertEquals(ScoreLevel.LOW, calculator.scoreLevel(score))
    }

    @Test
    fun `retrying a stroke does not lower the score`() {
        val retried = calculator.scoreTrace(
            traceResult(
                listOf(
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "b", attemptCount = 4),
                ),
            ),
        )

        assertEquals(100f, retried, DELTA)
    }

    @Test
    fun `tracing the wrong stroke lowers the score through the order metric`() {
        val outOfOrder = calculator.scoreTrace(
            traceResult(
                listOf(
                    strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
                    strokeResult(
                        coverage = 1f,
                        averageDistance = 0f,
                        id = "b",
                        attemptCount = 3,
                        outOfOrderAttempts = 1,
                    ),
                ),
            ),
        )

        // order = 1 - 1/4 = 0.75; score = 45 + 30 + 15 + 0.75*10 = 97.5
        assertEquals(97.5f, outOfOrder, DELTA)
        assertTrue(outOfOrder < 100f)
    }

    @Test
    fun `trace score with no strokes is zero`() {
        assertEquals(0f, calculator.scoreTrace(traceResult(emptyList(), isCompleted = false)), DELTA)
    }

    @Test
    fun `equal stroke weighting is the default so no stroke counts for more`() {
        val strokes = listOf(
            strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
            strokeResult(coverage = 0f, averageDistance = 8f, id = "b"),
        )

        val forwards = calculator.scoreTrace(traceResult(strokes))
        val reversed = calculator.scoreTrace(traceResult(strokes.reversed()))

        assertEquals(forwards, reversed, DELTA)
    }

    @Test
    fun `a weighted stroke model is a configuration, not a code change`() {
        val firstStrokeCountsDouble = TraceScoreCalculator(
            tolerance = tolerance,
            strokeWeighting = { index, _ -> if (index == 0) 2f else 1f },
        )
        val strokes = listOf(
            strokeResult(coverage = 1f, averageDistance = 0f, id = "a"),
            strokeResult(coverage = 0f, averageDistance = 8f, id = "b"),
        )

        // coverage and accuracy average to 2/3 instead of 1/2: 0.667*45 + 0.667*30 + 15 + 10 = 75
        assertEquals(75f, firstStrokeCountsDouble.scoreTrace(traceResult(strokes)), DELTA)
    }

    @Test
    fun `custom weights change how coverage and accuracy contribute`() {
        val accuracyOnly = TraceScoreCalculator(
            tolerance = tolerance,
            weights = ScoreWeights(
                coverageWeight = 0f,
                accuracyWeight = 1f,
                completionWeight = 0f,
                orderWeight = 0f,
            ),
        )

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
