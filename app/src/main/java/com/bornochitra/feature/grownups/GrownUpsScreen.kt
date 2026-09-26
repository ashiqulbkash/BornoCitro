package com.bornochitra.feature.grownups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.MasteryRule
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.components.BcFlatCard
import com.bornochitra.core.ui.components.BcIconCircle
import com.bornochitra.core.ui.components.BcLanguageSwitch
import com.bornochitra.core.ui.components.BcSectionLabel
import com.bornochitra.core.ui.components.BcStarSize
import com.bornochitra.core.ui.components.BcStars
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme
import com.bornochitra.feature.about.AboutFeature

// The legend's example rating: two of three stars.
private const val LEGEND_STARS = 2

/**
 * The Grown-ups tab (design/DESIGN_SPEC.md 5.5), in place of the old drawer and About page: the app's language, how
 * progress is counted, and what the app is.
 */
@Composable
fun GrownUpsScreen(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GrownUpsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GrownUpsContent(
        state = state,
        language = language,
        onLanguageSelected = onLanguageSelected,
        navigationBar = navigationBar,
        modifier = modifier,
    )
}

@Composable
private fun GrownUpsContent(
    state: GrownUpsUiState,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_grown_ups)) },
        bottomBar = navigationBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.section),
        ) {
            Section(label = stringResource(R.string.grown_ups_language_label)) {
                BcLanguageSwitch(language = language, onLanguageSelected = onLanguageSelected)
            }
            Section(label = stringResource(R.string.grown_ups_counting)) {
                CountingCard(state)
            }
            Section(label = stringResource(R.string.title_about)) {
                AboutSection()
            }
        }
    }
}

@Composable
private fun Section(
    label: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
        BcSectionLabel(text = label)
        content()
    }
}

@Composable
private fun CountingCard(state: GrownUpsUiState) {
    val scheme = MaterialTheme.colorScheme
    BcCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
            CountingRow(text = stringResource(R.string.grown_ups_stars)) {
                BcStars(count = LEGEND_STARS, size = BcStarSize.grownUps)
            }
            CountingRow(text = stringResource(R.string.mastery_rule, state.requiredCompletions, state.minBestScorePercent)) {
                BcIconCircle(
                    icon = R.drawable.bc_ic_check_bold,
                    containerColor = scheme.tertiary,
                    iconTint = scheme.onTertiary,
                    size = BcDimens.badge,
                    iconSize = BcDimens.iconSmall,
                )
            }
            CountingRow(text = stringResource(R.string.grown_ups_on_phone)) {
                Icon(
                    painter = painterResource(R.drawable.bc_ic_phone),
                    contentDescription = null,
                    tint = scheme.onSurfaceVariant,
                    modifier = Modifier.size(BcDimens.icon),
                )
            }
        }
    }
}

@Composable
private fun CountingRow(
    text: String,
    lead: @Composable () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.s), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(BcDimens.grownUpsLead), contentAlignment = Alignment.Center) { lead() }
        Text(text = text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun AboutSection() {
    Text(text = stringResource(R.string.about_heading), style = MaterialTheme.typography.titleLarge)
    Text(text = stringResource(R.string.about_description), style = MaterialTheme.typography.bodyMedium)
    BcFlatCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = BcSpacing.m, vertical = BcSpacing.tight)) {
            AboutFeature.entries.forEachIndexed { index, feature ->
                if (index > 0) HorizontalDivider(thickness = BcDimens.divider, color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.padding(vertical = BcSpacing.snug),
                    horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
                ) {
                    Text(
                        text = stringResource(R.string.list_number, index + 1),
                        style = BcType.bodySmallStrong,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(text = stringResource(feature.text), style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GrownUpsScreenPreview() {
    BornoChitraTheme {
        GrownUpsContent(
            state = GrownUpsUiState(MasteryRule.DEFAULT_REQUIRED_COMPLETIONS, MasteryRule.DEFAULT_MIN_BEST_SCORE.toInt()),
            language = AppLanguage.BANGLA,
            onLanguageSelected = {},
            navigationBar = {},
        )
    }
}
