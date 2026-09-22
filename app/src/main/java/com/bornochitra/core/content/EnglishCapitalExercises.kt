package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * English capital-letter stroke geometry (plan.md Step 3). Every letter is read off its rendered
 * glyph's centreline, never approximated by eye, the way the small letters are.
 *
 * Font: **Inter Bold** (The Inter Project Authors, SIL Open Font License 1.1), bundled as the
 * variable font `res/font/inter.ttf` and used for every English capital the app draws — the
 * reference glyph above the canvas, the category grid and the progress list
 * (`BcLatinCapitalFontFamily`). The guides are derived from the instance the app pins, weight 700
 * at the default optical size 14, so they sit under the exact glyph shown.
 */
internal val englishCapitalExercises: List<Exercise> = listOf(
    /**
     * A is a shade taller than it is wide (aspect 0.959), so it is fitted by height.
     *
     * Inter's A is two straight legs under a wide flat apex, with flat feet and a crossbar that is
     * part of the same ink. Written as taught: `left` from the apex down to the left foot, `right`
     * from the apex down to the right foot, then `bar` left to right. Each leg is a line through its
     * ink's row centres, which never meet inside the ink — the apex is a flat top about 23 units
     * wide — and legs meeting in a point would crowd the dots either side of it, so as in v they are
     * joined by a flat of exactly one dot spacing a few units under the flat top: `right` starts on
     * `left`'s first dot and runs along the flat before turning down. Each leg runs from its corner
     * of the flat to its row centre just above the flat foot, a whole 13 spacings.
     *
     * The bar's ends land exactly on each leg's tenth dot, so each join reads as one dot and the bar
     * visibly touches both legs; that dot sits 0.5 units above the bar ink's midline, and the bar
     * between them is a whole 7 spacings.
     */
    Exercise(
        id = "english-capital-a",
        title = "A",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "english-capital-a-left",
                points = StrokePoints.line(Point(46.76f, 12.67f), Point(20.7f, 86.24f)),
            ),
            Stroke(
                id = "english-capital-a-right",
                points = StrokePoints.polyline(
                    listOf(
                        Point(46.76f, 12.67f),
                        Point(52.76f, 12.67f),
                        Point(78.82f, 86.24f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-a-bar",
                points = StrokePoints.line(Point(28.73f, 63.57f), Point(70.79f, 63.57f)),
            ),
        ),
    ),
    /**
     * B is taller than it is wide (aspect 0.765), so it is fitted by height.
     *
     * Inter's B is a straight stem with two bowls, the lower one wider, joined by a thick middle
     * bar where the two bowls merge. Written as taught: `stem` top to bottom, then `upper` from the
     * top of the stem round the upper bowl to the middle, then `lower` from the middle round the
     * lower bowl to the foot. Each bowl's curve is the centreline of its band, read as the midpoint
     * of the ink along rays cast from the bowl's centre; the rays stop where the band runs into the
     * merged middle bar, which has no single centreline of its own.
     *
     * The stem's dots at y 12, 48 and 84 lie within 2 units of the top, middle and bottom bars'
     * midlines, so each bowl starts and ends exactly on a stem dot. Both bowls share the middle bar
     * from the stem to its fifth dot, so each dot there is one dot of both strokes, and they branch
     * at 51°, far enough apart that no dots crowd. Each bowl is a whole number of spacings long, so
     * its last dot lands on the stem.
     */
    Exercise(
        id = "english-capital-b",
        title = "B",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "english-capital-b-stem",
                points = StrokePoints.line(Point(26.75f, 12f), Point(26.75f, 84.02f)),
            ),
            Stroke(
                id = "english-capital-b-upper",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26.75f, 12f),
                        Point(29.75f, 12f),
                        Point(32.75f, 12f),
                        Point(35.75f, 12f),
                        Point(38.75f, 12.05f),
                        Point(41.74f, 12.21f),
                        Point(44.73f, 12.52f),
                        Point(47.69f, 13.01f),
                        Point(50.6f, 13.72f),
                        Point(53.47f, 14.6f),
                        Point(56.33f, 15.49f),
                        Point(59.19f, 16.4f),
                        Point(61.92f, 17.65f),
                        Point(64.42f, 19.29f),
                        Point(66.55f, 21.39f),
                        Point(68.1f, 23.96f),
                        Point(68.99f, 26.81f),
                        Point(69.21f, 29.8f),
                        Point(68.8f, 32.76f),
                        Point(67.64f, 35.52f),
                        Point(65.76f, 37.85f),
                        Point(63.4f, 39.7f),
                        Point(60.89f, 41.34f),
                        Point(58.38f, 42.99f),
                        Point(55.88f, 44.63f),
                        Point(53.37f, 46.28f),
                        Point(50.86f, 47.93f),
                        Point(47.88f, 48f),
                        Point(44.88f, 48f),
                        Point(41.88f, 48f),
                        Point(38.88f, 48f),
                        Point(35.88f, 48f),
                        Point(32.88f, 48f),
                        Point(29.88f, 48f),
                        Point(26.75f, 48f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-b-lower",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26.75f, 48f),
                        Point(29.75f, 48f),
                        Point(32.75f, 48f),
                        Point(35.75f, 48f),
                        Point(38.75f, 48f),
                        Point(41.75f, 48f),
                        Point(44.75f, 48f),
                        Point(47.75f, 48f),
                        Point(50.75f, 48f),
                        Point(53.49f, 49.22f),
                        Point(56.23f, 50.44f),
                        Point(58.97f, 51.66f),
                        Point(61.72f, 52.87f),
                        Point(64.46f, 54.08f),
                        Point(66.99f, 55.68f),
                        Point(69.15f, 57.75f),
                        Point(70.79f, 60.26f),
                        Point(71.79f, 63.08f),
                        Point(72.11f, 66.06f),
                        Point(71.87f, 69.04f),
                        Point(71.01f, 71.92f),
                        Point(69.53f, 74.51f),
                        Point(67.49f, 76.71f),
                        Point(65.09f, 78.51f),
                        Point(62.41f, 79.84f),
                        Point(59.56f, 80.78f),
                        Point(56.64f, 81.45f),
                        Point(53.71f, 82.1f),
                        Point(50.77f, 82.71f),
                        Point(47.81f, 83.21f),
                        Point(44.84f, 83.57f),
                        Point(41.85f, 83.82f),
                        Point(38.85f, 83.96f),
                        Point(35.85f, 84f),
                        Point(32.85f, 84f),
                        Point(29.85f, 84f),
                        Point(26.75f, 84f),
                    ),
                ),
            ),
        ),
    ),
)
