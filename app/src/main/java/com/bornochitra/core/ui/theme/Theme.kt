package com.bornochitra.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BcPrimaryLight,
    onPrimary = BcOnPrimaryLight,
    primaryContainer = BcPrimaryContainerLight,
    onPrimaryContainer = BcOnPrimaryContainerLight,
    secondary = BcSecondaryLight,
    onSecondary = BcOnSecondaryLight,
    secondaryContainer = BcSecondaryContainerLight,
    onSecondaryContainer = BcOnSecondaryContainerLight,
    tertiary = BcTertiaryLight,
    onTertiary = BcOnTertiaryLight,
    tertiaryContainer = BcTertiaryContainerLight,
    onTertiaryContainer = BcOnTertiaryContainerLight,
    background = BcBackgroundLight,
    onBackground = BcOnBackgroundLight,
    surface = BcSurfaceLight,
    onSurface = BcOnSurfaceLight,
    surfaceVariant = BcSurfaceVariantLight,
    onSurfaceVariant = BcOnSurfaceVariantLight,
    outline = BcOutlineLight,
    error = BcErrorLight,
    onError = BcOnErrorLight,
)

private val DarkColors = darkColorScheme(
    primary = BcPrimaryDark,
    onPrimary = BcOnPrimaryDark,
    primaryContainer = BcPrimaryContainerDark,
    onPrimaryContainer = BcOnPrimaryContainerDark,
    secondary = BcSecondaryDark,
    onSecondary = BcOnSecondaryDark,
    secondaryContainer = BcSecondaryContainerDark,
    onSecondaryContainer = BcOnSecondaryContainerDark,
    tertiary = BcTertiaryDark,
    onTertiary = BcOnTertiaryDark,
    tertiaryContainer = BcTertiaryContainerDark,
    onTertiaryContainer = BcOnTertiaryContainerDark,
    background = BcBackgroundDark,
    onBackground = BcOnBackgroundDark,
    surface = BcSurfaceDark,
    onSurface = BcOnSurfaceDark,
    surfaceVariant = BcSurfaceVariantDark,
    onSurfaceVariant = BcOnSurfaceVariantDark,
    outline = BcOutlineDark,
    error = BcErrorDark,
    onError = BcOnErrorDark,
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
        shapes = BcShapes,
        content = content,
    )
}
