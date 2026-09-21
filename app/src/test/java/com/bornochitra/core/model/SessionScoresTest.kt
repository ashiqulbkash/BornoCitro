package com.bornochitra.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SessionScoresTest {

    @Test
    fun `scores survive an encode and decode round trip`() {
        val scores = listOf(80f, 92.5f, 61f)

        assertEquals(scores, SessionScores.decode(SessionScores.encode(scores)))
    }

    @Test
    fun `no scores encode to an empty argument and decode back to nothing`() {
        assertEquals("", SessionScores.encode(emptyList()))
        assertEquals(emptyList<Float>(), SessionScores.decode(""))
        assertEquals(emptyList<Float>(), SessionScores.decode(null))
    }

    @Test
    fun `an unreadable entry is ignored rather than failing the screen`() {
        assertEquals(listOf(70f, 90f), SessionScores.decode("70,oops,90"))
    }

    @Test
    fun `average percent is the rounded mean of every try`() {
        assertEquals(80, SessionScores.averagePercent(listOf(70f, 90f)))
        assertEquals(76, SessionScores.averagePercent(listOf(75f, 76f, 77.6f)))
    }

    @Test
    fun `average percent is zero when nothing was tried`() {
        assertEquals(0, SessionScores.averagePercent(emptyList()))
    }
}
