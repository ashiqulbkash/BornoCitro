package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** Nothing to show, or something went wrong: icon circle, title, message and an optional action, centred. */
@Composable
fun BcEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(BcSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BcSpacing.xs, Alignment.CenterVertically),
    ) {
        BcIconCircle(
            icon = R.drawable.bc_ic_info,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
            size = BcDimens.emptyStateIcon,
            iconSize = BcDimens.dialogIconGlyph,
            modifier = Modifier.padding(bottom = BcSpacing.xs),
        )
        Text(text = title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (actionText != null) {
            BcPrimaryButton(text = actionText, onClick = onAction, modifier = Modifier.padding(top = BcSpacing.xs))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcEmptyStatePreview() {
    BornoChitraTheme {
        BcEmptyState(
            title = "এখনো কোনো স্বরবর্ণ নেই",
            message = "শিগগিরই অনুশীলনের জন্য অক্ষর আসবে।",
        )
    }
}
