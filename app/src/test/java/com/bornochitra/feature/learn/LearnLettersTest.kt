package com.bornochitra.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LearnLettersTest {

    @Test
    fun `the English table has every letter from A to Z, in order`() {
        assertEquals(('A'..'Z').map { it.toString() }, englishLearnLetters.map { it.letter })
    }

    @Test
    fun `every English word is there and starts with its letter`() {
        englishLearnLetters.forEach { (letter, word) ->
            assertTrue("$letter has no word", word.isNotBlank())
            assertTrue("$letter's word is \"$word\"", word.startsWith(letter, ignoreCase = true))
        }
    }

    @Test
    fun `the explanation names the letter and its word`() {
        assertEquals("A for apple", englishLearnLetters.first().explanation)
        assertEquals("B for ball", englishLearnLetters[1].explanation)
    }
}
