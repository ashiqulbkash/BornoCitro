package com.bornochitra.core.model

/**
 * How far a child has got with one exercise (plan.md section 41). Opening an exercise or attempting
 * it once is deliberately not "learned": an attempt has to be finished to reach [COMPLETED], and
 * mastery is awarded separately by [MasteryRule] when the exercise has been finished often enough
 * and well enough.
 */
enum class LearningState {
    NOT_STARTED,
    STARTED,
    PRACTICING,
    COMPLETED,
    MASTERED,
}

/** Mastery is read from the stored flag, which [MasteryRule] awards when a result is saved. */
fun ExerciseProgress?.toLearningState(): LearningState = when {
    this == null || attemptCount == 0 -> LearningState.NOT_STARTED
    isMastered -> LearningState.MASTERED
    completedCount > 0 -> LearningState.COMPLETED
    attemptCount == 1 -> LearningState.STARTED
    else -> LearningState.PRACTICING
}
