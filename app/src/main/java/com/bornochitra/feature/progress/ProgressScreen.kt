package com.bornochitra.feature.progress

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.components.BcActivityRow
import com.bornochitra.core.ui.components.BcBanner
import com.bornochitra.core.ui.components.BcBannerKind
import com.bornochitra.core.ui.components.BcDoneChip
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseShape
import com.bornochitra.core.ui.components.BcExerciseTile
import com.bornochitra.core.ui.components.BcFlatCard
import com.bornochitra.core.ui.components.BcGlyphTile
import com.bornochitra.core.ui.components.BcHeaderProgressCard
import com.bornochitra.core.ui.components.BcLearnedChip
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcProgressBar
import com.bornochitra.core.ui.components.BcStars
import com.bornochitra.core.ui.components.BcTileState
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.components.progressPercent
import com.bornochitra.core.ui.components.toTileState
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcShapes
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
import com.bornochitra.core.ui.theme.BornoChitraTheme
import com.bornochitra.feature.category.CategoryGridState
import com.bornochitra.feature.category.CategoryTileItem
import com.bornochitra.feature.hub.HubCategoryRow

private const val DETAIL_COLUMNS = 4
private const val HEADER_KEY = "header"

/**
 * How much has been learned overall and per category (design/DESIGN_SPEC.md 5.19), and behind each category's row
 * how far each of its exercises has got (5.20). Everything here comes from persisted progress, so it survives a
 * restart.
 */
@Composable
fun ProgressScreen(
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProgressContent(
        state = state,
        onEvent = viewModel::onEvent,
        navigationBar = navigationBar,
        modifier = modifier,
    )
}

@Composable
private fun ProgressContent(
    state: ProgressState,
    onEvent: (ProgressEvent) -> Unit,
    navigationBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val openCategory = state.categories.firstOrNull { it.type == state.openCategory }

    // Inside a section, back closes the section rather than leaving the screen.
    BackHandler(enabled = openCategory != null) { onEvent(ProgressEvent.CategoryClosed) }

    Scaffold(
        modifier = modifier,
        topBar = {
            BcTopAppBar(
                title = openCategory?.type?.label() ?: stringResource(R.string.title_progress),
                // The tab itself has no back arrow; an open category is a step below it and closes with Back.
                onBackClick = if (openCategory != null) ({ onEvent(ProgressEvent.CategoryClosed) }) else null,
            )
        },
        // The bar belongs to the tab; an open category is an inner screen, which has none (design/DESIGN_SPEC.md 6).
        bottomBar = { if (openCategory == null) navigationBar() },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when {
            state.error != null -> BcEmptyState(
                title = stringResource(R.string.progress_unavailable),
                message = stringResource(state.error),
                modifier = contentModifier,
            )

            state.isLoading -> Column(
                modifier = contentModifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }

            openCategory != null -> CategoryDetail(
                category = openCategory,
                masteryCompletions = state.masteryCompletions,
                masteryScorePercent = state.masteryScorePercent,
                modifier = contentModifier,
            )

            state.categories.isEmpty() -> BcEmptyState(
                title = stringResource(R.string.progress_empty),
                message = stringResource(R.string.progress_empty_message),
                modifier = contentModifier,
            )

            else -> ProgressOverview(
                state = state,
                onCategoryClick = { onEvent(ProgressEvent.CategoryOpened(it)) },
                modifier = contentModifier,
            )
        }
    }
}

/** The overall card, then a compact row per category that opens its detail. */
@Composable
private fun ProgressOverview(
    state: ProgressState,
    onCategoryClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = BcSpacing.screen, end = BcSpacing.screen, bottom = BcSpacing.s),
        verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
    ) {
        OverallCard(progress = state.overallProgress, learnedCount = state.learnedCount, doneCount = state.doneCount)
        state.categories.forEach { category ->
            CategoryRow(category = category, onClick = { onCategoryClick(category.type) })
        }
    }
}

