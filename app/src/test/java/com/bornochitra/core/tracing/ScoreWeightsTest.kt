package com.bornochitra.core.tracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ScoreWeightsTest {

    @Test
    fun `defaults cover all four metrics and sum to one`() {
        val weights = ScoreWeights()

        assertEquals(0.45f, weights.coverageWeight, DELTA)
        assertEquals(0.30f, weights.accuracyWeight, DELTA)
        assertEquals(0.15f, weights.completionWeight, DELTA)
        assertEquals(0.10f, weights.orderWeight, DELTA)
        assertEquals(
            1f,
            weights.coverageWeight + weights.accuracyWeight + weights.completionWeight + weights.orderWeight,
            DELTA,
        )
    }

    @Test
    fun `a single stroke still splits coverage and accuracy sixty-forty`() {
        val weights = ScoreWeights()

        assertEquals(0.6f, weights.coverageShareOfStroke, DELTA)
        assertEquals(0.4f, weights.accuracyShareOfStroke, DELTA)
    }

    @Test
    fun `weights that do not sum to one are rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScoreWeights(coverageWeight = 0.5f, accuracyWeight = 0.6f)
        }
    }

    @Test
    fun `weight outside zero to one is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScoreWeights(coverageWeight = 1.5f, accuracyWeight = -0.5f)
        }
    }

    @Test
    fun `an accuracy-only split is a valid configuration`() {
        val weights = ScoreWeights(
            coverageWeight = 0f,
            accuracyWeight = 1f,
            completionWeight = 0f,
            orderWeight = 0f,
        )

        assertEquals(0f, weights.coverageShareOfStroke, DELTA)
        assertEquals(1f, weights.accuracyShareOfStroke, DELTA)
    }

    @Test
    fun `completion and order can be weighted more heavily than the traced path`() {
        val weights = ScoreWeights(
            coverageWeight = 0.2f,
            accuracyWeight = 0.2f,
            completionWeight = 0.4f,
            orderWeight = 0.2f,
        )

        assertEquals(0.4f, weights.completionWeight, DELTA)
        assertEquals(0.5f, weights.coverageShareOfStroke, DELTA)
    }

    companion object {
        private const val DELTA = 0.0001f
    }
}
