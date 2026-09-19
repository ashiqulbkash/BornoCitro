package com.bornochitra.core.tracing

/**
 * How much each stroke of a whole-letter attempt contributes to the exercise's coverage and
 * accuracy — the model plan.md section 38 asks to be chosen deliberately rather than inherited.
 *
 * [Equal] (model A) is the default: every stroke of a letter is equally worth learning, so none
 * counts for more than another, and a single-stroke exercise is simply the one-stroke case of it.
 * A weighted model (B) is a configuration rather than a code change — pass another implementation,
 * for example `StrokeWeighting { index, _ -> if (index == 0) 2f else 1f }`.
 *
 * Weights are relative: they are normalized by their total, so they need not sum to 1.
 */
fun interface StrokeWeighting {

    fun weightOf(strokeIndex: Int, strokeCount: Int): Float

    companion object {
        val Equal = StrokeWeighting { _, _ -> 1f }
    }
}
