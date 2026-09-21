package com.bornochitra.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun BcCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val cardModifier = if (onClick != null) modifier.clickable(role = Role.Button, onClick = onClick) else modifier
    Card(
        modifier = cardModifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = BcDimens.cardElevation),
        content = { content() },
    )
}

/** A large tappable tile for a single letter/drawing exercise, e.g. in a Vowels or Consonants grid. */
@Composable
fun BcExerciseTile(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    statusText: String? = null,
) {
    BcCard(
        // A minimum rather than a fixed size, so a large font scale grows the tile instead of clipping
        // the status text. The box below fills the card and centres the content in its minimum height.
        modifier = modifier.sizeIn(minWidth = BcDimens.letterTileSize, minHeight = BcDimens.letterTileSize),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().heightIn(min = BcDimens.letterTileSize),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(BcSpacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Longer titles (e.g. drawing names) get a smaller style so they don't wrap or
                // overflow the tile.
                val labelStyle = if (label.isSingleGlyph()) {
                    MaterialTheme.typography.headlineLarge
                } else {
                    MaterialTheme.typography.titleMedium
                }
                Text(
                    text = label,
                    style = labelStyle,
                    fontFamily = label.letterFontFamily(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (statusText != null) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcCardPreview() {
    BornoChitraTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcExerciseTile(label = "অ", onClick = {}, statusText = "Mastered", modifier = Modifier.width(BcDimens.letterTileSize))
            BcExerciseTile(label = "আ", onClick = {}, statusText = "2 attempts", modifier = Modifier.width(BcDimens.letterTileSize))
        }
    }
}

@Preview(showBackground = true, name = "Long label")
@Composable
private fun BcCardLongLabelPreview() {
    BornoChitraTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcExerciseTile(label = "Triangle", onClick = {}, statusText = "Completed", modifier = Modifier.width(BcDimens.letterTileSize))
            BcExerciseTile(label = "Circle", onClick = {}, statusText = null, modifier = Modifier.width(BcDimens.letterTileSize))
        }
    }
}
