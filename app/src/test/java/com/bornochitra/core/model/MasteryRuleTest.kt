package com.bornochitra.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MasteryRuleTest {

    private val rule = MasteryRule()

    @Test
    fun `default rule is three completions and a best score of eighty`() {
        assertEquals(3, rule.requiredCompletions)
        assertEquals(80f, rule.minBestScore)
    }

    @Test
    fun `enough good completions earns mastery`() {
        assertTrue(rule.isMastered(completedCount = 3, bestScore = 80f))
        assertTrue(rule.isMastered(completedCount = 5, bestScore = 96f))
    }

    @Test
    fun `practising often is not enough on its own`() {
        assertFalse(rule.isMastered(completedCount = 10, bestScore = 79f))
    }

    @Test
    fun `one great attempt is not enough on its own`() {
        assertFalse(rule.isMastered(completedCount = 1, bestScore = 100f))
        assertFalse(rule.isMastered(completedCount = 2, bestScore = 100f))
    }

    @Test
    fun `nothing practised is never mastered`() {
        assertFalse(rule.isMastered(completedCount = 0, bestScore = 0f))
    }

    @Test
    fun `thresholds are configurable`() {
        val gentler = MasteryRule(requiredCompletions = 1, minBestScore = 60f)

        assertTrue(gentler.isMastered(completedCount = 1, bestScore = 60f))
        assertFalse(gentler.isMastered(completedCount = 1, bestScore = 59f))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a rule that can never be met is rejected`() {
        MasteryRule(requiredCompletions = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a score outside the 0 to 100 range is rejected`() {
        MasteryRule(minBestScore = 101f)
    }
}
