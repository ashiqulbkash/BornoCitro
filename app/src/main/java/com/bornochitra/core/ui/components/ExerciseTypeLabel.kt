package com.bornochitra.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType

/** The category's name as the child sees it on its buttons, headings and progress bars. */
@Composable
internal fun ExerciseType.label(): String = stringResource(labelRes())

@StringRes
private fun ExerciseType.labelRes(): Int = when (this) {
    ExerciseType.VOWEL -> R.string.category_vowel
    ExerciseType.CONSONANT -> R.string.category_consonant
    ExerciseType.ENGLISH_SMALL -> R.string.category_english_small
    ExerciseType.ENGLISH_CAPITAL -> R.string.category_english_capital
    ExerciseType.MATH -> R.string.category_math
    ExerciseType.BANGLA_NUMBER -> R.string.category_bangla_number
    ExerciseType.DRAWING -> R.string.category_drawing
}
