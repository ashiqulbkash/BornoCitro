package com.bornochitra.feature.fillblanks

import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.ExerciseType
import org.junit.Assert.assertEquals
import org.junit.Test

class SequenceCategoriesTest {

    @Test
    fun `the Bangla picker lists the Bangla hub's categories and math`() {
        assertEquals(
            listOf(ExerciseType.VOWEL, ExerciseType.CONSONANT, ExerciseType.BANGLA_NUMBER, ExerciseType.MATH),
            sequenceCategories(AppLanguage.BANGLA),
        )
    }

    @Test
    fun `the English picker lists the English hub's categories and math`() {
        assertEquals(
            listOf(ExerciseType.ENGLISH_SMALL, ExerciseType.ENGLISH_CAPITAL, ExerciseType.MATH),
            sequenceCategories(AppLanguage.ENGLISH),
        )
    }

    @Test
    fun `together the pickers offer every sequence category and never drawings`() {
        val all = AppLanguage.entries.flatMap(::sequenceCategories).toSet()

        assertEquals(ExerciseType.entries.toSet() - ExerciseType.DRAWING, all)
    }
}
