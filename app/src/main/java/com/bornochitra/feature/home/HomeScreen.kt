package com.bornochitra.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcExerciseShape
import com.bornochitra.core.ui.components.BcGlyphTile
import com.bornochitra.core.ui.components.BcHomeTopAppBar
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcStarSize
import com.bornochitra.core.ui.components.BcStars
import com.bornochitra.core.ui.components.BcSubjectCard
import com.bornochitra.core.ui.components.BcSurface
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcLatinLetterFontFamily
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val CONTINUE_META_ALPHA = 0.9f
private const val REMAINING_SEGMENT_ALPHA = 0.28f

@Composable
fun HomeScreen(
    onBanglaClick: () -> Unit,
    onEnglishClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    languageName: String,
    onLanguageClick: () -> Unit,
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onBanglaClick = onBanglaClick,
        onEnglishClick = onEnglishClick,
        onDrawingClick = onDrawingClick,
        onContinueClick = onContinueClick,
        languageName = languageName,
        onLanguageClick = onLanguageClick,
        navigationBar = navigationBar,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onBanglaClick: () -> Unit,
    onEnglishClick: () -> Unit,
    onDrawingClick: () -> Unit,
    onContinueClick: (exerciseId: String) -> Unit,
    languageName: String,
    onLanguageClick: () -> Unit,
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcHomeTopAppBar(languageName = languageName, onLanguageClick = onLanguageClick) },
        bottomBar = navigationBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = BcSpacing.screen, end = BcSpacing.screen, top = BcSpacing.xs, bottom = BcSpacing.m),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
        ) {
            Column {
                Text(text = stringResource(R.string.home_welcome), style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = stringResource(R.string.home_question),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            state.continueItem?.let { item ->
                ContinueCard(item = item, onClick = { onContinueClick(item.id) })
            }

            SubjectCards(
                state = state,
                onBanglaClick = onBanglaClick,
                onEnglishClick = onEnglishClick,
                onDrawingClick = onDrawingClick,
            )
        }
    }
}

@Composable
private fun SubjectCards(
    state: HomeState,
    onBanglaClick: () -> Unit,
    onEnglishClick: () -> Unit,
    onDrawingClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(BcSpacing.s)) {
        BcSubjectCard(
            name = stringResource(R.string.title_bangla),
            caption = stringResource(R.string.home_bangla_caption),
            progress = state.banglaProgress,
            containerColor = scheme.primaryContainer,
            contentColor = scheme.onPrimaryContainer,
            progressColor = scheme.primary,
            onClick = onBanglaClick,
        ) {
            SubjectMark(mark = stringResource(R.string.home_mark_bangla), containerColor = scheme.primary, contentColor = scheme.onPrimary)
        }
        BcSubjectCard(
            name = stringResource(R.string.title_english),
            caption = stringResource(R.string.home_english_caption),
            progress = state.englishProgress,
            containerColor = colors.englishContainer,
            contentColor = colors.onEnglishContainer,
            progressColor = colors.english,
            onClick = onEnglishClick,
        ) {
            SubjectMark(
                mark = stringResource(R.string.home_mark_english),
                containerColor = colors.english,
                contentColor = colors.englishContainer,
                isLatin = true,
            )
        }
        BcSubjectCard(
            name = ExerciseType.DRAWING.label(),
            caption = stringResource(R.string.home_drawing_caption),
            progress = state.drawingProgress,
            containerColor = scheme.secondaryContainer,
            contentColor = scheme.onSecondaryContainer,
            progressColor = scheme.secondary,
            onClick = onDrawingClick,
        ) {
            BcGlyphTile(size = BcDimens.subjectGlyphTile, shape = BcShapes.lg, containerColor = scheme.secondary) {
                Icon(
                    painter = painterResource(R.drawable.bc_ic_shapes),
                    contentDescription = null,
                    tint = scheme.onSecondary,
                    modifier = Modifier.size(BcDimens.subjectIcon),
                )
            }
        }
    }
}

/** A subject's mark: "অ", or "Aa" in Andika. It names the subject, so it keeps the design's fonts (D1). */
@Composable
private fun SubjectMark(
    mark: String,
    containerColor: Color,
    contentColor: Color,
    isLatin: Boolean = false,
) {
    BcGlyphTile(
        glyph = mark,
        size = BcDimens.subjectGlyphTile,
        shape = BcShapes.lg,
        containerColor = containerColor,
        contentColor = contentColor,
        style = if (isLatin) BcType.subjectGlyph.copy(fontFamily = BcLatinLetterFontFamily) else BcType.subjectGlyph,
        learning = false,
    )
}

