package com.bornochitra.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.bornochitra.R
import com.bornochitra.core.model.LearningState

/** The short status under an exercise tile, or null before its first attempt. */
@Composable
fun learningStatusText(state: LearningState, attemptCount: Int): String? = when (state) {
    LearningState.NOT_STARTED -> null
    LearningState.STARTED,
    LearningState.PRACTICING,
    -> pluralStringResource(R.plurals.status_attempts, attemptCount, attemptCount)
    LearningState.COMPLETED -> stringResource(R.string.status_completed)
    LearningState.MASTERED -> stringResource(R.string.status_mastered)
}
