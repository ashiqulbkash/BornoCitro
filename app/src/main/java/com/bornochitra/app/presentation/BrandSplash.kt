package com.bornochitra.app.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcAppLogo
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlin.math.roundToInt

// The system splash draws the icon at 288dp and shows its centre 192dp circle (the adaptive icon's
// visible part) at the screen centre. The logo starts there, at that size, so the handover is seamless.
private val SystemSplashIconVisibleSize = 192.dp
private const val SettleMillis = 300
private const val HoldMillis = 900
private const val FadeOutMillis = 300

/**
 * The branded frame that follows the system splash (design/DESIGN_SPEC.md 5.1): the logo, the wordmark and the
 * tagline, centred together. The logo takes over from the system splash's icon and settles into place as the words
 * fade in; then the frame fades away and calls [onFinished]. It sits over the start destination, which is already
 * composed beneath it. Touches are held back while it is visible.
 */
@Composable
fun BrandSplash(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 0 = the system splash's icon, words hidden; 1 = the design's layout. A static preview shows the design.
    val settled = remember { Animatable(0f) }
    val splashAlpha = remember { Animatable(1f) }
    val isPreview = LocalInspectionMode.current
    LaunchedEffect(Unit) {
        settled.animateTo(1f, tween(SettleMillis))
        splashAlpha.animateTo(0f, tween(FadeOutMillis, delayMillis = HoldMillis))
        onFinished()
    }

    Layout(
        content = {
            BcAppLogo(size = BcDimens.logoSplash)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.splash_tagline),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = splashAlpha.value }
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) awaitPointerEvent().changes.forEach { it.consume() }
                }
            }
            .padding(BcSpacing.l),
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val logo = measurables[0].measure(loose)
        val words = measurables[1].measure(loose)
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        layout(width, height) {
            val progress = if (isPreview) 1f else settled.value
            val groupTop = (height - (logo.height + BcDimens.splashGap.roundToPx() + words.height)) / 2
            val logoCentreY = lerp(height / 2f, groupTop + logo.height / 2f, progress)
            val startScale = SystemSplashIconVisibleSize / BcDimens.logoSplash
            logo.placeWithLayer(x = (width - logo.width) / 2, y = (logoCentreY - logo.height / 2f).roundToInt()) {
                val scale = lerp(startScale, 1f, progress)
                scaleX = scale
                scaleY = scale
            }
            words.placeWithLayer(
                x = (width - words.width) / 2,
                y = groupTop + logo.height + BcDimens.splashGap.roundToPx(),
            ) { alpha = progress }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandSplashPreview() {
    BornoChitraTheme {
        BrandSplash(onFinished = {})
    }
}
