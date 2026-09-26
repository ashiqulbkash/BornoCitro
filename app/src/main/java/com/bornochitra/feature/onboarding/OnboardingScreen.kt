package com.bornochitra.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcAppLogo
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.components.BcGlyphTile
import com.bornochitra.core.ui.components.BcPageIndicator
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme
import com.bornochitra.feature.about.AboutFeature
import com.bornochitra.feature.language.LanguageChoiceSize
import com.bornochitra.feature.language.LanguageChoices
import kotlinx.coroutines.launch

private const val PAGE_LANGUAGE = 0
private const val PAGE_ABOUT = 1
private const val PAGE_COUNT = 2

/**
 * The first-launch onboarding (design/DESIGN_SPEC.md 5.2a–b): the app's language, then what the app is. Swiping
 * works as well as the button. [onFinished] opens Home once onboarding is marked as seen.
 */
@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    OnboardingContent(
        state = state,
        onEvent = viewModel::onEvent,
        onStartClick = {
            viewModel.onEvent(OnboardingEvent.StartClicked)
            onFinished()
        },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingContent(
    state: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Saved, so the recreate that a language change causes keeps the child on the same page.
    val pagerState = rememberPagerState { PAGE_COUNT }
    val scope = rememberCoroutineScope()
    Column(modifier = modifier.fillMaxSize().systemBarsPadding()) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                PAGE_LANGUAGE -> LanguagePage(
                    language = state.language,
                    onLanguageSelected = { onEvent(OnboardingEvent.LanguageSelected(it)) },
                )
                else -> AboutPage()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.m, bottom = BcSpacing.l),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.screen),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BcPageIndicator(count = PAGE_COUNT, current = pagerState.currentPage)
            if (pagerState.currentPage == PAGE_ABOUT) {
                BcPrimaryButton(
                    text = stringResource(R.string.onboarding_start),
                    onClick = onStartClick,
                    trailingIcon = R.drawable.bc_ic_arrow_forward,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                BcPrimaryButton(
                    text = stringResource(R.string.welcome_continue),
                    onClick = { scope.launch { pagerState.animateScrollToPage(PAGE_ABOUT) } },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun LanguagePage(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxl + BcSpacing.m),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.l),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = BcSpacing.m),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BcSpacing.s),
        ) {
            BcAppLogo(size = BcDimens.logoOnboarding)
            Text(
                text = stringResource(R.string.home_welcome),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.semantics { heading() },
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                listOf(R.string.onboarding_language_question, R.string.onboarding_language_question_english).forEach {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        LanguageChoices(language = language, onLanguageSelected = onLanguageSelected, size = LanguageChoiceSize.LARGE)
        Text(
            text = stringResource(R.string.onboarding_change_later),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AboutPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.tight)) {
            Text(
                text = stringResource(R.string.about_heading),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics { heading() },
            )
            Text(text = stringResource(R.string.about_description), style = MaterialTheme.typography.bodyMedium)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.snug)) {
            AboutFeature.entries.forEach { feature -> FeatureCard(feature) }
        }
    }
}

@Composable
private fun FeatureCard(feature: AboutFeature) {
    BcCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(BcSpacing.roomy),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FeatureLead(feature)
            Text(text = stringResource(feature.text), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        }
    }
}

/** Each feature's mark: অa for letters, ১+২ for numbers, a house for drawing, a star for progress. */
@Composable
private fun FeatureLead(feature: AboutFeature) {
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    when (feature) {
        AboutFeature.LETTERS -> BcGlyphTile(size = BcDimens.rowLead, shape = BcShapes.md, containerColor = scheme.primaryContainer) {
            // Each glyph gets exactly half the tile: the fonts' own line boxes are taller than half, and clipped "a".
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                StackedGlyph(stringResource(R.string.about_mark_bangla), BcType.stackedMark, scheme.onPrimaryContainer)
                StackedGlyph(
                    stringResource(R.string.about_mark_latin),
                    BcType.stackedMark.copy(fontFamily = BcLatinLetterFontFamily),
                    scheme.onPrimaryContainer,
                )
            }
        }
        AboutFeature.NUMBERS -> BcGlyphTile(
            glyph = stringResource(R.string.about_mark_numbers),
            size = BcDimens.rowLead,
            shape = BcShapes.md,
            containerColor = colors.englishContainer,
            contentColor = colors.onEnglishContainer,
            style = BcType.rowLeadCompact,
            learning = false,
        )
        AboutFeature.DRAWING -> IconLead(R.drawable.bc_ic_house, scheme.secondaryContainer, scheme.onSecondaryContainer)
        AboutFeature.PROGRESS -> IconLead(R.drawable.bc_ic_star_outline, scheme.tertiaryContainer, scheme.onTertiaryContainer)
    }
}

@Composable
private fun StackedGlyph(text: String, style: TextStyle, color: Color) {
    Box(modifier = Modifier.height(BcDimens.rowLead / 2).wrapContentHeight(unbounded = true), contentAlignment = Alignment.Center) {
        Text(text = text, style = style, color = color)
    }
}

@Composable
private fun IconLead(@DrawableRes icon: Int, container: Color, tint: Color) {
    BcGlyphTile(size = BcDimens.rowLead, shape = BcShapes.md, containerColor = container) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = tint, modifier = Modifier.size(BcDimens.icon))
    }
}

@Preview(showBackground = true, name = "Language")
@Composable
private fun OnboardingLanguagePreview() {
    BornoChitraTheme {
        Box { OnboardingContent(state = OnboardingUiState(), onEvent = {}, onStartClick = {}) }
    }
}

@Preview(showBackground = true, name = "About")
@Composable
private fun OnboardingAboutPreview() {
    BornoChitraTheme { AboutPage() }
}
