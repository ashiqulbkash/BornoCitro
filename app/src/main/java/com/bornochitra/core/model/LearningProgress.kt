package com.bornochitra.core.model

/** Aggregate learning progress shown on Home/Progress. See plan.md sections 8 and 19. */
data class LearningProgress(
    val overallProgress: Float = 0f,
    val vowelProgress: Float = 0f,
    val consonantProgress: Float = 0f,
    val englishSmallProgress: Float = 0f,
    val englishCapitalProgress: Float = 0f,
    val mathProgress: Float = 0f,
    val drawingProgress: Float = 0f,
    val continueExerciseId: String? = null,
)