/** "সব মিলিয়ে" with its percentage, bar, and the learned and finished counts (a count of 0 hides its chip). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OverallCard(
    progress: Float,
    learnedCount: Int,
    doneCount: Int,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val label = stringResource(R.string.overall)
    val percent = progressPercent(progress)
    val description = stringResource(R.string.labeled_progress_description, label, percent)
    BcFlatCard(modifier = modifier.fillMaxWidth(), color = scheme.primaryContainer) {
        Column(
            modifier = Modifier.padding(horizontal = BcSpacing.m, vertical = BcSpacing.roomy),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = scheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f).semantics { heading() },
                )
                Text(
                    text = stringResource(R.string.percent, percent),
                    style = BcType.percentHeader,
                    color = scheme.onPrimaryContainer,
                    modifier = Modifier.clearAndSetSemantics { contentDescription = description },
                )
            }
            BcProgressBar(
                progress = progress,
                height = BcDimens.progressBarHeader,
                fillColor = scheme.primary,
                trackColor = BcTheme.colors.onContainerTrack,
            )
            if (learnedCount > 0 || doneCount > 0) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(BcSpacing.xs),
                ) {
                    if (learnedCount > 0) BcLearnedChip(count = learnedCount, containerColor = scheme.surfaceContainerLowest)
                    if (doneCount > 0) BcDoneChip(count = doneCount, containerColor = scheme.surfaceContainerLowest)
                }
            }
        }
    }
}

/** A category's compact row in its subject's colours: the hubs' glyph lead, or the shapes icon for Drawing. */
@Composable
private fun CategoryRow(category: ProgressCategory, onClick: () -> Unit) {
    val language = category.type.subjectLanguage()
    if (language != null) {
        HubCategoryRow(type = category.type, language = language, progress = category.progress, onClick = onClick, compact = true)
        return
    }
    val scheme = MaterialTheme.colorScheme
    BcActivityRow(
        name = category.type.label(),
        progress = category.progress,
        progressColor = scheme.secondary,
        onClick = onClick,
        compact = true,
    ) {
        BcGlyphTile(size = BcDimens.rowLeadCompact, shape = BcShapes.smallTile, containerColor = scheme.secondaryContainer) {
            Icon(
                painter = painterResource(R.drawable.bc_ic_shapes),
                contentDescription = null,
                tint = scheme.secondary,
                modifier = Modifier.size(BcDimens.rowLeadIconCompact),
            )
        }
    }
}

/** The header card, a 4-column grid of the category's exercises, and the mastery rule underneath. */
@Composable
private fun CategoryDetail(
    category: ProgressCategory,
    masteryCompletions: Int,
    masteryScorePercent: Int,
    modifier: Modifier = Modifier,
) {
    val grid = category.grid
    if (grid.items.isEmpty()) {
        BcEmptyState(
            title = stringResource(R.string.empty_exercises),
            message = stringResource(R.string.empty_letters_message),
            modifier = modifier,
        )
        return
    }

    val title = category.type.label()
    val isDrawing = category.type == ExerciseType.DRAWING
    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(DETAIL_COLUMNS),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = BcSpacing.screen,
                end = BcSpacing.screen,
                top = BcSpacing.xxs,
                bottom = BcSpacing.m,
            ),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.snug),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.s),
        ) {
            item(key = HEADER_KEY, span = { GridItemSpan(maxLineSpan) }) {
                BcHeaderProgressCard(
                    label = title,
                    progress = category.progress,
                    fillColor = category.type.subjectColor(),
                    learnedCount = grid.learnedCount.takeIf { it > 0 },
                    doneCount = grid.doneCount.takeIf { it > 0 },
                    runningCount = grid.runningCount.takeIf { it > 0 },
                    // The grid's 12dp row gap plus this makes the design's 16dp between the card and the tiles.
                    modifier = Modifier.padding(bottom = BcSpacing.xxs),
                )
            }
            items(grid.items, key = { it.id }) { item -> DetailTile(item = item, isDrawing = isDrawing) }
        }
        BcBanner(
            kind = BcBannerKind.INFO,
            text = stringResource(R.string.mastery_rule, masteryCompletions, masteryScorePercent),
            compact = true,
            modifier = Modifier.padding(start = BcSpacing.screen, end = BcSpacing.screen, bottom = BcSpacing.screen),
        )
    }
}

