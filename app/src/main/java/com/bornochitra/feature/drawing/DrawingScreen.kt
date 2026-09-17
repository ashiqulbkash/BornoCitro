package com.bornochitra.feature.drawing

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

/** Placeholder for the Step 9 Drawing screen. Wired into navigation now so the flow is reachable end to end. */
@Composable
fun DrawingScreen(
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "আঁকা", onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            Text(text = "Drawing list coming soon", style = MaterialTheme.typography.headlineSmall)
            BcPrimaryButton(
                text = "Circle",
                onClick = { onExerciseClick("drawing-circle") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawingScreenPreview() {
    BornoChitraTheme {
        DrawingScreen(onBackClick = {}, onExerciseClick = {})
    }
}
