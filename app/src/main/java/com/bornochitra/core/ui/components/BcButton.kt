package com.bornochitra.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** Normal buttons are 56dp tall for children; [SMALL] is the 44dp variant. */
enum class BcButtonSize { NORMAL, SMALL }

/** The screen's one main action (design/DESIGN_SPEC.md 4, Buttons). */
@Composable
fun BcPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    size: BcButtonSize = BcButtonSize.NORMAL,
) {
    val colors = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = size.height),
        enabled = enabled,
        shape = BcShapes.full,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            disabledContainerColor = colors.surfaceContainerHigh,
            disabledContentColor = colors.outline,
        ),
        contentPadding = size.padding,
    ) {
        ButtonContent(text, icon, trailingIcon, size)
    }
}

/** The second most likely step, such as Next. */
@Composable
fun BcTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    size: BcButtonSize = BcButtonSize.NORMAL,
) {
    val colors = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = size.height),
        shape = BcShapes.full,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer,
        ),
        contentPadding = size.padding,
    ) {
        ButtonContent(text, icon, trailingIcon, size)
    }
}

/** Tools next to the canvas, such as Reset and Help. */
@Composable
fun BcOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    size: BcButtonSize = BcButtonSize.NORMAL,
) {
    val colors = MaterialTheme.colorScheme
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = size.height),
        enabled = enabled,
        shape = BcShapes.full,
        border = BorderStroke(BcDimens.tileBorder, colors.outlineVariant),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.primary,
            disabledContentColor = colors.outline,
        ),
        contentPadding = size.padding,
    ) {
        ButtonContent(text, icon, trailingIcon = null, size = size)
    }
}

/** Quiet exits, such as View progress or Later. */
@Composable
fun BcTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: BcButtonSize = BcButtonSize.NORMAL,
    colors: ButtonColors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = size.height),
        shape = BcShapes.full,
        colors = colors,
        contentPadding = PaddingValues(horizontal = BcSpacing.m),
    ) {
        ButtonContent(text, icon = null, trailingIcon = null, size = size)
    }
}

@Composable
private fun RowScope.ButtonContent(
    text: String,
    @DrawableRes icon: Int?,
    @DrawableRes trailingIcon: Int?,
    size: BcButtonSize,
) {
    if (icon != null) {
        Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(BcDimens.buttonIcon))
        Spacer(Modifier.width(BcSpacing.xs))
    }
    Text(
        text = text,
        style = if (size == BcButtonSize.SMALL) BcType.labelLargeSmall else MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
    )
    if (trailingIcon != null) {
        Spacer(Modifier.width(BcSpacing.xs))
        Icon(painter = painterResource(trailingIcon), contentDescription = null, modifier = Modifier.size(BcDimens.buttonIcon))
    }
}

private val BcButtonSize.height get() = if (this == BcButtonSize.SMALL) BcDimens.buttonHeightSmall else BcDimens.buttonHeight

private val BcButtonSize.padding
    get() = PaddingValues(horizontal = if (this == BcButtonSize.SMALL) BcDimens.buttonPaddingSmall else BcDimens.buttonPadding)

@Preview(showBackground = true)
@Composable
private fun BcButtonPreview() {
    BornoChitraTheme {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
            BcPrimaryButton(text = "আবার অনুশীলন করো", onClick = {}, icon = R.drawable.bc_ic_reset)
            BcTonalButton(text = "পরেরটি: ই", onClick = {}, trailingIcon = R.drawable.bc_ic_arrow_forward)
            BcOutlineButton(text = "আবার শুরু", onClick = {}, icon = R.drawable.bc_ic_reset)
            BcTextButton(text = "অগ্রগতি দেখো", onClick = {})
            BcPrimaryButton(text = "পরের সারি", onClick = {}, enabled = false)
            BcPrimaryButton(text = "নামাও", onClick = {}, icon = R.drawable.bc_ic_download, size = BcButtonSize.SMALL)
        }
    }
}
