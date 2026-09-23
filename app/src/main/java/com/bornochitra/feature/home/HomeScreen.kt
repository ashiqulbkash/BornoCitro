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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.ui.components.BcLabeledProgress
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun HomeScreen(
    onVowelsClick: () -> Unit,
    onConsonantsClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onBanglaNumbersClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onProgressClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onVowelsClick = onVowelsClick,
        onConsonantsClick = onConsonantsClick,
        onEnglishSmallClick = onEnglishSmallClick,
        onEnglishCapitalClick = onEnglishCapitalClick,
        onMathClick = onMathClick,
        onBanglaNumbersClick = onBanglaNumbersClick,
        onDrawingClick = onDrawingClick,
        onProgressClick = onProgressClick,
        onContinueClick = onContinueClick,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onVowelsClick: () -> Unit,
    onConsonantsClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onBanglaNumbersClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onProgressClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "বর্ণচিত্র") },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            Text(text = "Welcome!", style = MaterialTheme.typography.headlineMedium)

            val continueExerciseId = state.continueExerciseId
            if (continueExerciseId != null) {
                BcPrimaryButton(
                    text = "Continue Learning",
                    onClick = { onContinueClick(continueExerciseId) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            BcPrimaryButton(text = "স্বরবর্ণ", onClick = onVowelsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "ব্যঞ্জনবর্ণ", onClick = onConsonantsClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "ছোট হাতের অক্ষর", onClick = onEnglishSmallClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "বড় হাতের অক্ষর", onClick = onEnglishCapitalClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "সংখ্যা ও চিহ্ন", onClick = onMathClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "বাংলা সংখ্যা", onClick = onBanglaNumbersClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = "আঁকা", onClick = onDrawingClick, modifier = Modifier.fillMaxWidth())

            Text(text = "Your Progress", style = MaterialTheme.typography.titleLarge)
            BcLabeledProgress(label = "Overall", progress = state.overallProgress)
            BcLabeledProgress(label = "স্বরবর্ণ", progress = state.vowelProgress)
            BcLabeledProgress(label = "ব্যঞ্জনবর্ণ", progress = state.consonantProgress)
            BcLabeledProgress(label = "ছোট হাতের অক্ষর", progress = state.englishSmallProgress)
            BcLabeledProgress(label = "বড় হাতের অক্ষর", progress = state.englishCapitalProgress)
            BcLabeledProgress(label = "সংখ্যা ও চিহ্ন", progress = state.mathProgress)
            BcLabeledProgress(label = "বাংলা সংখ্যা", progress = state.banglaNumberProgress)
            BcLabeledProgress(label = "আঁকা", progress = state.drawingProgress)

            BcSecondaryButton(text = "View Full Progress", onClick = onProgressClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, name = "With progress")
@Composable
private fun HomeScreenPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(
                overallProgress = 0.8f,
                vowelProgress = 1f,
                consonantProgress = 0.6f,
                englishSmallProgress = 0.2f,
                englishCapitalProgress = 0.1f,
                mathProgress = 0.3f,
                banglaNumberProgress = 0.5f,
                drawingProgress = 0.4f,
                continueExerciseId = "vowel-e",
            ),
            onVowelsClick = {},
            onConsonantsClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
            onBanglaNumbersClick = {},
            onDrawingClick = {},
            onProgressClick = {},
            onContinueClick = {},
        )
    }
}

@Preview(showBackground = true, name = "No progress yet")
@Composable
private fun HomeScreenEmptyPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(),
            onVowelsClick = {},
            onConsonantsClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
            onBanglaNumbersClick = {},
            onDrawingClick = {},
            onProgressClick = {},
            onContinueClick = {},
        )
    }
}
