package com.bornochitra.core.tips

import com.bornochitra.core.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TipRulesTest {

    @Test
    fun `defaults are two low scores below eighty, from advanced`() {
        val rules = TipRules()

        assertEquals(2, rules.repeatedLowScoreCount)
        assertEquals(80f, rules.lowScoreBelow)
        assertEquals(Difficulty.ADVANCED, rules.difficultFrom)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a low-score count of zero is rejected`() {
        TipRules(repeatedLowScoreCount = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a score bar outside 0 to 100 is rejected`() {
        TipRules(lowScoreBelow = 101f)
    }

    @Test
    fun `every tip has its own non-empty wording`() {
        val messages = ContextualTip.entries.map { it.message }

        assertTrue(messages.all { it.isNotBlank() })
        assertEquals(messages.size, messages.toSet().size)
    }

    @Test
    fun `tip wording never blames the child`() {
        val discouraging = listOf("wrong", "fail", "bad", "mistake")

        ContextualTip.entries.forEach { tip ->
            discouraging.forEach { word ->
                assertFalse("${tip.name} contains \"$word\"", tip.message.contains(word, ignoreCase = true))
            }
        }
    }
}
