package com.bornochitra.core.ui.components

import com.bornochitra.core.model.ExerciseType

/** The category's name as the child sees it on its buttons, headings and progress bars. */
internal fun ExerciseType.label(): String = when (this) {
    ExerciseType.VOWEL -> "স্বরবর্ণ"
    ExerciseType.CONSONANT -> "ব্যঞ্জনবর্ণ"
    ExerciseType.ENGLISH_SMALL -> "ছোট হাতের অক্ষর"
    ExerciseType.ENGLISH_CAPITAL -> "বড় হাতের অক্ষর"
    ExerciseType.MATH -> "সংখ্যা ও চিহ্ন"
    ExerciseType.DRAWING -> "আঁকা"
}
