package com.bornochitra.core.ui.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlin.math.roundToInt

/** A labelled progress bar, e.g. "স্বরবর্ণ ████████░░ 80%" on Home/Progress screens. */
@Composable
fun BcLabeledProgress(
    label: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
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
            progress = { progress.coerceIn(0f, 1f) },
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
