package com.bornochitra.feature.result

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bornochitra.R
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.ui.components.BcCard
import com.bornochitra.core.ui.components.BcEmptyState
import com.bornochitra.core.ui.components.BcExerciseShape
import com.bornochitra.core.ui.components.BcLetterText
import com.bornochitra.core.ui.components.BcPrimaryButton
import com.bornochitra.core.ui.components.BcResultStars
import com.bornochitra.core.ui.components.BcTextButton
import com.bornochitra.core.ui.components.BcTip
import com.bornochitra.core.ui.components.BcTonalButton
import com.bornochitra.core.ui.components.BcTopAppBar
import com.bornochitra.core.ui.components.exerciseTitle
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BcType
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
    onPracticeClick: (exerciseId: String, sessionScores: List<Float>) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // Back would reopen the finished attempt, which completes again straight away and replays this
    // result, so the child leaves through one of the buttons. The error state has no buttons.
    BackHandler(enabled = state.error == null) {}
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
    onPracticeClick: (exerciseId: String, sessionScores: List<Float>) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Scaffold(
            topBar = { BcTopAppBar(title = stringResource(R.string.title_result), centered = true) },
        ) { innerPadding ->
            when {
                state.error != null -> BcEmptyState(
                    title = stringResource(R.string.result_unavailable),
                    message = stringResource(state.error),
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
                        .padding(start = BcSpacing.screen, end = BcSpacing.screen, bottom = BcSpacing.l),
                )
            }
        }
        // Drawn over the top bar as in the design; it has no touch handling, so it never blocks a tap.
        if (state.attempt != null && state.attempt.scoreLevel != ScoreLevel.LOW) {
            Confetti(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .height(BcDimens.confettiHeight),
            )
        }
    }
}

