package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val SHEET_HANDLE_ALPHA = 0.6f

/** An icon on a round tonal background: sheet headers (48) and dialogs (56). */
@Composable
fun BcIconCircle(
    @DrawableRes icon: Int,
    containerColor: Color,
    iconTint: Color,
    size: Dp,
    iconSize: Dp,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(size).background(containerColor, BcShapes.full), contentAlignment = Alignment.Center) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = iconTint, modifier = Modifier.size(iconSize))
    }
}

/**
 * The design's bottom sheet (design/DESIGN_SPEC.md 4, Bottom sheet): surfaceContainerLow, 28dp top corners, a
 * 36×4 handle, the scrim behind. Dismissed by drag, scrim or Back.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BcBottomSheet(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = BcShapes.sheet,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        scrimColor = MaterialTheme.colorScheme.scrim,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = BcSpacing.snug)
                    .size(width = BcDimens.sheetHandleWidth, height = BcDimens.sheetHandleHeight)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = SHEET_HANDLE_ALPHA), BcShapes.full),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.m, bottom = BcSpacing.xl - BcSpacing.xxs)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
            content = content,
        )
    }
}

/** A sheet's header: a 48dp tonal icon circle, the title and an optional caption. */
@Composable
fun BcSheetHeader(
    @DrawableRes icon: Int,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BcIconCircle(icon = icon, containerColor = iconContainerColor, iconTint = iconTint, size = BcDimens.sheetIcon, iconSize = BcDimens.icon)
        Column {
            Text(text = title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
            if (caption != null) {
                Text(text = caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * The design's dialog (design/DESIGN_SPEC.md 4, Dialog): a 56dp icon circle, centred title and body, any
 * [extra] content, then the [buttons] — stacked full width, or a right-aligned row.
 */
@Composable
fun BcDialog(
    onDismissRequest: () -> Unit,
    @DrawableRes icon: Int,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    extra: (@Composable ColumnScope.() -> Unit)? = null,
    buttons: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        // The window's own dim is the scrim: black at the scheme scrim's strength, not the platform's lighter default.
        val scrimAlpha = MaterialTheme.colorScheme.scrim.alpha
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { dialogWindow?.setDimAmount(scrimAlpha) }
        Surface(
            modifier = modifier.fillMaxWidth().padding(horizontal = BcSpacing.l),
            shape = BcShapes.xxl,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(BcSpacing.l),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BcIconCircle(
                    icon = icon,
                    containerColor = iconContainerColor,
                    iconTint = iconTint,
                    size = BcDimens.dialogIcon,
                    iconSize = BcDimens.dialogIconGlyph,
                )
                Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.tight), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics { heading() },
                    )
                    Text(
                        text = body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                extra?.invoke(this)
                buttons()
            }
        }
    }
}

/**
 * Asks before Reset wipes what the child has written (design/DESIGN_SPEC.md 5.11). Only shown when there is ink;
 * with an empty canvas Reset starts over at once.
 */
@Composable
fun BcRestartDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    BcDialog(
        onDismissRequest = onDismiss,
        icon = R.drawable.bc_ic_reset,
        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
        title = stringResource(R.string.restart_title),
        body = stringResource(R.string.restart_message),
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
            BcPrimaryButton(text = stringResource(R.string.restart_confirm), onClick = onConfirm, modifier = Modifier.fillMaxWidth())
            BcTextButton(text = stringResource(R.string.restart_dismiss), onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}

/** Pager dots: 8dp dots, the current one a 24dp primary pill (design/DESIGN_SPEC.md 4, Page indicator). */
@Composable
fun BcPageIndicator(
    count: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
        repeat(count) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) BcDimens.pageDotActive else BcDimens.pageDot, label = "pageDot")
            Box(
                modifier = Modifier
                    .height(BcDimens.pageDot)
                    .width(width)
                    .background(
                        if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        BcShapes.full,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcOverlaysPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.m)) {
            BcSheetHeader(
                icon = R.drawable.bc_ic_globe,
                iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.primary,
                title = "অ্যাপের ভাষা",
                caption = "App language",
            )
            BcPageIndicator(count = 2, current = 0)
        }
    }
}
