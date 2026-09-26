package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
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

private const val SUBJECT_CAPTION_ALPHA = 0.85f

/** A coloured square that leads a row or card and holds a glyph or icon. */
@Composable
fun BcGlyphTile(
    size: Dp,
    shape: Shape,
    containerColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.size(size).clip(shape).background(containerColor),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

/**
 * A glyph tile holding [glyph]. A [learning] glyph (a category's lead letter) takes its guide's font through
 * [BcLetterText]; a mark (the language choice's "অ", the "Aa" mark) keeps [style]'s design font.
 */
@Composable
fun BcGlyphTile(
    glyph: String,
    size: Dp,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    style: TextStyle,
    modifier: Modifier = Modifier,
    learning: Boolean = true,
) {
    BcGlyphTile(size = size, shape = shape, containerColor = containerColor, modifier = modifier) {
        if (learning) {
            BcLetterText(text = glyph, style = style, color = contentColor)
        } else {
            Text(text = glyph, style = style, color = contentColor)
        }
    }
}

/**
 * A Home subject card (design/DESIGN_SPEC.md 4, Subject card): the subject's glyph tile, name, caption and progress,
 * all in the subject's container colour. Always shows its progress, empty at 0%.
 */
@Composable
fun BcSubjectCard(
    name: String,
    caption: String,
    progress: Float,
    containerColor: Color,
    contentColor: Color,
    progressColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glyph: @Composable () -> Unit,
) {
    BcSurface(
        modifier = modifier.fillMaxWidth().heightIn(min = BcDimens.subjectCardMinHeight),
        onClick = onClick,
        shape = BcShapes.xl,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(start = BcSpacing.roomy, end = BcSpacing.m, top = BcSpacing.m, bottom = BcSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            glyph()
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BcSpacing.tight)) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(text = caption, style = MaterialTheme.typography.bodySmall, color = contentColor.copy(alpha = SUBJECT_CAPTION_ALPHA))
                BcPercentProgress(
                    label = name,
                    progress = progress,
                    fillColor = progressColor,
                    trackColor = BcTheme.colors.onContainerTrack,
                    percentStyle = BcType.bodySmallBold,
                    percentColor = contentColor,
                )
            }
        }
    }
}

/**
 * A row that opens a category (design/DESIGN_SPEC.md 4, Activity row): lead tile, name, progress and a chevron.
 * [compact] is the Progress list's smaller variant.
 */
@Composable
fun BcActivityRow(
    name: String,
    progress: Float,
    progressColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    lead: @Composable () -> Unit,
) {
    BcCard(modifier = modifier.fillMaxWidth(), onClick = onClick, shape = BcShapes.lg) {
        Row(
            modifier = Modifier
                .heightIn(min = if (compact) BcDimens.rowMinHeightCompact else BcDimens.rowMinHeight)
                .padding(
                    horizontal = if (compact) BcSpacing.snug else BcSpacing.s,
                    vertical = if (compact) BcSpacing.xs else BcSpacing.s,
                ),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            lead()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(if (compact) BcSpacing.xxs else BcSpacing.tight),
            ) {
                Text(text = name, style = if (compact) BcType.rowNameCompact else MaterialTheme.typography.titleMedium)
                BcPercentProgress(label = name, progress = progress, fillColor = progressColor, percentStyle = BcType.bodySmallStrong)
            }
            Icon(
                painter = painterResource(R.drawable.bc_ic_chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(BcDimens.icon),
            )
        }
    }
}

/** An activity row's lead: [glyph] on a 52dp (44dp when [compact]) tile. */
@Composable
fun BcRowLead(
    glyph: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    style: TextStyle = if (compact) BcType.rowLeadCompact else BcType.rowLead,
) {
    BcGlyphTile(
        glyph = glyph,
        size = if (compact) BcDimens.rowLeadCompact else BcDimens.rowLead,
        shape = if (compact) BcShapes.smallTile else BcShapes.md,
        containerColor = containerColor,
        contentColor = contentColor,
        style = style,
        modifier = modifier,
    )
}

/** A section's label above its rows or cards, read as a heading (labelMedium, onSurfaceVariant). */
@Composable
fun BcSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.semantics { heading() },
    )
}

/**
 * A hub game (design/DESIGN_SPEC.md 4, Game card): a mini picture of the game on top, its name at the bottom.
 * Two per row.
 */
@Composable
fun BcGameCard(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    picture: @Composable () -> Unit,
) {
    BcCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.heightIn(min = BcDimens.gameCardMinHeight).padding(BcSpacing.m),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
        ) {
            picture()
            Spacer(Modifier.weight(1f))
            Text(text = name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcRowsPreview() {
    BornoChitraTheme {
        val scheme = MaterialTheme.colorScheme
        Column(modifier = Modifier.padding(BcSpacing.screen), verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
            BcSubjectCard(
                name = "বাংলা",
                caption = "বর্ণ, সংখ্যা, খেলা",
                progress = 0.05f,
                containerColor = scheme.primaryContainer,
                contentColor = scheme.onPrimaryContainer,
                progressColor = scheme.primary,
                onClick = {},
            ) {
                BcGlyphTile("অ", BcDimens.subjectGlyphTile, BcShapes.lg, scheme.primary, scheme.onPrimary, BcType.subjectGlyph)
            }
            BcActivityRow(name = "স্বরবর্ণ", progress = 0.27f, progressColor = scheme.primary, onClick = {}) {
                BcRowLead(glyph = "অ", containerColor = scheme.primaryContainer, contentColor = scheme.onPrimaryContainer)
            }
            BcActivityRow(name = "ছোট হাতের অক্ষর", progress = 0.08f, progressColor = BcTheme.colors.english, onClick = {}, compact = true) {
                BcRowLead(glyph = "a", containerColor = BcTheme.colors.englishContainer, contentColor = BcTheme.colors.onEnglishContainer, compact = true)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
                BcGameCard(name = "শূন্যস্থান পূরণ", onClick = {}, modifier = Modifier.weight(1f)) { Spacer(Modifier.width(BcSpacing.m)) }
                BcGameCard(name = "শুনে শিখি", onClick = {}, modifier = Modifier.weight(1f)) { Spacer(Modifier.width(BcSpacing.m)) }
            }
        }
    }
}
