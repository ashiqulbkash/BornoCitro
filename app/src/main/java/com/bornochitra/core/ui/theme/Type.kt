package com.bornochitra.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.bornochitra.R

/** Display, titles, buttons and marks (Bengali + Latin). Only the weights the design uses are bundled. */
val BcBalooFontFamily = FontFamily(
    Font(R.font.baloo_da_2_medium, FontWeight.Medium),
    Font(R.font.baloo_da_2_semibold, FontWeight.SemiBold),
    Font(R.font.baloo_da_2_bold, FontWeight.Bold),
    Font(R.font.baloo_da_2_extrabold, FontWeight.ExtraBold),
)

/** Body text, captions and labels inside text. */
val BcHindFontFamily = FontFamily(
    Font(R.font.hind_siliguri_regular, FontWeight.Normal),
    Font(R.font.hind_siliguri_medium, FontWeight.Medium),
    Font(R.font.hind_siliguri_semibold, FontWeight.SemiBold),
    Font(R.font.hind_siliguri_bold, FontWeight.Bold),
)

/**
 * Andika, a school print font, for English small letters: its single-storey a, g and y are the shapes
 * children are taught, and the English guides are derived from this exact face (see
 * `EnglishSmallExercises.kt`). The design also uses it for the word "English" and the "Aa" mark. Only the
 * bold weight is bundled.
 */
val BcLatinLetterFontFamily = FontFamily(Font(R.font.andika_bold, FontWeight.Bold))

/**
 * Inter, for English capital letters and math, whose guides are derived from this exact face (see
 * `EnglishCapitalExercises.kt` and `MathExercises.kt`). `inter.ttf` is variable, so both of its axes are pinned to the
 * instance the guides were read off — Bold at the default optical size — rather than left to the
 * platform. Only that weight is declared, so every letter style draws the same glyph.
 */
@OptIn(ExperimentalTextApi::class)
val BcLatinCapitalFontFamily = FontFamily(
    Font(
        R.font.inter,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight),
            FontVariation.Setting("opsz", 14f),
        ),
    ),
)

/**
 * The platform's Noto Sans Bengali, for Bengali letters and numbers the child learns. Their guides were derived
 * from the phone's own system font at Bold, so this is deliberately not a bundled file (a newer Noto redraws ন).
 */
val BcBengaliLetterFontFamily: FontFamily = FontFamily.Default

// Every line box is exactly the design's line height, as in the design's CSS. Baloo and Hind are ~1.65em tall, so
// without Tight the first and last lines grow back to the font's own height and every gap drifts from the spec.
// Tall matras may reach past the box, which the design's line heights and gaps already allow for; Text only clips
// when it overflows, so they are still drawn.
private val BengaliLineHeight = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.Both,
    mode = LineHeightStyle.Mode.Tight,
)

private fun style(
    family: FontFamily,
    weight: FontWeight,
    size: TextUnit,
    lineHeight: TextUnit,
) = TextStyle(
    fontFamily = family,
    fontWeight = weight,
    fontSize = size,
    lineHeight = lineHeight,
    lineHeightStyle = BengaliLineHeight,
)

private fun baloo(weight: FontWeight, size: TextUnit, lineHeight: TextUnit) = style(BcBalooFontFamily, weight, size, lineHeight)

private fun hind(weight: FontWeight, size: TextUnit, lineHeight: TextUnit) = style(BcHindFontFamily, weight, size, lineHeight)

// design/DESIGN_SPEC.md 2. The roles the design does not name keep their M3 sizes in the design's fonts.
val BcTypography = Typography(
    displayLarge = baloo(FontWeight.ExtraBold, 57.sp, 68.sp),
    displayMedium = baloo(FontWeight.ExtraBold, 45.sp, 56.sp),
    displaySmall = baloo(FontWeight.ExtraBold, 40.sp, 50.sp),
    headlineLarge = baloo(FontWeight.Bold, 32.sp, 42.sp),
    headlineMedium = baloo(FontWeight.Bold, 28.sp, 38.sp),
    headlineSmall = baloo(FontWeight.Bold, 24.sp, 32.sp),
    titleLarge = baloo(FontWeight.Bold, 22.sp, 30.sp),
    titleMedium = baloo(FontWeight.Bold, 18.sp, 24.sp),
    titleSmall = baloo(FontWeight.Bold, 16.sp, 22.sp),
    bodyLarge = hind(FontWeight.Medium, 18.sp, 29.sp),
    bodyMedium = hind(FontWeight.Medium, 16.sp, 25.sp),
    bodySmall = hind(FontWeight.Medium, 14.sp, 20.sp),
    labelLarge = baloo(FontWeight.Bold, 17.sp, 20.sp),
    labelMedium = baloo(FontWeight.Bold, 15.sp, 20.sp),
    labelSmall = hind(FontWeight.SemiBold, 13.sp, 16.sp),
)

