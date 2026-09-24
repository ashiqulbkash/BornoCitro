package com.bornochitra.core.recognition

import com.bornochitra.core.content.ExerciseCatalog
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.tracing.TracePoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShapeMatcherTest {

    private val consonants = ExerciseCatalog.all.filter { it.type == ExerciseType.CONSONANT }
    private val small = ExerciseCatalog.all.filter { it.type == ExerciseType.ENGLISH_SMALL }
    private val ko = consonants.single { it.title == "ক" }
    private val kho = consonants.single { it.title == "খ" }

    private fun List<List<TracePoint>>.asPoints() = map { stroke -> stroke.map { Point(it.x, it.y) } }

    @Test
    fun `a letter written smaller, elsewhere and in another stroke order is recognised`() {
        assertTrue(ShapeMatcher.isRecognisedAs(freehandInk(ko).asPoints(), ko, consonants))
    }

    @Test
    fun `a letter is not recognised as a different one`() {
        assertFalse(ShapeMatcher.isRecognisedAs(freehandInk(ko).asPoints(), kho, consonants))
    }

    @Test
    fun `every small letter written freehand is recognised as itself`() {
        small.forEach { letter ->
            assertTrue(letter.title, ShapeMatcher.isRecognisedAs(freehandInk(letter).asPoints(), letter, small))
        }
    }

    @Test
    fun `a straight scribble is not read as a curly letter`() {
        val scribble = listOf(listOf(Point(10f, 10f), Point(90f, 90f)))
        assertFalse(ShapeMatcher.isRecognisedAs(scribble, ko, consonants))
    }

    @Test
    fun `ink with no length is never recognised`() {
        assertFalse(ShapeMatcher.isRecognisedAs(emptyList(), ko, consonants))
        assertFalse(ShapeMatcher.isRecognisedAs(listOf(listOf(Point(5f, 5f))), ko, consonants))
    }

    @Test
    fun `distance ignores size, place and stroke direction`() {
        val line = listOf(listOf(Point(0f, 0f), Point(100f, 0f)))
        val movedAndReversed = listOf(listOf(Point(60f, 40f), Point(10f, 40f)))
        assertEquals(0f, ShapeMatcher.distance(line, movedAndReversed), 0.01f)
    }
}
