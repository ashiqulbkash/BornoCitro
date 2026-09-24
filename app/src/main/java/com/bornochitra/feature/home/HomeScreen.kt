package com.bornochitra.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    onFillBlanksClick: () -> Unit,
    onProgressClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
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

    LaunchedEffect(state.openFillBlanks) {
        if (state.openFillBlanks) {
            viewModel.onEvent(HomeEvent.FillBlanksOpened)
            onFillBlanksClick()
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
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
    state.modelDialog?.let { dialog -> HandwritingModelDialog(dialog = dialog, onEvent = onEvent) }
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
            BcPrimaryButton(text = "শূন্যস্থান পূরণ", onClick = { onEvent(HomeEvent.FillBlanksClicked) }, modifier = Modifier.fillMaxWidth())

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

/**
 * The gate in front of fill-in-the-blanks: offers the one-time download, asks for the internet when
 * the device is offline, shows the check or the download running, and offers a retry if it failed.
 * Hiding it while downloading leaves the download running.
 */
@Composable
private fun HandwritingModelDialog(
    dialog: ModelDialog,
    onEvent: (HomeEvent) -> Unit,
) {
    val message = when (dialog) {
        ModelDialog.CHECKING -> "Checking for the models."
        ModelDialog.OFFER ->
            "শূন্যস্থান পূরণ reads your handwriting. It needs a one-time download of about 50 MB, " +
                "and opens once the download is done."
        ModelDialog.NO_INTERNET ->
            "শূন্যস্থান পূরণ needs its handwriting models, and your internet is off. " +
                "Turn on Wi-Fi or mobile data to download them."
        ModelDialog.DOWNLOADING -> "Downloading. This can take a minute."
        ModelDialog.WAITING_FOR_INTERNET ->
            "The internet went off. Turn on Wi-Fi or mobile data and the download carries on."
        ModelDialog.FAILED -> "The download did not finish. Try again."
    }
    val isBusy = dialog == ModelDialog.CHECKING || dialog == ModelDialog.DOWNLOADING ||
        dialog == ModelDialog.WAITING_FOR_INTERNET
    AlertDialog(
        onDismissRequest = { onEvent(HomeEvent.ModelDialogDismissed) },
        title = { Text(text = "Download handwriting models") },
        text = {
            if (isBusy) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BcSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator()
                    Text(text = message)
                }
            } else {
                Text(text = message)
            }
        },
        confirmButton = {
            when (dialog) {
                ModelDialog.OFFER -> BcPrimaryButton(
                    text = "Download",
                    onClick = { onEvent(HomeEvent.DownloadModelsClicked) },
                )
                ModelDialog.FAILED -> BcPrimaryButton(
                    text = "Try again",
                    onClick = { onEvent(HomeEvent.DownloadModelsClicked) },
                )
                ModelDialog.CHECKING,
                ModelDialog.NO_INTERNET,
                ModelDialog.DOWNLOADING,
                ModelDialog.WAITING_FOR_INTERNET,
                -> Unit
            }
        },
        dismissButton = {
            BcSecondaryButton(
                text = if (isBusy) "Hide" else "Not now",
                onClick = { onEvent(HomeEvent.ModelDialogDismissed) },
            )
        },
    )
}

@Preview(showBackground = true, name = "Download offer")
@Composable
private fun HandwritingModelDialogPreview() {
    BornoChitraTheme {
        HandwritingModelDialog(dialog = ModelDialog.OFFER, onEvent = {})
    }
}

@Preview(showBackground = true, name = "No internet")
@Composable
private fun HandwritingModelDialogOfflinePreview() {
    BornoChitraTheme {
        HandwritingModelDialog(dialog = ModelDialog.NO_INTERNET, onEvent = {})
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
            onEvent = {},
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
            onEvent = {},
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