/**
 * The redesigned "শেখা চালিয়ে যাও" (design/DESIGN_SPEC.md 4, Continue card): the letter to practise next, its
 * category and stars, and how many more finishes — or which score — it needs to be learned.
 */
@Composable
private fun ContinueCard(item: ContinueItem, onClick: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val colors = BcTheme.colors
    val title = exerciseTitle(item.id, item.title)
    BcSurface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = BcShapes.xxl,
        color = scheme.primary,
        contentColor = scheme.onPrimary,
    ) {
        Column(modifier = Modifier.padding(BcSpacing.m), verticalArrangement = Arrangement.spacedBy(BcSpacing.roomy)) {
            Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.roomy), verticalAlignment = Alignment.CenterVertically) {
                ContinueTile(item = item, title = title)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(BcSpacing.xxs)) {
                    Text(text = stringResource(R.string.home_continue), style = MaterialTheme.typography.titleLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.tight), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (item.stars > 0) stringResource(R.string.home_continue_meta, item.type.label()) else item.type.label(),
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onPrimary.copy(alpha = CONTINUE_META_ALPHA),
                        )
                        if (item.stars > 0) BcStars(count = item.stars, size = BcStarSize.card)
                    }
                }
                Box(
                    modifier = Modifier.size(BcDimens.continuePlay).background(colors.accent, BcShapes.full),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.bc_ic_play),
                        contentDescription = null,
                        tint = colors.onAccent,
                        modifier = Modifier.size(BcDimens.continuePlayIcon),
                    )
                }
            }
            Text(
                text = if (item.remainingCompletions > 0) {
                    pluralStringResource(R.plurals.home_mastery_remaining, item.remainingCompletions, item.remainingCompletions, title)
                } else {
                    stringResource(R.string.home_mastery_score, item.minBestScorePercent, title)
                },
                style = BcType.bodySmallStrong,
            )
            MasterySegments(filled = item.completedSegments, total = item.requiredCompletions)
        }
    }
}

/** The letter to continue on a white tile with the accent ring; a drawing shows its shape instead. */
@Composable
private fun ContinueTile(item: ContinueItem, title: String) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(BcDimens.continueTile)
            .background(scheme.onPrimary, BcShapes.lg)
            .border(BorderStroke(BcDimens.continueRing, BcTheme.colors.accent), BcShapes.lg),
        contentAlignment = Alignment.Center,
    ) {
        if (item.type == ExerciseType.DRAWING) {
            BcExerciseShape(strokes = item.strokes, color = scheme.primary)
        } else {
            BcLetterText(text = title, style = BcType.letterContinue, color = scheme.primary)
        }
    }
}

/** One segment per finish that mastery needs: finished ones in accent, the rest faint. */
@Composable
private fun MasterySegments(filled: Int, total: Int) {
    val accent = BcTheme.colors.accent
    val remaining = MaterialTheme.colorScheme.onPrimary.copy(alpha = REMAINING_SEGMENT_ALPHA)
    val shape = RoundedCornerShape(BcDimens.masterySegmentCorner)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(BcSpacing.tight)) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(BcDimens.masterySegmentHeight)
                    .background(if (index < filled) accent else remaining, shape),
            )
        }
    }
}

private val previewContinueItem = ContinueItem(
    id = "vowel-aa",
    title = "আ",
    type = ExerciseType.VOWEL,
    strokes = emptyList(),
    stars = 3,
    completedSegments = 1,
    requiredCompletions = 3,
    remainingCompletions = 2,
    minBestScorePercent = 80,
)

@Preview(showBackground = true, name = "With an exercise to continue")
@Composable
private fun HomeScreenPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(continueItem = previewContinueItem, banglaProgress = 0.05f, englishProgress = 0.04f),
            onBanglaClick = {},
            onEnglishClick = {},
            onDrawingClick = {},
            onContinueClick = {},
            languageName = "বাংলা",
            onLanguageClick = {},
            navigationBar = {},
        )
    }
}

@Preview(showBackground = true, name = "Nothing to continue")
@Composable
private fun HomeScreenEmptyPreview() {
    BornoChitraTheme {
        HomeContent(
            state = HomeState(),
            onBanglaClick = {},
            onEnglishClick = {},
            onDrawingClick = {},
            onContinueClick = {},
            languageName = "বাংলা",
            onLanguageClick = {},
            navigationBar = {},
        )
    }
}
