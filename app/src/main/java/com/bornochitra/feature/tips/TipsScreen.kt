package com.bornochitra.feature.tips

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private data class Tip(val stepNumber: Int, @StringRes val text: Int)

// Static onboarding copy — see plan.md section 18. Not dynamic, so no ViewModel/repository needed.
private val tips = listOf(
    Tip(1, R.string.tips_follow_dots),
    Tip(2, R.string.tips_start_at_end),
    Tip(3, R.string.tips_move_slowly),
    Tip(4, R.string.tips_stay_close),
    Tip(5, R.string.tips_complete_strokes),
)

@Composable
fun TipsScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_tips)) },
        bottomBar = {
            BcPrimaryButton(
                text = stringResource(R.string.tips_continue),
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
            Text(text = stringResource(R.string.tips_step_number, tip.stepNumber), style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(tip.text), style = MaterialTheme.typography.bodyLarge)
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
