package com.bornochitra.core.recognition

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecognizedTextTest {

    @Test
    fun `the same text matches`() {
        assertTrue(RecognizedText.matches("ক", "ক"))
        assertTrue(RecognizedText.matches(" 12 ", "12"))
    }

    @Test
    fun `a different letter does not match`() {
        assertFalse(RecognizedText.matches("খ", "ক"))
        assertFalse(RecognizedText.matches("b", "d"))
    }

    @Test
    fun `composed and decomposed forms match`() {
        assertTrue(RecognizedText.matches("ড়", "ড়"))
    }

    @Test
    fun `a mark shown on a dotted circle matches the mark`() {
        assertTrue(RecognizedText.matches("◌ং", "ং"))
    }

    @Test
    fun `letters whose two cases look the same match either case`() {
        assertTrue(RecognizedText.matches("C", "c"))
        assertTrue(RecognizedText.matches("o", "O"))
    }

    @Test
    fun `letters whose two cases look different do not`() {
        assertFalse(RecognizedText.matches("B", "b"))
        assertFalse(RecognizedText.matches("a", "A"))
    }

    @Test
    fun `digits read as the letters they look like still match`() {
        assertTrue(RecognizedText.matches("1O", "10"))
        assertTrue(RecognizedText.matches("l", "1"))
        assertFalse(RecognizedText.matches("17", "10"))
    }
}
