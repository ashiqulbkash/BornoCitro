package com.bornochitra.feature.fillblanks

import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Test

class HintRuleTest {

    @Test
    fun `without a hint the score and level are kept`() {
        assertEquals(98f, HintRule.score(98f, hintUsed = false))
        assertEquals(ScoreLevel.PERFECT, HintRule.level(98f, ScoreLevel.PERFECT, hintUsed = false))
    }

    @Test
    fun `a hint caps the score and a perfect trace drops to medium`() {
        assertEquals(HintRule.HINTED_SCORE_CAP, HintRule.score(98f, hintUsed = true))
        assertEquals(ScoreLevel.MEDIUM, HintRule.level(98f, ScoreLevel.PERFECT, hintUsed = true))
    }

    @Test
    fun `a hinted score already under the cap is kept`() {
        assertEquals(40f, HintRule.score(40f, hintUsed = true))
        assertEquals(ScoreLevel.LOW, HintRule.level(40f, ScoreLevel.LOW, hintUsed = true))
    }
}
