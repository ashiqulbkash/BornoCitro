package com.bornochitra.core.tracing

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class TracingToleranceTest {

    @Test
    fun `distance under the max is within tolerance`() {
        val tolerance = TracingTolerance(maxDistance = 8f)

        assertTrue(tolerance.isWithinTolerance(3f))
    }

    @Test
    fun `distance exactly at the max is within tolerance`() {
        val tolerance = TracingTolerance(maxDistance = 8f)

        assertTrue(tolerance.isWithinTolerance(8f))
    }

    @Test
    fun `distance beyond the max is not within tolerance`() {
        val tolerance = TracingTolerance(maxDistance = 8f)

        assertFalse(tolerance.isWithinTolerance(8.01f))
    }

    @Test
    fun `non-positive max distance is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            TracingTolerance(maxDistance = 0f)
        }
    }
}
