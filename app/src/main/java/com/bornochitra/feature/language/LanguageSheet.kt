package com.bornochitra.feature.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.ui.components.BcBottomSheet
import com.bornochitra.core.ui.components.BcChoiceRow
import com.bornochitra.core.ui.components.BcGlyphTile
import com.bornochitra.core.ui.components.BcSheetHeader
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** Whether the language cards are the large onboarding ones or the smaller sheet ones. */
enum class LanguageChoiceSize(val minHeight: Dp, val lead: Dp) {
    LARGE(BcDimens.choiceMinHeight, BcDimens.choiceLead),
    SMALL(BcDimens.choiceMinHeightSmall, BcDimens.choiceLeadSmall),
}

/**
 * The two language cards, each named in its own language so it can be found from either. Choosing one applies it
 * at once and keeps it (the same setting as the Grown-ups switch).
 */
@Composable
fun LanguageChoices(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    size: LanguageChoiceSize,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    val markStyle = if (size == LanguageChoiceSize.LARGE) BcType.learnLetter else BcType.rowLead
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
        BcChoiceRow(
            label = stringResource(R.string.language_bangla),
            selected = language == AppLanguage.BANGLA,
            onClick = { onLanguageSelected(AppLanguage.BANGLA) },
            minHeight = size.minHeight,
        ) {
            BcGlyphTile(
                glyph = stringResource(R.string.logo_letter),
                size = size.lead,
                shape = if (size == LanguageChoiceSize.LARGE) BcShapes.md else BcShapes.smallTile,
                containerColor = scheme.primary,
                contentColor = scheme.onPrimary,
                style = markStyle,
                learning = false,
            )
        }
        BcChoiceRow(
            label = stringResource(R.string.language_english),
            labelStyle = MaterialTheme.typography.titleLarge.copy(fontFamily = BcLatinLetterFontFamily),
            selected = language == AppLanguage.ENGLISH,
            onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
            minHeight = size.minHeight,
        ) {
            BcGlyphTile(
                glyph = stringResource(R.string.language_english_mark),
                size = size.lead,
                shape = if (size == LanguageChoiceSize.LARGE) BcShapes.md else BcShapes.smallTile,
                containerColor = colors.englishContainer,
                contentColor = colors.onEnglishContainer,
                style = markStyle.copy(fontFamily = BcLatinLetterFontFamily),
                learning = false,
            )
        }
    }
}

/** The language sheet over Home (design/DESIGN_SPEC.md 5.4): header, the two language cards and a reassuring note. */
@Composable
fun LanguageSheet(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismissRequest: () -> Unit,
) {
    BcBottomSheet(onDismissRequest = onDismissRequest) {
        LanguageSheetContent(language = language, onLanguageSelected = onLanguageSelected)
    }
}

@Composable
private fun LanguageSheetContent(
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
) {
    BcSheetHeader(
        icon = R.drawable.bc_ic_globe,
        iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
        iconTint = MaterialTheme.colorScheme.primary,
        title = stringResource(R.string.language_title),
        caption = stringResource(R.string.language_title_english),
    )
    LanguageChoices(language = language, onLanguageSelected = onLanguageSelected, size = LanguageChoiceSize.SMALL)
    Text(
        text = stringResource(R.string.language_sheet_note),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
private fun LanguageSheetContentPreview() {
    BornoChitraTheme {
        Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.m)) {
            LanguageSheetContent(language = AppLanguage.BANGLA, onLanguageSelected = {})
        }
    }
}
