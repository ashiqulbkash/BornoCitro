package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Placeholder consonant stroke geometry, same caveats as [vowelExercises]; plan.md Step 10.9
 * refines ক specifically and Step 23 validates every letter's handwriting accuracy before release.
 */
internal val consonantExercises: List<Exercise> = listOf(
    Exercise(
        id = "consonant-ko",
        title = "ক",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "consonant-ko-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 15f),
                        Point(30f, 85f),
                        Point(70f, 85f),
                        Point(30f, 50f),
                        Point(70f, 20f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "consonant-kho",
        title = "খ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "consonant-kho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 15f),
                        Point(30f, 85f),
                        Point(75f, 85f),
                        Point(75f, 45f),
                        Point(30f, 45f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "consonant-go",
        title = "গ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "consonant-go-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 20f),
                        Point(70f, 20f),
                        Point(30f, 50f),
                        Point(70f, 55f),
                        Point(45f, 85f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "consonant-gho",
        title = "ঘ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "consonant-gho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 15f),
                        Point(30f, 85f),
                        Point(70f, 85f),
                        Point(70f, 15f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-gho-cross",
                points = StrokePoints.line(Point(30f, 50f), Point(70f, 50f)),
            ),
        ),
    ),
    Exercise(
        id = "consonant-ngo",
        title = "ঙ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "consonant-ngo-body",
                points = StrokePoints.arc(center = Point(50f, 50f), radius = 30f, startDeg = -30f, sweepDeg = 300f, samples = 24),
            ),
            Stroke(
                id = "consonant-ngo-dot",
                points = StrokePoints.arc(center = Point(50f, 85f), radius = 4f, startDeg = 0f, sweepDeg = 360f, samples = 12),
            ),
        ),
    ),
)
