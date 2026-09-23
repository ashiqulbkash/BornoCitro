package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Bengali number stroke geometry (plan.md Step 5): the numbers ১-২০, ordered by value so a run of
 * numbers can be read straight off the catalog.
 *
 * Font: **Noto Sans Bengali v2.001 Bold** — the phone's own variable `NotoSansBengali-VF.ttf` at
 * weight 700, which is how the Practice heading (`displayMedium`, Bold) draws a Bengali title; the app
 * sets no font of its own for it, so the reference glyph above the canvas is the face every guide is
 * read from (v3 redraws some glyphs, and weight 400 is visibly thinner). Each number is read off its
 * rendered glyph's centreline, never drawn by eye, fitted on its own (y 7..90, or by width for a
 * glyph wider than tall), and every stroke is a whole number of dot spacings.
 */
internal val banglaNumberExercises: List<Exercise> = listOf(
    /**
     * ১ is taller than it is wide (aspect 0.681), so it is fitted by height. It is one movement with no
     * junction: from the slanted cut at the top down the short stem, along the long diagonal to the right,
     * down the right side, round the bottom and up into the hook, whose rounded end sits inside the bowl.
     * At ~138 canvas units it is held in one pass, so `body` is the whole numeral, a whole 23 spacings on
     * the centreline, trimmed so its first and last dots sit a full dot inside the two ends.
     */
    Exercise(
        id = "bangla-number-1",
        title = "১",
        type = ExerciseType.BANGLA_NUMBER,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(
            Stroke(
                id = "bangla-number-1-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(28.59f, 12.39f),
                        Point(30.16f, 14.87f),
                        Point(31.28f, 17.57f),
                        Point(31.5f, 20.49f),
                        Point(31.64f, 23.42f),
                        Point(32.34f, 26.26f),
                        Point(33.7f, 28.86f),
                        Point(35.55f, 31.14f),
                        Point(37.69f, 33.14f),
                        Point(40.01f, 34.95f),
                        Point(42.41f, 36.63f),
                        Point(44.86f, 38.25f),
                        Point(47.35f, 39.81f),
                        Point(49.84f, 41.36f),
                        Point(52.32f, 42.93f),
                        Point(54.78f, 44.53f),
                        Point(57.2f, 46.19f),
                        Point(59.55f, 47.96f),
                        Point(61.78f, 49.86f),
                        Point(63.85f, 51.95f),
                        Point(65.67f, 54.25f),
                        Point(67.15f, 56.78f),
                        Point(68.22f, 59.51f),
                        Point(68.85f, 62.38f),
                        Point(69.06f, 65.3f),
                        Point(68.82f, 68.23f),
                        Point(68.07f, 71.06f),
                        Point(66.77f, 73.69f),
                        Point(64.98f, 76.01f),
                        Point(62.79f, 77.96f),
                        Point(60.31f, 79.52f),
                        Point(57.63f, 80.71f),
                        Point(54.81f, 81.53f),
                        Point(51.92f, 82.03f),
                        Point(48.99f, 82.24f),
                        Point(46.06f, 82.19f),
                        Point(43.14f, 81.86f),
                        Point(40.29f, 81.15f),
                        Point(37.6f, 80f),
                        Point(35.19f, 78.33f),
                        Point(33.23f, 76.15f),
                        Point(31.93f, 73.53f),
                        Point(31.43f, 70.65f),
                        Point(31.76f, 67.74f),
                        Point(32.88f, 65.04f),
                        Point(34.61f, 62.67f),
                        Point(36.7f, 60.61f),
                        Point(38.94f, 58.44f),
                    ),
                ),
            ),
        ),
        order = 1,
    ),
    /**
     * ২ is taller than it is wide (aspect 0.783), so it is fitted by height. The glyph is a hook over a
     * base: the base is one band from its pointed left end, through where the hook comes in, down to the
     * tail's slanted cut, and the hook — top cut, across the shoulder, down the right side — runs into
     * that band from above. So `hook` is written first (13 spacings) and ends exactly on the base's fifth
     * dot, where the curve meets the band, and `base` is the one long movement along the bottom, a whole
     * 9 spacings. The band is centred with the merged junction left out, so the base does not kink where
     * the hook joins it.
     */
    Exercise(
        id = "bangla-number-2",
        title = "২",
        type = ExerciseType.BANGLA_NUMBER,
        difficulty = Difficulty.BEGINNER,
        strokes = listOf(
            Stroke(
                id = "bangla-number-2-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(32.72f, 17.11f),
                        Point(34.48f, 19.67f),
                        Point(35.79f, 22.37f),
                        Point(37.67f, 24.69f),
                        Point(40.09f, 26.45f),
                        Point(42.8f, 27.73f),
                        Point(45.64f, 28.69f),
                        Point(48.54f, 29.46f),
                        Point(51.46f, 30.17f),
                        Point(54.36f, 30.93f),
                        Point(57.22f, 31.84f),
                        Point(59.98f, 33.01f),
                        Point(62.55f, 34.55f),
                        Point(64.8f, 36.53f),
                        Point(66.51f, 38.98f),
                        Point(67.57f, 41.78f),
                        Point(67.99f, 44.74f),
                        Point(67.8f, 47.73f),
                        Point(66.99f, 50.61f),
                        Point(65.54f, 53.23f),
                        Point(63.56f, 55.48f),
                        Point(61.2f, 57.32f),
                        Point(58.63f, 58.87f),
                        Point(56f, 60.31f),
                        Point(53.36f, 61.75f),
                        Point(50.73f, 63.19f),
                        Point(48.06f, 64.55f),
                    ),
                ),
            ),
            Stroke(
                id = "bangla-number-2-base",
                points = StrokePoints.polyline(
                    listOf(
                        Point(24.61f, 59.73f),
                        Point(27.57f, 60.23f),
                        Point(30.53f, 60.71f),
                        Point(33.49f, 61.16f),
                        Point(36.46f, 61.6f),
                        Point(39.42f, 62.1f),
                        Point(42.36f, 62.71f),
                        Point(45.25f, 63.51f),
                        Point(48.06f, 64.55f),
                        Point(50.76f, 65.85f),
                        Point(53.33f, 67.39f),
                        Point(55.78f, 69.13f),
                        Point(58.12f, 71.01f),
                        Point(60.39f, 72.97f),
                        Point(62.62f, 74.97f),
                        Point(64.82f, 77.01f),
                        Point(67f, 79.07f),
                        Point(69.19f, 81.12f),
                        Point(71.41f, 83.18f),
                    ),
                ),
            ),
        ),
        order = 2,
    ),
    /**
     * ৩ is a shade narrower than tall (aspect 0.981), so it is fitted by height. It is one band with no
     * junction: from the slanted cut at the top left, down the left side, round the bottom, up the right,
     * over the top and down into the slanted inner end. At ~190 canvas units that is too long for one
     * pass, so it is split at its lowest point, where the pen turns from going down to going up: `down`
     * runs from the top-left cut to that point (13 spacings) and `up` carries on from the same dot to the
     * inner end (19 spacings). The whole band was trimmed to whole spacings before the split, so both
     * halves are whole and share the dot at the bottom.
     */
    Exercise(
        id = "bangla-number-3",
        title = "৩",
        type = ExerciseType.BANGLA_NUMBER,
        difficulty = Difficulty.INTERMEDIATE,
        strokes = listOf(
            Stroke(
                id = "bangla-number-3-down",
                points = StrokePoints.polyline(
                    listOf(
                        Point(14.61f, 27.25f),
                        Point(17.49f, 28.05f),
                        Point(19.56f, 30.15f),
                        Point(20.56f, 32.97f),
                        Point(21.27f, 35.89f),
                        Point(21.99f, 38.8f),
                        Point(22.74f, 41.71f),
                        Point(23.53f, 44.6f),
                        Point(24.39f, 47.47f),
                        Point(25.31f, 50.33f),
                        Point(26.3f, 53.16f),
                        Point(27.37f, 55.96f),
                        Point(28.52f, 58.73f),
                        Point(29.78f, 61.46f),
                        Point(31.16f, 64.12f),
                        Point(32.67f, 66.71f),
                        Point(34.36f, 69.19f),
                        Point(36.24f, 71.53f),
                        Point(38.3f, 73.7f),
                        Point(40.57f, 75.67f),
                        Point(43.05f, 77.36f),
                        Point(45.7f, 78.75f),
                        Point(48.5f, 79.82f),
                        Point(51.4f, 80.6f),
                        Point(54.36f, 81.09f),
                        Point(57.35f, 81.28f),
                        Point(60.45f, 81.18f),
                    ),
                ),
            ),
            Stroke(
                id = "bangla-number-3-up",
                points = StrokePoints.polyline(
                    listOf(
                        Point(60.45f, 81.18f),
                        Point(63.32f, 80.78f),
                        Point(66.22f, 80.01f),
                        Point(68.97f, 78.84f),
                        Point(71.51f, 77.24f),
                        Point(73.73f, 75.24f),
                        Point(75.61f, 72.9f),
                        Point(77.13f, 70.31f),
                        Point(78.3f, 67.56f),
                        Point(79.19f, 64.69f),
                        Point(79.84f, 61.76f),
                        Point(80.31f, 58.8f),
                        Point(80.62f, 55.82f),
                        Point(80.78f, 52.82f),
                        Point(80.81f, 49.82f),
                        Point(80.69f, 46.82f),
                        Point(80.44f, 43.83f),
                        Point(80.03f, 40.86f),
                        Point(79.47f, 37.92f),
                        Point(78.75f, 35.01f),
                        Point(77.86f, 32.14f),
                        Point(76.76f, 29.35f),
                        Point(75.44f, 26.66f),
                        Point(73.86f, 24.11f),
                        Point(72.01f, 21.75f),
                        Point(69.87f, 19.65f),
                        Point(67.44f, 17.9f),
                        Point(64.74f, 16.59f),
                        Point(61.86f, 15.78f),
                        Point(58.88f, 15.48f),
                        Point(55.89f, 15.68f),
                        Point(52.98f, 16.39f),
                        Point(50.27f, 17.66f),
                        Point(47.92f, 19.52f),
                        Point(46.14f, 21.92f),
                        Point(45.12f, 24.73f),
                        Point(44.78f, 27.71f),
                        Point(44.86f, 30.71f),
                        Point(45f, 33.92f),
                    ),
                ),
            ),
        ),
        order = 3,
    ),
    /**
     * ৪ is taller than it is wide (aspect 0.824), so it is fitted by height. It is two rings whose bands
     * cross at the waist like an 8 — the upper-left arm runs on into the lower-right one — so it is
     * written the way Step 4's 8 is: two laps through the crossing (50, 46), each scaled about it to whole
     * spacings (upper 20, lower 22). `s` runs from the top down the upper left, through the crossing and
     * round the lower right to the bottom; `back` returns up the lower left, through the crossing and up
     * the upper right to the top. Both are 21 spacings and the crossing is a dot of each. The glyph's arms
     * meet there at a shallow angle, which crowds the dots either side of the crossing, so the guide
     * crosses a little steeper inside the solid waist (14 units tall at the crossing).
     */
    Exercise(
        id = "bangla-number-4",
        title = "৪",
        type = ExerciseType.BANGLA_NUMBER,
        difficulty = Difficulty.INTERMEDIATE,
        strokes = listOf(
            Stroke(
                id = "bangla-number-4-s",
                points = StrokePoints.polyline(
                    listOf(
                        Point(49.76f, 14.93f),
                        Point(46.75f, 15.02f),
                        Point(43.76f, 15.31f),
                        Point(40.8f, 15.83f),
                        Point(37.9f, 16.61f),
                        Point(35.1f, 17.72f),
                        Point(32.5f, 19.23f),
                        Point(30.21f, 21.16f),
                        Point(28.39f, 23.54f),
                        Point(27.28f, 26.31f),
                        Point(27.04f, 29.29f),
                        Point(27.68f, 32.21f),
                        Point(29.08f, 34.86f),
                        Point(31.04f, 37.13f),
                        Point(33.44f, 38.93f),
                        Point(36.13f, 40.25f),
                        Point(38.94f, 41.33f),
                        Point(41.75f, 42.4f),
                        Point(44.59f, 43.4f),
                        Point(47.31f, 44.66f),
                        Point(50f, 46f),
                        Point(52.69f, 47.34f),
                        Point(55.4f, 48.63f),
                        Point(58.21f, 49.7f),
                        Point(61f, 50.8f),
                        Point(63.79f, 51.93f),
                        Point(66.48f, 53.26f),
                        Point(68.96f, 54.96f),
                        Point(71.11f, 57.04f),
                        Point(72.84f, 59.5f),
                        Point(73.94f, 62.28f),
                        Point(74.38f, 65.24f),
                        Point(74.2f, 68.23f),
                        Point(73.42f, 71.12f),
                        Point(72f, 73.76f),
                        Point(70.01f, 76f),
                        Point(67.62f, 77.81f),
                        Point(64.96f, 79.21f),
                        Point(62.14f, 80.24f),
                        Point(59.23f, 80.95f),
                        Point(56.25f, 81.4f),
                        Point(53.26f, 81.64f),
                        Point(50.25f, 81.72f),
                    ),
                ),
            ),
            Stroke(
                id = "bangla-number-4-back",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50.25f, 81.72f),
                        Point(47.24f, 81.65f),
                        Point(44.24f, 81.41f),
                        Point(41.27f, 80.95f),
                        Point(38.35f, 80.23f),
                        Point(35.54f, 79.18f),
                        Point(32.88f, 77.78f),
                        Point(30.46f, 76f),
                        Point(28.4f, 73.83f),
                        Point(26.82f, 71.27f),
                        Point(25.89f, 68.43f),
                        Point(25.67f, 65.45f),
                        Point(26.17f, 62.5f),
                        Point(27.34f, 59.74f),
                        Point(29.06f, 57.28f),
                        Point(31.17f, 55.16f),
                        Point(33.61f, 53.39f),
                        Point(36.24f, 51.95f),
                        Point(38.98f, 50.7f),
                        Point(41.77f, 49.56f),
                        Point(44.57f, 48.58f),
                        Point(47.31f, 47.34f),
                        Point(50f, 46f),
                        Point(52.69f, 44.66f),
                        Point(55.42f, 43.4f),
                        Point(58.23f, 42.37f),
                        Point(61.02f, 41.23f),
                        Point(63.77f, 40.01f),
                        Point(66.39f, 38.54f),
                        Point(68.74f, 36.67f),
                        Point(70.68f, 34.39f),
                        Point(72.07f, 31.74f),
                        Point(72.74f, 28.82f),
                        Point(72.51f, 25.84f),
                        Point(71.36f, 23.09f),
                        Point(69.48f, 20.76f),
                        Point(67.12f, 18.91f),
                        Point(64.48f, 17.48f),
                        Point(61.66f, 16.43f),
                        Point(58.74f, 15.72f),
                        Point(55.77f, 15.27f),
                        Point(52.77f, 15.01f),
                        Point(49.76f, 14.93f),
                    ),
                ),
            ),
        ),
        order = 4,
    ),
    /**
     * ৫ is taller than it is wide (aspect 0.886), so it is fitted by height. It is three movements: the
     * big outer curve, the hook at the top right, and the inner S-curve. `outer` runs from the slanted
     * top cut down the left, round the bottom to the slanted cut at the lower right (22 spacings).
     * `hook` leaves it on its second dot, where the notch side parts from the curve, and runs down the
     * notch and right to the ear's cut (5 spacings). `inner` leaves the hook on its fifth dot and runs
     * down-left and round the S into the outer curve, ending exactly on its second-last dot (8 spacings):
     * in the glyph the S merges into the outer curve and the two end as one terminal, so the guide joins
     * them there and only `outer` runs on to the cut. The join sits where the S's approach keeps every
     * dot beside it at least 5 units from the outer curve's dots.
     */
    Exercise(
        id = "bangla-number-5",
        title = "৫",
        type = ExerciseType.BANGLA_NUMBER,
        difficulty = Difficulty.INTERMEDIATE,
        strokes = listOf(
            Stroke(
                id = "bangla-number-5-outer",
                points = StrokePoints.polyline(
                    listOf(
                        Point(56.86f, 13.67f),
                        Point(57.45f, 16.6f),
                        Point(56.42f, 19.28f),
                        Point(53.86f, 20.82f),
                        Point(51.08f, 21.93f),
                        Point(48.37f, 23.22f),
                        Point(45.79f, 24.75f),
                        Point(43.28f, 26.4f),
                        Point(40.82f, 28.12f),
                        Point(38.43f, 29.92f),
                        Point(36.11f, 31.82f),
                        Point(33.88f, 33.83f),
                        Point(31.77f, 35.96f),
                        Point(29.79f, 38.22f),
                        Point(27.96f, 40.59f),
                        Point(26.32f, 43.1f),
                        Point(24.92f, 45.76f),
                        Point(23.79f, 48.53f),
                        Point(22.97f, 51.42f),
                        Point(22.47f, 54.37f),
                        Point(22.28f, 57.37f),
                        Point(22.4f, 60.36f),
                        Point(22.84f, 63.33f),
                        Point(23.64f, 66.22f),
                        Point(24.81f, 68.98f),
                        Point(26.36f, 71.54f),
                        Point(28.26f, 73.86f),
                        Point(30.45f, 75.91f),
                        Point(32.88f, 77.66f),
                        Point(35.5f, 79.12f),
                        Point(38.27f, 80.29f),
                        Point(41.13f, 81.19f),
                        Point(44.05f, 81.85f),
                        Point(47.02f, 82.28f),
                        Point(50.01f, 82.5f),
                        Point(53.01f, 82.55f),
                        Point(56.01f, 82.45f),
                        Point(58.99f, 82.16f),
                        Point(61.92f, 81.5f),
                        Point(64.65f, 80.28f),
                        Point(67.27f, 78.81f),
                        Point(69.98f, 77.54f),
                        Point(72.83f, 76.61f),
                        Point(75.78f, 76.07f),
                        Point(78.97f, 75.6f),
                    ),
                ),
            ),
            Stroke(
                id = "bangla-number-5-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(56.3f, 19.35f),
                        Point(57.89f, 20.85f),
                        Point(59.53f, 23.02f),
                        Point(60.94f, 25.36f),
                        Point(62.17f, 27.79f),
                        Point(63.55f, 30.14f),
                        Point(65.28f, 32.25f),
                        Point(67.24f, 34.14f),
                        Point(69.4f, 35.81f),
                        Point(71.81f, 37.07f),
                        Point(74.42f, 37.84f),
                        Point(77.6f, 38.77f),
                    ),
                ),
            ),
            Stroke(
                id = "bangla-number-5-inner",
                points = StrokePoints.polyline(
                    listOf(
                        Point(71.82f, 37.07f),
                        Point(70.13f, 38.72f),
                        Point(68.61f, 41.15f),
                        Point(67.13f, 43.6f),
                        Point(65.46f, 45.89f),
                        Point(63.72f, 48.14f),
                        Point(62.16f, 50.52f),
                        Point(60.92f, 53.13f),
                        Point(60.15f, 55.93f),
                        Point(59.92f, 58.86f),
                        Point(60.33f, 61.76f),
                        Point(61.43f, 64.46f),
                        Point(63.03f, 66.83f),
                        Point(64.92f, 68.94f),
                        Point(66.91f, 70.92f),
                        Point(68.97f, 72.84f),
                        Point(71.07f, 74.68f),
                        Point(73.03f, 76.57f),
                    ),
                ),
            ),
        ),
        order = 5,
    ),
)
