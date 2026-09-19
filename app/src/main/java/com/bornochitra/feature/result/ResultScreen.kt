package com.bornochitra.feature.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseHeading
import com.bornochitra.core.ui.components.BcFeedbackBanner
import com.bornochitra.core.ui.components.BcFeedbackTone
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcSecondaryButton
import com.bornochitra.core.ui.components.BcTip
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val SCORE_REVEAL_MS = 700
private const val POP_START_SCALE = 0.7f

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
    // Scrolls when the content is taller than a short screen or a large font allows, so no button is
    // ever out of reach; the minimum height keeps it centred when everything fits.
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight),
            verticalArrangement = Arrangement.spacedBy(BcSpacing.md, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BcFeedbackBanner(tone = wording.tone, title = wording.headline, message = wording.message)

            ResultHeadline(
                title = attempt.title,
                scorePercent = attempt.scorePercent,
                celebrate = attempt.scoreLevel == ScoreLevel.PERFECT,
            )

            attempt.tip?.let { BcTip(tip = it) }

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
}

/**
 * The exercise and its score, revealed with a short count-up (plan.md section 44). A perfect score
 * also pops in with a gentle bounce. Kept as its own composable so the per-frame count only
 * recomposes this small part of the screen, and the bounce is applied while drawing.
 *
 * With animations turned off in system settings both finish immediately, so the final score is
 * simply shown.
 */
@Composable
private fun ResultHeadline(
    title: String,
    scorePercent: Int,
    celebrate: Boolean,
    modifier: Modifier = Modifier,
) {
    // A static preview never advances an animation, so it starts from the finished state.
    val animate = !LocalInspectionMode.current
    val reveal = remember { Animatable(if (animate) 0f else 1f) }
    val pop = remember { Animatable(if (celebrate && animate) POP_START_SCALE else 1f) }
    LaunchedEffect(Unit) {
        launch { reveal.animateTo(1f, tween(durationMillis = SCORE_REVEAL_MS)) }
        pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }

    Column(
        modifier = modifier.graphicsLayer {
            scaleX = pop.value
            scaleY = pop.value
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BcSpacing.md),
    ) {
        BcExerciseHeading(title = title)
        Text(
            text = "${countedScore(scorePercent, reveal.value)}%",
            style = MaterialTheme.typography.headlineLarge,
            // A counting number is noise to a screen reader, so it gets the final score straight away.
            modifier = Modifier.clearAndSetSemantics { contentDescription = "$scorePercent percent" },
        )
    }
}

/** The score to show part-way through the count-up, from 0 up to [target] as [progress] goes 0..1. */
internal fun countedScore(target: Int, progress: Float): Int =
    (target * progress.coerceIn(0f, 1f)).roundToInt()

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
                    tip = null,
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
                    tip = null,
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
                    tip = null,
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
