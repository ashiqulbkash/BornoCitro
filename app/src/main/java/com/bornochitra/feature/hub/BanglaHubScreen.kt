package com.bornochitra.feature.hub

import androidx.compose.material3.MaterialTheme
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
import com.bornochitra.core.ui.theme.BornoChitraTheme

private val FillCells = listOf("অ", GAME_BLANK, "ই")
private const val LISTEN_GLYPH = "অ"

/**
 * The Bangla hub: the Bangla practice categories, Math, which the English hub also opens, fill in the
 * blanks with the Bangla categories, and learning the vowels and consonants by listening.
 */
@Composable
fun BanglaHubScreen(
    onBackClick: () -> Unit,
    onVowelsClick: () -> Unit,
    onConsonantsClick: () -> Unit,
    onBanglaNumbersClick: () -> Unit,
    onMathClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HubViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BanglaHubContent(
        state = state,
        onBackClick = onBackClick,
        onVowelsClick = onVowelsClick,
        onConsonantsClick = onConsonantsClick,
        onBanglaNumbersClick = onBanglaNumbersClick,
        onMathClick = onMathClick,
        onFillBlanksClick = onFillBlanksClick,
        onLearnClick = onLearnClick,
        modifier = modifier,
    )
}

@Composable
private fun BanglaHubContent(
    state: HubState,
    onBackClick: () -> Unit,
    onVowelsClick: () -> Unit,
    onConsonantsClick: () -> Unit,
    onBanglaNumbersClick: () -> Unit,
    onMathClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    HubScaffold(
        title = stringResource(R.string.title_bangla),
        onBackClick = onBackClick,
        onFillBlanksClick = onFillBlanksClick,
        onLearnClick = onLearnClick,
        fillCells = FillCells,
        listenGlyph = {
            ListenGamePicture(glyph = LISTEN_GLYPH, tileColor = scheme.primary, glyphColor = scheme.onPrimary, speakerColor = scheme.primary)
        },
        modifier = modifier,
    ) {
        HubCategoryRow(ExerciseType.VOWEL, AppLanguage.BANGLA, state.progressOf(ExerciseType.VOWEL), onVowelsClick)
        HubCategoryRow(ExerciseType.CONSONANT, AppLanguage.BANGLA, state.progressOf(ExerciseType.CONSONANT), onConsonantsClick)
        HubCategoryRow(ExerciseType.BANGLA_NUMBER, AppLanguage.BANGLA, state.progressOf(ExerciseType.BANGLA_NUMBER), onBanglaNumbersClick)
        HubCategoryRow(ExerciseType.MATH, AppLanguage.BANGLA, state.progressOf(ExerciseType.MATH), onMathClick)
    }
}

@Preview(showBackground = true)
@Composable
private fun BanglaHubScreenPreview() {
    BornoChitraTheme {
        BanglaHubContent(
            state = HubState(categoryProgress = mapOf(ExerciseType.VOWEL to 0.27f)),
            onBackClick = {},
            onVowelsClick = {},
            onConsonantsClick = {},
            onBanglaNumbersClick = {},
            onMathClick = {},
            onFillBlanksClick = {},
            onLearnClick = {},
        )
    }
}
