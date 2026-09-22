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
    /**
     * C is taller than it is wide (aspect 0.872), so it is fitted by height.
     *
     * Inter's C is one counter-clockwise curve between two horizontal flat cuts, symmetric about
     * y 48.5. Its centreline is 168 canvas units, past what a child holds in one pass, and it turns
     * smoothly all the way, so it is split at its leftmost point, where the pen travels straight
     * down (small c's split): `top` from the upper terminal, over the top and down to the split,
     * then `bottom` on round the bottom to the lower terminal. The curve is the midpoint of the ink
     * along rays from the bowl's centre; near each terminal, where the band runs almost vertically
     * into its cut, it is the ink's row centre instead. `bottom` is `top` mirrored, so the two
     * strokes share the dot at the split.
     *
     * Each end stops 3.6 units inside its cut, where the stroke is a whole 14 spacings, so the last
     * dot lands on the split and on the lower terminal.
     */
    Exercise(
        id = "english-capital-c",
        title = "C",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "english-capital-c-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(76.94f, 32.1f),
                        Point(75.82f, 29.32f),
                        Point(74.53f, 26.62f),
                        Point(72.86f, 24.12f),
                        Point(70.93f, 21.83f),
                        Point(68.73f, 19.8f),
                        Point(66.25f, 18.11f),
                        Point(63.6f, 16.71f),
                        Point(60.78f, 15.67f),
                        Point(57.88f, 14.93f),
                        Point(54.91f, 14.48f),
                        Point(51.92f, 14.34f),
                        Point(48.92f, 14.44f),
                        Point(45.95f, 14.82f),
                        Point(43.03f, 15.51f),
                        Point(40.2f, 16.5f),
                        Point(37.5f, 17.8f),
                        Point(34.94f, 19.38f),
                        Point(32.61f, 21.26f),
                        Point(30.5f, 23.39f),
                        Point(28.62f, 25.72f),
                        Point(27.01f, 28.25f),
                        Point(25.63f, 30.91f),
                        Point(24.5f, 33.69f),
                        Point(23.62f, 36.56f),
                        Point(22.96f, 39.49f),
                        Point(22.51f, 42.45f),
                        Point(22.27f, 45.44f),
                        Point(22.17f, 48.5f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-c-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(22.17f, 48.5f),
                        Point(22.27f, 51.56f),
                        Point(22.51f, 54.55f),
                        Point(22.96f, 57.51f),
                        Point(23.62f, 60.44f),
                        Point(24.5f, 63.31f),
                        Point(25.63f, 66.09f),
                        Point(27.01f, 68.75f),
                        Point(28.62f, 71.28f),
                        Point(30.5f, 73.61f),
                        Point(32.61f, 75.74f),
                        Point(34.94f, 77.62f),
                        Point(37.5f, 79.2f),
                        Point(40.2f, 80.5f),
                        Point(43.03f, 81.49f),
                        Point(45.95f, 82.18f),
                        Point(48.92f, 82.56f),
                        Point(51.92f, 82.66f),
                        Point(54.91f, 82.52f),
                        Point(57.88f, 82.07f),
                        Point(60.78f, 81.33f),
                        Point(63.6f, 80.29f),
                        Point(66.25f, 78.89f),
                        Point(68.73f, 77.2f),
                        Point(70.93f, 75.17f),
                        Point(72.86f, 72.88f),
                        Point(74.53f, 70.38f),
                        Point(75.82f, 67.68f),
                        Point(76.94f, 64.9f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * D is taller than it is wide (aspect 0.839), so it is fitted by height.
     *
     * Inter's D is a straight stem and one bowl that leaves the stem's top, runs flat along the top
     * bar, round the right side and back flat along the bottom bar into the stem's foot. The bowl's
     * curve is the centreline of its band, read as the midpoint of the ink along rays cast from the
     * counter's centroid; every ray from -90 to +90 degrees crosses a run of 14.8 to 17.0 units, so
     * the band stands alone over the whole sweep and no ray has to be dropped.
     *
     * The whole bowl is 148 canvas units, more than a child holds in one pass, and it turns smoothly
     * from end to end, so it is split at its rightmost point, where the pen travels straight down --
     * C's split at its own leftmost point, and the only landmark on the curve a child can see.
     * `bottom` is `top` mirrored about y 48.5, which the glyph is symmetric about to within 0.16
     * units, so the two strokes share the dot at the split.
     *
     * The stem runs between the two join points, y 15.5 to 81.5: a whole 11 spacings apart and
     * 1.17 units off each bar's midline, which a 14.66-unit bar swallows. Each half of the bowl is a
     * whole 12 spacings, so `top` starts on the stem's first dot and `bottom` ends on its last.
     */
    Exercise(
        id = "english-capital-d",
        title = "D",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "english-capital-d-stem",
                points = StrokePoints.line(Point(23.69f, 15.5f), Point(23.69f, 81.52f)),
            ),
            Stroke(
                id = "english-capital-d-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(23.69f, 15.5f),
                        Point(26.69f, 15.5f),
                        Point(29.69f, 15.5f),
                        Point(32.69f, 15.5f),
                        Point(35.69f, 15.5f),
                        Point(38.69f, 15.5f),
                        Point(41.69f, 15.52f),
                        Point(44.69f, 15.55f),
                        Point(47.69f, 15.62f),
                        Point(50.67f, 15.92f),
                        Point(53.61f, 16.51f),
                        Point(56.49f, 17.33f),
                        Point(59.27f, 18.46f),
                        Point(61.93f, 19.84f),
                        Point(64.38f, 21.57f),
                        Point(66.61f, 23.58f),
                        Point(68.58f, 25.84f),
                        Point(70.27f, 28.31f),
                        Point(71.68f, 30.96f),
                        Point(72.85f, 33.72f),
                        Point(73.74f, 36.59f),
                        Point(74.39f, 39.51f),
                        Point(74.82f, 42.48f),
                        Point(75.06f, 45.47f),
                        Point(75.16f, 48.5f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-d-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(75.16f, 48.5f),
                        Point(75.06f, 51.53f),
                        Point(74.82f, 54.52f),
                        Point(74.39f, 57.49f),
                        Point(73.74f, 60.41f),
                        Point(72.85f, 63.28f),
                        Point(71.68f, 66.04f),
                        Point(70.27f, 68.69f),
                        Point(68.58f, 71.16f),
                        Point(66.61f, 73.42f),
                        Point(64.38f, 75.43f),
                        Point(61.93f, 77.16f),
                        Point(59.27f, 78.54f),
                        Point(56.49f, 79.67f),
                        Point(53.61f, 80.49f),
                        Point(50.67f, 81.08f),
                        Point(47.69f, 81.38f),
                        Point(44.69f, 81.45f),
                        Point(41.69f, 81.48f),
                        Point(38.69f, 81.5f),
                        Point(35.69f, 81.5f),
                        Point(32.69f, 81.5f),
                        Point(29.69f, 81.5f),
                        Point(26.69f, 81.5f),
                        Point(23.69f, 81.5f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * E is much taller than it is wide (aspect 0.670), so it is fitted by height.
     *
     * Inter's E is a straight stem with three straight arms, every part flat, so each stroke is a
     * line along its ink's centre. It is written as it is taught: `stem` down, then `top`, `middle`
     * and `bottom` left to right out of it.
     *
     * The three arms' midlines are 34.01 and 34.90 apart, neither a multiple of the 6-unit dot
     * spacing, so the stem's dots sit at y 12.5, 48.5 and 84.5 -- 36 apart, a whole 6 spacings each
     * -- and every arm leaves the stem exactly on one of them. That puts an arm at most 1.55 units
     * off its own midline, inside the 2 units B's bowls already take, and a 14-unit arm swallows it.
     *
     * Each arm is a whole 7 spacings; 8 would carry the last dot past the ink. The middle arm's ink
     * is the shortest of the three and so fixes the stem at x 30.25, the rightmost quarter-unit that
     * still leaves that arm's last dot a full 2.5-unit dot radius inside its flat cut. The top and
     * bottom arms then stop 5.5 units inside their own cuts, the price of arms that are all the same
     * length -- which Inter's are to within 3 units.
     */
    Exercise(
        id = "english-capital-e",
        title = "E",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "english-capital-e-stem",
                points = StrokePoints.line(Point(30.25f, 12.5f), Point(30.25f, 84.52f)),
            ),
            Stroke(
                id = "english-capital-e-top",
                points = StrokePoints.line(Point(30.25f, 12.5f), Point(72.27f, 12.5f)),
            ),
            Stroke(
                id = "english-capital-e-middle",
                points = StrokePoints.line(Point(30.25f, 48.5f), Point(72.27f, 48.5f)),
            ),
            Stroke(
                id = "english-capital-e-bottom",
                points = StrokePoints.line(Point(30.25f, 84.5f), Point(72.27f, 84.5f)),
            ),
        ),
    ),
    /**
     * F is much taller than it is wide (aspect 0.658), so it is fitted by height.
     *
     * Inter's F is E without the bottom arm: a straight stem carrying two straight arms, so each
     * stroke is a line along its ink's centre. Written as taught: `stem` down, then `top` and
     * `middle` left to right out of it.
     *
     * The two arms' midlines are 37.19 apart, so as in E the stem's dots are put a whole 6 spacings
     * apart, at y 14.5 and 50.5, and each arm leaves the stem exactly on one of them, at most 0.73
     * units off its own midline. The stem runs on past the middle arm to stop 3.5 units above the
     * foot, a whole 12 spacings in all.
     *
     * Unlike E's, F's two arms are not the same length -- the middle arm's ink stops 3.67 units
     * short of the top one's -- and they cannot both be 7 spacings without the middle arm's last dot
     * leaving the ink. So the top arm is 7 spacings and the middle 6, which keeps the glyph's own
     * order and both last dots a full dot radius inside their cuts.
     */
    Exercise(
        id = "english-capital-f",
        title = "F",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "english-capital-f-stem",
                points = StrokePoints.line(Point(31.25f, 14.5f), Point(31.25f, 86.52f)),
            ),
            Stroke(
                id = "english-capital-f-top",
                points = StrokePoints.line(Point(31.25f, 14.5f), Point(73.27f, 14.5f)),
            ),
            Stroke(
                id = "english-capital-f-middle",
                points = StrokePoints.line(Point(31.25f, 50.5f), Point(67.27f, 50.5f)),
            ),
        ),
    ),
    /**
     * G is taller than it is wide (aspect 0.879), so it is fitted by height.
     *
     * Inter's G is C's bowl -- the same counter-clockwise curve, symmetric about y 48.5, with a
     * horizontal flat cut at the top terminal -- carried on up a short vertical spur into a bar that
     * runs left from the right edge. It is written as taught, "big curve, up and in", and split as C
     * is, near the leftmost point where the pen travels straight down: `top` from the terminal over
     * the top to the split, then `bottom` round the bottom, up the spur and left along the bar. The
     * curve is the midpoint of the ink along rays from the bowl's centre; past the bottom the rays
     * come from a lower centre so they cross the lower right of the bowl rather than the bar.
     *
     * The corner where the spur turns into the bar is sharp, as the glyph's is, and carries a dot of
     * its own. The spur runs at x 79.5, 1.1 units right of its ink centre, so that the bar is a whole
     * 4 spacings and its last dot still sits a full dot radius inside the bar's cut. Both strokes
     * are whole spacings -- `top` 14 and `bottom` 20 -- by sliding the split 3.5 degrees below the
     * leftmost point and solving the corner's height, which lands on the bar's midline.
     */
    Exercise(
        id = "english-capital-g",
        title = "G",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 7,
        strokes = listOf(
            Stroke(
                id = "english-capital-g-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(76.23f, 30.68f),
                        Point(74.97f, 27.96f),
                        Point(73.43f, 25.38f),
                        Point(71.63f, 22.99f),
                        Point(69.51f, 20.87f),
                        Point(67.17f, 18.99f),
                        Point(64.61f, 17.44f),
                        Point(61.86f, 16.22f),
                        Point(59.01f, 15.3f),
                        Point(56.07f, 14.74f),
                        Point(53.08f, 14.45f),
                        Point(50.08f, 14.45f),
                        Point(47.09f, 14.68f),
                        Point(44.14f, 15.21f),
                        Point(41.27f, 16.09f),
                        Point(38.51f, 17.25f),
                        Point(35.9f, 18.73f),
                        Point(33.46f, 20.48f),
                        Point(31.25f, 22.5f),
                        Point(29.27f, 24.75f),
                        Point(27.5f, 27.17f),
                        Point(26.02f, 29.78f),
                        Point(24.78f, 32.51f),
                        Point(23.8f, 35.34f),
                        Point(23.04f, 38.25f),
                        Point(22.5f, 41.2f),
                        Point(22.14f, 44.18f),
                        Point(21.99f, 47.17f),
                        Point(21.98f, 50.21f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-g-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(21.98f, 50.21f),
                        Point(22.2f, 53.21f),
                        Point(22.55f, 56.18f),
                        Point(23.1f, 59.13f),
                        Point(23.91f, 62.02f),
                        Point(24.92f, 64.84f),
                        Point(26.2f, 67.56f),
                        Point(27.74f, 70.13f),
                        Point(29.49f, 72.56f),
                        Point(31.51f, 74.78f),
                        Point(33.76f, 76.76f),
                        Point(36.21f, 78.49f),
                        Point(38.86f, 79.89f),
                        Point(41.65f, 80.98f),
                        Point(44.54f, 81.79f),
                        Point(47.5f, 82.29f),
                        Point(50.49f, 82.52f),
                        Point(53.49f, 82.49f),
                        Point(56.48f, 82.23f),
                        Point(59.42f, 81.67f),
                        Point(62.3f, 80.82f),
                        Point(65.06f, 79.66f),
                        Point(67.68f, 78.19f),
                        Point(70.08f, 76.4f),
                        Point(72.21f, 74.3f),
                        Point(74.06f, 71.94f),
                        Point(75.6f, 69.36f),
                        Point(76.77f, 66.6f),
                        Point(77.81f, 63.79f),
                        Point(79.5f, 60f),
                        Point(79.5f, 52.1f),
                        Point(55.48f, 52.1f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * H is taller than it is wide (aspect 0.845), so it is fitted by height.
     *
     * Inter's H is two straight stems and a straight crossbar, every part flat, so each stroke is a
     * line along its ink's centre. Written as taught: `left` down, `right` down, then `bar` left to
     * right between them.
     *
     * The bar must leave and reach the stems exactly on a stem dot, so the stems are a whole 9
     * spacings apart, at x 23 and 77 -- each 0.445 units off its own ink centre, symmetric about the
     * middle. Each stem is a whole 12 spacings, y 12 to 84, placed so that its seventh dot sits on
     * the bar, 0.4 units under the bar's midline. 13 spacings would reach a dot radius from both cuts
     * but put the bar's dots 2.1 units above its midline, visibly off Inter's centred bar; 12 keeps
     * the bar where the glyph has it and stops each stem 5 and 6 units inside its cuts, as E's arms
     * stop 5.5 inside theirs.
     */
    Exercise(
        id = "english-capital-h",
        title = "H",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 8,
        strokes = listOf(
            Stroke(
                id = "english-capital-h-left",
                points = StrokePoints.line(Point(23f, 12f), Point(23f, 84.02f)),
            ),
            Stroke(
                id = "english-capital-h-right",
                points = StrokePoints.line(Point(77f, 12f), Point(77f, 84.02f)),
            ),
            Stroke(
                id = "english-capital-h-bar",
                points = StrokePoints.line(Point(23f, 48f), Point(77.02f, 48f)),
            ),
        ),
    ),
    /**
     * I is a single upright slab (aspect 0.205), so it is fitted by height.
     *
     * Inter's I has no serifs: it is one straight stem with flat cuts at the top and the foot, so it
     * is one stroke, `stem`, a line down its ink's centre. Nothing joins it, so it takes the longest
     * whole run that keeps both end dots inside the ink, 13 spacings from y 9.5 to 87.5 -- each end
     * exactly a dot radius inside its cut.
     */
    Exercise(
        id = "english-capital-i",
        title = "I",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 9,
        strokes = listOf(
            Stroke(
                id = "english-capital-i-stem",
                points = StrokePoints.line(Point(50f, 9.5f), Point(50f, 87.52f)),
            ),
        ),
    ),
)
