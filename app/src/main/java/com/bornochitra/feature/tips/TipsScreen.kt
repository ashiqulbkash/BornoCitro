package com.bornochitra.feature.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private data class Tip(val stepNumber: Int, val text: String)

// Static onboarding copy — see plan.md section 18. Not dynamic, so no ViewModel/repository needed.
private val tips = listOf(
    Tip(1, "Follow the dots."),
    Tip(2, "Start from the highlighted dot."),
    Tip(3, "Move your finger slowly."),
    Tip(4, "Stay close to the dotted line."),
    Tip(5, "Complete all strokes."),
)

@Composable
fun TipsScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "How to Practice") },
        bottomBar = {
            BcPrimaryButton(
                text = "Continue",
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(BcSpacing.md),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.sm),
            contentPadding = PaddingValues(vertical = BcSpacing.md),
        ) {
            items(tips, key = { it.stepNumber }) { tip ->
                TipRow(tip = tip)
            }
        }
    }
}

@Composable
private fun TipRow(
    tip: Tip,
    modifier: Modifier = Modifier,
) {
    BcCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(BcSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = tip.stepNumber.toString(), style = MaterialTheme.typography.headlineMedium)
            Text(text = tip.text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TipsScreenPreview() {
    BornoChitraTheme {
        TipsScreen(onContinueClick = {})
    }
}
