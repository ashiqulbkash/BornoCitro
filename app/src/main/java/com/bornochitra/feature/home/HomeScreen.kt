package com.bornochitra.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun HomeScreen(
    onBanglaClick: () -> Unit,
    onEnglishClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onBanglaClick = onBanglaClick,
        onEnglishClick = onEnglishClick,
        onDrawingClick = onDrawingClick,
        onContinueClick = onContinueClick,
        onMenuClick = onMenuClick,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onBanglaClick: () -> Unit,
    onEnglishClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_home), onMenuClick = onMenuClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            Text(text = stringResource(R.string.home_welcome), style = MaterialTheme.typography.headlineMedium)

            val continueExerciseId = state.continueExerciseId
            if (continueExerciseId != null) {
                BcPrimaryButton(
                    text = stringResource(R.string.home_continue),
                    onClick = { onContinueClick(continueExerciseId) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            BcPrimaryButton(text = stringResource(R.string.title_bangla), onClick = onBanglaClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = stringResource(R.string.title_english), onClick = onEnglishClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.DRAWING.label(), onClick = onDrawingClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, name = "With an exercise to continue")
@Composable
private fun HomeScreenPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(continueExerciseId = "vowel-e"),
            onBanglaClick = {},
            onEnglishClick = {},
            onDrawingClick = {},
            onContinueClick = {},
            onMenuClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Nothing to continue")
@Composable
private fun HomeScreenEmptyPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(),
            onBanglaClick = {},
            onEnglishClick = {},
            onDrawingClick = {},
            onContinueClick = {},
            onMenuClick = {},
        )
    }
}
