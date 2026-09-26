package com.bornochitra.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val CONTINUE_HALO_ALPHA = 0.22f
private val TileContentGap = 2.dp
private val BadgeOffset = 6.dp

/**
 * The design's card: white with a soft purple shadow in light, a lighter surface with a 1dp outline in dark
 * (design/DESIGN_SPEC.md 1.2, Card elevation).
 */
@Composable
fun BcCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = BcShapes.xl,
    content: @Composable () -> Unit,
) {
    val colors = BcTheme.colors
    val container = if (colors.cardHasShadow) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    val border = if (colors.cardHasShadow) null else BorderStroke(BcDimens.outline, MaterialTheme.colorScheme.outlineVariant)
    val cardModifier = if (colors.cardHasShadow) {
        modifier.shadow(BcDimens.cardShadow, shape, ambientColor = colors.cardShadow, spotColor = colors.cardShadow)
    } else {
        modifier
    }
    BcSurface(
        modifier = cardModifier,
        onClick = onClick,
        shape = shape,
        color = container,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = border,
        content = content,
    )
}

/** A flat card: surfaceContainerLow, no shadow. Header cards, sheets' content blocks. */
@Composable
fun BcFlatCard(
    modifier: Modifier = Modifier,
    shape: Shape = BcShapes.xl,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    content: @Composable () -> Unit,
) {
    BcSurface(modifier = modifier, onClick = null, shape = shape, color = color, contentColor = MaterialTheme.colorScheme.onSurface, content = content)
}

/** A [Surface] that is a button only when [onClick] is given. */
@Composable
internal fun BcSurface(
    modifier: Modifier,
    onClick: (() -> Unit)?,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Surface(onClick = onClick, modifier = modifier.semantics { role = Role.Button }, shape = shape, color = color, contentColor = contentColor, border = border, content = content)
    } else {
        Surface(modifier = modifier, shape = shape, color = color, contentColor = contentColor, border = border, content = content)
    }
}

/** How far an exercise has got, as its tile shows it (design/DESIGN_SPEC.md 4, Letter tile). */
enum class BcTileState { NOT_STARTED, PRACTISING, COMPLETED, MASTERED }

/** A tile has no separate "started" look: one unfinished attempt already shows as practising. */
fun LearningState.toTileState(): BcTileState = when (this) {
    LearningState.NOT_STARTED -> BcTileState.NOT_STARTED
    LearningState.STARTED, LearningState.PRACTICING -> BcTileState.PRACTISING
    LearningState.COMPLETED -> BcTileState.COMPLETED
    LearningState.MASTERED -> BcTileState.MASTERED
}

/**
 * The tile shell of a letter or drawing: its state's fill and border, the mastered check badge, and the
 * "continue here" ring, halo and flag. [content] is centred in a column, [contentGap] apart. The tile fills the
 * height [modifier] gives it (a square grid tile), and is at least [minHeight] tall. The small Progress detail
 * tiles need a narrower [contentPadding] for their status line, and say "learned" there instead of showing the
 * badge ([showMasteredBadge] false).
 */
@Composable
fun BcExerciseTile(
    state: BcTileState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isContinueHere: Boolean = false,
    cornerRadius: Dp = BcDimens.tileCorner,
    minHeight: Dp = BcDimens.tileMinSize,
    contentGap: Dp = TileContentGap,
    contentPadding: Dp = BcSpacing.xs,
    showMasteredBadge: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    val (fill, borderColor, contentColor) = when (state) {
        BcTileState.NOT_STARTED -> Triple(scheme.surfaceContainerLowest, scheme.outlineVariant, scheme.onSurface)
        BcTileState.PRACTISING -> Triple(scheme.primaryContainer, scheme.primary, scheme.onPrimaryContainer)
        BcTileState.COMPLETED -> Triple(scheme.surfaceContainerLow, scheme.outlineVariant, scheme.onSurface)
        BcTileState.MASTERED -> Triple(scheme.tertiaryContainer, scheme.tertiary, scheme.onTertiaryContainer)
    }
    val border = if (isContinueHere) BorderStroke(BcDimens.continueRing, colors.accent) else BorderStroke(BcDimens.tileBorder, borderColor)
    Box(modifier = modifier) {
        BcSurface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .heightIn(min = minHeight)
                .then(if (isContinueHere) Modifier.continueHalo(cornerRadius, colors.accent.copy(alpha = CONTINUE_HALO_ALPHA)) else Modifier),
            onClick = onClick,
            shape = shape,
            color = fill,
            contentColor = contentColor,
            border = border,
        ) {
            Column(
                modifier = Modifier.heightIn(min = minHeight).padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(contentGap, Alignment.CenterVertically),
                content = content,
            )
        }
        if (state == BcTileState.MASTERED && showMasteredBadge) {
            MasteredBadge(modifier = Modifier.align(Alignment.TopEnd).offset(x = BadgeOffset, y = -BadgeOffset))
        }
        if (isContinueHere) {
            ContinueFlag(modifier = Modifier.align(Alignment.TopCenter).offset(y = -BcDimens.flagOffset))
        }
    }
}

