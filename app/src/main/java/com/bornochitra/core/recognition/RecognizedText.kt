package com.bornochitra.core.recognition

import java.text.Normalizer

/**
 * Decides whether text a handwriting recognizer read is the expected item. A single letter written
 * alone in a box has no line to size it against, so letters whose small and capital forms have the
 * same shape, and the digits that share a shape with a letter, count as each other.
 */
object RecognizedText {

    /** Each group's characters look the same when written on their own. */
    private val lookAlikeGroups = listOf("cC", "oO0", "sS", "uU", "vV", "wW", "xX", "zZ", "lI1|")

    /** The recognizer shows a combining mark on a dotted circle; the catalog's titles do not. */
    private const val DOTTED_CIRCLE = '◌'

    fun matches(recognized: String, expected: String): Boolean {
        val read = normalize(recognized)
        val wanted = normalize(expected)
        if (read == wanted) return true
        return read.length == wanted.length && read.indices.all { isLookAlike(read[it], wanted[it]) }
    }

    private fun normalize(text: String): String =
        Normalizer.normalize(text, Normalizer.Form.NFC).filterNot { it.isWhitespace() || it == DOTTED_CIRCLE }

    private fun isLookAlike(first: Char, second: Char): Boolean =
        first == second || lookAlikeGroups.any { first in it && second in it }
}
