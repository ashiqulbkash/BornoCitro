package com.bornochitra.core.model

/** A single traceable letter or drawing, made of one or more ordered [strokes]. See plan.md sections 8-9. */
data class Exercise(
    val id: String,
    val title: String,
    val type: ExerciseType,
    val difficulty: Difficulty,
    val strokes: List<Stroke>,
    val order: Int,
)
