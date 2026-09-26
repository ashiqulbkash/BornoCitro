package com.bornochitra.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The design's colours that have no Material 3 role (design/DESIGN_SPEC.md 1.2). Subject identity:
 * Bangla = primary, English = [english], Drawing = secondary.
 */
@Immutable
data class BcColors(
    /** Pencil orange: "continue here" ring and flag, Continue play button, filled-answer cells, mastery segments. */
    val accent: Color,
    val onAccent: Color,
    val star: Color,
    /** Outline around a filled star; transparent where the star needs none. */
    val starOutline: Color,
    val starEmpty: Color,
    /** Tracing guide dots and line. */
    val guide: Color,
    val english: Color,
    val englishContainer: Color,
    val onEnglishContainer: Color,
    /** Progress track drawn on a coloured container, such as a subject card. */
    val onContainerTrack: Color,
    /** Light cards carry a soft shadow; dark cards use a 1dp outline instead. */
    val cardShadow: Color,
    val cardHasShadow: Boolean,
)

internal val LightBcColors = BcColors(
    accent = AccentLight,
    onAccent = OnAccent,
    star = StarLight,
    starOutline = StarOutlineLight,
    starEmpty = OutlineVariantLight,
    guide = GuideLight,
    english = EnglishLight,
    englishContainer = EnglishContainerLight,
    onEnglishContainer = OnEnglishContainerLight,
    onContainerTrack = OnContainerTrackLight,
    cardShadow = CardShadowLight,
    cardHasShadow = true,
)

internal val DarkBcColors = BcColors(
    accent = AccentDark,
    onAccent = OnAccent,
    star = StarDark,
    starOutline = Color.Transparent,
    starEmpty = StarEmptyDark,
    guide = GuideDark,
    english = EnglishDark,
    englishContainer = EnglishContainerDark,
    onEnglishContainer = OnEnglishContainerDark,
    onContainerTrack = OnContainerTrackDark,
    cardShadow = Color.Transparent,
    cardHasShadow = false,
)

internal val LocalBcColors = staticCompositionLocalOf { LightBcColors }
