package com.bornochitra.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bornochitra.R
import com.bornochitra.core.model.StarRule
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme

// The same star as res/drawable/bc_ic_star_filled.xml, on its 24-unit grid.
private const val STAR_PATH = "M12 2.5l2.9 5.9 6.5 0.9-4.7 4.6 1.1 6.4L12 17.3l-5.8 3 1.1-6.4-4.7-4.6 6.5-0.9z"
private const val STAR_GRID = 24f

// The empty result star's outline is 1.6 on the 24-unit grid.
private const val EMPTY_STROKE_UNITS = 1.6f
private val StarOutlineWidth = 1.5.dp
private val StarGap = 1.dp
private val StarPath = PathParser().parsePathString(STAR_PATH).toPath()

/** Star sizes the design uses: tiles and chips 13, cards 14, grown-ups 18, legend 30. Text-sized so they scale with the font. */
object BcStarSize {
    val tile: TextUnit = 13.sp
    val card: TextUnit = 14.sp
    val grownUps: TextUnit = 18.sp
    val legend: TextUnit = 30.sp
}

/**
 * A row of [StarRule.MAX_STARS] stars with [count] filled, announced as one sentence. Filled stars are the star
 * colour with a thin darker outline; empty ones are filled with the empty-star colour (design/DESIGN_SPEC.md 4, Stars).
 */
@Composable
fun BcStars(
    count: Int,
    modifier: Modifier = Modifier,
    size: TextUnit = BcStarSize.tile,
) {
    val description = stringResource(R.string.progress_stars_description, count, StarRule.MAX_STARS)
    val starSize = with(LocalDensity.current) { size.toDp() }
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(StarGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(StarRule.MAX_STARS) { index -> BcStar(filled = index < count, size = starSize) }
    }
}

/**
 * One star. The row of [BcStars] draws them small; Result screens draw them large with [outlineWhenEmpty], because the
 * design's empty result stars are an outlineVariant outline only.
 */
@Composable
fun BcStar(
    filled: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
    outlineWhenEmpty: Boolean = false,
) {
    val colors = BcTheme.colors
    val resultEmptyColor = MaterialTheme.colorScheme.outlineVariant
    Canvas(modifier = modifier.size(size)) {
        val unit = this.size.minDimension / STAR_GRID
        scale(scale = unit, pivot = Offset.Zero) {
            if (filled) {
                drawPath(StarPath, color = colors.star)
                if (colors.starOutline != Color.Transparent) {
                    drawPath(StarPath, color = colors.starOutline, style = Stroke(width = StarOutlineWidth.toPx() / unit, join = StrokeJoin.Round))
                }
            } else if (outlineWhenEmpty) {
                drawPath(StarPath, color = resultEmptyColor, style = Stroke(width = EMPTY_STROKE_UNITS, join = StrokeJoin.Round))
            } else {
                drawPath(StarPath, color = colors.starEmpty)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcStarsPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
            (0..3).forEach { BcStars(count = it) }
            BcStars(count = 2, size = BcStarSize.legend)
            Row { BcStar(filled = true, size = 64.dp); BcStar(filled = false, size = 88.dp, outlineWhenEmpty = true) }
        }
    }
}
