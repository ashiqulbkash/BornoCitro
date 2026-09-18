package com.bornochitra.core.tracing

import com.bornochitra.core.model.ExerciseType
import org.junit.Assert.assertEquals
import org.junit.Test

class TracingStepLabelTest {

    @Test
    fun `stroke-by-stroke pass counts strokes without naming the exercise`() {
        ExerciseType.entries.forEach { type ->
            assertEquals(
                "Stroke 2 of 3",
                tracingStepLabel(TracingPhase.STROKE_BY_STROKE, strokeNumber = 2, totalStrokes = 3, exerciseType = type),
            )
        }
    }

    @Test
    fun `whole-letter pass says letter for letters`() {
        listOf(ExerciseType.VOWEL, ExerciseType.CONSONANT).forEach { type ->
            assertEquals(
                "Whole letter — stroke 1 of 3",
                tracingStepLabel(TracingPhase.FULL_LETTER, strokeNumber = 1, totalStrokes = 3, exerciseType = type),
            )
        }
    }

    @Test
    fun `whole-letter pass says shape for drawings`() {
        assertEquals(
            "Whole shape — stroke 1 of 2",
            tracingStepLabel(
                TracingPhase.FULL_LETTER,
                strokeNumber = 1,
                totalStrokes = 2,
                exerciseType = ExerciseType.DRAWING,
            ),
        )
    }
}
