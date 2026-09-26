package com.bornochitra.core.ui.components

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.bornochitra.core.ui.theme.BcBalooFontFamily
import com.bornochitra.core.ui.theme.BcBengaliLetterFontFamily
import com.bornochitra.core.ui.theme.BcLatinCapitalFontFamily
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class LetterFontFamilyTest {

    @Test
    fun `english small letters are drawn in the font their guides are derived from`() {
        assertEquals(BcLatinLetterFontFamily, "a".letterFontFamily())
        assertEquals(BcLatinLetterFontFamily, "z".letterFontFamily())
    }

    @Test
    fun `english capital letters are drawn in the font their guides are derived from`() {
        assertEquals(BcLatinCapitalFontFamily, "A".letterFontFamily())
        assertEquals(BcLatinCapitalFontFamily, "Z".letterFontFamily())
    }

    @Test
    fun `numbers and operators are drawn in the font their guides are derived from`() {
        assertEquals(BcLatinCapitalFontFamily, "1".letterFontFamily())
        assertEquals(BcLatinCapitalFontFamily, "20".letterFontFamily())
        listOf("+", "−", "×", "÷", "=").forEach { assertEquals(BcLatinCapitalFontFamily, it.letterFontFamily()) }
    }

    @Test
    fun `bengali numbers are drawn in the platform font their guides are derived from`() {
        assertEquals(BcBengaliLetterFontFamily, "১".letterFontFamily())
        assertEquals(BcBengaliLetterFontFamily, "৫".letterFontFamily())
        assertEquals(BcBengaliLetterFontFamily, "২০".letterFontFamily())
    }

    @Test
    fun `bengali letters are drawn in the platform font their guides are derived from`() {
        assertEquals(BcBengaliLetterFontFamily, "ক".letterFontFamily())
        assertEquals(BcBengaliLetterFontFamily, "ড়".letterFontFamily())
    }

    @Test
    fun `words and empty text are not learning characters`() {
        assertNull("Line".letterFontFamily())
        assertNull("রেখা".letterFontFamily())
        assertNull("".letterFontFamily())
    }

    @Test
    fun `a learning character keeps the style's size but takes its guide's font at bold`() {
        val base = TextStyle(fontFamily = BcBalooFontFamily, fontWeight = FontWeight.ExtraBold)
        val letter = letterStyle("A", base)
        assertEquals(BcLatinCapitalFontFamily, letter.fontFamily)
        assertEquals(FontWeight.Bold, letter.fontWeight)
        assertEquals(base.fontSize, letter.fontSize)
    }

    @Test
    fun `a word keeps the design font`() {
        val base = TextStyle(fontFamily = BcBalooFontFamily, fontWeight = FontWeight.Bold)
        assertSame(base, letterStyle("রেখা", base))
    }
}
