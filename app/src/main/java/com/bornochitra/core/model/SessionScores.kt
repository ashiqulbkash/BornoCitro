package com.bornochitra.core.model

import kotlin.math.roundToInt

/**
 * The scores of every completed try on one character in a practice session. A session lives only in
 * navigation, so it is carried between Practice and Result as a comma-separated argument.
 */
object SessionScores {
    const val ARG = "scores"

    fun encode(scores: List<Float>): String = scores.joinToString(",")

    fun decode(encoded: String?): List<Float> =
        encoded.orEmpty().split(',').mapNotNull { it.toFloatOrNull() }

    /** The rounded average percent, or 0 when nothing has been tried. */
    fun averagePercent(scores: List<Float>): Int =
        if (scores.isEmpty()) 0 else scores.average().roundToInt()
}
