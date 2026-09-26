package com.bornochitra.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.R
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BornoChitraTheme

// An adaptive icon's layers are 108 units across, of which the centre 72 are visible: 1.5 times the visible circle.
private const val ICON_LAYER_TO_VISIBLE = 1.5f

/** The launcher icon — purple circle, white "অ", dotted line and pencil — drawn as a [size] circle. */
@Composable
fun BcAppLogo(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(size).clip(BcShapes.full), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier.requiredSize(size * ICON_LAYER_TO_VISIBLE),
        )
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.requiredSize(size * ICON_LAYER_TO_VISIBLE),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BcAppLogoPreview() {
    BornoChitraTheme {
        BcAppLogo(size = BcDimens.logoOnboarding)
    }
}
