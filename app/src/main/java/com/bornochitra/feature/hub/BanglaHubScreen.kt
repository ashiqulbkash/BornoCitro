package com.bornochitra.feature.hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

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
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_bangla), onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            BcPrimaryButton(text = ExerciseType.VOWEL.label(), onClick = onVowelsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.CONSONANT.label(), onClick = onConsonantsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.BANGLA_NUMBER.label(), onClick = onBanglaNumbersClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.MATH.label(), onClick = onMathClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = stringResource(R.string.title_fill_blanks), onClick = onFillBlanksClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = stringResource(R.string.title_learn), onClick = onLearnClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BanglaHubScreenPreview() {
    BornoChitraTheme {
        BanglaHubScreen(
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
