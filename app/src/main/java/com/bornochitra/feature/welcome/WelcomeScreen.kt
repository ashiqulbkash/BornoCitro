package com.bornochitra.feature.welcome

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_about)) },
        bottomBar = {
            BcPrimaryButton(
                text = stringResource(R.string.welcome_continue),
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(BcSpacing.md),
            )
        },
    ) { innerPadding ->
        AboutContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(BcSpacing.md),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    BornoChitraTheme {
        WelcomeScreen(onContinueClick = {})
    }
}
