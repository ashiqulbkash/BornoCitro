package com.bornochitra.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BcPrimaryLight,
    onPrimary = BcOnPrimaryLight,
    background = BcBackgroundLight,
    onBackground = BcOnBackgroundLight,
)

private val DarkColors = darkColorScheme(
    primary = BcPrimaryDark,
    onPrimary = BcOnPrimaryDark,
    background = BcBackgroundDark,
    onBackground = BcOnBackgroundDark,
)

@Composable
fun BornoChitraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = BcTypography,
        content = content,
    )
}
