package com.bornochitra.feature.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcFeedbackBanner
import com.bornochitra.core.ui.components.BcFeedbackTone
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * Reports one finished attempt (plan.md section 39). Wording is encouraging at every level: a low
 * score offers another go rather than telling the child they failed.
 */
@Composable
fun ResultScreen(
    onPracticeClick: (exerciseId: String) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ResultContent(
        state = state,
        onPracticeClick = onPracticeClick,
        onProgressClick = onProgressClick,
        modifier = modifier,
    )
}

@Composable
private fun ResultContent(
    state: ResultState,
    onPracticeClick: (exerciseId: String) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { BcTopAppBar(title = "Result") },
    ) { innerPadding ->
        when {
            state.error != null -> BcEmptyState(
                title = "Result unavailable",
                message = state.error,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )

            state.isLoading || state.attempt == null -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }

            else -> AttemptSummary(
                attempt = state.attempt,
                onPracticeClick = onPracticeClick,
                onProgressClick = onProgressClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(BcSpacing.md),
            )
        }
    }
}

@Composable
private fun AttemptSummary(
    attempt: ResultAttempt,
    onPracticeClick: (exerciseId: String) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wording = attempt.scoreLevel.wording()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BcSpacing.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BcFeedbackBanner(tone = wording.tone, title = wording.headline, message = wording.message)

        Text(text = attempt.title, style = MaterialTheme.typography.displayMedium)
        Text(text = "${attempt.scorePercent}%", style = MaterialTheme.typography.headlineLarge)

        BcPrimaryButton(
            text = wording.retryText,
            onClick = { onPracticeClick(attempt.exerciseId) },
            modifier = Modifier.fillMaxWidth(),
        )

        // A low score keeps the child on this exercise: moving on would skip the letter they are
        // still learning (plan.md section 39).
        val nextExerciseId = attempt.nextExerciseId
        if (nextExerciseId != null && attempt.scoreLevel != ScoreLevel.LOW) {
            BcSecondaryButton(
                text = "Next",
                onClick = { onPracticeClick(nextExerciseId) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        BcSecondaryButton(
            text = "View Progress",
            onClick = onProgressClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private data class ResultWording(
    val tone: BcFeedbackTone,
    val headline: String,
    val message: String,
    val retryText: String,
)

private fun ScoreLevel.wording(): ResultWording = when (this) {
    ScoreLevel.PERFECT -> ResultWording(
        tone = BcFeedbackTone.GREAT,
        headline = "Great Job! 🎉",
        message = "Perfect!",
        retryText = "Practice Again",
    )

    ScoreLevel.MEDIUM -> ResultWording(
        tone = BcFeedbackTone.GOOD,
        headline = "Good Try!",
        message = "Nice tracing — keep going!",
        retryText = "Try Again",
    )

    ScoreLevel.LOW -> ResultWording(
        tone = BcFeedbackTone.ENCOURAGING,
        headline = "Let's Practice Again!",
        message = "Follow the dots slowly. You can do it!",
        retryText = "Try Again",
    )
}

@Preview(showBackground = true, name = "Perfect")
@Composable
private fun ResultScreenPerfectPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(
                isLoading = false,
                attempt = ResultAttempt(
                    exerciseId = "vowel-o",
                    title = "অ",
                    scorePercent = 94,
                    scoreLevel = ScoreLevel.PERFECT,
                    nextExerciseId = "vowel-aa",
                ),
            ),
            onPracticeClick = {},
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Medium")
@Composable
private fun ResultScreenMediumPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(
                isLoading = false,
                attempt = ResultAttempt(
                    exerciseId = "vowel-o",
                    title = "অ",
                    scorePercent = 76,
                    scoreLevel = ScoreLevel.MEDIUM,
                    nextExerciseId = "vowel-aa",
                ),
            ),
            onPracticeClick = {},
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Low")
@Composable
private fun ResultScreenLowPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(
                isLoading = false,
                attempt = ResultAttempt(
                    exerciseId = "vowel-o",
                    title = "অ",
                    scorePercent = 48,
                    scoreLevel = ScoreLevel.LOW,
                    nextExerciseId = "vowel-aa",
                ),
            ),
            onPracticeClick = {},
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ResultScreenErrorPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(isLoading = false, error = "We couldn't find that practice result."),
            onPracticeClick = {},
            onProgressClick = {},
        )
    }
}
