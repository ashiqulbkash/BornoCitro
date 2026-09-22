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
    /**
     * J is taller than it is wide (aspect 0.657), so it is fitted by height.
     *
     * Inter's J is a straight stem that hooks round the bottom and up into a short left arm ending
     * in a horizontal flat cut. It is written as taught, in one stroke of about 114 units: down the
     * stem, round the bottom and up to the terminal. The stem and the arm are lines along their ink
     * centres; the bowl between them is the midpoint of the ink along rays from (50, 67), the centre
     * that keeps every point furthest inside the band.
     *
     * The stroke ends a dot radius inside the arm's cut, and its start slides down inside the top
     * cut so that it is a whole 19 spacings; 20 would put the first dot past the cut.
     */
    Exercise(
        id = "english-capital-j",
        title = "J",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 10,
        strokes = listOf(
            Stroke(
                id = "english-capital-j-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(69f, 13.43f),
                        Point(69f, 67f),
                        Point(68.41f, 69.94f),
                        Point(67.57f, 72.82f),
                        Point(66.25f, 75.51f),
                        Point(64.39f, 77.86f),
                        Point(62.15f, 79.85f),
                        Point(59.56f, 81.35f),
                        Point(56.74f, 82.36f),
                        Point(53.81f, 83.01f),
                        Point(50.82f, 83.23f),
                        Point(47.82f, 83.16f),
                        Point(44.86f, 82.73f),
                        Point(41.96f, 81.95f),
                        Point(39.26f, 80.65f),
                        Point(36.82f, 78.92f),
                        Point(34.72f, 76.78f),
                        Point(33.1f, 74.26f),
                        Point(31.98f, 71.48f),
                        Point(31.32f, 68.56f),
                        Point(31.1f, 67f),
                        Point(31.1f, 62.07f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * K is taller than it is wide (aspect 0.864), so it is fitted by height.
     *
     * Inter's K is a stem, an arm running down-left from the top right into the stem's right edge,
     * and a leg that leaves the arm -- not the stem -- and runs down-right to a flat foot. Written as
     * taught: `stem` down, `arm` from the top right in to the stem, then `leg` out from the arm to
     * the foot. The arm and the leg run along lines fitted through their ink's row centres.
     *
     * Each join lands exactly on a dot of the stroke it meets: the leg starts on the arm's eighth
     * dot, 1.45 units up the arm from where the two centrelines cross, and the arm ends on the
     * stem's ninth dot. The arm meets the stem at about 40 degrees, and a straight arm would leave
     * its second-last dot overlapping a stem dot, so it leaves its centreline at the stem's edge and
     * turns in at 68 degrees for its last spacing; every other dot then clears the stem's by a full
     * dot width. As H's are, the stem is a whole 12 spacings, y 12 to 84, placed to put a dot on the
     * join; the leg is a whole 9 and reaches a dot radius inside the foot.
     */
    Exercise(
        id = "english-capital-k",
        title = "K",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 11,
        strokes = listOf(
            Stroke(
                id = "english-capital-k-stem",
                points = StrokePoints.line(Point(22.5f, 12f), Point(22.5f, 84.02f)),
            ),
            Stroke(
                id = "english-capital-k-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(72.32f, 9.5f),
                        Point(32.91f, 55.79f),
                        Point(22.5f, 60f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-k-leg",
                points = StrokePoints.line(Point(45.09f, 41.48f), Point(73.93f, 87.16f)),
            ),
        ),
    ),
    /**
     * L is taller than it is wide (aspect 0.636), so it is fitted by height.
     *
     * Inter's L is a straight stem and a straight foot bar meeting at a square corner. It is written
     * as taught, "down, then across", in one stroke of 114 units: down the stem's centre and right
     * along the bar's midline. The corner is a right angle, so the dots either side of it sit well
     * apart and it carries a dot of its own; the stem is a whole 12 spacings down to it.
     *
     * The bar is a whole 7 spacings. That needs the stem 0.37 units left of its ink centre, at x
     * 31.75, for the bar's last dot to sit a full dot radius inside the cut; at 6 spacings the bar
     * would stop 8.4 units short of it.
     */
    Exercise(
        id = "english-capital-l",
        title = "L",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 12,
        strokes = listOf(
            Stroke(
                id = "english-capital-l-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(31.75f, 11f),
                        Point(31.75f, 83f),
                        Point(73.77f, 83f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * M is wider than it is tall (aspect 1.10), so it is fitted by width.
     *
     * Inter's M is two straight stems and two straight diagonals, symmetric about x 49.94. Each
     * diagonal is a line through its ink's row centres, 17° off vertical. Written as taught: `left`
     * stem down, `down` from the stem's top dot along the flat top and down the left diagonal, `up`
     * up the right diagonal and along the flat top to the right stem's top dot, then `right` stem
     * down; `down` and `up` together are 186 units, too long for one stroke.
     *
     * The diagonals meet at 34.5°, below the ink, so as in v and A they are joined by a flat of
     * exactly one spacing where they are 6 apart, and `up` starts on `down`'s last dot. The stem and
     * a diagonal meet at 17°, so each diagonal leaves its stem's top dot along a flat of exactly two
     * spacings, which puts a dot on the corner and keeps its neighbours well apart. The top is
     * placed so each diagonal is a whole 13 spacings; the stems then sit 0.44 units inside their ink
     * centres and are a whole 13 spacings.
     */
    Exercise(
        id = "english-capital-m",
        title = "M",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 13,
        strokes = listOf(
            Stroke(
                id = "english-capital-m-left",
                points = StrokePoints.line(Point(12.11f, 11.23f), Point(12.11f, 89.25f)),
            ),
            Stroke(
                id = "english-capital-m-down",
                points = StrokePoints.polyline(
                    listOf(
                        Point(12.11f, 11.23f),
                        Point(24.12f, 11.23f),
                        Point(46.94f, 85.83f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-m-up",
                points = StrokePoints.polyline(
                    listOf(
                        Point(46.94f, 85.83f),
                        Point(52.94f, 85.83f),
                        Point(75.76f, 11.23f),
                        Point(87.77f, 11.23f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-m-right",
                points = StrokePoints.line(Point(87.77f, 11.23f), Point(87.77f, 89.25f)),
            ),
        ),
    ),
    /**
     * N is taller than it is wide (aspect 0.867), so it is fitted by height.
     *
     * Inter's N is two straight stems and a straight diagonal, point-symmetric about the diagonal's
     * midpoint. The diagonal is a line through its ink's row centres, 30° off vertical. Written as
     * taught, "down, slant down, up": `left` stem down, `diagonal` from the left stem's top dot down
     * to the right stem's bottom dot, then `right` from that dot up the stem.
     *
     * The diagonal meets each stem at 30°, where dots either side of a shared point would crowd, so
     * as in v and A it joins each stem by a flat of exactly one spacing, placed where its centre
     * line is 6 units from the stem's. The stems sit 0.11 units in from their ink centres so the
     * diagonal between the flats is a whole 14 spacings, and each stem is a whole 12 spacings from
     * its join, stopping 6 units short of the far cut, as H's stems do.
     */
    Exercise(
        id = "english-capital-n",
        title = "N",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 14,
        strokes = listOf(
            Stroke(
                id = "english-capital-n-left",
                points = StrokePoints.line(Point(22.81f, 12.14f), Point(22.81f, 84.16f)),
            ),
            Stroke(
                id = "english-capital-n-diagonal",
                points = StrokePoints.polyline(
                    listOf(
                        Point(22.81f, 12.14f),
                        Point(28.81f, 12.14f),
                        Point(71.15f, 84.71f),
                        Point(77.15f, 84.71f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-n-right",
                points = StrokePoints.line(Point(77.15f, 84.71f), Point(77.15f, 12.69f)),
            ),
        ),
    ),
    /**
     * O is taller than it is wide (aspect 0.908), so it is fitted by height.
     *
     * Inter's O is one closed ring, symmetric about x 50 and y 48.5 to within 0.1 units. Its
     * centreline is 204 units, too long for one stroke, so as with small o it is written
     * counter-clockwise from the top and split at the top and the bottom: `left` down the left side
     * to the bottom, then `right` up the right side and back to the top, closing onto the first dot.
     * The curve is the midpoint of the ink along rays from the counter's centre, each averaged with
     * its mirror on the other side; `right` is `left` mirrored and reversed, so the two share the dots
     * at the top and the bottom.
     *
     * Each half is a whole 17 spacings with every midpoint moved 0.21 units outward along its ray,
     * well inside a band 14.7 to 16.9 units thick.
     */
    Exercise(
        id = "english-capital-o",
        title = "O",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 15,
        strokes = listOf(
            Stroke(
                id = "english-capital-o-left",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50f, 14.07f),
                        Point(47f, 14.2f),
                        Point(44.04f, 14.61f),
                        Point(41.12f, 15.32f),
                        Point(38.3f, 16.33f),
                        Point(35.61f, 17.65f),
                        Point(33.09f, 19.27f),
                        Point(30.76f, 21.17f),
                        Point(28.67f, 23.32f),
                        Point(26.82f, 25.68f),
                        Point(25.22f, 28.21f),
                        Point(23.87f, 30.89f),
                        Point(22.77f, 33.68f),
                        Point(21.89f, 36.54f),
                        Point(21.25f, 39.47f),
                        Point(20.85f, 42.45f),
                        Point(20.56f, 45.43f),
                        Point(20.48f, 48.43f),
                        Point(20.55f, 51.43f),
                        Point(20.83f, 54.42f),
                        Point(21.23f, 57.39f),
                        Point(21.86f, 60.32f),
                        Point(22.72f, 63.19f),
                        Point(23.82f, 65.99f),
                        Point(25.19f, 68.65f),
                        Point(26.79f, 71.19f),
                        Point(28.62f, 73.56f),
                        Point(30.71f, 75.71f),
                        Point(33.03f, 77.62f),
                        Point(35.55f, 79.24f),
                        Point(38.25f, 80.55f),
                        Point(41.06f, 81.58f),
                        Point(43.97f, 82.3f),
                        Point(46.94f, 82.74f),
                        Point(50f, 82.86f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-o-right",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50f, 82.86f),
                        Point(53.06f, 82.74f),
                        Point(56.03f, 82.3f),
                        Point(58.94f, 81.58f),
                        Point(61.75f, 80.55f),
                        Point(64.45f, 79.24f),
                        Point(66.97f, 77.62f),
                        Point(69.29f, 75.71f),
                        Point(71.38f, 73.56f),
                        Point(73.21f, 71.19f),
                        Point(74.81f, 68.65f),
                        Point(76.18f, 65.99f),
                        Point(77.28f, 63.19f),
                        Point(78.14f, 60.32f),
                        Point(78.77f, 57.39f),
                        Point(79.17f, 54.42f),
                        Point(79.45f, 51.43f),
                        Point(79.52f, 48.43f),
                        Point(79.44f, 45.43f),
                        Point(79.15f, 42.45f),
                        Point(78.75f, 39.47f),
                        Point(78.11f, 36.54f),
                        Point(77.23f, 33.68f),
                        Point(76.13f, 30.89f),
                        Point(74.78f, 28.21f),
                        Point(73.18f, 25.68f),
                        Point(71.33f, 23.32f),
                        Point(69.24f, 21.17f),
                        Point(66.91f, 19.27f),
                        Point(64.39f, 17.65f),
                        Point(61.7f, 16.33f),
                        Point(58.88f, 15.32f),
                        Point(55.96f, 14.61f),
                        Point(53f, 14.2f),
                        Point(50f, 14.07f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * P is taller than it is wide (aspect 0.747), so it is fitted by height.
     *
     * Inter's P is a straight stem and one bowl off its upper half. The bowl leaves the stem at the
     * top bar, runs flat along it, round the right side and back flat along the lower bar into the
     * stem. Its curve is the midpoint of the ink along rays cast from the counter's centre, as D's;
     * every ray from -90 to +90 degrees crosses a lone run of 13.8 to 17.6 units.
     *
     * Written as taught, "down, then round": `stem` from the top bar down to the foot, then `bowl`
     * from the stem's first dot round to the stem. The bars' midlines are 42 units apart, a whole
     * 7 spacings, so the stem starts on the top bar's midline (y 14, as D's stem starts on its top
     * join) and its 8th dot sits on the lower bar's midline; the bowl starts and ends exactly on
     * those two stem dots. The stem is 12 spacings to y 86, 4 inside the flat foot, and the bowl is
     * a whole 19 spacings with no ray offset needed.
     */
    Exercise(
        id = "english-capital-p",
        title = "P",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 16,
        strokes = listOf(
            Stroke(
                id = "english-capital-p-stem",
                points = StrokePoints.line(Point(27.5f, 14f), Point(27.5f, 86f)),
            ),
            Stroke(
                id = "english-capital-p-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(27.5f, 14f),
                        Point(30.5f, 14f),
                        Point(33.5f, 14f),
                        Point(36.5f, 14f),
                        Point(39.5f, 14f),
                        Point(42.5f, 14f),
                        Point(45.5f, 14f),
                        Point(48.5f, 14f),
                        Point(51.5f, 14.04f),
                        Point(54.49f, 14.31f),
                        Point(57.43f, 14.89f),
                        Point(60.28f, 15.82f),
                        Point(62.96f, 17.16f),
                        Point(65.41f, 18.88f),
                        Point(67.53f, 20.99f),
                        Point(69.26f, 23.44f),
                        Point(70.6f, 26.12f),
                        Point(71.51f, 28.98f),
                        Point(72.03f, 31.93f),
                        Point(72.21f, 34.93f),
                        Point(72.05f, 37.92f),
                        Point(71.55f, 40.88f),
                        Point(70.61f, 43.72f),
                        Point(69.29f, 46.41f),
                        Point(67.56f, 48.86f),
                        Point(65.44f, 50.97f),
                        Point(63f, 52.72f),
                        Point(60.34f, 54.09f),
                        Point(57.49f, 55.02f),
                        Point(54.55f, 55.62f),
                        Point(51.56f, 55.91f),
                        Point(48.56f, 55.99f),
                        Point(45.56f, 55.99f),
                        Point(42.56f, 56f),
                        Point(39.56f, 56f),
                        Point(36.56f, 56f),
                        Point(33.56f, 56f),
                        Point(30.56f, 56f),
                        Point(27.5f, 56f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * Q is taller than it is wide (aspect 0.850), so it is fitted by height.
     *
     * Inter's Q is O's ring with a straight tail, 38 degrees off vertical, crossing its lower right.
     * The ring is written as O's is, counter-clockwise from the top in two halves, `left` then
     * `right`, and the tail is added last, from inside the counter out through the ring. The ring's
     * curve is the midpoint of the ink along rays from its centre; the tail merges with the ring's
     * lower right, so only the left half is read (taking the outermost run on each ray, past the
     * tail's end inside the counter) and the rest is its point reflection, since the ring is
     * symmetric about both axes. Each half is a whole 16 spacings with every midpoint moved 0.33
     * units outward along its ray.
     *
     * The tail crosses the ring at nearly a right angle, so the two strokes share one dot there, as
     * small x's diagonals do. Because the ring is point-symmetric, any two opposite points split it
     * into equal halves, so the split is slid 2.5 units round from the top to put a `right` dot
     * exactly on the tail's centre line. The tail is a whole 3 spacings either side of that dot.
     * Its ink runs only ~41 units along the centre line, so at the glyph's own angle the last dot
     * would sit 1.7 units from the flat foot. The tail is therefore turned 4 degrees about the shared
     * dot, which lengthens its run to the flat cuts and keeps a whole dot radius of ink under both
     * end dots; the ends move 1.3 units sideways in a tail about 10 units wide.
     */
    Exercise(
        id = "english-capital-q",
        title = "Q",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 17,
        strokes = listOf(
            Stroke(
                id = "english-capital-q-left",
                points = StrokePoints.polyline(
                    listOf(
                        Point(52.5f, 13.66f),
                        Point(50f, 13.48f),
                        Point(47.01f, 13.62f),
                        Point(44.04f, 14.05f),
                        Point(41.14f, 14.8f),
                        Point(38.34f, 15.89f),
                        Point(35.7f, 17.31f),
                        Point(33.23f, 19.01f),
                        Point(31f, 21.01f),
                        Point(29.02f, 23.26f),
                        Point(27.3f, 25.72f),
                        Point(25.84f, 28.34f),
                        Point(24.64f, 31.08f),
                        Point(23.69f, 33.93f),
                        Point(23f, 36.85f),
                        Point(22.55f, 39.81f),
                        Point(22.26f, 42.8f),
                        Point(22.19f, 45.8f),
                        Point(22.26f, 48.8f),
                        Point(22.54f, 51.78f),
                        Point(22.99f, 54.75f),
                        Point(23.66f, 57.67f),
                        Point(24.59f, 60.52f),
                        Point(25.81f, 63.26f),
                        Point(27.27f, 65.88f),
                        Point(28.95f, 68.36f),
                        Point(30.95f, 70.6f),
                        Point(33.19f, 72.59f),
                        Point(35.63f, 74.33f),
                        Point(38.28f, 75.74f),
                        Point(41.08f, 76.81f),
                        Point(43.97f, 77.6f),
                        Point(46.94f, 78.03f),
                        Point(47.5f, 78.06f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-q-right",
                points = StrokePoints.polyline(
                    listOf(
                        Point(47.5f, 78.06f),
                        Point(50f, 78.17f),
                        Point(52.99f, 78.1f),
                        Point(55.96f, 77.67f),
                        Point(58.86f, 76.92f),
                        Point(61.66f, 75.83f),
                        Point(64.3f, 74.41f),
                        Point(66.77f, 72.71f),
                        Point(69f, 70.71f),
                        Point(70.98f, 68.46f),
                        Point(72.7f, 66f),
                        Point(74.16f, 63.38f),
                        Point(75.36f, 60.64f),
                        Point(76.31f, 57.79f),
                        Point(77f, 54.87f),
                        Point(77.45f, 51.91f),
                        Point(77.74f, 48.92f),
                        Point(77.81f, 45.92f),
                        Point(77.74f, 42.92f),
                        Point(77.46f, 39.94f),
                        Point(77.01f, 36.97f),
                        Point(76.34f, 34.05f),
                        Point(75.41f, 31.2f),
                        Point(74.19f, 28.46f),
                        Point(72.73f, 25.84f),
                        Point(71.05f, 23.36f),
                        Point(69.05f, 21.12f),
                        Point(66.81f, 19.13f),
                        Point(64.37f, 17.39f),
                        Point(61.72f, 15.98f),
                        Point(58.92f, 14.91f),
                        Point(56.03f, 14.12f),
                        Point(53.06f, 13.69f),
                        Point(52.5f, 13.66f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-q-tail",
                points = StrokePoints.line(Point(52.66f, 60.76f), Point(76.8f, 87.5f)),
            ),
        ),
    ),
    /**
     * R is taller than it is wide (aspect 0.782), so it is fitted by height.
     *
     * Inter's R is P's stem and bowl with a straight leg, 28 degrees off vertical, leaving the bowl's
     * lower bar for the flat foot. Written as taught, "down, round, then kick out": `stem`, then
     * `bowl` from the stem's first dot round to the stem, then `leg` from a bowl dot to the foot.
     *
     * The bars' midlines are 39.5 units apart, not a whole number of spacings, so the stem's join
     * dots sit a whole 6 spacings apart, at y 15.5 and 51.5, 1.5 and 2 inside the midlines of 13.8-unit
     * bars (as E's arms sit off theirs). The stem is a whole 12 spacings to y 87.5, a dot radius above
     * the flat foot. The bowl's curve is the midpoint of the ink along rays from the counter's centre,
     * squeezed vertically about the bowl's middle so that it runs into the join lines without a step.
     * The lower rays run on into the leg, so the lower half is the upper half mirrored. The bowl is a
     * whole 18 spacings, so it starts and ends exactly on the two join dots.
     *
     * The leg starts exactly on the bowl dot nearest its centre line (2 units off it), where the bar
     * turns into the leg, and runs a whole 7 spacings to a point on the centre line 2.8 above the flat
     * foot, 2.4 degrees steeper than the glyph.
     */
    Exercise(
        id = "english-capital-r",
        title = "R",
        type = ExerciseType.ENGLISH_CAPITAL,
        difficulty = Difficulty.BEGINNER,
        order = 18,
        strokes = listOf(
            Stroke(
                id = "english-capital-r-stem",
                points = StrokePoints.line(Point(26.05f, 15.5f), Point(26.05f, 87.5f)),
            ),
            Stroke(
                id = "english-capital-r-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26.05f, 15.5f),
                        Point(29.05f, 15.5f),
                        Point(32.05f, 15.5f),
                        Point(35.05f, 15.5f),
                        Point(38.05f, 15.5f),
                        Point(41.05f, 15.51f),
                        Point(44.05f, 15.53f),
                        Point(47.05f, 15.59f),
                        Point(50.02f, 15.92f),
                        Point(53.01f, 16.21f),
                        Point(55.97f, 16.68f),
                        Point(58.85f, 17.5f),
                        Point(61.6f, 18.69f),
                        Point(64.13f, 20.3f),
                        Point(66.31f, 22.35f),
                        Point(68.04f, 24.8f),
                        Point(69.25f, 27.54f),
                        Point(69.97f, 30.45f),
                        Point(70.23f, 33.5f),
                        Point(69.97f, 36.55f),
                        Point(69.25f, 39.46f),
                        Point(68.04f, 42.2f),
                        Point(66.31f, 44.65f),
                        Point(64.13f, 46.7f),
                        Point(61.6f, 48.31f),
                        Point(58.85f, 49.5f),
                        Point(55.97f, 50.32f),
                        Point(53.01f, 50.79f),
                        Point(50.02f, 51.08f),
                        Point(47.05f, 51.41f),
                        Point(44.05f, 51.47f),
                        Point(41.05f, 51.49f),
                        Point(38.05f, 51.5f),
                        Point(35.05f, 51.5f),
                        Point(32.05f, 51.5f),
                        Point(29.05f, 51.5f),
                        Point(26.05f, 51.5f),
                    ),
                ),
            ),
            Stroke(
                id = "english-capital-r-leg",
                points = StrokePoints.line(Point(50.08f, 51.07f), Point(71.59f, 87.17f)),
            ),
        ),
    ),
)
