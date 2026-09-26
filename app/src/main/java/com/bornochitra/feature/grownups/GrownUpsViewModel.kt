package com.bornochitra.feature.grownups

import androidx.lifecycle.ViewModel
import com.bornochitra.core.model.MasteryRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.roundToInt

/** How progress is counted, in the numbers of the rule the app really applies. */
data class GrownUpsUiState(
    val requiredCompletions: Int,
    val minBestScorePercent: Int,
)

/** The Grown-ups tab's own content. The language switch belongs to the shared language ViewModel. */
@HiltViewModel
class GrownUpsViewModel @Inject constructor(
    masteryRule: MasteryRule,
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        GrownUpsUiState(
            requiredCompletions = masteryRule.requiredCompletions,
            minBestScorePercent = masteryRule.minBestScore.roundToInt(),
        ),
    )
    val uiState: StateFlow<GrownUpsUiState> = mutableState.asStateFlow()
}
