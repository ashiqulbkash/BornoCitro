package com.bornochitra.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class LearningStateTest {

    private fun progress(
        attemptCount: Int,
        completedCount: Int = 0,
        isMastered: Boolean = false,
    ) = ExerciseProgress(
        exerciseId = "vowel-o",
        attemptCount = attemptCount,
        completedCount = completedCount,
        bestScore = 90f,
        lastScore = 90f,
        lastPracticedAt = 0L,
        isMastered = isMastered,
    )

    @Test
    fun `an exercise with no stored progress has not been started`() {
        assertEquals(LearningState.NOT_STARTED, null.toLearningState())
    }

    @Test
    fun `a row with no attempts yet has not been started`() {
        assertEquals(LearningState.NOT_STARTED, progress(attemptCount = 0).toLearningState())
    }

    @Test
    fun `one unfinished attempt is started, several are practicing`() {
        assertEquals(LearningState.STARTED, progress(attemptCount = 1).toLearningState())
        assertEquals(LearningState.PRACTICING, progress(attemptCount = 2).toLearningState())
    }

    @Test
    fun `finishing an attempt completes the exercise`() {
        assertEquals(
            LearningState.COMPLETED,
            progress(attemptCount = 4, completedCount = 1).toLearningState(),
        )
    }

    @Test
    fun `mastery outranks completion`() {
        assertEquals(
            LearningState.MASTERED,
            progress(attemptCount = 5, completedCount = 3, isMastered = true).toLearningState(),
        )
    }
}