/** The design's custom styles (`BcType`). Letter styles are resized by [BcLetterStyle] for learning characters. */
object BcType {
    val letterTile = baloo(FontWeight.Bold, 40.sp, 46.sp)
    val letterPractice = baloo(FontWeight.ExtraBold, 56.sp, 70.sp)
    val letterResult = baloo(FontWeight.ExtraBold, 72.sp, 80.sp)
    val scoreXL = baloo(FontWeight.ExtraBold, 56.sp, 62.sp)
    val scoreL = baloo(FontWeight.ExtraBold, 48.sp, 53.sp)
    val percentHeader = baloo(FontWeight.ExtraBold, 30.sp, 33.sp)
    val flag = baloo(FontWeight.Bold, 11.sp, 14.sp)

    /** Small button labels. */
    val labelLargeSmall = baloo(FontWeight.Bold, 15.sp, 20.sp)

    /** Smallest size a button label shrinks to when a large font scale would wrap it: the spec's minimum text. */
    val buttonLabelMinSize = 13.sp

    /**
     * Smallest size the Progress detail tile status shrinks to. It is below the spec's 11sp because it is only
     * reached at large font scales, where the scale lifts it back: "শিখে ফেলেছ" must fit one line of a 4-column tile.
     */
    val statusTinyMinSize = 9.sp

    /** Segmented-switch labels. */
    val segment = baloo(FontWeight.Bold, 17.sp, 20.sp)

    /** Banner text. */
    val banner = hind(FontWeight.Medium, 15.sp, 22.sp)

    /** Compact banner text (dialog info banner, Progress detail). */
    val bannerSmall = hind(FontWeight.Medium, 14.sp, 20.sp)

    /** Captions and chips that the design sets at weight 600. */
    val bodySmallStrong = hind(FontWeight.SemiBold, 14.sp, 20.sp)

    /** Percentages on subject cards: bodySmall at 700. */
    val bodySmallBold = hind(FontWeight.Bold, 14.sp, 20.sp)

    /** Instructions: bodyLarge at 600. */
    val instruction = hind(FontWeight.SemiBold, 18.sp, 29.sp)

    /** Compact activity-row names. */
    val rowNameCompact = baloo(FontWeight.Bold, 16.sp, 22.sp)

    /** Tile status on the 4-column Progress detail grid; 12sp per the user's correction d. */
    val statusTiny = hind(FontWeight.SemiBold, 12.sp, 16.sp)

    /** Listen example text: 18sp Hind 600. */
    val example = hind(FontWeight.SemiBold, 18.sp, 26.sp)

    /** The glyph on a Home subject card's 72dp tile. */
    val subjectGlyph = baloo(FontWeight.ExtraBold, 38.sp, 44.sp)

    /** The glyph on a 52dp activity-row lead. */
    val rowLead = baloo(FontWeight.ExtraBold, 26.sp, 32.sp)

    /** The Bangla hub's "+" lead, drawn larger than a letter so the thin sign reads as clearly. */
    val rowLeadSign = baloo(FontWeight.ExtraBold, 30.sp, 36.sp)

    /** The letter on Home's Continue card tile: letterPractice at 44sp. */
    val letterContinue = baloo(FontWeight.ExtraBold, 44.sp, 52.sp)

    /** The glyph on a 44dp compact-row lead. */
    val rowLeadCompact = baloo(FontWeight.ExtraBold, 22.sp, 28.sp)

    /** The two stacked glyphs of the "অ a" mark on onboarding's letters card. */
    val stackedMark = baloo(FontWeight.ExtraBold, 18.sp, 20.sp)

    /** Game-card mini cells. */
    val gameCell = baloo(FontWeight.Bold, 18.sp, 24.sp)

    /** Fill-the-blanks sequence cells. */
    val sequenceCell = baloo(FontWeight.Bold, 28.sp, 36.sp)

    /** Difficulty mini-pattern cells. */
    val miniCell = baloo(FontWeight.Bold, 13.sp, 16.sp)

    /** Listen and learn letter buttons. */
    val learnLetter = baloo(FontWeight.ExtraBold, 30.sp, 38.sp)

    /** Progress detail tile characters. */
    val detailTile = baloo(FontWeight.Bold, 32.sp, 40.sp)

    /** The app logo's "অ" inside the 36dp top-bar circle. */
    val logoSmall = baloo(FontWeight.ExtraBold, 20.sp, 24.sp)
}
