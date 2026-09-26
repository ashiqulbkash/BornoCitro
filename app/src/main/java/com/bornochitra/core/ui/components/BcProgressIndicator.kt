package com.bornochitra.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlin.math.roundToInt

private const val PROGRESS_FILL_MS = 600

/** Whole percent of a 0..1 progress, as every label shows it. */
fun progressPercent(progress: Float): Int = (progress.coerceIn(0f, 1f) * 100).roundToInt()

/**
 * A rounded progress bar with no end dot (design/DESIGN_SPEC.md 4, Progress bar). It fills smoothly when it
 * first appears and whenever [progress] changes; any value above 0 is at least [BcDimens.progressMinFill] wide so
 * a first finished letter is visible, and 0 shows the empty track.
 */
@Composable
fun BcProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = BcDimens.progressBar,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
) {
    val target = progress.coerceIn(0f, 1f)
    // A static preview never advances an animation, so it starts from the finished state.
    val isPreview = LocalInspectionMode.current
    val filled = remember { Animatable(if (isPreview) target else 0f) }
    LaunchedEffect(target) { filled.animateTo(target, tween(durationMillis = PROGRESS_FILL_MS)) }

    Canvas(modifier = modifier.fillMaxWidth().height(height)) {
        val radius = CornerRadius(size.height / 2)
        drawRoundRect(color = trackColor, cornerRadius = radius)
        if (filled.value > 0f) {
            val width = maxOf(size.width * filled.value, BcDimens.progressMinFill.toPx()).coerceAtMost(size.width)
            drawRoundRect(color = fillColor, size = Size(width, size.height), cornerRadius = radius)
        }
    }
}

/** A bar with its percentage after it, read as one sentence: "[label], 27 percent". */
@Composable
fun BcPercentProgress(
    label: String,
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = BcDimens.progressBar,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    percentStyle: TextStyle = MaterialTheme.typography.bodySmall,
    percentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val percent = progressPercent(progress)
    val description = stringResource(R.string.labeled_progress_description, label, percent)
    Row(
        modifier = modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.snug),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BcProgressBar(progress = progress, height = height, fillColor = fillColor, trackColor = trackColor, modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.percent, percent), style = percentStyle, color = percentColor)
    }
}

/** Interim labelled bar for screens not redesigned yet (removed in Phase 6): the label above a bar with its %. */
@Composable
fun BcLabeledProgress(
    label: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val percent = progressPercent(progress)
    val description = stringResource(R.string.labeled_progress_description, label, percent)
    Column(
        modifier = modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description },
        verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        BcPercentProgress(label = label, progress = progress)
    }
}

@Preview(showBackground = true)
@Composable
private fun BcProgressPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.m)) {
            BcPercentProgress(label = "স্বরবর্ণ", progress = 0.27f)
            BcPercentProgress(label = "ব্যঞ্জনবর্ণ", progress = 0.01f)
            BcPercentProgress(label = "আঁকা", progress = 0f, fillColor = MaterialTheme.colorScheme.secondary)
            BcPercentProgress(label = "English", progress = 0.08f, fillColor = BcTheme.colors.english, height = BcDimens.progressBarHeader)
        }
    }
}
