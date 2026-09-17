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
}
