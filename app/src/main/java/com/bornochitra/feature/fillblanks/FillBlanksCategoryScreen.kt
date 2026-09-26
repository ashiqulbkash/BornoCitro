package com.bornochitra.feature.fillblanks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcChoiceColumn
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcSectionLabel
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme
import com.bornochitra.feature.hub.HubCategoryRow
import com.bornochitra.feature.hub.HubState
import com.bornochitra.feature.hub.HubViewModel

/**
 * The categories whose items form an ordered sequence (drawings do not), split by the hub that opens the
 * picker. Math is in both, as it is in both hubs.
 */
internal fun sequenceCategories(language: AppLanguage): List<ExerciseType> = when (language) {
    AppLanguage.BANGLA -> listOf(
        ExerciseType.VOWEL,
        ExerciseType.CONSONANT,
        ExerciseType.BANGLA_NUMBER,
        ExerciseType.MATH,
    )
    AppLanguage.ENGLISH -> listOf(
        ExerciseType.ENGLISH_SMALL,
        ExerciseType.ENGLISH_CAPITAL,
        ExerciseType.MATH,
    )
}

/** The mini sequence on a difficulty card: which items it shows, with null for a blank. */
private fun difficultyPattern(language: AppLanguage, difficulty: Difficulty): List<String?> {
    val items = when (language) {
        AppLanguage.BANGLA -> listOf("অ", "আ", "ই", "ঈ", "উ")
        AppLanguage.ENGLISH -> listOf("a", "b", "c", "d", "e")
    }
    val blanks = when (difficulty) {
        // As in BlankSequenceGenerator, only the hard sequences put blanks side by side.
        Difficulty.BEGINNER, Difficulty.INTERMEDIATE -> setOf(1, 3)
        Difficulty.ADVANCED -> setOf(1, 2)
    }
    return items.mapIndexed { index, item -> item.takeUnless { index in blanks } }
}

/**
 * Picks the category and difficulty for fill-in-the-blanks (design/DESIGN_SPEC.md 5.13), from the categories of
 * the [language] whose hub opened it, each with its progress. The difficulty choice is plain UI state held until
 * a category is tapped.
 */
@Composable
fun FillBlanksCategoryScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onCategoryClick: (type: ExerciseType, difficulty: Difficulty) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HubViewModel = hiltViewModel(),
) {
    val progress by viewModel.uiState.collectAsStateWithLifecycle()
    var difficulty by rememberSaveable { mutableStateOf(Difficulty.BEGINNER) }
    FillBlanksCategoryContent(
        language = language,
        progress = progress,
        difficulty = difficulty,
        onDifficultyChange = { difficulty = it },
        onBackClick = onBackClick,
        onCategoryClick = { type -> onCategoryClick(type, difficulty) },
        modifier = modifier,
    )
}

@Composable
private fun FillBlanksCategoryContent(
    language: AppLanguage,
    progress: HubState,
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    onBackClick: () -> Unit,
    onCategoryClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = stringResource(R.string.title_fill_blanks), onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xxs, bottom = BcSpacing.screen),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.s),
        ) {
            BcSectionLabel(text = stringResource(R.string.fill_blanks_difficulty))
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
            ) {
                DifficultyChoice(
                    language = language,
                    difficulty = Difficulty.BEGINNER,
                    label = stringResource(R.string.fill_blanks_easy),
                    caption = stringResource(R.string.fill_blanks_easy_caption),
                    selected = difficulty == Difficulty.BEGINNER,
                    onClick = { onDifficultyChange(Difficulty.BEGINNER) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                DifficultyChoice(
                    language = language,
                    difficulty = Difficulty.ADVANCED,
                    label = stringResource(R.string.fill_blanks_hard),
                    caption = stringResource(R.string.fill_blanks_hard_caption),
                    selected = difficulty == Difficulty.ADVANCED,
                    onClick = { onDifficultyChange(Difficulty.ADVANCED) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }

            BcSectionLabel(text = stringResource(R.string.fill_blanks_choose_category), modifier = Modifier.padding(top = BcSpacing.s))
            sequenceCategories(language).forEach { type ->
                HubCategoryRow(type = type, language = language, progress = progress.progressOf(type), onClick = { onCategoryClick(type) })
            }
        }
    }
}

@Composable
private fun DifficultyChoice(
    language: AppLanguage,
    difficulty: Difficulty,
    label: String,
    caption: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BcChoiceColumn(label = label, selected = selected, onClick = onClick, modifier = modifier) {
        MiniPattern(cells = difficultyPattern(language, difficulty))
        Text(text = caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** A difficulty's picture: five small cells, the blanks empty. Decorative; the caption says the same. */
@Composable
private fun MiniPattern(cells: List<String?>) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
        horizontalArrangement = Arrangement.spacedBy(BcSpacing.xxs),
    ) {
        cells.forEach { cell ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(BcDimens.miniCellHeight)
                    .background(scheme.surfaceContainerLowest, BcShapes.miniCell)
                    .border(BcDimens.outline, scheme.outlineVariant, BcShapes.miniCell),
                contentAlignment = Alignment.Center,
            ) {
                if (cell != null) BcLetterText(text = cell, style = BcType.miniCell, color = scheme.onSurface)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FillBlanksCategoryScreenPreview() {
    BornoChitraTheme {
        FillBlanksCategoryContent(
            language = AppLanguage.BANGLA,
            progress = HubState(categoryProgress = mapOf(ExerciseType.VOWEL to 0.27f)),
            difficulty = Difficulty.BEGINNER,
            onDifficultyChange = {},
            onBackClick = {},
            onCategoryClick = {},
        )
    }
}
