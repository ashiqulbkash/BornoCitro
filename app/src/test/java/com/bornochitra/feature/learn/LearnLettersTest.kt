package com.bornochitra.feature.learn

import com.bornochitra.core.content.consonantExercises
import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.locale.AppLanguage
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
    fun `the English explanation names the letter and its word`() {
        assertEquals("A for apple", englishLearnLetters.first().explanation(AppLanguage.ENGLISH))
        assertEquals("B for ball", englishLearnLetters[1].explanation(AppLanguage.ENGLISH))
    }

    @Test
    fun `the Bangla table has every vowel and consonant, in the order they are traced`() {
        val traced = (vowelExercises + consonantExercises).map { it.title }

        assertEquals(11 + 39, banglaLearnLetters.size)
        assertEquals(traced, banglaLearnLetters.map { it.letter })
    }

    // Not "starts with": no Bangla word starts with ঙ, ং or the other marks, so those are taught
    // with a word they end in.
    @Test
    fun `every Bangla word is there and has its letter in it`() {
        banglaLearnLetters.forEach { (letter, word) ->
            assertTrue("$letter has no word", word.isNotBlank())
            assertTrue("$letter's word is \"$word\"", word.contains(letter))
        }
    }

    @Test
    fun `the Bangla explanation names the letter and its word`() {
        assertEquals("অ তে অজগর", banglaLearnLetters.first().explanation(AppLanguage.BANGLA))
        assertEquals("খ তে খরগোশ", banglaLearnLetters.first { it.letter == "খ" }.explanation(AppLanguage.BANGLA))
    }

    @Test
    fun `the vowels a lone letter cannot voice are spoken by their primer names`() {
        val spoken = banglaLearnLetters.take(11).associate { it.letter to it.spoken }

        assertEquals(
            mapOf(
                "অ" to "স্বরে অ", "আ" to "স্বরে আ", "ই" to "হ্রস্ব ই", "ঈ" to "দীর্ঘ ঈ", "উ" to "হ্রস্ব উ", "ঊ" to "দীর্ঘ ঊ",
                "ঋ" to "ঋ", "এ" to "স্বরে এ", "ঐ" to "ঐ", "ও" to "স্বরে ও", "ঔ" to "ঔ",
            ),
            spoken,
        )
    }

    @Test
    fun `the consonants a lone letter cannot voice or tell apart are spoken by their primer names`() {
        val named = banglaLearnLetters.drop(11).filter { it.spoken != it.letter }.associate { it.letter to it.spoken }

        assertEquals(
            mapOf(
                "জ" to "বর্গীয় জ", "ণ" to "মূর্ধন্য ণ", "ন" to "দন্ত্য ন", "য" to "অন্তঃস্থ য",
                "শ" to "তালব্য শ", "ষ" to "মূর্ধন্য ষ", "স" to "দন্ত্য স",
                "ড়" to "ড-এ শূন্য ড়", "ঢ়" to "ঢ-এ শূন্য ঢ়", "য়" to "য-এ শূন্য য়",
                "ৎ" to "খণ্ড ত",
            ),
            named,
        )
    }

    @Test
    fun `every spoken letter still names its letter, and English letters are spoken as written`() {
        // খণ্ড ত is the one name that does not end in its letter: ৎ is the khanda form of ত.
        banglaLearnLetters.filter { it.letter != "ৎ" }.forEach {
            assertTrue("${it.letter} is spoken as \"${it.spoken}\"", it.spoken.endsWith(it.letter))
        }
        englishLearnLetters.forEach { assertEquals(it.letter, it.spoken) }
    }

    @Test
    fun `each language has its own table`() {
        assertEquals(banglaLearnLetters, learnLetters(AppLanguage.BANGLA))
        assertEquals(englishLearnLetters, learnLetters(AppLanguage.ENGLISH))
    }
}
