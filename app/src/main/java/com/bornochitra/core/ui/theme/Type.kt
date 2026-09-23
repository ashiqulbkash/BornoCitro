package com.bornochitra.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bornochitra.R

/**
 * Andika, a school print font, for English small letters: its single-storey a, g and y are the shapes
 * children are taught, and the English guides are derived from this exact face (see
 * `EnglishSmallExercises.kt`). Only the bold weight the letter styles use is bundled.
 */
val BcLatinLetterFontFamily = FontFamily(Font(R.font.andika_bold, FontWeight.Bold))

/**
 * Inter, for English capital letters and math, whose guides are derived from this exact face (see
 * `EnglishCapitalExercises.kt` and `MathExercises.kt`). `inter.ttf` is variable, so both of its axes are pinned to the
 * instance the guides were read off — Bold at the default optical size — rather than left to the
 * platform. Only that weight is declared, so every letter style draws the same glyph.
 */
@OptIn(ExperimentalTextApi::class)
val BcLatinCapitalFontFamily = FontFamily(
    Font(
        R.font.inter,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight),
            FontVariation.Setting("opsz", 14f),
        ),
    ),
)

// Sized up from the M3 defaults for readability by young children and to give
// Bengali characters (displayLarge) enough room to render clearly.
val BcTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 96.sp,
        lineHeight = 104.sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 72.sp,
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)
