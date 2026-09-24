package com.bornochitra.feature.fillblanks

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Stroke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BlankSequenceGeneratorTest {

    private fun items(count: Int) = (1..count).map { order ->
        Exercise(
            id = "item-$order",
            title = "$order",
            type = ExerciseType.MATH,
            difficulty = Difficulty.BEGINNER,
            strokes = listOf(Stroke(id = "item-$order-stroke", points = emptyList())),
            order = order,
        )
    }

    private val seeds = 0L until 500L

    @Test
    fun `the same seed gives the same sequence`() {
        val catalog = items(20)
        seeds.forEach { seed ->
            assertEquals(
                BlankSequenceGenerator.generate(catalog, Difficulty.BEGINNER, seed),
                BlankSequenceGenerator.generate(catalog, Difficulty.BEGINNER, seed),
            )
        }
    }

    @Test
    fun `different seeds give different sequences`() {
        val catalog = items(20)
        val sequences = seeds.map { BlankSequenceGenerator.generate(catalog, Difficulty.ADVANCED, it) }.toSet()
        assertTrue(sequences.size > seeds.count() / 2)
    }

    @Test
    fun `a window is 5 to 8 consecutive items in catalog order`() {
        val catalog = items(20).shuffled(kotlin.random.Random(7))
        seeds.forEach { seed ->
            val window = BlankSequenceGenerator.generate(catalog, Difficulty.BEGINNER, seed).items
            assertTrue(window.size in 5..8)
            val orders = window.map { it.order }
            assertEquals((orders.first()..orders.last()).toList(), orders)
        }
    }

    @Test
    fun `every window size from 5 to 8 occurs`() {
        val sizes = seeds.map { BlankSequenceGenerator.generate(items(20), Difficulty.BEGINNER, it).items.size }.toSet()
        assertEquals(setOf(5, 6, 7, 8), sizes)
    }

    @Test
    fun `there are 1 to 3 blanks, never the first item, in increasing order`() {
        Difficulty.entries.forEach { difficulty ->
            seeds.forEach { seed ->
                val sequence = BlankSequenceGenerator.generate(items(20), difficulty, seed)
                val blanks = sequence.blankIndices
                assertTrue(blanks.size in 1..3)
                assertTrue(blanks.all { it in 1 until sequence.items.size })
                assertEquals(blanks.sorted().distinct(), blanks)
            }
        }
    }

    @Test
    fun `below advanced no two blanks are adjacent`() {
        listOf(Difficulty.BEGINNER, Difficulty.INTERMEDIATE).forEach { difficulty ->
            seeds.forEach { seed ->
                val blanks = BlankSequenceGenerator.generate(items(20), difficulty, seed).blankIndices
                assertTrue(blanks.zipWithNext().all { (a, b) -> b - a >= 2 })
            }
        }
    }

    @Test
    fun `below advanced three separate blanks still occur where the window fits them`() {
        val counts = seeds.map { BlankSequenceGenerator.generate(items(20), Difficulty.BEGINNER, it).blankIndices.size }
        assertEquals(setOf(1, 2, 3), counts.toSet())
    }

    @Test
    fun `advanced sometimes places blanks side by side`() {
        val anyAdjacent = seeds.any { seed ->
            BlankSequenceGenerator.generate(items(20), Difficulty.ADVANCED, seed).blankIndices
                .zipWithNext().any { (a, b) -> b - a == 1 }
        }
        assertTrue(anyAdjacent)
    }

    @Test
    fun `windows reach both ends of the category`() {
        val catalog = items(11)
        val windows = seeds.map { BlankSequenceGenerator.generate(catalog, Difficulty.BEGINNER, it).items }
        assertTrue(windows.any { it.first().order == 1 })
        assertTrue(windows.any { it.last().order == 11 })
        assertTrue(windows.all { it.first().order >= 1 && it.last().order <= 11 })
    }

    @Test
    fun `a category of exactly five items gives the whole category`() {
        val catalog = items(5)
        seeds.forEach { seed ->
            assertEquals(catalog, BlankSequenceGenerator.generate(catalog, Difficulty.BEGINNER, seed).items)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `fewer than five items is rejected`() {
        BlankSequenceGenerator.generate(items(4), Difficulty.BEGINNER, seed = 1L)
    }
}
