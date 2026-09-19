package com.bornochitra.core.content

import com.bornochitra.core.model.ExerciseType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseCatalogTest {

    @Test
    fun `every exercise id is unique`() {
        val ids = ExerciseCatalog.all.map { it.id }

        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every exercise has at least one stroke and every stroke has at least two points`() {
        ExerciseCatalog.all.forEach { exercise ->
            assertTrue("${exercise.id} has no strokes", exercise.strokes.isNotEmpty())
            exercise.strokes.forEach { stroke ->
                assertTrue("${exercise.id} stroke ${stroke.id} has fewer than two points", stroke.points.size >= 2)
            }
        }
    }

    @Test
    fun `order is sequential starting at 1 within each exercise type`() {
        ExerciseType.entries.forEach { type ->
            val orders = ExerciseCatalog.all.filter { it.type == type }.map { it.order }.sorted()

            assertEquals((1..orders.size).toList(), orders)
        }
    }

    @Test
    fun `known placeholder ids used elsewhere in the app resolve to real exercises`() {
        val ids = ExerciseCatalog.all.map { it.id }.toSet()

        assertTrue(ids.contains("vowel-o"))
        assertTrue(ids.contains("consonant-ko"))
        assertTrue(ids.contains("drawing-circle"))
    }

    @Test
    fun `vowels and consonants carry the right characters in alphabet order`() {
        fun titles(type: ExerciseType) =
            ExerciseCatalog.all.filter { it.type == type }.sortedBy { it.order }.joinToString("") { it.title }

        assertEquals("অআইঈউঊঋএঐওঔ", titles(ExerciseType.VOWEL))
        assertEquals("কখগঘঙচছজঝঞটঠডঢণত", titles(ExerciseType.CONSONANT))
    }

    @Test
    fun `every exercise has the stroke count of its real letter or shape`() {
        val expected = mapOf(
            "vowel-o" to 3, "vowel-aa" to 4, "vowel-i" to 3, "vowel-ii" to 4, "vowel-u" to 4,
            "vowel-uu" to 5, "vowel-ri" to 6, "vowel-e" to 3, "vowel-oi" to 4, "vowel-oa" to 2,
            "vowel-au" to 3,
            "consonant-ko" to 4, "consonant-kho" to 4, "consonant-go" to 3, "consonant-gho" to 4,
            "consonant-ngo" to 2, "consonant-cho" to 3, "consonant-chho" to 4,
            "consonant-jo" to 4, "consonant-jho" to 6, "consonant-nio" to 5,
            "consonant-tto" to 4,
            "consonant-ttho" to 3,
            "consonant-ddo" to 3,
            "consonant-ddho" to 3,
            "consonant-nno" to 3,
            "consonant-to" to 3,
            "drawing-line" to 1, "drawing-circle" to 1, "drawing-square" to 1, "drawing-triangle" to 1,
            "drawing-house" to 2,
        )

        assertEquals(expected, ExerciseCatalog.all.associate { it.id to it.strokes.size })
    }

    @Test
    fun `stroke ids are unique and belong to their exercise`() {
        ExerciseCatalog.all.forEach { exercise ->
            val ids = exercise.strokes.map { it.id }

            assertEquals("${exercise.id} repeats a stroke id", ids.size, ids.toSet().size)
            assertTrue("${exercise.id} has a stroke id from another exercise", ids.all { it.startsWith("${exercise.id}-") })
        }
    }

    @Test
    fun `the matra is always written last, as Bengali is`() {
        ExerciseCatalog.all.forEach { exercise ->
            val matraIndex = exercise.strokes.indexOfFirst { it.id.endsWith("-matra") }

            if (matraIndex != -1) assertEquals("${exercise.id}'s matra is not last", exercise.strokes.lastIndex, matraIndex)
        }
    }

    @Test
    fun `every guide stays inside the canvas and is centred on it`() {
        ExerciseCatalog.all.forEach { exercise ->
            val points = exercise.strokes.flatMap { it.points }
            val xs = points.map { it.x }
            val ys = points.map { it.y }

            assertTrue("${exercise.id} leaves the 0..100 canvas", xs.all { it in 0f..100f } && ys.all { it in 0f..100f })
            assertEquals("${exercise.id} is off-centre horizontally", 50f, (xs.min() + xs.max()) / 2, MAX_CENTRE_OFFSET)
            assertEquals("${exercise.id} is off-centre vertically", 50f, (ys.min() + ys.max()) / 2, MAX_CENTRE_OFFSET)
        }
    }

    private companion object {
        const val MAX_CENTRE_OFFSET = 3f
    }
}
