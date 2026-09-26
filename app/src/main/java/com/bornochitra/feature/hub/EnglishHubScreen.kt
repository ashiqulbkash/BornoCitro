package com.bornochitra.feature.hub

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme

private val FillCells = listOf("a", GAME_BLANK, "c")
private const val LISTEN_GLYPH = "A"

/**
 * The English hub: small and capital letters, Math, which the Bangla hub also opens, fill in the
 * blanks with the English categories, and learning the letters by listening.
 */
@Composable
fun EnglishHubScreen(
    onBackClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HubViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    EnglishHubContent(
        state = state,
        onBackClick = onBackClick,
        onEnglishSmallClick = onEnglishSmallClick,
        onEnglishCapitalClick = onEnglishCapitalClick,
        onMathClick = onMathClick,
        onFillBlanksClick = onFillBlanksClick,
        onLearnClick = onLearnClick,
        modifier = modifier,
    )
}

@Composable
private fun EnglishHubContent(
    state: HubState,
    onBackClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bcColors = BcTheme.colors
    HubScaffold(
        title = stringResource(R.string.title_english),
        onBackClick = onBackClick,
        onFillBlanksClick = onFillBlanksClick,
        onLearnClick = onLearnClick,
        fillCells = FillCells,
        listenGlyph = {
            ListenGamePicture(
                glyph = LISTEN_GLYPH,
                tileColor = bcColors.english,
                glyphColor = bcColors.englishContainer,
                speakerColor = bcColors.english,
            )
        },
        modifier = modifier,
    ) {
        HubCategoryRow(ExerciseType.ENGLISH_SMALL, AppLanguage.ENGLISH, state.progressOf(ExerciseType.ENGLISH_SMALL), onEnglishSmallClick)
        HubCategoryRow(ExerciseType.ENGLISH_CAPITAL, AppLanguage.ENGLISH, state.progressOf(ExerciseType.ENGLISH_CAPITAL), onEnglishCapitalClick)
        HubCategoryRow(ExerciseType.MATH, AppLanguage.ENGLISH, state.progressOf(ExerciseType.MATH), onMathClick)
        Text(
            text = stringResource(R.string.hub_math_shared, ExerciseType.MATH.label()),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = BcSpacing.xxs),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnglishHubScreenPreview() {
    BornoChitraTheme {
        EnglishHubContent(
            state = HubState(categoryProgress = mapOf(ExerciseType.ENGLISH_SMALL to 0.08f)),
            onBackClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
            onFillBlanksClick = {},
            onLearnClick = {},
        )
    }
}
