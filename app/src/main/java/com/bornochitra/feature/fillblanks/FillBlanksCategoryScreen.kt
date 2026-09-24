package com.bornochitra.feature.fillblanks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/** The categories whose items form an ordered sequence; drawings do not. */
private val sequenceCategories = listOf(
    ExerciseType.VOWEL,
    ExerciseType.CONSONANT,
    ExerciseType.ENGLISH_SMALL,
    ExerciseType.ENGLISH_CAPITAL,
    ExerciseType.MATH,
    ExerciseType.BANGLA_NUMBER,
)

internal const val FILL_BLANKS_TITLE = "শূন্যস্থান পূরণ"

/**
 * Picks the category and difficulty for fill-in-the-blanks (plan.md Step 6). The list is fixed, so
 * there is no ViewModel; the difficulty choice is plain UI state held until a category is tapped.
 */
@Composable
fun FillBlanksCategoryScreen(
    onBackClick: () -> Unit,
    onCategoryClick: (type: ExerciseType, difficulty: Difficulty) -> Unit,
    modifier: Modifier = Modifier,
) {
    var difficulty by rememberSaveable { mutableStateOf(Difficulty.BEGINNER) }
    FillBlanksCategoryContent(
        difficulty = difficulty,
        onDifficultyChange = { difficulty = it },
        onBackClick = onBackClick,
        onCategoryClick = { type -> onCategoryClick(type, difficulty) },
        modifier = modifier,
    )
}

@Composable
private fun FillBlanksCategoryContent(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    onBackClick: () -> Unit,
    onCategoryClick: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = FILL_BLANKS_TITLE, onBackClick = onBackClick) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(BcSpacing.md),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
        ) {
            Text(text = "Difficulty", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.sm)) {
                DifficultyButton(
                    text = "Easy",
                    selected = difficulty == Difficulty.BEGINNER,
                    onClick = { onDifficultyChange(Difficulty.BEGINNER) },
                    modifier = Modifier.weight(1f),
                )
                DifficultyButton(
                    text = "Hard",
                    selected = difficulty == Difficulty.ADVANCED,
                    onClick = { onDifficultyChange(Difficulty.ADVANCED) },
                    modifier = Modifier.weight(1f),
                )
            }

            Text(text = "Choose a category", style = MaterialTheme.typography.titleLarge)
            sequenceCategories.forEach { type ->
                BcPrimaryButton(text = type.label(), onClick = { onCategoryClick(type) }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun DifficultyButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (selected) {
        BcPrimaryButton(text = text, onClick = onClick, modifier = modifier)
    } else {
        BcSecondaryButton(text = text, onClick = onClick, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun FillBlanksCategoryScreenPreview() {
    BornoChitraTheme {
        FillBlanksCategoryContent(
            difficulty = Difficulty.BEGINNER,
            onDifficultyChange = {},
            onBackClick = {},
            onCategoryClick = {},
        )
    }
}
