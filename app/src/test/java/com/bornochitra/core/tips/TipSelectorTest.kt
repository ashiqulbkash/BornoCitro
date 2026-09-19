package com.bornochitra.core.tips

import com.bornochitra.core.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TipSelectorTest {

    private val selector = TipSelector(TipRules())

    @Test
    fun `an exercise never attempted opens with the first-attempt tip`() {
        assertEquals(ContextualTip.FIRST_ATTEMPT, selector.beforeFirstAttempt(previousAttempts = 0))
    }

    @Test
    fun `an exercise attempted before opens without a tip`() {
        assertNull(selector.beforeFirstAttempt(previousAttempts = 1))
        assertNull(selector.beforeFirstAttempt(previousAttempts = 7))
    }

    @Test
    fun `a stroke traced well needs no tip`() {
        assertNull(selector.afterStrokeAttempt(consecutiveMisses = 0))
    }

    @Test
    fun `a first or second miss gets the gentle try-again tip`() {
        assertEquals(ContextualTip.MISSED_STROKE, selector.afterStrokeAttempt(consecutiveMisses = 1))
        assertEquals(ContextualTip.MISSED_STROKE, selector.afterStrokeAttempt(consecutiveMisses = 2))
    }

    @Test
    fun `missing the same stroke three times in a row escalates the tip`() {
        assertEquals(ContextualTip.REPEATED_MISSES, selector.afterStrokeAttempt(consecutiveMisses = 3))
        assertEquals(ContextualTip.REPEATED_MISSES, selector.afterStrokeAttempt(consecutiveMisses = 9))
    }

    @Test
    fun `two weak attempts in a row earn the slow-down tip`() {
        assertEquals(
            ContextualTip.REPEATED_LOW_SCORES,
            selector.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(70f, 65f)),
        )
    }

    @Test
    fun `one weak attempt is not repeated`() {
        assertNull(selector.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(70f)))
    }

    @Test
    fun `only the latest attempts count, so an earlier weak one is forgiven`() {
        // newest first: a good attempt now, weak ones before it
        assertNull(selector.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(95f, 60f, 62f)))
    }

    @Test
    fun `a score exactly at the bar is not low`() {
        assertNull(selector.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(80f, 80f)))
    }

    @Test
    fun `no history and an easy exercise earn no tip`() {
        assertNull(selector.afterAttempt(Difficulty.BEGINNER, recentScores = emptyList()))
    }

    @Test
    fun `finishing an advanced exercise earns the well-done tip`() {
        assertEquals(
            ContextualTip.DIFFICULT_COMPLETED,
            selector.afterAttempt(Difficulty.ADVANCED, recentScores = listOf(96f)),
        )
    }

    @Test
    fun `intermediate exercises are not treated as difficult by default`() {
        assertNull(selector.afterAttempt(Difficulty.INTERMEDIATE, recentScores = listOf(96f)))
    }

    @Test
    fun `repeated weak attempts outrank the well-done tip`() {
        assertEquals(
            ContextualTip.REPEATED_LOW_SCORES,
            selector.afterAttempt(Difficulty.ADVANCED, recentScores = listOf(70f, 68f)),
        )
    }

    @Test
    fun `the selector says how much history it needs`() {
        assertEquals(2, selector.recentResultsNeeded)
        assertEquals(4, TipSelector(TipRules(repeatedLowScoreCount = 4)).recentResultsNeeded)
    }

    @Test
    fun `thresholds are configurable`() {
        val patient = TipSelector(
            TipRules(
                repeatedMissesThreshold = 5,
                repeatedLowScoreCount = 3,
                lowScoreBelow = 60f,
                difficultFrom = Difficulty.INTERMEDIATE,
            ),
        )

        assertEquals(ContextualTip.MISSED_STROKE, patient.afterStrokeAttempt(consecutiveMisses = 4))
        assertEquals(ContextualTip.REPEATED_MISSES, patient.afterStrokeAttempt(consecutiveMisses = 5))
        assertNull(patient.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(70f, 70f, 70f)))
        assertEquals(
            ContextualTip.REPEATED_LOW_SCORES,
            patient.afterAttempt(Difficulty.BEGINNER, recentScores = listOf(55f, 50f, 40f)),
        )
        assertEquals(
            ContextualTip.DIFFICULT_COMPLETED,
            patient.afterAttempt(Difficulty.INTERMEDIATE, recentScores = listOf(90f)),
        )
    }
}
