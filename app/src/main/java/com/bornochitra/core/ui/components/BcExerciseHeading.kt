package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcLatinCapitalFontFamily
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** A Bengali letter is one glyph; anything longer is a word, such as a drawing's name. */
private const val SINGLE_GLYPH_MAX_LENGTH = 2

internal fun String.isSingleGlyph(): Boolean = length <= SINGLE_GLYPH_MAX_LENGTH

/** The math operators, whose guides are derived from Inter like the digits. */
private const val MATH_OPERATORS = "+−×÷="

/**
 * The font an exercise title is drawn in: English letters and math use the face their guides are
 * derived from — [BcLatinLetterFontFamily] for small letters, [BcLatinCapitalFontFamily] for capitals,
 * numbers and operators; everything else keeps the theme's font.
 */
internal fun String.letterFontFamily(): FontFamily? = when {
    isNotEmpty() && all { it in '0'..'9' } -> BcLatinCapitalFontFamily
    length != 1 -> null
    single() in 'a'..'z' -> BcLatinLetterFontFamily
    single() in 'A'..'Z' -> BcLatinCapitalFontFamily
    single() in MATH_OPERATORS -> BcLatinCapitalFontFamily
    else -> null
}

/**
 * The exercise's name above a tracing canvas or a result. A letter is drawn large because it is the
 * model the child copies; a drawing's name is only a label — the shape itself is on the canvas — so
 * it is set at heading size, where it also fits a narrow screen or a large font scale.
 */
@Composable
fun BcExerciseHeading(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = if (title.isSingleGlyph()) {
            MaterialTheme.typography.displayMedium
        } else {
            MaterialTheme.typography.headlineMedium
        },
        fontFamily = title.letterFontFamily(),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview(showBackground = true)
@Composable
private fun BcExerciseHeadingPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcExerciseHeading(title = "অ")
            BcExerciseHeading(title = "Triangle")
        }
    }
}
