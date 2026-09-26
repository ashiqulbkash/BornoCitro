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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.R
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.ui.components.BcChoiceColumn
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.label
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

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

/**
 * Picks the category and difficulty for fill-in-the-blanks (plan.md Step 6), from the categories of the
 * [language] whose hub opened it. The list is fixed, so there is no ViewModel; the difficulty choice
 * is plain UI state held until a category is tapped.
 */
@Composable
fun FillBlanksCategoryScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onCategoryClick: (type: ExerciseType, difficulty: Difficulty) -> Unit,
    modifier: Modifier = Modifier,
) {
    var difficulty by rememberSaveable { mutableStateOf(Difficulty.BEGINNER) }
    FillBlanksCategoryContent(
        categories = sequenceCategories(language),
        difficulty = difficulty,
        onDifficultyChange = { difficulty = it },
        onBackClick = onBackClick,
        onCategoryClick = { type -> onCategoryClick(type, difficulty) },
        modifier = modifier,
    )
}

@Composable
private fun FillBlanksCategoryContent(
    categories: List<ExerciseType>,
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
                .padding(BcSpacing.m),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
        ) {
            Text(text = stringResource(R.string.fill_blanks_difficulty), style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(BcSpacing.xs)) {
                BcChoiceColumn(
                    label = stringResource(R.string.fill_blanks_easy),
                    selected = difficulty == Difficulty.BEGINNER,
                    onClick = { onDifficultyChange(Difficulty.BEGINNER) },
                    modifier = Modifier.weight(1f),
                )
                BcChoiceColumn(
                    label = stringResource(R.string.fill_blanks_hard),
                    selected = difficulty == Difficulty.ADVANCED,
                    onClick = { onDifficultyChange(Difficulty.ADVANCED) },
                    modifier = Modifier.weight(1f),
                )
            }

            Text(text = stringResource(R.string.fill_blanks_choose_category), style = MaterialTheme.typography.titleLarge)
            categories.forEach { type ->
                BcPrimaryButton(text = type.label(), onClick = { onCategoryClick(type) }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FillBlanksCategoryScreenPreview() {
    BornoChitraTheme {
        FillBlanksCategoryContent(
            categories = sequenceCategories(AppLanguage.BANGLA),
            difficulty = Difficulty.BEGINNER,
            onDifficultyChange = {},
            onBackClick = {},
            onCategoryClick = {},
        )
    }
}
