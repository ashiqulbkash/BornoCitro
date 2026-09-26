package com.bornochitra.feature.grownups

import com.bornochitra.core.model.MasteryRule
import org.junit.Assert.assertEquals
import org.junit.Test

class GrownUpsViewModelTest {

    @Test
    fun `the counting rule shows the numbers of the rule the app applies`() {
        val state = GrownUpsViewModel(MasteryRule()).uiState.value

        assertEquals(MasteryRule.DEFAULT_REQUIRED_COMPLETIONS, state.requiredCompletions)
        assertEquals(MasteryRule.DEFAULT_MIN_BEST_SCORE.toInt(), state.minBestScorePercent)
    }

    @Test
    fun `a changed rule changes the numbers shown`() {
        val state = GrownUpsViewModel(MasteryRule(requiredCompletions = 5, minBestScore = 75f)).uiState.value

        assertEquals(5, state.requiredCompletions)
        assertEquals(75, state.minBestScorePercent)
    }
}
