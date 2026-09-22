package com.bornochitra.core.ui.components

import com.bornochitra.core.ui.theme.BcLatinCapitalFontFamily
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun `bengali letters and drawing names keep the theme font`() {
        assertNull("ক".letterFontFamily())
        assertNull("ড়".letterFontFamily())
        assertNull("Line".letterFontFamily())
        assertNull("".letterFontFamily())
    }
}
