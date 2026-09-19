package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** A Bengali letter is one glyph; anything longer is a word, such as a drawing's name. */
private const val SINGLE_GLYPH_MAX_LENGTH = 2

internal fun String.isSingleGlyph(): Boolean = length <= SINGLE_GLYPH_MAX_LENGTH

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
