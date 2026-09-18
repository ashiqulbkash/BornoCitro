package com.bornochitra.core.tracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ScoreWeightsTest {

    @Test
    fun `defaults weight coverage more than accuracy and sum to one`() {
        val weights = ScoreWeights()

        assertEquals(1f, weights.coverageWeight + weights.accuracyWeight, 0.0001f)
        assertEquals(0.6f, weights.coverageWeight, 0.0001f)
        assertEquals(0.4f, weights.accuracyWeight, 0.0001f)
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
        val weights = ScoreWeights(coverageWeight = 0f, accuracyWeight = 1f)

        assertEquals(0f, weights.coverageWeight, 0.0001f)
        assertEquals(1f, weights.accuracyWeight, 0.0001f)
    }
}