/** A letter tile: the character, then the attempt count while practising or the best-score stars once finished. */
@Composable
fun BcLetterTile(
    character: String,
    state: BcTileState,
    attemptCount: Int,
    stars: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isContinueHere: Boolean = false,
) {
    BcExerciseTile(state = state, onClick = onClick, isContinueHere = isContinueHere, modifier = modifier) {
        BcLetterText(text = character, style = BcType.letterTile)
        BcTileStatus(state = state, attemptCount = attemptCount, stars = stars)
    }
}

/** The status line under a tile's character: empty, "২ বার", or stars. Always at least 16dp tall. */
@Composable
fun BcTileStatus(
    state: BcTileState,
    attemptCount: Int,
    stars: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.heightIn(min = BcDimens.tileStatusMinHeight), contentAlignment = Alignment.Center) {
        when (state) {
            BcTileState.NOT_STARTED -> Unit
            BcTileState.PRACTISING -> Text(
                text = pluralStringResource(R.plurals.tile_attempts, attemptCount, attemptCount),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
            )
            BcTileState.COMPLETED, BcTileState.MASTERED -> BcStars(count = stars)
        }
    }
}

/** A 4dp accent halo drawn just outside the tile. */
private fun Modifier.continueHalo(cornerRadius: Dp, color: Color): Modifier = drawBehind {
    val halo = BcDimens.continueHalo.toPx()
    val radius = cornerRadius.toPx() + halo
    drawRoundRect(
        color = color,
        topLeft = Offset(-halo, -halo),
        size = Size(size.width + halo * 2, size.height + halo * 2),
        cornerRadius = CornerRadius(radius),
    )
}

@Composable
private fun MasteredBadge(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .size(BcDimens.badge)
            .background(scheme.surface, BcShapes.full)
            .padding(BcDimens.badgeRing)
            .background(scheme.tertiary, BcShapes.full),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.bc_ic_check_bold),
            contentDescription = stringResource(R.string.status_mastered),
            tint = scheme.onTertiary,
            modifier = Modifier.size(BcDimens.iconSmall),
        )
    }
}

@Composable
private fun ContinueFlag(modifier: Modifier = Modifier) {
    val colors = BcTheme.colors
    Text(
        text = stringResource(R.string.tile_continue_here),
        style = BcType.flag,
        color = colors.onAccent,
        modifier = modifier
            .background(colors.accent, BcShapes.full)
            .padding(horizontal = BcSpacing.xs, vertical = BcSpacing.xxs),
    )
}

@Preview(showBackground = true)
@Composable
private fun BcLetterTilePreview() {
    BornoChitraTheme {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Row(modifier = Modifier.padding(BcSpacing.m), horizontalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
                val tile = Modifier.width(BcDimens.tileMinSize)
                BcLetterTile("অ", BcTileState.MASTERED, 4, 3, {}, tile)
                BcLetterTile("আ", BcTileState.COMPLETED, 3, 3, {}, tile, isContinueHere = true)
                BcLetterTile("ই", BcTileState.PRACTISING, 2, 0, {}, tile)
                BcLetterTile("ঊ", BcTileState.NOT_STARTED, 0, 0, {}, tile)
            }
            Spacer(Modifier.size(BcSpacing.m))
        }
    }
}
