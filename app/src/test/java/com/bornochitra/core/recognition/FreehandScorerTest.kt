package com.bornochitra.core.recognition

import com.bornochitra.core.content.ExerciseCatalog
import com.bornochitra.core.model.Point
import com.bornochitra.core.tracing.TracePoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FreehandScorerTest {

    private val ko = ExerciseCatalog.all.single { it.id == "consonant-ko" }

    @Test
    fun `writing on the guide scores as highly as tracing it`() {
        val onGuide = freehandInk(ko, scale = 1f, dx = 0f, dy = 0f, wobble = 0f, reversed = false)
        assertTrue(FreehandScorer.score(onGuide, ko) >= 95f)
    }

    @Test
    fun `size and place do not change the score`() {
        val onGuide = FreehandScorer.score(freehandInk(ko, scale = 1f, dx = 0f, dy = 0f, wobble = 0f), ko)
        val smallAndMoved = FreehandScorer.score(freehandInk(ko, scale = 0.5f, dx = 30f, dy = 5f, wobble = 0f), ko)
        assertEquals(onGuide, smallAndMoved, 2f)
    }

    @Test
    fun `a sloppier letter scores lower`() {
        val neat = FreehandScorer.score(freehandInk(ko, wobble = 0f), ko)
        val wobbly = FreehandScorer.score(freehandInk(ko, wobble = 6f), ko)
        assertTrue("neat $neat, wobbly $wobbly", wobbly < neat)
    }

    @Test
    fun `ink is fitted onto the guide's box keeping its proportions`() {
        val ink = listOf(listOf(TracePoint(0f, 0f, 0L), TracePoint(10f, 5f, 1L)))
        val guide = listOf(Point(20f, 20f), Point(80f, 50f))
        val fitted = FreehandScorer.fitOnto(ink, guide).single()
        assertEquals(20f, fitted[0].x, 0.01f)
        assertEquals(20f, fitted[0].y, 0.01f)
        assertEquals(80f, fitted[1].x, 0.01f)
        assertEquals(50f, fitted[1].y, 0.01f)
    }
}
