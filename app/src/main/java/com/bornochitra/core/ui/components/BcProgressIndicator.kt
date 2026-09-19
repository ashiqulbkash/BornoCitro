package com.bornochitra.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlin.math.roundToInt

private const val PROGRESS_FILL_MS = 600

/**
 * A labelled progress bar, e.g. "স্বরবর্ণ ████████░░ 80%" on Home/Progress screens. The bar fills
 * smoothly when it first appears and whenever [progress] changes (plan.md section 44); the
 * percentage beside it always shows the real value.
 */
@Composable
fun BcLabeledProgress(
    label: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val target = progress.coerceIn(0f, 1f)
    // A static preview never advances an animation, so it starts from the finished state.
    val isPreview = LocalInspectionMode.current
    val filled = remember { Animatable(if (isPreview) target else 0f) }
    LaunchedEffect(target) { filled.animateTo(target, tween(durationMillis = PROGRESS_FILL_MS)) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${(progress * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        LinearProgressIndicator(
            progress = { filled.value },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BcSpacing.xs)
                .height(BcDimens.progressBarHeight)
                .clip(RoundedCornerShape(BcDimens.progressBarHeight / 2)),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BcLabeledProgressPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.md)) {
            BcLabeledProgress(label = "স্বরবর্ণ", progress = 1f)
            BcLabeledProgress(label = "ব্যঞ্জনবর্ণ", progress = 0.6f)
        }
    }
}
