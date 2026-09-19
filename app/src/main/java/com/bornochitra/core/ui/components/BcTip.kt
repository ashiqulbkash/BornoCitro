package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.tips.ContextualTip
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * A quiet hint shown at a moment it helps, deliberately lighter than a feedback banner so it
 * reads as a nudge rather than a verdict (plan.md section 43).
 */
@Composable
fun BcTip(
    tip: ContextualTip,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Text(
            text = tip.message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(BcSpacing.md),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BcTipPreview() {
    BornoChitraTheme {
        Column(
            modifier = Modifier.padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
        ) {
            ContextualTip.entries.forEach { BcTip(tip = it) }
        }
    }
}
