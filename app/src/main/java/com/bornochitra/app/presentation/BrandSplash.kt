package com.bornochitra.app.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bornochitra.R
import com.bornochitra.core.ui.components.BcAppLogo
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

// The system splash draws the icon at 288dp and shows its centre 192dp circle (the adaptive icon's
// visible part); drawing it the same way at the screen centre makes the handover seamless.
private val SplashIconVisibleSize = 192.dp
private const val MessageFadeInMillis = 300
private const val MessageHoldMillis = 900
private const val FadeOutMillis = 300

/**
 * The branded frame that follows the system splash: the same icon, now with the app's message.
 * It sits over the start destination, which is already composed beneath it, then fades away and
 * calls [onFinished]. Touches are held back while it is visible.
 */
@Composable
fun BrandSplash(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val messageAlpha = remember { Animatable(0f) }
    val splashAlpha = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        messageAlpha.animateTo(1f, tween(MessageFadeInMillis))
        splashAlpha.animateTo(0f, tween(FadeOutMillis, delayMillis = MessageHoldMillis))
        onFinished()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = splashAlpha.value }
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) awaitPointerEvent().changes.forEach { it.consume() }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        BcAppLogo(size = SplashIconVisibleSize)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Text(
                text = stringResource(R.string.splash_message),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BcSpacing.m, vertical = BcSpacing.l)
                    .graphicsLayer { alpha = messageAlpha.value },
            )
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
