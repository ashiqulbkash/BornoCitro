package com.bornochitra.feature.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcBanner
import com.bornochitra.core.ui.components.BcBannerKind
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcSectionLabel
import com.bornochitra.core.ui.components.BcSurface
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun LearnScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LearnContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

/** The screen's subject colours: Bangla's are primary, English's are english (design/DESIGN_SPEC.md 5.18). */
@Immutable
private data class LearnColors(
    val letterContainer: Color,
    val letterContent: Color,
    val accent: Color,
)

@Composable
private fun learnColors(language: AppLanguage): LearnColors = when (language) {
    AppLanguage.BANGLA -> {
        val scheme = MaterialTheme.colorScheme
        LearnColors(letterContainer = scheme.primary, letterContent = scheme.onPrimary, accent = scheme.primary)
    }
    AppLanguage.ENGLISH -> {
        val colors = BcTheme.colors
        LearnColors(letterContainer = colors.english, letterContent = colors.englishContainer, accent = colors.english)
    }
}

@Composable
private fun LearnContent(
    state: LearnUiState,
    onEvent: (LearnEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_learn), onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Above the list rather than in it: the check ends after the list is laid out, and a
            // keyed list keeps its first letter in place, so a banner added as an item would land
            // above the top of the screen.
            if (state.voiceStatus == VoiceStatus.MISSING) {
                NoVoiceBanner(
                    language = state.language,
                    modifier = Modifier.padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs),
                )
            }
            val colors = learnColors(state.language)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.screen),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
            ) {
                state.items.groupBy { it.group }.forEach { (group, items) ->
                    learnGroup(group = group, items = items, colors = colors, onEvent = onEvent)
                }
            }
        }
    }
}

private fun LazyListScope.learnGroup(
    group: LearnGroup,
    items: List<LearnItem>,
    colors: LearnColors,
    onEvent: (LearnEvent) -> Unit,
) {
    item(key = group.name) { BcSectionLabel(text = group.label()) }
    items(items, key = { it.letter }) { item ->
        LearnRow(item = item, colors = colors, onEvent = onEvent)
    }
}

@Composable
private fun LearnGroup.label(): String = when (this) {
    LearnGroup.VOWELS -> ExerciseType.VOWEL.label()
    LearnGroup.CONSONANTS -> ExerciseType.CONSONANT.label()
    LearnGroup.ENGLISH_LETTERS -> stringResource(R.string.learn_section_english)
}

@Composable
private fun NoVoiceBanner(
    language: AppLanguage,
    modifier: Modifier = Modifier,
) {
    val languageName = stringResource(
        when (language) {
            AppLanguage.BANGLA -> R.string.title_bangla
            AppLanguage.ENGLISH -> R.string.title_english
        },
    )
    BcBanner(
        kind = BcBannerKind.INFO,
        text = stringResource(R.string.learn_no_voice_message, languageName),
        modifier = modifier,
    )
}

/** A letter button that says the letter, and an example button that says "অ তে অজগর". */
@Composable
private fun LearnRow(
    item: LearnItem,
    colors: LearnColors,
    onEvent: (LearnEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.snug),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BcSurface(
            modifier = Modifier.size(width = BcDimens.learnLetterWidth, height = BcDimens.learnRowHeight),
            onClick = { onEvent(LearnEvent.LetterClicked(item)) },
            shape = BcShapes.lg,
            color = colors.letterContainer,
            contentColor = colors.letterContent,
        ) {
            Box(contentAlignment = Alignment.Center) {
                BcLetterText(text = item.letter, style = BcType.learnLetter)
                Icon(
                    painter = painterResource(R.drawable.bc_ic_speaker_small),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(BcSpacing.xs)
                        .size(BcDimens.learnSpeakerSmall),
                )
            }
        }
        BcSurface(
            modifier = Modifier
                .weight(1f)
                .height(BcDimens.learnRowHeight),
            onClick = { onEvent(LearnEvent.ExplanationClicked(item)) },
            shape = BcShapes.lg,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface,
            border = BorderStroke(BcDimens.tileBorder, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = BcSpacing.m),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.bc_ic_speaker),
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(BcDimens.icon),
                )
                Text(text = exampleText(item, colors.accent), style = BcType.example, maxLines = 1)
            }
        }
    }
}

/** The example with its word bold in the subject colour. */
private fun exampleText(item: LearnItem, wordColor: Color) = buildAnnotatedString {
    val wordStart = item.explanation.lastIndexOf(item.word).takeIf { it >= 0 } ?: item.explanation.length
    append(item.explanation.substring(0, wordStart))
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = wordColor)) {
        append(item.explanation.substring(wordStart))
    }
}

private val previewItems = englishLearnLetters.take(3).map {
    LearnItem(letter = it.letter, explanation = it.explanation(AppLanguage.ENGLISH), word = it.word, group = LearnGroup.ENGLISH_LETTERS)
}

@Preview(showBackground = true, name = "Voice ready")
@Composable
private fun LearnScreenPreview() {
    BornoChitraTheme {
        LearnContent(
            state = LearnUiState(language = AppLanguage.ENGLISH, items = previewItems, voiceStatus = VoiceStatus.READY),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "No voice")
@Composable
private fun LearnScreenNoVoicePreview() {
    BornoChitraTheme {
        LearnContent(
            state = LearnUiState(language = AppLanguage.ENGLISH, items = previewItems, voiceStatus = VoiceStatus.MISSING),
            onEvent = {},
            onBackClick = {},
        )
    }
}