@Composable
private fun AttemptSummary(
    attempt: ResultAttempt,
    onPracticeClick: (exerciseId: String, sessionScores: List<Float>) -> Unit,
    onProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wording = attempt.scoreLevel.wording()
    val isLow = attempt.scoreLevel == ScoreLevel.LOW
    val levelColor = if (isLow) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    // Scrolls when the content is taller than a short screen or a large font allows, so no button is
    // ever out of reach; the minimum height keeps the buttons at the bottom when everything fits.
    BoxWithConstraints(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.m),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BcResultStars(count = attempt.stars, modifier = Modifier.padding(top = BcSpacing.xs))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(wording.headline),
                        style = MaterialTheme.typography.displaySmall,
                        color = levelColor,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(wording.message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                }
                ScoreCard(
                    attempt = attempt,
                    scoreColor = if (isLow) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.result_session_summary,
                        attempt.sessionAttempts,
                        attempt.sessionAttempts,
                        attempt.sessionAveragePercent,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                attempt.tip?.let { BcTip(tip = it) }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BcSpacing.m),
                verticalArrangement = Arrangement.spacedBy(BcSpacing.snug),
            ) {
                BcPrimaryButton(
                    text = stringResource(R.string.result_practice_again),
                    onClick = { onPracticeClick(attempt.exerciseId, attempt.sessionScores) },
                    icon = R.drawable.bc_ic_reset,
                    modifier = Modifier.fillMaxWidth(),
                )

                // A low score keeps the child on this exercise: moving on would skip the letter they are
                // still learning (plan.md section 39).
                val next = attempt.nextExercise
                if (next != null && !isLow) {
                    BcTonalButton(
                        text = stringResource(R.string.result_next, exerciseTitle(next.id, next.title)),
                        onClick = { onPracticeClick(next.id, emptyList()) },
                        trailingIcon = R.drawable.bc_ic_arrow_forward,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                BcTextButton(
                    text = stringResource(R.string.result_view_progress),
                    onClick = onProgressClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
private fun ScoreCard(
    attempt: ResultAttempt,
    scoreColor: Color,
    modifier: Modifier = Modifier,
) {
    val celebrate = attempt.scoreLevel == ScoreLevel.PERFECT
    // A static preview never advances an animation, so it starts from the finished state.
    val animate = !LocalInspectionMode.current
    val reveal = remember { Animatable(if (animate) 0f else 1f) }
    val pop = remember { Animatable(if (celebrate && animate) POP_START_SCALE else 1f) }
    LaunchedEffect(Unit) {
        launch { reveal.animateTo(1f, tween(durationMillis = SCORE_REVEAL_MS)) }
        pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }

    val scoreDescription = stringResource(R.string.percent_description, attempt.scorePercent)
    BcCard(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = pop.value
                scaleY = pop.value
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = BcSpacing.screen, vertical = BcSpacing.m),
            horizontalArrangement = Arrangement.spacedBy(BcSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val strokes = attempt.drawingStrokes
            if (strokes != null) {
                BcExerciseShape(strokes = strokes, size = BcDimens.practiceTile, color = MaterialTheme.colorScheme.onSurface)
            } else {
                BcLetterText(text = exerciseTitle(attempt.exerciseId, attempt.title), style = BcType.letterResult)
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.percent, countedScore(attempt.scorePercent, reveal.value)),
                    style = BcType.scoreL,
                    color = scoreColor,
                    // A counting number is noise to a screen reader, so it gets the final score straight away.
                    modifier = Modifier.clearAndSetSemantics { contentDescription = scoreDescription },
                )
                Text(
                    text = stringResource(R.string.result_score_caption),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** The score to show part-way through the count-up, from 0 up to [target] as [progress] goes 0..1. */
internal fun countedScore(target: Int, progress: Float): Int =
    (target * progress.coerceIn(0f, 1f)).roundToInt()

private enum class ConfettiColor { PRIMARY, ACCENT, ENGLISH, SUCCESS, STAR }

/** One decorative piece: its centre as a fraction of the width and a dp from the top, its size and tilt. */
private data class ConfettiPiece(
    val xFraction: Float,
    val y: Dp,
    val width: Dp,
    val height: Dp,
    val rotation: Float,
    val color: ConfettiColor,
)

// The six pieces of the design's 360dp-wide artboard. A piece as wide as it is tall is a dot.
private val ConfettiPieces = listOf(
    ConfettiPiece(0.24f, 45.dp, 10.dp, 10.dp, 0f, ConfettiColor.PRIMARY),
    ConfettiPiece(0.74f, 35.dp, 8.dp, 8.dp, 0f, ConfettiColor.ACCENT),
    ConfettiPiece(0.85f, 80.dp, 8.dp, 16.dp, 30f, ConfettiColor.ENGLISH),
    ConfettiPiece(0.11f, 102.dp, 8.dp, 14.dp, -30f, ConfettiColor.ACCENT),
    ConfettiPiece(0.89f, 164.dp, 7.dp, 7.dp, 0f, ConfettiColor.SUCCESS),
    ConfettiPiece(0.07f, 197.dp, 6.dp, 12.dp, 20f, ConfettiColor.STAR),
)

/** The static confetti behind a good result. Decorative only. */
@Composable
private fun Confetti(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val bcColors = BcTheme.colors
    fun ConfettiColor.resolve(): Color = when (this) {
        ConfettiColor.PRIMARY -> scheme.primary
        ConfettiColor.ACCENT -> bcColors.accent
        ConfettiColor.ENGLISH -> bcColors.english
        ConfettiColor.SUCCESS -> scheme.tertiary
        ConfettiColor.STAR -> bcColors.star
    }
    Canvas(modifier = modifier) {
        ConfettiPieces.forEach { piece ->
            val center = Offset(size.width * piece.xFraction, piece.y.toPx())
            val pieceSize = Size(piece.width.toPx(), piece.height.toPx())
            rotate(degrees = piece.rotation, pivot = center) {
                drawRoundRect(
                    color = piece.color.resolve(),
                    topLeft = center - Offset(pieceSize.width / 2, pieceSize.height / 2),
                    size = pieceSize,
                    cornerRadius = CornerRadius(pieceSize.width / 2),
                )
            }
        }
    }
}

private data class ResultWording(
    @StringRes val headline: Int,
    @StringRes val message: Int,
)

private fun ScoreLevel.wording(): ResultWording = when (this) {
    ScoreLevel.PERFECT -> ResultWording(headline = R.string.result_perfect_headline, message = R.string.result_perfect_message)
    ScoreLevel.MEDIUM -> ResultWording(headline = R.string.result_medium_headline, message = R.string.result_medium_message)
    ScoreLevel.LOW -> ResultWording(headline = R.string.result_low_headline, message = R.string.result_low_message)
}

private fun previewAttempt(scorePercent: Int, scoreLevel: ScoreLevel, stars: Int) = ResultAttempt(
    exerciseId = "vowel-aa",
    title = "আ",
    scorePercent = scorePercent,
    scoreLevel = scoreLevel,
    stars = stars,
    nextExercise = NextExercise(id = "vowel-i", title = "ই"),
    tip = null,
)

@Preview(showBackground = true, name = "Perfect", heightDp = 800)
@Composable
private fun ResultScreenPerfectPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(isLoading = false, attempt = previewAttempt(93, ScoreLevel.PERFECT, stars = 3)),
            onPracticeClick = { _, _ -> },
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Medium", heightDp = 800)
@Composable
private fun ResultScreenMediumPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(isLoading = false, attempt = previewAttempt(76, ScoreLevel.MEDIUM, stars = 2)),
            onPracticeClick = { _, _ -> },
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Low", heightDp = 800)
@Composable
private fun ResultScreenLowPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(isLoading = false, attempt = previewAttempt(42, ScoreLevel.LOW, stars = 1)),
            onPracticeClick = { _, _ -> },
            onProgressClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun ResultScreenErrorPreview() {
    BornoChitraTheme {
        ResultContent(
            state = ResultState(isLoading = false, error = R.string.error_result_not_found),
            onPracticeClick = { _, _ -> },
            onProgressClick = {},
        )
    }
}
