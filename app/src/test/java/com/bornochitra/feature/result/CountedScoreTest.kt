package com.bornochitra.feature.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CountedScoreTest {

    @Test
    fun `the count-up starts at zero and ends on the real score`() {
        assertEquals(0, countedScore(target = 96, progress = 0f))
        assertEquals(96, countedScore(target = 96, progress = 1f))
    }

    @Test
    fun `part-way through it shows a proportional score`() {
        assertEquals(48, countedScore(target = 96, progress = 0.5f))
    }

    @Test
    fun `the number only ever counts upwards`() {
        val shown = (0..50).map { countedScore(target = 79, progress = it / 50f) }

        shown.zipWithNext { earlier, later -> assertTrue(later >= earlier) }
    }

    @Test
    fun `progress outside 0 to 1 never overshoots the real score`() {
        assertEquals(0, countedScore(target = 79, progress = -1f))
        assertEquals(79, countedScore(target = 79, progress = 2f))
    }

    @Test
    fun `a score of zero stays at zero`() {
        assertEquals(0, countedScore(target = 0, progress = 1f))
    }
}
