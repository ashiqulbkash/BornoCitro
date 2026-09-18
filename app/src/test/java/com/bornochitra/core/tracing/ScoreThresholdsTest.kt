package com.bornochitra.core.tracing

import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ScoreThresholdsTest {

    private val defaults = ScoreThresholds()

    @Test
    fun `score just below the default medium boundary is LOW`() {
        assertEquals(ScoreLevel.LOW, defaults.classify(59f))
    }

    @Test
    fun `score at the default medium boundary is MEDIUM`() {
        assertEquals(ScoreLevel.MEDIUM, defaults.classify(60f))
    }

    @Test
    fun `score just below the default perfect boundary is MEDIUM`() {
        assertEquals(ScoreLevel.MEDIUM, defaults.classify(89f))
    }

    @Test
    fun `score at the default perfect boundary is PERFECT`() {
        assertEquals(ScoreLevel.PERFECT, defaults.classify(90f))
    }

    @Test
    fun `zero and one hundred are classified at the extremes`() {
        assertEquals(ScoreLevel.LOW, defaults.classify(0f))
        assertEquals(ScoreLevel.PERFECT, defaults.classify(100f))
    }

    @Test
    fun `custom thresholds are honored`() {
        val lenient = ScoreThresholds(mediumMinScore = 40f, perfectMinScore = 70f)

        assertEquals(ScoreLevel.LOW, lenient.classify(39f))
        assertEquals(ScoreLevel.MEDIUM, lenient.classify(40f))
        assertEquals(ScoreLevel.PERFECT, lenient.classify(70f))
    }

    @Test
    fun `medium threshold above perfect threshold is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScoreThresholds(mediumMinScore = 95f, perfectMinScore = 90f)
        }
    }

    @Test
    fun `out of range threshold is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            ScoreThresholds(mediumMinScore = -1f)
        }
        assertThrows(IllegalArgumentException::class.java) {
            ScoreThresholds(perfectMinScore = 101f)
        }
    }
}
