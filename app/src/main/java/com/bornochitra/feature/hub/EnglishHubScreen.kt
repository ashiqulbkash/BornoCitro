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
 * The English hub: small and capital letters, Math, which the Bangla hub also opens, and fill in the
 * blanks with the English categories.
 */
@Composable
fun EnglishHubScreen(
    onBackClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_english), onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            BcPrimaryButton(text = ExerciseType.ENGLISH_SMALL.label(), onClick = onEnglishSmallClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.ENGLISH_CAPITAL.label(), onClick = onEnglishCapitalClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.MATH.label(), onClick = onMathClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = stringResource(R.string.title_fill_blanks), onClick = onFillBlanksClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnglishHubScreenPreview() {
    BornoChitraTheme {
        EnglishHubScreen(
            onBackClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
            onFillBlanksClick = {},
        )
    }
}
