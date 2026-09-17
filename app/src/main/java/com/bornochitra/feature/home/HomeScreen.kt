package com.bornochitra.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** Placeholder for the Step 5 Home screen. Wired into navigation now so the flow is reachable end to end. */
@Composable
fun HomeScreen(
    onVowelsClick: () -> Unit,
    onConsonantsClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "বর্ণচিত্র") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            Text(text = "Home screen coming soon", style = MaterialTheme.typography.headlineSmall)
            BcPrimaryButton(text = "স্বরবর্ণ", onClick = onVowelsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "ব্যঞ্জনবর্ণ", onClick = onConsonantsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "আঁকা", onClick = onDrawingClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "Progress", onClick = onProgressClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BornoChitraTheme {
        HomeScreen(
            onVowelsClick = {},
            onConsonantsClick = {},
            onDrawingClick = {},
            onProgressClick = {},
        )
    }
}
