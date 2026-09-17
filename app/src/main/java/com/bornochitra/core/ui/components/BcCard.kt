package com.bornochitra.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val cardModifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier
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
        modifier = modifier.size(BcDimens.letterTileSize),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(BcSpacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = label, style = MaterialTheme.typography.headlineLarge)
            if (statusText != null) {
                Text(text = statusText, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcCardPreview() {
    BornoChitraTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcExerciseTile(label = "অ", onClick = {}, statusText = "Mastered")
            BcExerciseTile(label = "আ", onClick = {}, statusText = "2 attempts")
        }
    }
}
