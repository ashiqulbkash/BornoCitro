package com.bornochitra.feature.fillblanks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcOutlineButton
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * The gate in front of fill-in-the-blanks: offers the one-time download, asks for the internet when
 * the device is offline, shows the check or the download running, and offers a retry if it failed.
 * Hiding it while downloading leaves the download running.
 */
@Composable
fun HandwritingModelDialog(
    dialog: ModelDialog,
    onEvent: (FillBlanksGateEvent) -> Unit,
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
        onDismissRequest = { onEvent(FillBlanksGateEvent.ModelDialogDismissed) },
        title = { Text(text = stringResource(R.string.model_dialog_title)) },
        text = {
            if (isBusy) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(BcSpacing.m),
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
                    onClick = { onEvent(FillBlanksGateEvent.DownloadModelsClicked) },
                )
                ModelDialog.FAILED -> BcPrimaryButton(
                    text = stringResource(R.string.model_try_again),
                    onClick = { onEvent(FillBlanksGateEvent.DownloadModelsClicked) },
                )
                ModelDialog.CHECKING,
                ModelDialog.NO_INTERNET,
                ModelDialog.DOWNLOADING,
                ModelDialog.WAITING_FOR_INTERNET,
                -> Unit
            }
        },
        dismissButton = {
            BcOutlineButton(
                text = if (isBusy) stringResource(R.string.model_hide) else stringResource(R.string.model_not_now),
                onClick = { onEvent(FillBlanksGateEvent.ModelDialogDismissed) },
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
