package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Placeholder vowel stroke geometry. These paths approximate each letter's silhouette so the
 * content pipeline and tracing prototype have real data to work against; plan.md Step 10.8
 * refines অ specifically and Step 23 validates every letter's handwriting accuracy before release.
 */
internal val vowelExercises: List<Exercise> = listOf(
    Exercise(
        id = "vowel-o",
        title = "অ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "vowel-o-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(65f, 15f),
                        Point(35f, 20f),
                        Point(25f, 45f),
                        Point(45f, 60f),
                        Point(70f, 55f),
                        Point(65f, 80f),
                        Point(40f, 88f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "vowel-aa",
        title = "আ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "vowel-aa-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(65f, 15f),
                        Point(35f, 20f),
                        Point(25f, 45f),
                        Point(45f, 60f),
                        Point(70f, 55f),
                        Point(65f, 80f),
                        Point(40f, 88f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-aa-tail",
                points = StrokePoints.line(Point(70f, 30f), Point(85f, 85f)),
            ),
        ),
    ),
    Exercise(
        id = "vowel-i",
        title = "ই",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "vowel-i-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 20f),
                        Point(55f, 25f),
                        Point(35f, 45f),
                        Point(60f, 55f),
                        Point(35f, 70f),
                        Point(60f, 85f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-i-dot",
                points = StrokePoints.arc(center = Point(75f, 20f), radius = 4f, startDeg = 0f, sweepDeg = 360f, samples = 12),
            ),
        ),
    ),
    Exercise(
        id = "vowel-ii",
        title = "ঈ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "vowel-ii-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 20f),
                        Point(55f, 25f),
                        Point(35f, 45f),
                        Point(60f, 55f),
                        Point(35f, 70f),
                        Point(60f, 85f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ii-tail",
                points = StrokePoints.line(Point(65f, 30f), Point(85f, 82f)),
            ),
        ),
    ),
    Exercise(
        id = "vowel-u",
        title = "উ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "vowel-u-body",
                points = StrokePoints.line(Point(30f, 20f), Point(30f, 65f)),
            ),
            Stroke(
                id = "vowel-u-hook",
                points = StrokePoints.arc(center = Point(45f, 65f), radius = 15f, startDeg = 180f, sweepDeg = 160f, samples = 16),
            ),
        ),
    ),
    Exercise(
        id = "vowel-e",
        title = "এ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "vowel-e-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 50f),
                        Point(75f, 50f),
                        Point(55f, 20f),
                        Point(30f, 35f),
                        Point(55f, 80f),
                        Point(75f, 65f),
                    ),
                ),
            ),
        ),
    ),
)
