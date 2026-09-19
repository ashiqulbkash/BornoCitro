package com.bornochitra.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.theme.BcFeedbackEncourage
import com.bornochitra.core.ui.theme.BcFeedbackGood
import com.bornochitra.core.ui.theme.BcFeedbackGreat
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * UI-layer feedback tone. Feature screens map their own scoring result
 * (e.g. a LOW/MEDIUM/PERFECT score level) onto one of these; this component
 * does not depend on the scoring domain model.
 */
enum class BcFeedbackTone(val color: Color) {
    ENCOURAGING(BcFeedbackEncourage),
    GOOD(BcFeedbackGood),
    GREAT(BcFeedbackGreat),
}

@Composable
fun BcFeedbackBanner(
    tone: BcFeedbackTone,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = tone.color.copy(alpha = 0.15f)),
    ) {
        Column(
            modifier = Modifier.padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
        ) {
            // The tone colours the card; the title stays in the body colour because the tone colours
            // are too light to read as text on their own tint. The wording carries the meaning too.
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcFeedbackBannerPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
            BcFeedbackBanner(
                tone = BcFeedbackTone.GREAT,
                title = "Great Job! 🎉",
                message = "Perfect! 94%",
            )
            BcFeedbackBanner(
                tone = BcFeedbackTone.GOOD,
                title = "Good Try!",
                message = "76%",
            )
            BcFeedbackBanner(
                tone = BcFeedbackTone.ENCOURAGING,
                title = "Let's Practice Again!",
                message = "48%",
            )
        }
    }
}
