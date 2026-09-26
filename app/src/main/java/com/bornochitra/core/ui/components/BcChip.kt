package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** A 32dp pill with an optional 16dp icon (design/DESIGN_SPEC.md 4, Chip). */
@Composable
fun BcChip(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconTint: Color = contentColor,
    iconSize: Dp = BcDimens.iconSmall,
    height: Dp = BcDimens.chipHeight,
    textStyle: TextStyle = BcType.bodySmallStrong,
    padding: PaddingValues = PaddingValues(horizontal = BcSpacing.s),
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .heightIn(min = height)
            .clip(BcShapes.full)
            .background(containerColor)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .padding(padding),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.tight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = iconTint, modifier = Modifier.size(iconSize))
        }
        Text(text = text, style = textStyle, color = contentColor)
    }
}

/**
 * The Home top bar's language chip: globe and the current language's name. 40dp tall, inside a 48dp touch area.
 * Opens the language sheet.
 */
@Composable
fun BcLanguageChip(
    languageName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.language_chip_description, languageName)
    Box(
        modifier = modifier
            .heightIn(min = BcDimens.iconButton)
            // Read as "App language: বাংলা", so it is not mistaken for the Bangla subject.
            .clearAndSetSemantics {
                contentDescription = description
                role = Role.Button
                onClick { onClick(); true }
            },
        contentAlignment = Alignment.Center,
    ) {
        BcChip(
            text = languageName,
            icon = R.drawable.bc_ic_globe,
            iconSize = BcDimens.iconChip,
            height = BcDimens.chipTopBarHeight,
            textStyle = MaterialTheme.typography.labelMedium,
            contentColor = MaterialTheme.colorScheme.onSurface,
            padding = PaddingValues(start = BcSpacing.snug, end = BcSpacing.roomy),
            onClick = onClick,
        )
    }
}

/**
 * How far a category has got (design/DESIGN_SPEC.md 4, Header progress card): a 14dp bar with its % and
 * chips counting learned, finished and in-progress items. A null count hides its chip.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BcHeaderProgressCard(
    label: String,
    progress: Float,
    modifier: Modifier = Modifier,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    learnedCount: Int? = null,
    doneCount: Int? = null,
    runningCount: Int? = null,
) {
    BcFlatCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = BcSpacing.m, vertical = BcSpacing.roomy),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
        ) {
            BcPercentProgress(
                label = label,
                progress = progress,
                height = BcDimens.progressBarHeader,
                fillColor = fillColor,
                percentStyle = MaterialTheme.typography.titleMedium,
                percentColor = MaterialTheme.colorScheme.onSurface,
            )
            if (learnedCount != null || doneCount != null || runningCount != null) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
                ) {
                    learnedCount?.let { BcLearnedChip(count = it) }
                    doneCount?.let { BcDoneChip(count = it) }
                    runningCount?.let {
                        BcChip(text = pluralStringResource(R.plurals.header_running, it, it))
                    }
                }
            }
        }
    }
}

/** "১টি শিখেছ" with a check, on the success container. */
@Composable
fun BcLearnedChip(count: Int, modifier: Modifier = Modifier) {
    BcChip(
        text = pluralStringResource(R.plurals.header_learned, count, count),
        icon = R.drawable.bc_ic_check_bold,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        iconTint = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
    )
}

/** "★ ২টি শেষ". */
@Composable
fun BcDoneChip(
    count: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    BcChip(
        text = pluralStringResource(R.plurals.header_done, count, count),
        icon = R.drawable.bc_ic_star_filled,
        iconTint = BcTheme.colors.star,
        containerColor = containerColor,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun BcChipPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.m)) {
            BcLanguageChip(languageName = "বাংলা", onClick = {})
            BcHeaderProgressCard(label = "স্বরবর্ণ", progress = 0.27f, learnedCount = 1, doneCount = 2, runningCount = 1)
        }
    }
}
