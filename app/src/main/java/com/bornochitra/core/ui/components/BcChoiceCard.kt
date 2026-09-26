package com.bornochitra.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private val ChoicePadding = PaddingValues(horizontal = BcSpacing.m, vertical = BcSpacing.roomy)

/**
 * One option of a small set in a row: an optional [lead] tile, the [label] and a radio at the end — the
 * language choice on onboarding and in the language sheet.
 */
@Composable
fun BcChoiceRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.titleLarge,
    minHeight: Dp = BcDimens.choiceMinHeight,
    lead: (@Composable () -> Unit)? = null,
) {
    ChoiceSurface(selected = selected, onClick = onClick, modifier = modifier.heightIn(min = minHeight)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(ChoicePadding),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            lead?.invoke()
            Text(
                text = label,
                style = labelStyle,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            BcRadio(selected = selected)
        }
    }
}

/** One option of a small set as a column: radio and [label], then a picture and caption (difficulty). */
@Composable
fun BcChoiceColumn(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    ChoiceSurface(selected = selected, onClick = onClick, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(BcSpacing.roomy),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BcRadio(selected = selected)
                Text(text = label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
            content()
        }
    }
}

@Composable
private fun ChoiceSurface(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(BcShapes.lg)
            .background(if (selected) colors.primaryContainer else colors.surfaceContainerLowest)
            .border(
                width = if (selected) BcDimens.selectedBorder else BcDimens.tileBorder,
                color = if (selected) colors.primary else colors.outlineVariant,
                shape = BcShapes.lg,
            )
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
    ) {
        content()
    }
}

/** 24dp radio: a 2dp outline ring, or a 7dp primary ring when selected. */
@Composable
private fun BcRadio(selected: Boolean) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(BcDimens.choiceRadio)
            .border(
                width = if (selected) BcDimens.choiceRadioSelectedRing else BcDimens.tileBorder,
                color = if (selected) colors.primary else colors.outline,
                shape = BcShapes.full,
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun BcChoiceCardPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
            BcChoiceRow(label = "বাংলা", selected = true, onClick = {})
            BcChoiceRow(label = "English", selected = false, onClick = {})
            Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
                BcChoiceColumn(label = "সহজ", selected = true, onClick = {}, modifier = Modifier.weight(1f))
                BcChoiceColumn(label = "কঠিন", selected = false, onClick = {}, modifier = Modifier.weight(1f))
            }
        }
    }
}
