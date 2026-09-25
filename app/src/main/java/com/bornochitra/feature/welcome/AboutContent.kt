package com.bornochitra.feature.welcome

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private val features = listOf(
    R.string.about_feature_letters,
    R.string.about_feature_numbers,
    R.string.about_feature_drawing,
    R.string.about_feature_progress,
)

/** What the app is about. Shared by the first-launch Welcome screen and the drawer's About page. */
@Composable
fun AboutContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
    ) {
        Text(text = stringResource(R.string.about_heading), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.about_description), style = MaterialTheme.typography.bodyLarge)
        features.forEach { FeatureCard(text = it) }
    }
}

@Composable
private fun FeatureCard(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    BcCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(BcSpacing.md),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutContentPreview() {
    BornoChitraTheme {
        AboutContent(modifier = Modifier.padding(BcSpacing.md))
    }
}
