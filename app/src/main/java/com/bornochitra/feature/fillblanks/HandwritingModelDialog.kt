package com.bornochitra.feature.fillblanks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcBanner
import com.bornochitra.core.ui.components.BcBannerKind
import com.bornochitra.core.ui.components.BcDialog
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTextButton
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * The gate in front of fill-in-the-blanks (design/DESIGN_SPEC.md 5.14): offers the one-time download, asks for
 * the internet when the device is offline, shows the check or the download running, and offers a retry if it
 * failed. Hiding it while downloading leaves the download running.
 */
@Composable
fun HandwritingModelDialog(
    dialog: ModelDialog,
    onEvent: (FillBlanksGateEvent) -> Unit,
) {
    val look = dialog.look()
    val onDismiss = { onEvent(FillBlanksGateEvent.ModelDialogDismissed) }
    BcDialog(
        onDismissRequest = onDismiss,
        icon = look.icon,
        iconContainerColor = look.iconContainer,
        iconTint = look.iconTint,
        title = stringResource(look.title),
        body = stringResource(look.body),
        extra = when (dialog) {
            ModelDialog.OFFER -> {
                {
                    BcBanner(
                        kind = BcBannerKind.INFO,
                        text = stringResource(R.string.model_internet_note),
                        compact = true,
                        icon = R.drawable.bc_ic_wifi,
                    )
                }
            }
            ModelDialog.CHECKING, ModelDialog.DOWNLOADING -> {
                { LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), trackColor = MaterialTheme.colorScheme.surfaceContainerHigh) }
            }
            ModelDialog.NO_INTERNET,
            ModelDialog.WAITING_FOR_INTERNET,
            ModelDialog.FAILED,
            -> null
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val isBusy = dialog == ModelDialog.CHECKING || dialog == ModelDialog.DOWNLOADING ||
                dialog == ModelDialog.WAITING_FOR_INTERNET
            BcTextButton(
                text = if (isBusy) stringResource(R.string.model_hide) else stringResource(R.string.model_not_now),
                onClick = onDismiss,
            )
            when (dialog) {
                ModelDialog.OFFER -> BcPrimaryButton(
                    text = stringResource(R.string.model_download),
                    onClick = { onEvent(FillBlanksGateEvent.DownloadModelsClicked) },
                    icon = R.drawable.bc_ic_download,
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
        }
    }
}

private data class ModelDialogLook(
    @DrawableRes val icon: Int,
    val iconContainer: Color,
    val iconTint: Color,
    @StringRes val title: Int,
    @StringRes val body: Int,
)

@Composable
private fun ModelDialog.look(): ModelDialogLook {
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    return when (this) {
        ModelDialog.CHECKING -> ModelDialogLook(
            R.drawable.bc_ic_download, scheme.primaryContainer, scheme.primary, R.string.model_checking_title, R.string.model_checking,
        )
        ModelDialog.OFFER -> ModelDialogLook(
            R.drawable.bc_ic_download, scheme.primaryContainer, scheme.primary, R.string.model_offer_title, R.string.model_offer,
        )
        ModelDialog.DOWNLOADING -> ModelDialogLook(
            R.drawable.bc_ic_download, scheme.primaryContainer, scheme.primary, R.string.model_downloading_title, R.string.model_downloading,
        )
        ModelDialog.NO_INTERNET -> ModelDialogLook(
            R.drawable.bc_ic_wifi_off, colors.englishContainer, colors.onEnglishContainer,
            R.string.model_no_internet_title, R.string.model_no_internet,
        )
        ModelDialog.WAITING_FOR_INTERNET -> ModelDialogLook(
            R.drawable.bc_ic_wifi_off, colors.englishContainer, colors.onEnglishContainer,
            R.string.model_waiting_for_internet_title, R.string.model_waiting_for_internet,
        )
        ModelDialog.FAILED -> ModelDialogLook(
            R.drawable.bc_ic_reset, scheme.errorContainer, scheme.onErrorContainer, R.string.model_failed_title, R.string.model_failed,
        )
    }
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
