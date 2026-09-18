package com.bornochitra.core.tracing

import com.bornochitra.core.model.ExerciseType

/** Which pass of an exercise attempt the child is working through. */
enum class TracingPhase {

    /** One stroke is guided and traced at a time, in order. */
    STROKE_BY_STROKE,

    /** Every stroke's guide is shown at once so the whole letter or shape is drawn in one go. */
    FULL_LETTER,
}

/**
 * Caption telling the child which stroke of which pass they are on. The final pass is driven by
 * stroke count rather than exercise type, so a multi-stroke drawing reaches it too — hence the
 * wording follows [exerciseType] instead of always saying "letter".
 */
fun tracingStepLabel(
    phase: TracingPhase,
    strokeNumber: Int,
    totalStrokes: Int,
    exerciseType: ExerciseType,
): String = when (phase) {
    TracingPhase.STROKE_BY_STROKE -> "Stroke $strokeNumber of $totalStrokes"
    TracingPhase.FULL_LETTER -> "${wholeExerciseWording(exerciseType)} — stroke $strokeNumber of $totalStrokes"
}

private fun wholeExerciseWording(exerciseType: ExerciseType): String = when (exerciseType) {
    ExerciseType.VOWEL, ExerciseType.CONSONANT -> "Whole letter"
    ExerciseType.DRAWING -> "Whole shape"
}
