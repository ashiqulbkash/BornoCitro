package com.bornochitra.feature.result

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

/** Placeholder for the Step 13 Result screen. Wired into navigation now so the flow is reachable end to end. */
@Composable
fun ResultScreen(
    sessionId: String,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "Result") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Result for $sessionId coming soon", style = MaterialTheme.typography.headlineSmall)
            BcPrimaryButton(text = "View Progress", onClick = onProgressClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {
    BornoChitraTheme {
        ResultScreen(sessionId = "session-vowel-o", onProgressClick = {})
    }
}
