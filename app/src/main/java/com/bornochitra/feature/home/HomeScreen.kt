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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

@Composable
fun HomeScreen(
    onBanglaClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBanglaClick = onBanglaClick,
        onEnglishSmallClick = onEnglishSmallClick,
        onEnglishCapitalClick = onEnglishCapitalClick,
        onMathClick = onMathClick,
        onDrawingClick = onDrawingClick,
        onContinueClick = onContinueClick,
        onMenuClick = onMenuClick,
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
    onBanglaClick: () -> Unit,
    onEnglishSmallClick: () -> Unit,
    onEnglishCapitalClick: () -> Unit,
    onMathClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    state.modelDialog?.let { dialog -> HandwritingModelDialog(dialog = dialog, onEvent = onEvent) }
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
            BcPrimaryButton(text = ExerciseType.ENGLISH_SMALL.label(), onClick = onEnglishSmallClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.ENGLISH_CAPITAL.label(), onClick = onEnglishCapitalClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.MATH.label(), onClick = onMathClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = ExerciseType.DRAWING.label(), onClick = onDrawingClick, modifier = Modifier.fillMaxWidth())
            BcPrimaryButton(text = stringResource(R.string.title_fill_blanks), onClick = { onEvent(HomeEvent.FillBlanksClicked) }, modifier = Modifier.fillMaxWidth())
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
        ModelDialog.CHECKING -> stringResource(R.string.model_checking)
        ModelDialog.OFFER -> stringResource(R.string.model_offer)
        ModelDialog.NO_INTERNET -> stringResource(R.string.model_no_internet)
        ModelDialog.DOWNLOADING -> stringResource(R.string.model_downloading)
        ModelDialog.WAITING_FOR_INTERNET -> stringResource(R.string.model_waiting_for_internet)
        ModelDialog.FAILED -> stringResource(R.string.model_failed)
    }
    val isBusy = dialog == ModelDialog.CHECKING || dialog == ModelDialog.DOWNLOADING ||
        dialog == ModelDialog.WAITING_FOR_INTERNET
    AlertDialog(
        onDismissRequest = { onEvent(HomeEvent.ModelDialogDismissed) },
        title = { Text(text = stringResource(R.string.model_dialog_title)) },
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
                    text = stringResource(R.string.model_download),
                    onClick = { onEvent(HomeEvent.DownloadModelsClicked) },
                )
                ModelDialog.FAILED -> BcPrimaryButton(
                    text = stringResource(R.string.model_try_again),
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
                text = if (isBusy) stringResource(R.string.model_hide) else stringResource(R.string.model_not_now),
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

@Preview(showBackground = true, name = "With an exercise to continue")
@Composable
private fun HomeScreenPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(continueExerciseId = "vowel-e"),
            onEvent = {},
            onBanglaClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
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
            onEvent = {},
            onBanglaClick = {},
            onEnglishSmallClick = {},
            onEnglishCapitalClick = {},
            onMathClick = {},
            onDrawingClick = {},
            onContinueClick = {},
            onMenuClick = {},
        )
    }
}
