package com.bornochitra.feature.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.LearningState
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseShape
import com.bornochitra.core.ui.components.BcExerciseTile
import com.bornochitra.core.ui.components.BcHeaderProgressCard
import com.bornochitra.core.ui.components.BcLetterTile
import com.bornochitra.core.ui.components.BcTileState
import com.bornochitra.core.ui.components.BcTileStatus
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.components.toTileState
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val LETTER_COLUMNS = 3
private const val DRAWING_COLUMNS = 2
private const val HEADER_KEY = "header"

/**
 * A letter category (design/DESIGN_SPEC.md 5.8): the header progress card, then a 3-column grid of letter tiles
 * that open Practice. With no exercises it shows the category's empty state instead.
 */
@Composable
fun LetterGridScreen(
    title: String,
    state: CategoryGridState,
    emptyTitle: String,
    emptyMessage: String,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
) {
    CategoryGridScaffold(
        title = title,
        state = state,
        columns = LETTER_COLUMNS,
        emptyTitle = emptyTitle,
        emptyMessage = emptyMessage,
        onBackClick = onBackClick,
        progressColor = progressColor,
        modifier = modifier,
    ) { item ->
        BcLetterTile(
            character = item.title,
            state = item.learningState.toTileState(),
            attemptCount = item.attemptCount,
            stars = item.stars,
            isContinueHere = item.isContinueHere,
            onClick = { onExerciseClick(item.id) },
            modifier = Modifier.aspectRatio(1f),
        )
    }
}

/**
 * The drawing grid (design/DESIGN_SPEC.md 5.9): the header card, then 2 columns of 150dp tiles, each with its
 * shape and name, in the letter tiles' states.
 */
@Composable
fun DrawingGridScreen(
    title: String,
    state: CategoryGridState,
    emptyTitle: String,
    emptyMessage: String,
    onBackClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CategoryGridScaffold(
        title = title,
        state = state,
        columns = DRAWING_COLUMNS,
        emptyTitle = emptyTitle,
        emptyMessage = emptyMessage,
        onBackClick = onBackClick,
        progressColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
    ) { item ->
        val tileState = item.learningState.toTileState()
        BcExerciseTile(
            state = tileState,
            isContinueHere = item.isContinueHere,
            onClick = { onExerciseClick(item.id) },
            contentGap = BcSpacing.xs,
            modifier = Modifier.height(BcDimens.drawingTileHeight),
        ) {
            BcExerciseShape(strokes = item.strokes)
            Text(
                text = exerciseTitle(item.id, item.title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            if (tileState != BcTileState.NOT_STARTED) {
                BcTileStatus(state = tileState, attemptCount = item.attemptCount, stars = item.stars)
            }
        }
    }
}

@Composable
private fun CategoryGridScaffold(
    title: String,
    state: CategoryGridState,
    columns: Int,
    emptyTitle: String,
    emptyMessage: String,
    onBackClick: () -> Unit,
    progressColor: Color,
    modifier: Modifier = Modifier,
    tile: @Composable (CategoryTileItem) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = title, onBackClick = onBackClick) },
    ) { innerPadding ->
        if (state.items.isEmpty()) {
            BcEmptyState(
                title = emptyTitle,
                message = emptyMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
            return@Scaffold
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = BcSpacing.screen,
                end = BcSpacing.screen,
                top = innerPadding.calculateTopPadding() + BcSpacing.xxs,
                bottom = innerPadding.calculateBottomPadding() + BcSpacing.screen,
            ),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.s),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.roomy),
        ) {
            item(key = HEADER_KEY, span = { GridItemSpan(maxLineSpan) }) {
                BcHeaderProgressCard(
                    label = title,
                    progress = state.progress,
                    fillColor = progressColor,
                    learnedCount = state.learnedCount.takeIf { it > 0 },
                    doneCount = state.doneCount.takeIf { it > 0 },
                    runningCount = state.runningCount.takeIf { it > 0 },
                    // The grid's 14dp row gap plus this makes the design's 20dp between the card and the tiles.
                    modifier = Modifier.padding(bottom = BcSpacing.tight),
                )
            }
            items(state.items, key = { it.id }) { item -> tile(item) }
        }
    }
}

private val previewState = CategoryGridState(
    items = listOf(
        CategoryTileItem(id = "vowel-o", title = "অ", learningState = LearningState.MASTERED, attemptCount = 4, stars = 3),
        CategoryTileItem(id = "vowel-aa", title = "আ", learningState = LearningState.COMPLETED, attemptCount = 3, stars = 3, isContinueHere = true),
        CategoryTileItem(id = "vowel-i", title = "ই", learningState = LearningState.PRACTICING, attemptCount = 2),
        CategoryTileItem(id = "vowel-ii", title = "ঈ", learningState = LearningState.COMPLETED, attemptCount = 1, stars = 2),
        CategoryTileItem(id = "vowel-u", title = "উ"),
    ),
    progress = 3 / 11f,
    learnedCount = 1,
    doneCount = 2,
    runningCount = 1,
)

@Preview(showBackground = true, name = "Letters")
@Composable
private fun LetterGridScreenPreview() {
    BornoChitraTheme {
        LetterGridScreen(title = "স্বরবর্ণ", state = previewState, emptyTitle = "", emptyMessage = "", onBackClick = {}, onExerciseClick = {})
    }
}

@Preview(showBackground = true, name = "Drawings")
@Composable
private fun DrawingGridScreenPreview() {
    BornoChitraTheme {
        DrawingGridScreen(
            title = "আঁকা",
            state = CategoryGridState(
                items = listOf(
                    CategoryTileItem(id = "drawing-line", title = "Line", strokes = listOf(Stroke("l", listOf(Point(15f, 50f), Point(85f, 50f))))),
                    CategoryTileItem(id = "drawing-circle", title = "Circle", learningState = LearningState.COMPLETED, stars = 2, attemptCount = 1),
                ),
            ),
            emptyTitle = "",
            emptyMessage = "",
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun CategoryGridEmptyPreview() {
    BornoChitraTheme {
        LetterGridScreen(
            title = "স্বরবর্ণ",
            state = CategoryGridState(),
            emptyTitle = "এখনো কোনো স্বরবর্ণ নেই",
            emptyMessage = "শিগগিরই অনুশীলনের জন্য অক্ষর আসবে।",
            onBackClick = {},
            onExerciseClick = {},
        )
    }
}
