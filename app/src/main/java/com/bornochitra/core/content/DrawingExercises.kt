package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Simple hand-control drawings. Unlike letters these carry no handwriting-accuracy concerns: each
 * shape is exact geometry rather than a traced glyph, which is why plan.md Step 11.16 verifies Line,
 * Circle, Square and Triangle instead of re-deriving them. That exactness — a constant radius, four
 * equal axis-aligned sides, an isosceles triangle on a level base — is pinned by unit test, so an
 * edit cannot quietly distort a shape. See plan.md section 9.
 */
internal val drawingExercises: List<Exercise> = listOf(
    Exercise(
        id = "drawing-line",
        title = "Line",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(id = "drawing-line-body", points = StrokePoints.line(Point(15f, 50f), Point(85f, 50f))),
        ),
    ),
    Exercise(
        id = "drawing-circle",
        title = "Circle",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "drawing-circle-body",
                points = StrokePoints.arc(center = Point(50f, 50f), radius = 35f, startDeg = 0f, sweepDeg = 360f, samples = 32),
            ),
        ),
    ),
    Exercise(
        id = "drawing-square",
        title = "Square",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.INTERMEDIATE,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "drawing-square-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(20f, 20f),
                        Point(80f, 20f),
                        Point(80f, 80f),
                        Point(20f, 80f),
                        Point(20f, 20f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "drawing-triangle",
        title = "Triangle",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "drawing-triangle-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50f, 15f),
                        Point(85f, 80f),
                        Point(15f, 80f),
                        Point(50f, 15f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * The house is the one composite drawing, so plan.md Step 11.17 re-derives it. Its walls used to
     * run between x 25 and 75 while the roof spanned 15 to 85, which left every free end of both
     * strokes in mid-air: each eave stopped 10 units to the side of a wall top, and the roof's slope
     * passed 8.6 units above it. The roof now lands exactly on the wall tops, so the two strokes
     * close into one outline — a square with a triangle on it, the way a child draws a house. The
     * roof goes on first and the walls hang from its eaves.
     */
    Exercise(
        id = "drawing-house",
        title = "House",
        type = ExerciseType.DRAWING,
        difficulty = Difficulty.ADVANCED,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "drawing-house-roof",
                points = StrokePoints.polyline(listOf(Point(15f, 45f), Point(50f, 15f), Point(85f, 45f))),
            ),
            Stroke(
                id = "drawing-house-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(15f, 45f),
                        Point(15f, 85f),
                        Point(85f, 85f),
                        Point(85f, 45f),
                    ),
                ),
            ),
        ),
    ),
)
