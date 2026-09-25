package com.bornochitra.feature.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcLanguageSwitch
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    WelcomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        onContinueClick = {
            viewModel.onEvent(WelcomeEvent.ContinueClicked)
            onContinueClick()
        },
        modifier = modifier,
    )
}

@Composable
private fun WelcomeContent(
    state: WelcomeUiState,
    onEvent: (WelcomeEvent) -> Unit,
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            BcLanguageSwitch(
                language = state.language,
                onLanguageSelected = { onEvent(WelcomeEvent.LanguageSelected(it)) },
            )
            AboutContent(modifier = Modifier.weight(1f).fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    BornoChitraTheme {
        WelcomeContent(
            state = WelcomeUiState(language = AppLanguage.BANGLA),
            onEvent = {},
            onContinueClick = {},
        )
    }
}
