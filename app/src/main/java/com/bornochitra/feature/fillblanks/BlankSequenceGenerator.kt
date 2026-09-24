package com.bornochitra.feature.fillblanks

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import kotlin.random.Random

/** A run of consecutive items from one category, with the items at [blankIndices] left for the child to fill in. */
data class BlankSequence(
    val items: List<Exercise>,
    val blankIndices: List<Int>,
)

/**
 * Builds fill-in-the-blanks sequences (plan.md Step 6): a window of [MIN_WINDOW]..[MAX_WINDOW]
 * consecutive items, in catalog order, with [MIN_BLANKS]..[MAX_BLANKS] of them blanked. The first
 * item is never blank, so the child always has a place to start from. Blanks sit next to each other
 * only at [Difficulty.ADVANCED]; below it they are kept apart, so every blank has a shown item on
 * each side (or the end of the window after it), and a window too short for the drawn number of
 * separate blanks gets as many as fit.
 *
 * Pure and deterministic: the same items, difficulty and seed always give the same sequence.
 */
object BlankSequenceGenerator {

    const val MIN_WINDOW = 5
    const val MAX_WINDOW = 8
    const val MIN_BLANKS = 1
    const val MAX_BLANKS = 3

    fun generate(items: List<Exercise>, difficulty: Difficulty, seed: Long): BlankSequence {
        require(items.size >= MIN_WINDOW) { "a sequence needs at least $MIN_WINDOW items" }
        val random = Random(seed)
        val ordered = items.sortedBy { it.order }

        val windowSize = random.nextInt(MIN_WINDOW, minOf(MAX_WINDOW, ordered.size) + 1)
        val start = random.nextInt(0, ordered.size - windowSize + 1)
        val window = ordered.subList(start, start + windowSize)

        val blankCount = random.nextInt(MIN_BLANKS, MAX_BLANKS + 1)
        // Every position but the first may be blank.
        val candidateCount = windowSize - 1
        val blanks = if (difficulty == Difficulty.ADVANCED) {
            (0 until candidateCount).shuffled(random).take(blankCount).sorted()
        } else {
            nonAdjacentPositions(candidateCount, blankCount, random)
        }
        return BlankSequence(items = window, blankIndices = blanks.map { it + 1 })
    }

    /**
     * Up to [count] positions in 0 until [size], no two next to each other, drawn uniformly. Choosing k
     * such positions is the same as choosing any k of size - k + 1 slots and then spreading them out by
     * adding i to the i-th, which leaves a gap of at least one between neighbours.
     */
    private fun nonAdjacentPositions(size: Int, count: Int, random: Random): List<Int> {
        val fitting = minOf(count, (size + 1) / 2)
        return (0 until size - fitting + 1).shuffled(random).take(fitting).sorted()
            .mapIndexed { index, slot -> slot + index }
    }
}
