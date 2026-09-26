package com.bornochitra.feature.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcFeedbackBanner
import com.bornochitra.core.ui.components.BcFeedbackTone
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcOutlineButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
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
                    modifier = Modifier.padding(start = BcSpacing.m, end = BcSpacing.m, top = BcSpacing.m),
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(BcSpacing.m),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
            ) {
                items(state.items, key = { it.letter }) { item ->
                    LearnRow(item = item, onEvent = onEvent)
                }
            }
        }
    }
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
    BcFeedbackBanner(
        tone = BcFeedbackTone.ENCOURAGING,
        title = stringResource(R.string.learn_no_voice_title),
        message = stringResource(R.string.learn_no_voice_message, languageName),
        modifier = modifier,
    )
}

@Composable
private fun LearnRow(
    item: LearnItem,
    onEvent: (LearnEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BcPrimaryButton(
            text = item.letter,
            onClick = { onEvent(LearnEvent.LetterClicked(item)) },
            modifier = Modifier.width(BcDimens.tileMinSize),
        )
        BcOutlineButton(
            text = item.explanation,
            onClick = { onEvent(LearnEvent.ExplanationClicked(item)) },
            modifier = Modifier.weight(1f),
        )
    }
}

private val previewItems = englishLearnLetters.take(3).map {
    LearnItem(letter = it.letter, explanation = it.explanation(AppLanguage.ENGLISH))
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