/** A detail tile: the character (a drawing's shape), its stars, and a word on how far it has got. Not tappable. */
@Composable
private fun DetailTile(item: CategoryTileItem, isDrawing: Boolean) {
    val tileState = item.learningState.toTileState()
    BcExerciseTile(
        state = tileState,
        cornerRadius = BcDimens.detailTileCorner,
        minHeight = BcDimens.detailTileMinHeight,
        contentPadding = BcSpacing.xxs,
        showMasteredBadge = false,
        modifier = Modifier.height(BcDimens.detailTileMinHeight),
    ) {
        if (isDrawing) {
            val name = exerciseTitle(item.id, item.title)
            BcExerciseShape(
                strokes = item.strokes,
                size = BcDimens.detailShape,
                strokeWidth = BcDimens.detailShapeStroke,
                inset = BcSpacing.xxs,
                modifier = Modifier.semantics { contentDescription = name },
            )
        } else {
            BcLetterText(text = item.title, style = BcType.detailTile)
        }
        BcStars(count = item.stars)
        tileState.statusText(item.attemptCount)?.let { status ->
            Text(text = status, style = BcType.statusTiny, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}

@Composable
private fun BcTileState.statusText(attemptCount: Int): String? = when (this) {
    BcTileState.NOT_STARTED -> null
    BcTileState.PRACTISING -> pluralStringResource(R.plurals.tile_attempts, attemptCount, attemptCount)
    BcTileState.COMPLETED -> stringResource(R.string.status_completed)
    BcTileState.MASTERED -> stringResource(R.string.status_mastered)
}

/** The hub whose colours a category takes: Math shows in Bangla's (design/DESIGN_SPEC.md 5.19); Drawing has none. */
private fun ExerciseType.subjectLanguage(): AppLanguage? = when (this) {
    ExerciseType.VOWEL, ExerciseType.CONSONANT, ExerciseType.BANGLA_NUMBER, ExerciseType.MATH -> AppLanguage.BANGLA
    ExerciseType.ENGLISH_SMALL, ExerciseType.ENGLISH_CAPITAL -> AppLanguage.ENGLISH
    ExerciseType.DRAWING -> null
}

@Composable
private fun ExerciseType.subjectColor(): Color = when (subjectLanguage()) {
    AppLanguage.BANGLA -> MaterialTheme.colorScheme.primary
    AppLanguage.ENGLISH -> BcTheme.colors.english
    null -> MaterialTheme.colorScheme.secondary
}

private val previewVowels = CategoryGridState(
    items = listOf(
        CategoryTileItem(id = "vowel-o", title = "অ", learningState = LearningState.MASTERED, attemptCount = 4, stars = 3),
        CategoryTileItem(id = "vowel-aa", title = "আ", learningState = LearningState.COMPLETED, attemptCount = 3, stars = 3),
        CategoryTileItem(id = "vowel-i", title = "ই", learningState = LearningState.PRACTICING, attemptCount = 2, stars = 1),
        CategoryTileItem(id = "vowel-ii", title = "ঈ", learningState = LearningState.COMPLETED, attemptCount = 1, stars = 2),
        CategoryTileItem(id = "vowel-u", title = "উ"),
    ),
    learnedCount = 1,
    doneCount = 2,
    runningCount = 1,
)

private val previewState = ProgressState(
    isLoading = false,
    overallProgress = 0.03f,
    learnedCount = 1,
    doneCount = 5,
    categories = PROGRESS_CATEGORY_ORDER.map { type ->
        when (type) {
            ExerciseType.VOWEL -> ProgressCategory(type, progress = 3 / 11f, grid = previewVowels)
            ExerciseType.DRAWING -> ProgressCategory(
                type = type,
                progress = 0f,
                grid = CategoryGridState(
                    items = listOf(
                        CategoryTileItem(id = "drawing-line", title = "Line", strokes = listOf(Stroke("l", listOf(Point(15f, 50f), Point(85f, 50f))))),
                    ),
                ),
            )
            else -> ProgressCategory(type, progress = 0f, grid = CategoryGridState())
        }
    },
)

@Preview(showBackground = true, name = "Categories")
@Composable
private fun ProgressScreenPreview() {
    BornoChitraTheme {
        ProgressContent(state = previewState, onEvent = {}, navigationBar = {})
    }
}

@Preview(showBackground = true, name = "Vowel section")
@Composable
private fun ProgressScreenCategoryPreview() {
    BornoChitraTheme {
        ProgressContent(
            state = previewState.copy(openCategory = ExerciseType.VOWEL),
            onEvent = {},
            navigationBar = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ProgressScreenLoadingPreview() {
    BornoChitraTheme {
        ProgressContent(state = ProgressState(), onEvent = {}, navigationBar = {})
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ProgressScreenEmptyPreview() {
    BornoChitraTheme {
        ProgressContent(state = ProgressState(isLoading = false), onEvent = {}, navigationBar = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ProgressScreenErrorPreview() {
    BornoChitraTheme {
        ProgressContent(
            state = ProgressState(isLoading = false, error = R.string.error_progress_load),
            onEvent = {},
            navigationBar = {},
        )
    }
}
