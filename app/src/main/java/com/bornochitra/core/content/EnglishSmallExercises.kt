package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * English small-letter stroke geometry (plan.md Step 3). Every letter is read off its rendered
 * glyph's centreline, never approximated by eye, the way the Bengali letters are.
 *
 * Font: **Andika Bold** (SIL International, SIL Open Font License 1.1), bundled as
 * `res/font/andika_bold.ttf` and used for every English letter the app draws — the reference glyph
 * above the canvas, the category grid and the progress list (`BcLatinLetterFontFamily`). Andika is
 * a print font designed for children learning to read: its `a`, `g` and `y` are the single-storey
 * shapes taught in school, where the phone's own Roboto draws `a` and `g` double-storey. Bold is
 * the weight the app's letter styles ask for, so the guide is derived from the exact glyph shown.
 */
internal val englishSmallExercises: List<Exercise> = listOf(
    /**
     * a is a shade wider than it is tall (aspect 1.005), but height-fitting leaves its ink at
     * x 8..92, comfortably inside the canvas, so it is fitted by height like প and ম.
     *
     * Andika's single-storey a is a bowl closed onto a straight stem: the bowl leaves the stem's top
     * corner, runs left over the top, down the left side and round the bottom, then climbs the
     * diagonal back into the stem at mid-height. That is written as it is taught — round first,
     * counter-clockwise, then the stem top to bottom. The bowl is about 167 canvas units in one
     * movement, more than a child can hold in one pass, and it turns smoothly all the way round, so
     * it is split at its lowest point, where the letter sits on the writing line — the split ত and ঢ
     * take — leaving `arc` 112 units and `bowl` 56; the two share the dot at the foot.
     *
     * The stem is almost straight (x 75.6 over its upper half, leaning out to 80 at the foot), so it
     * is taken from the ink's row centres rather than the skeleton, which turns into the bowl at the
     * top and runs off into the foot's outer corner at the bottom. It starts 3.5 units inside its
     * flat top and stops 3 units above its flat foot, so both end dots sit wholly on the ink.
     */
    Exercise(
        id = "english-small-a",
        title = "a",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "english-small-a-arc",
                points = StrokePoints.polyline(
                    listOf(
                        Point(74f, 20f),
                        Point(73f, 18f),
                        Point(70f, 17f),
                        Point(67f, 17f),
                        Point(64f, 16f),
                        Point(61f, 16f),
                        Point(59f, 16f),
                        Point(55f, 16f),
                        Point(53f, 16f),
                        Point(50f, 16f),
                        Point(47f, 17f),
                        Point(44f, 17f),
                        Point(41f, 18f),
                        Point(39f, 19f),
                        Point(36f, 20f),
                        Point(34f, 21f),
                        Point(32f, 23f),
                        Point(29f, 25f),
                        Point(28f, 27f),
                        Point(26f, 30f),
                        Point(25f, 32f),
                        Point(24f, 35f),
                        Point(23f, 37f),
                        Point(22f, 40f),
                        Point(21f, 43f),
                        Point(21f, 46f),
                        Point(20f, 48f),
                        Point(20f, 51f),
                        Point(20f, 54f),
                        Point(20f, 57f),
                        Point(20f, 60f),
                        Point(20f, 63f),
                        Point(21f, 66f),
                        Point(21f, 68f),
                        Point(22f, 71f),
                        Point(24f, 73f),
                        Point(25f, 76f),
                        Point(27f, 78f),
                        Point(30f, 79f),
                        Point(32f, 80f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-a-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(32f, 80f),
                        Point(35f, 79f),
                        Point(38f, 79f),
                        Point(41f, 78f),
                        Point(43f, 77f),
                        Point(46f, 77f),
                        Point(48f, 75f),
                        Point(50f, 73f),
                        Point(52f, 71f),
                        Point(54f, 69f),
                        Point(56f, 67f),
                        Point(57f, 65f),
                        Point(59f, 62f),
                        Point(60f, 60f),
                        Point(61f, 58f),
                        Point(63f, 56f),
                        Point(65f, 54f),
                        Point(68f, 53f),
                        Point(70f, 52f),
                        Point(72f, 51f),
                        Point(75f, 49f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-a-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(76f, 12f),
                        Point(76f, 33f),
                        Point(77f, 63f),
                        Point(80f, 85f),
                    ),
                ),
            ),
        ),
    ),
)
