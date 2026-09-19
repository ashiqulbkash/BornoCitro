package com.bornochitra.core.tracing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StartMarkerTest {

    @Test
    fun `the ring starts just outside the dot and fully visible`() {
        val start = pulseFrame(0f)

        assertEquals(2f, start.radiusFactor)
        assertTrue("ring should be visible at the start", start.alpha > 0.5f)
    }

    @Test
    fun `the ring has grown and faded away by the end of the pulse`() {
        val end = pulseFrame(1f)

        assertEquals(5f, end.radiusFactor)
        assertEquals(0f, end.alpha)
    }

    @Test
    fun `the ring only ever grows and fades as the pulse advances`() {
        val frames = (0..20).map { pulseFrame(it / 20f) }

        frames.zipWithNext { earlier, later ->
            assertTrue(later.radiusFactor >= earlier.radiusFactor)
            assertTrue(later.alpha <= earlier.alpha)
        }
    }

    @Test
    fun `progress outside 0 to 1 is held at the ends`() {
        assertEquals(pulseFrame(0f), pulseFrame(-0.5f))
        assertEquals(pulseFrame(1f), pulseFrame(1.5f))
    }

    @Test
    fun `the ring never overlaps the solid start dot`() {
        // The dot is drawn at 1.8x the guide dot radius; the ring must begin outside it.
        assertTrue(pulseFrame(0f).radiusFactor > 1.8f)
    }
}
