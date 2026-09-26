package com.bornochitra.feature.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** The drawer's About page: the Welcome content without the language switch and Continue button. */
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_about), onBackClick = onBackClick) },
    ) { innerPadding ->
        AboutContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = BcSpacing.m),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    BornoChitraTheme {
        AboutScreen(onBackClick = {})
    }
}
