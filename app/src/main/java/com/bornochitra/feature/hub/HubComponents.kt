package com.bornochitra.feature.hub

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcActivityRow
import com.bornochitra.core.ui.components.BcGameCard
import com.bornochitra.core.ui.components.BcGlyphTile
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcRowLead
import com.bornochitra.core.ui.components.BcSectionLabel
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType

/** The blank in a fill-the-blanks game card's mini sequence. */
internal const val GAME_BLANK = "?"

/**
 * A hub (design/DESIGN_SPEC.md 5.6–5.7): "লিখে শেখো" with the category [rows], then "খেলে শেখো" with the fill
 * the blanks and listen game cards side by side.
 */
@Composable
internal fun HubScaffold(
    title: String,
    onBackClick: () -> Unit,
    onFillBlanksClick: () -> Unit,
    onLearnClick: () -> Unit,
    fillCells: List<String>,
    listenGlyph: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    rows: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = title, onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.s),
        ) {
            BcSectionLabel(text = stringResource(R.string.hub_learn_by_writing))
            rows()
            BcSectionLabel(text = stringResource(R.string.hub_learn_by_playing), modifier = Modifier.padding(top = BcSpacing.s))
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
            ) {
                BcGameCard(
                    name = stringResource(R.string.title_fill_blanks),
                    onClick = onFillBlanksClick,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) { FillGamePicture(cells = fillCells) }
                BcGameCard(
                    name = stringResource(R.string.title_learn),
                    onClick = onLearnClick,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) { listenGlyph() }
            }
        }
    }
}

/** A hub's subject colours: Bangla's are primary, English's are english (design/DESIGN_SPEC.md 1.2). */
@Immutable
internal data class HubColors(
    val leadContainer: Color,
    val leadContent: Color,
    val progress: Color,
)

@Composable
internal fun hubColors(language: AppLanguage): HubColors = when (language) {
    AppLanguage.BANGLA -> {
        val scheme = MaterialTheme.colorScheme
        HubColors(leadContainer = scheme.primaryContainer, leadContent = scheme.onPrimaryContainer, progress = scheme.primary)
    }
    AppLanguage.ENGLISH -> {
        val colors = BcTheme.colors
        HubColors(leadContainer = colors.englishContainer, leadContent = colors.onEnglishContainer, progress = colors.english)
    }
}

/**
 * The glyph that leads a category's row, and its style. Math has one in each hub: a sign in the Bangla hub,
 * "1+" in the English one, which needs the smaller style to fit.
 */
private fun hubLead(type: ExerciseType, language: AppLanguage): Pair<String, TextStyle> = when (type) {
    ExerciseType.VOWEL -> "অ" to BcType.rowLead
    ExerciseType.CONSONANT -> "ক" to BcType.rowLead
    ExerciseType.BANGLA_NUMBER -> "১" to BcType.rowLead
    ExerciseType.ENGLISH_SMALL -> "a" to BcType.rowLead
    ExerciseType.ENGLISH_CAPITAL -> "A" to BcType.rowLead
    ExerciseType.MATH -> when (language) {
        AppLanguage.BANGLA -> "+" to BcType.rowLeadSign
        AppLanguage.ENGLISH -> "1+" to BcType.rowLeadCompact
    }
    ExerciseType.DRAWING -> error("Drawing is not a hub category")
}

/**
 * A row that opens the [type] category, led by its glyph in the [language] hub's colours. The hubs, the
 * fill-the-blanks picker and, [compact], the Progress tab use it.
 */
@Composable
internal fun HubCategoryRow(
    type: ExerciseType,
    language: AppLanguage,
    progress: Float,
    onClick: () -> Unit,
    compact: Boolean = false,
) {
    val colors = hubColors(language)
    val (glyph, glyphStyle) = hubLead(type, language)
    BcActivityRow(name = type.label(), progress = progress, progressColor = colors.progress, onClick = onClick, compact = compact) {
        BcRowLead(
            glyph = glyph,
            containerColor = colors.leadContainer,
            contentColor = colors.leadContent,
            compact = compact,
            style = if (compact) BcType.rowLeadCompact else glyphStyle,
        )
    }
}

/** The fill-the-blanks game's picture: a mini sequence whose [GAME_BLANK] cell is the one to fill. */
@Composable
private fun FillGamePicture(cells: List<String>) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.clearAndSetSemantics {},
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.tight),
    ) {
        cells.forEach { cell ->
            val isBlank = cell == GAME_BLANK
            val border = if (isBlank) {
                BorderStroke(BcDimens.selectedBorder, scheme.primary)
            } else {
                BorderStroke(BcDimens.tileBorder, scheme.outlineVariant)
            }
            Box(
                modifier = Modifier
                    .size(BcDimens.gameCell)
                    .background(if (isBlank) scheme.primaryContainer else scheme.surfaceContainerLowest, BcShapes.cell)
                    .border(border, BcShapes.cell),
                contentAlignment = Alignment.Center,
            ) {
                if (isBlank) {
                    Text(text = cell, style = BcType.gameCell, color = scheme.primary)
                } else {
                    BcLetterText(text = cell, style = BcType.gameCell, color = scheme.onSurface)
                }
            }
        }
    }
}

/** The listen game's picture: [glyph] on a small tile and a speaker, in the hub's colours. */
@Composable
internal fun ListenGamePicture(
    glyph: String,
    tileColor: Color,
    glyphColor: Color,
    speakerColor: Color,
) {
    Row(
        modifier = Modifier.clearAndSetSemantics {},
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BcGlyphTile(
            glyph = glyph,
            size = BcDimens.gameGlyphTile,
            shape = BcShapes.smallTile,
            containerColor = tileColor,
            contentColor = glyphColor,
            style = BcType.rowLeadCompact,
        )
        Icon(
            painter = painterResource(R.drawable.bc_ic_speaker),
            contentDescription = null,
            tint = speakerColor,
            modifier = Modifier.size(BcDimens.gameSpeaker),
        )
    }
}
