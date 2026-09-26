package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcBengaliLetterFontFamily
import com.bornochitra.core.ui.theme.BcLatinCapitalFontFamily
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** A Bengali letter is one glyph of up to two code units (ড়); anything longer is a word, such as a drawing's name. */
private const val SINGLE_GLYPH_MAX_LENGTH = 2

internal fun String.isSingleGlyph(): Boolean = length <= SINGLE_GLYPH_MAX_LENGTH

/** The math operators, whose guides are derived from Inter like the digits. */
private const val MATH_OPERATORS = "+−×÷="

private val BengaliBlock = 'ঀ'..'৿'

/**
 * The font a learning character is drawn in: the face its tracing guide was derived from, so the model the child
 * copies has the guide's shape — [BcLatinLetterFontFamily] for small letters, [BcLatinCapitalFontFamily] for
 * capitals, numbers and operators, [BcBengaliLetterFontFamily] for Bengali letters and numbers. Null for a word
 * (a drawing's name), which is UI text in the design's fonts.
 *
 * This is the one place that decides it (design/REDESIGN_PLAN.md D1).
 */
internal fun String.letterFontFamily(): FontFamily? = when {
    isEmpty() -> null
    all { it in '0'..'9' } -> BcLatinCapitalFontFamily
    all { it in BengaliBlock } && (isSingleGlyph() || all { it.isDigit() }) -> BcBengaliLetterFontFamily
    length != 1 -> null
    single() in 'a'..'z' -> BcLatinLetterFontFamily
    single() in 'A'..'Z' -> BcLatinCapitalFontFamily
    single() in MATH_OPERATORS -> BcLatinCapitalFontFamily
    else -> null
}

/**
 * [style] for [text]: a learning character keeps the size, line height and colour of [style] but takes its guide's
 * font at Bold, the weight every guide was derived at. Anything else is returned unchanged.
 */
fun letterStyle(text: String, style: TextStyle): TextStyle {
    val family = text.letterFontFamily() ?: return style
    return style.copy(fontFamily = family, fontWeight = FontWeight.Bold)
}

/** A learning character — or, for a drawing, its name — drawn with [letterStyle]. */
@Composable
fun BcLetterText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
) {
    Text(
        text = text,
        modifier = modifier,
        style = letterStyle(text, style),
        color = color,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
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
    BcLetterText(
        text = title,
        modifier = modifier,
        style = if (title.isSingleGlyph()) BcType.letterPractice else MaterialTheme.typography.headlineMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun BcLetterPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
            listOf("অ", "ঊ", "ঐ", "ক্ষ", "১৫", "a", "A", "12", "+").forEach { BcLetterText(text = it, style = BcType.letterTile) }
            BcExerciseHeading(title = "ত্রিভুজ")
        }
    }
}
