package com.bornochitra.feature.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * Placeholder for the Step 11 Practice screen (and the Step 10.x tracing engine that will drive it).
 * Wired into navigation now so the flow is reachable end to end.
 */
@Composable
fun PracticeScreen(
    exerciseId: String,
    onBackClick: () -> Unit,
    onCompleteClick: (sessionId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "Practice", onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Practice for $exerciseId coming soon", style = MaterialTheme.typography.headlineSmall)
            BcPrimaryButton(text = "Finish", onClick = { onCompleteClick("session-$exerciseId") })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PracticeScreenPreview() {
    BornoChitraTheme {
        PracticeScreen(exerciseId = "vowel-o", onBackClick = {}, onCompleteClick = {})
    }
}
