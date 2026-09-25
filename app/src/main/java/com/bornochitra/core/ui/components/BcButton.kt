package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun BcPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(BcDimens.minTouchTarget),
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun BcSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(BcDimens.minTouchTarget),
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

/**
 * One option of a small set, such as a difficulty or the app's language: filled when [selected],
 * outlined otherwise, and announced as selected to a screen reader.
 */
@Composable
fun BcChoiceButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val choiceModifier = modifier.semantics { this.selected = selected }
    if (selected) {
        BcPrimaryButton(text = text, onClick = onClick, modifier = choiceModifier)
    } else {
        BcSecondaryButton(text = text, onClick = onClick, modifier = choiceModifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun BcButtonPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcPrimaryButton(text = "Try Again", onClick = {})
            BcSecondaryButton(text = "Back", onClick = {})
            BcPrimaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}
