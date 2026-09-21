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
    /**
     * b is much taller than it is wide (aspect 0.617), so it is fitted by height.
     *
     * It is a straight stem with a bowl hanging off its lower half: the bowl leaves the stem at
     * mid-height, climbs up-right over its top, runs clockwise down the right side and round the
     * bottom, and closes back into the stem's foot. The skeleton draws that as one closed loop
     * through the stem, so the letter is written as it is taught — the stem top to bottom, then the
     * bowl out of the stem and round, 105 canvas units, one pass. The skeleton's only other branch
     * runs up-left from the stem into the top-left corner of the slanted cut that caps it; that is
     * the cut's corner, not a path, so the stem is a straight line down its centre (x 32 all the
     * way), starting and ending 3 units inside its flat top and foot.
     */
    Exercise(
        id = "english-small-b",
        title = "b",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "english-small-b-stem",
                points = StrokePoints.line(Point(32f, 10f), Point(32f, 84f)),
            ),
            Stroke(
                id = "english-small-b-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(32f, 52f),
                        Point(35f, 51f),
                        Point(37f, 50f),
                        Point(40f, 49f),
                        Point(43f, 48f),
                        Point(45f, 46f),
                        Point(47f, 45f),
                        Point(50f, 44f),
                        Point(53f, 43f),
                        Point(55f, 43f),
                        Point(58f, 42f),
                        Point(61f, 43f),
                        Point(63f, 45f),
                        Point(65f, 47f),
                        Point(66f, 49f),
                        Point(67f, 52f),
                        Point(68f, 55f),
                        Point(68f, 57f),
                        Point(68f, 60f),
                        Point(68f, 63f),
                        Point(68f, 66f),
                        Point(68f, 69f),
                        Point(67f, 72f),
                        Point(66f, 74f),
                        Point(65f, 77f),
                        Point(63f, 79f),
                        Point(61f, 81f),
                        Point(59f, 82f),
                        Point(56f, 83f),
                        Point(53f, 84f),
                        Point(51f, 84f),
                        Point(48f, 84f),
                        Point(45f, 84f),
                        Point(42f, 84f),
                        Point(39f, 83f),
                        Point(37f, 82f),
                        Point(34f, 81f),
                        Point(32f, 79f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * c is taller than it is wide (aspect 0.791), so it is fitted by height.
     *
     * It is one counter-clockwise curve between two flat cuts, each of which forks the skeleton into
     * a prong per corner. Run as one stroke, from 3 units short of the top cut's midpoint round to
     * 3 units short of the bottom cut's, it is 142 canvas units — past what a child can hold in one
     * pass, where য and ভ were already split at 139 — and it turns smoothly all the way, with no
     * corner or neck. So it is split at its leftmost point, where the pen is travelling straight
     * down and the skeleton grows a spur into the thick left of the bowl — the extremum ৎ breaks
     * at — into `top` (70 units) and `bottom` (72). The split sits one unit above the exact
     * extremum so that `bottom` is a whole 12 dot spacings and its last dot lands on its tip rather
     * than a spacing short of it.
     *
     * Both ends stop 3 units short of their cut: 1 and 2 (and 2.5, once rounded to whole units)
     * leave the top end's dot hanging past the cut, and 3 is the shortest inset that keeps every
     * dot wholly on the ink.
     *
     * Andika's c is much heavier on its left than at its terminals, so its centreline sits right of
     * the ink's centre: fitted with the ink centred, the guide's bbox centre would be x=54. The
     * guide is therefore moved 4 units left so that it, not the ink, is centred on the canvas; the
     * canvas draws only the guide, so this is a translation, not a change of shape.
     */
    Exercise(
        id = "english-small-c",
        title = "c",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "english-small-c-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(73f, 21f),
                        Point(71f, 19f),
                        Point(68f, 18f),
                        Point(65f, 17f),
                        Point(63f, 17f),
                        Point(60f, 16f),
                        Point(57f, 16f),
                        Point(54f, 16f),
                        Point(51f, 16f),
                        Point(48f, 16f),
                        Point(46f, 17f),
                        Point(43f, 18f),
                        Point(40f, 19f),
                        Point(38f, 20f),
                        Point(36f, 22f),
                        Point(34f, 24f),
                        Point(32f, 26f),
                        Point(30f, 28f),
                        Point(29f, 31f),
                        Point(28f, 33f),
                        Point(27f, 36f),
                        Point(26f, 39f),
                        Point(26f, 41f),
                        Point(25f, 44f),
                        Point(25f, 47f),
                        Point(25f, 49f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-c-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 49f),
                        Point(25f, 53f),
                        Point(25f, 56f),
                        Point(26f, 59f),
                        Point(26f, 61f),
                        Point(27f, 64f),
                        Point(28f, 66f),
                        Point(30f, 69f),
                        Point(31f, 71f),
                        Point(33f, 73f),
                        Point(35f, 75f),
                        Point(37f, 77f),
                        Point(40f, 78f),
                        Point(42f, 79f),
                        Point(45f, 80f),
                        Point(48f, 80f),
                        Point(51f, 81f),
                        Point(53f, 81f),
                        Point(56f, 81f),
                        Point(59f, 80f),
                        Point(62f, 80f),
                        Point(65f, 79f),
                        Point(67f, 79f),
                        Point(70f, 78f),
                        Point(72f, 76f),
                        Point(75f, 75f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * d is much taller than it is wide (aspect 0.671), so it is fitted by height.
     *
     * It is b mirrored, with a's foot: a tall stem on the right, flat-topped, with a closed bowl on
     * its left. The skeleton meets the stem twice, at y 45 where the bowl leaves it and at y 73
     * where it closes back in. That is written as it is taught, round first like a: `bowl` from the
     * stem, counter-clockwise over the top, down the left side, round the bottom and back into the
     * stem, 107 canvas units in one pass — then `stem` top to bottom.
     *
     * The stem is straight at x 66.8 down to y 70, then leans out with the foot's slanted cut
     * (row centre 70.6 at y 88), so it is taken from the ink's row centres rather than the skeleton,
     * which runs off into the foot's outer corner. It starts 3 units inside its flat top and stops
     * about 3.5 above its foot, keeping both end dots wholly on the ink.
     */
    Exercise(
        id = "english-small-d",
        title = "d",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "english-small-d-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(67f, 45f),
                        Point(64f, 45f),
                        Point(61f, 45f),
                        Point(58f, 44f),
                        Point(55f, 44f),
                        Point(53f, 43f),
                        Point(50f, 43f),
                        Point(47f, 42f),
                        Point(44f, 42f),
                        Point(41f, 43f),
                        Point(39f, 44f),
                        Point(37f, 46f),
                        Point(35f, 48f),
                        Point(33f, 50f),
                        Point(32f, 53f),
                        Point(31f, 55f),
                        Point(30f, 58f),
                        Point(30f, 61f),
                        Point(30f, 64f),
                        Point(30f, 67f),
                        Point(30f, 70f),
                        Point(31f, 72f),
                        Point(31f, 75f),
                        Point(33f, 77f),
                        Point(34f, 80f),
                        Point(36f, 82f),
                        Point(39f, 83f),
                        Point(42f, 84f),
                        Point(44f, 84f),
                        Point(47f, 83f),
                        Point(50f, 83f),
                        Point(53f, 82f),
                        Point(55f, 80f),
                        Point(57f, 78f),
                        Point(59f, 77f),
                        Point(62f, 76f),
                        Point(64f, 75f),
                        Point(67f, 73f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-d-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(67f, 10f),
                        Point(67f, 71f),
                        Point(70f, 85f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * e is taller than it is wide (aspect 0.868), so it is fitted by height.
     *
     * It is written as one movement: across the crossbar left to right, sharply up the right side,
     * counter-clockwise over the top and down the left, round the bottom and up into the tail's flat
     * cut. That is about 218 canvas units, far past one pass, so it is split at the two landmarks
     * the child can see: the corner where the bar turns up into the bowl, and the leftmost point,
     * where the bar's own left end meets the side (the extremum c splits at). That leaves `bar`
     * (48, straight), `top` (91) and `bottom` (79), with `top` and `bottom` sharing the dot at the
     * split. The skeleton's spur down-right from the bar's corner runs into the corner of the flat
     * cut under the bar, not along a path.
     *
     * The tail's cut forks the skeleton into a prong per corner, and the curve stops 3 units short
     * of the cut's midpoint: 1 lands its last dot in the same place but leaves 2.7 units of bare
     * path after it, where 3 leaves 0.7.
     *
     * Like c, Andika's e is heavier on its left, so the guide is moved 4 units left to centre it
     * (its bbox centre would otherwise be x=53.5); the canvas draws only the guide, so this is a
     * translation, not a change of shape.
     */
    Exercise(
        id = "english-small-e",
        title = "e",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "english-small-e-bar",
                points = StrokePoints.line(Point(22f, 46f), Point(70f, 46f)),
            ),
            Stroke(
                id = "english-small-e-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(71f, 43f),
                        Point(71f, 41f),
                        Point(71f, 38f),
                        Point(70f, 35f),
                        Point(70f, 32f),
                        Point(69f, 29f),
                        Point(68f, 27f),
                        Point(67f, 24f),
                        Point(66f, 22f),
                        Point(64f, 20f),
                        Point(61f, 18f),
                        Point(59f, 17f),
                        Point(56f, 16f),
                        Point(53f, 16f),
                        Point(50f, 16f),
                        Point(47f, 16f),
                        Point(44f, 16f),
                        Point(42f, 16f),
                        Point(39f, 17f),
                        Point(36f, 18f),
                        Point(34f, 19f),
                        Point(31f, 20f),
                        Point(29f, 22f),
                        Point(28f, 25f),
                        Point(26f, 27f),
                        Point(25f, 29f),
                        Point(24f, 32f),
                        Point(23f, 35f),
                        Point(23f, 38f),
                        Point(22f, 40f),
                        Point(22f, 43f),
                        Point(21f, 46f),
                        Point(21f, 49f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-e-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(21f, 49f),
                        Point(21f, 52f),
                        Point(22f, 55f),
                        Point(22f, 58f),
                        Point(23f, 61f),
                        Point(24f, 63f),
                        Point(24f, 66f),
                        Point(26f, 69f),
                        Point(27f, 71f),
                        Point(29f, 73f),
                        Point(31f, 75f),
                        Point(33f, 77f),
                        Point(36f, 78f),
                        Point(38f, 79f),
                        Point(41f, 80f),
                        Point(44f, 80f),
                        Point(47f, 81f),
                        Point(50f, 81f),
                        Point(53f, 81f),
                        Point(56f, 81f),
                        Point(59f, 80f),
                        Point(61f, 80f),
                        Point(64f, 79f),
                        Point(67f, 79f),
                        Point(70f, 78f),
                        Point(72f, 77f),
                        Point(75f, 76f),
                        Point(78f, 75f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * f is much taller than it is wide (aspect 0.515), so it is fitted by height.
     *
     * It is a stem that curls over into a hook at the top, ending in a flat slanted cut, with a
     * crossbar that is separate ink. It is written as it is taught: `stem` from the hook's tip, left
     * over the top and straight down to the foot (89 canvas units, one pass), then `bar` across it
     * left to right. The hook's cut forks the skeleton into a prong per corner, and the stroke starts
     * 3 units short of the cut's midpoint: 1 and 2 leave the first dot hanging past the cut. Below
     * the hook the stem is straight at x 46 (the ink's row centre, leaning only to 46.6 at the foot),
     * so it is one segment, stopping 3 units above the flat foot, where the skeleton runs off into
     * the foot's outer corner. The bar sits on its ink's centreline (y 44) and stops about 3 units
     * inside each end, a whole 5 dot spacings long.
     */
    Exercise(
        id = "english-small-f",
        title = "f",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "english-small-f-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(67f, 15f),
                        Point(64f, 14f),
                        Point(61f, 14f),
                        Point(58f, 13f),
                        Point(56f, 14f),
                        Point(53f, 15f),
                        Point(51f, 16f),
                        Point(49f, 19f),
                        Point(48f, 21f),
                        Point(47f, 24f),
                        Point(47f, 27f),
                        Point(46f, 30f),
                        Point(46f, 87f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-f-bar",
                points = StrokePoints.line(Point(32f, 44f), Point(62f, 44f)),
            ),
        ),
    ),
    /**
     * g is much taller than it is wide (aspect 0.636), so it is fitted by height.
     *
     * Andika's single-storey g is a's bowl closed onto a stem that drops below the line into a hook
     * ending in a flat slanted cut. The bowl's arch runs straight into the stem's top-right corner,
     * so there is no stem top standing above it. It is written like a and d, round first: `bowl`
     * from the stem just under its top, counter-clockwise over the top, down the left side, round
     * the bottom and back into the stem at y 48 (107 canvas units, one pass); then `stem` top to
     * bottom, straight at x 69 (the ink's row centre), curling left along the bottom into the hook
     * (101 units, one pass). The stem starts 4 units under its top edge. The hook's cut forks the
     * skeleton into a prong per corner, and the tail stops 3 units short of the cut's midpoint:
     * 1 and 2 leave the last dot hanging past the cut, and 3 leaves less bare path after the last
     * dot than 2.5.
     */
    Exercise(
        id = "english-small-g",
        title = "g",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 7,
        strokes = listOf(
            Stroke(
                id = "english-small-g-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(69f, 18f),
                        Point(67f, 16f),
                        Point(64f, 15f),
                        Point(61f, 15f),
                        Point(58f, 14f),
                        Point(56f, 14f),
                        Point(53f, 14f),
                        Point(50f, 14f),
                        Point(47f, 15f),
                        Point(44f, 15f),
                        Point(42f, 17f),
                        Point(39f, 18f),
                        Point(37f, 20f),
                        Point(36f, 23f),
                        Point(35f, 25f),
                        Point(34f, 28f),
                        Point(33f, 31f),
                        Point(32f, 34f),
                        Point(32f, 36f),
                        Point(32f, 39f),
                        Point(32f, 42f),
                        Point(32f, 45f),
                        Point(33f, 48f),
                        Point(34f, 51f),
                        Point(35f, 53f),
                        Point(37f, 55f),
                        Point(40f, 57f),
                        Point(42f, 57f),
                        Point(45f, 57f),
                        Point(48f, 56f),
                        Point(51f, 56f),
                        Point(53f, 55f),
                        Point(56f, 53f),
                        Point(58f, 52f),
                        Point(61f, 51f),
                        Point(63f, 50f),
                        Point(66f, 49f),
                        Point(69f, 48f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-g-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(69f, 13f),
                        Point(69f, 58f),
                        Point(68f, 61f),
                        Point(68f, 64f),
                        Point(68f, 67f),
                        Point(67f, 70f),
                        Point(67f, 73f),
                        Point(65f, 75f),
                        Point(64f, 78f),
                        Point(62f, 80f),
                        Point(59f, 81f),
                        Point(57f, 82f),
                        Point(54f, 83f),
                        Point(51f, 83f),
                        Point(48f, 83f),
                        Point(45f, 83f),
                        Point(42f, 83f),
                        Point(39f, 82f),
                        Point(37f, 82f),
                        Point(34f, 80f),
                        Point(32f, 79f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * h is much taller than it is wide (aspect 0.619), so it is fitted by height.
     *
     * A straight stem (ink row centre x 32.4 from its flat top to its flat foot) and an arch that
     * leaves it at mid-height, runs up-right over the shoulder and straight down the right leg
     * (x 68.4) to its own flat foot. Written as taught: `stem` top to bottom, then `arch` out of the
     * stem — the point where the skeleton joins the two, as b's bowl does — over and down in one
     * pass of 78 canvas units. The arch follows the skeleton until the leg turns straight and is one
     * segment from there; the skeleton itself stops 7 units above the foot, where the flat cut
     * forks it into its corners.
     *
     * Both straight runs stop 3 units inside the flat ends (y 10 and 87): 2 units inside the top or
     * foot hangs the stem's end dot past the cut (min ink 2.1 / 2.0 against the 2.5 dot radius).
     */
    Exercise(
        id = "english-small-h",
        title = "h",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 8,
        strokes = listOf(
            Stroke(
                id = "english-small-h-stem",
                points = StrokePoints.line(Point(32f, 10f), Point(32f, 87f)),
            ),
            Stroke(
                id = "english-small-h-arch",
                points = StrokePoints.polyline(
                    listOf(
                        Point(32f, 52f),
                        Point(35f, 51f),
                        Point(38f, 50f),
                        Point(40f, 49f),
                        Point(43f, 49f),
                        Point(45f, 47f),
                        Point(48f, 45f),
                        Point(50f, 44f),
                        Point(53f, 44f),
                        Point(56f, 43f),
                        Point(59f, 43f),
                        Point(62f, 43f),
                        Point(64f, 45f),
                        Point(66f, 47f),
                        Point(67f, 50f),
                        Point(68f, 53f),
                        Point(68f, 55f),
                        Point(68f, 58f),
                        Point(68f, 61f),
                        Point(68f, 87f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * i is far taller than it is wide (aspect 0.223), so it is fitted by height.
     *
     * Two separate pieces of ink: a straight stem (row centre x 50, flat ends at y 35.8 and 90) and
     * a round dot above it. Written as a writer adds them — `stem` top to bottom, then `dot`. The
     * stem stops 3 units inside each end (y 39 and 87): 2 hangs the end dot past the cut (min ink
     * 2.4 top / 2.0 foot against the 2.5 dot radius), and 39..87 is a whole 8 dot spacings.
     *
     * The dot follows the ring rule র set: its largest inscribed circle is 9.25 canvas units at
     * (50, 16), and the ring inside it has radius 5.73 — about 60% of that, and the radius whose
     * circumference is a whole 6 dot spacings, so its last dot closes onto its first. It starts at
     * the top and runs counter-clockwise, the way round letters are written.
     */
    Exercise(
        id = "english-small-i",
        title = "i",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 9,
        strokes = listOf(
            Stroke(
                id = "english-small-i-stem",
                points = StrokePoints.line(Point(50f, 39f), Point(50f, 87f)),
            ),
            Stroke(
                id = "english-small-i-dot",
                points = StrokePoints.arc(
                    center = Point(50f, 16f),
                    radius = 5.73f,
                    startDeg = -90f,
                    sweepDeg = -360f,
                    samples = 32,
                ),
            ),
        ),
    ),
    /**
     * j is far taller than it is wide (aspect 0.357), so it is fitted by height.
     *
     * i's two pieces with a hook: a stem (row centre x 58) that runs straight down from its slanted
     * top and curls left along the bottom into a flat cut, and a dot above it. Written as i is —
     * `stem` top to bottom and round the hook in one pass (67 canvas units), then `dot`. The stem
     * starts 3 units inside its top edge (y 29.1 at x 58); 2 hangs the first dot past it. It is one
     * straight segment down to y 68, where the curl begins, and the skeleton from there. The cut
     * forks the skeleton into a prong per corner and the hook stops 2.5 units short of their
     * midpoint: 1 and 2 leave the tip over 1.6 and 2.5 units of ink, and 2.5 leaves 0.8 units of
     * bare path past the last dot to 3's 1.0.
     *
     * The dot is smaller than i's — its inscribed circle is 7.12 units at (58, 14) — so the ring is
     * 4 dot spacings round (radius 3.82): 5 spacings would put the guide's dots within 2.1 units of
     * the dot's edge, hanging them past it. Started at the top and swept counter-clockwise, as i's is.
     */
    Exercise(
        id = "english-small-j",
        title = "j",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 10,
        strokes = listOf(
            Stroke(
                id = "english-small-j-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(58f, 32f),
                        Point(58f, 68f),
                        Point(57f, 71f),
                        Point(57f, 73f),
                        Point(57f, 76f),
                        Point(56f, 79f),
                        Point(54f, 81f),
                        Point(52f, 83f),
                        Point(50f, 84f),
                        Point(47f, 85f),
                        Point(44f, 85f),
                        Point(41f, 84f),
                        Point(39f, 83f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-j-dot",
                points = StrokePoints.arc(
                    center = Point(58f, 14f),
                    radius = 3.82f,
                    startDeg = -90f,
                    sweepDeg = -360f,
                    samples = 32,
                ),
            ),
        ),
    ),
    /**
     * k is much taller than it is wide (aspect 0.629), so it is fitted by height.
     *
     * A straight stem and an angle: an arm running down-left from a flat top and a leg running
     * down-right to a flat foot, fused onto the stem's right edge. Written as taught: `stem` top to
     * bottom, then `angle` in to the stem and out again in one movement. Both diagonals are
     * straight and run along lines fitted through the ink's row centres (the skeleton runs off into
     * the outer corner of each flat end).
     *
     * Reworked after review on the device, where the first pass looked wrong in two ways:
     * - The diagonals' own centrelines meet at (43, 60), and a point there left a wide gap between
     *   the angle's dots and the stem's, so the angle looked detached. The point is now against the
     *   stem, its dot overlapping the stem's; the two dots either side of it spill a little into the
     *   white notches above and below the join, which is the price of a closed join.
     * - The stem ran 77 units, so its last dot fell 5 units short of the end and a bare line trailed
     *   below it. Every stroke is now a whole number of 6-unit dot spacings, so a dot lands on each
     *   end and on the point: the stem is y 9.5 to 87.5 (78 units, 2.5 inside each flat end, where
     *   the end dot's radius just fits), the arm exactly 30 (a 3-4-5 triangle, so no float rounding
     *   can drop the dot on the point) and the leg 36.07.
     *
     * The whole guide is 1 unit right of the ink's centreline (stem at x 33) so its bbox centre is
     * 48, not the catalog test's exact 3-unit limit; this moves placement, not shape.
     */
    Exercise(
        id = "english-small-k",
        title = "k",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 11,
        strokes = listOf(
            Stroke(
                id = "english-small-k-stem",
                points = StrokePoints.line(Point(33f, 9.5f), Point(33f, 87.5f)),
            ),
            Stroke(
                id = "english-small-k-angle",
                points = StrokePoints.polyline(
                    listOf(
                        Point(61f, 42f),
                        Point(37f, 60f),
                        Point(63f, 85f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * l is far taller than it is wide (aspect 0.198), so it is fitted by height.
     *
     * Andika's l is a plain straight stem with no foot or tail: ink row centre x 50 from a flat top
     * at y 7 to a flat foot at y 90. One stroke, top to bottom, sized as k's stem is to a whole 13
     * dot spacings (y 9.5 to 87.5) so a dot lands on each end and no bare line trails the last dot;
     * 2.5 units inside each end is exactly where the end dot's radius fits.
     */
    Exercise(
        id = "english-small-l",
        title = "l",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 12,
        strokes = listOf(
            Stroke(
                id = "english-small-l-stem",
                points = StrokePoints.line(Point(50f, 9.5f), Point(50f, 87.5f)),
            ),
        ),
    ),
    /**
     * m is much wider than it is tall (aspect 1.443), so it is fitted by width.
     *
     * h with a second arch: three straight legs (ink row centres x 14.9, 51.5 and 88.2, flat feet at
     * y 82.6, the stem's flat top at y 18.8) joined by two shoulders. Written as taught: `stem` top to
     * bottom, then `arch1` out of the stem, over the first shoulder and down the middle leg, then
     * `arch2` out of the middle leg, over the second shoulder and down the right leg. The arches
     * follow the skeleton until their leg turns straight and are one segment from there.
     *
     * Every stroke is a whole number of 6-unit dot spacings, so a dot lands on each end, and each
     * arch leaves its leg exactly on one of that leg's dots, so each join is closed but reads as one
     * dot rather than a clump of overlapping ones (the first pass, with each arch starting between
     * two leg dots, drew that clump; see p). The stem is y 23 to 77 (54 units; 60 would not fit
     * between the flat ends with the end dots' 2.5 radius inside the ink). `arch1` leaves the stem's
     * dot at y 35, just below the skeleton junction at y 36.3, and ends at y 76.8, a whole 14
     * spacings; `arch2` leaves arch1's dot at y 40.5 on the middle leg, cutting straight across to
     * the shoulder (the skeleton's first few points hug the leg and would put its second dot against
     * arch1's dot above), and ends at y 78.6, a whole 15 spacings. Each foot height is free inside
     * the ink (the flat feet are at y 82.6), which is what lets both arches be whole spacings without
     * reshaping them, and every other arch dot clears the dots already drawn by at least 5 units.
     */
    Exercise(
        id = "english-small-m",
        title = "m",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 13,
        strokes = listOf(
            Stroke(
                id = "english-small-m-stem",
                points = StrokePoints.line(Point(15f, 23f), Point(15f, 77f)),
            ),
            Stroke(
                id = "english-small-m-arch1",
                points = StrokePoints.polyline(
                    listOf(
                        Point(15f, 35f),
                        Point(17f, 34f),
                        Point(20f, 33f),
                        Point(22f, 32f),
                        Point(25f, 31f),
                        Point(27f, 30f),
                        Point(30f, 29f),
                        Point(32f, 28f),
                        Point(35f, 27f),
                        Point(38f, 26f),
                        Point(41f, 26f),
                        Point(44f, 26f),
                        Point(46f, 27f),
                        Point(48f, 29f),
                        Point(49f, 31f),
                        Point(50f, 34f),
                        Point(51.5f, 38f),
                        Point(51.5f, 76.8f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-m-arch2",
                points = StrokePoints.polyline(
                    listOf(
                        Point(51.5f, 40.5f),
                        Point(59f, 32f),
                        Point(62f, 32f),
                        Point(64f, 30f),
                        Point(67f, 29f),
                        Point(69f, 28f),
                        Point(72f, 27f),
                        Point(75f, 26f),
                        Point(78f, 26f),
                        Point(80f, 26f),
                        Point(83f, 27f),
                        Point(85f, 29f),
                        Point(86f, 31f),
                        Point(87f, 34f),
                        Point(87f, 37f),
                        Point(88f, 40f),
                        Point(88f, 43f),
                        Point(88.2f, 46f),
                        Point(88.2f, 78.6f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * n is a shade taller than it is wide (aspect 0.988), so it is fitted by height.
     *
     * m with one arch: two straight legs (ink row centres x 24.2 and 79.8, the stem's flat top at
     * y 8.6, both flat feet at y 90) joined by a shoulder. Written as m is: `stem` top to bottom, then
     * `arch` out of the stem, over the shoulder and down the right leg, following the skeleton until
     * the leg turns straight at y 50 and one segment from there.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and the arch leaves the stem exactly on
     * one of the stem's dots, so the join is closed but reads as one dot rather than a clump of
     * overlapping ones (the first pass, with the arch starting between two stem dots, drew that
     * clump; see p). The stem is y 12 to 84 (72 units; 78 would not fit between the flat ends with
     * the end dots' 2.5 radius inside the ink), and the arch leaves its dot at y 36, just below the
     * skeleton junction at y 34, cutting straight across to the shoulder (the skeleton's first points
     * hug the stem and would put the arch's second dot against the stem dot above). Its body is
     * scaled about its centre by 1.003 so the arch, ending at the stem's foot height, is a whole 20
     * spacings (120 units), with every other dot clear of the stem's.
     */
    Exercise(
        id = "english-small-n",
        title = "n",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 14,
        strokes = listOf(
            Stroke(
                id = "english-small-n-stem",
                points = StrokePoints.line(Point(24.2f, 12f), Point(24.2f, 84f)),
            ),
            Stroke(
                id = "english-small-n-arch",
                points = StrokePoints.polyline(
                    listOf(
                        Point(24.2f, 36f),
                        Point(31.9f, 30f),
                        Point(33.9f, 29f),
                        Point(36.9f, 28f),
                        Point(38.9f, 27f),
                        Point(40.9f, 25f),
                        Point(43f, 24f),
                        Point(46f, 22f),
                        Point(48f, 21f),
                        Point(51f, 20f),
                        Point(54f, 19f),
                        Point(56f, 19f),
                        Point(59f, 18f),
                        Point(62f, 18f),
                        Point(65f, 17f),
                        Point(67f, 18f),
                        Point(70f, 19f),
                        Point(72f, 20f),
                        Point(74f, 23f),
                        Point(76.1f, 25f),
                        Point(77.1f, 27f),
                        Point(78.1f, 30f),
                        Point(78.1f, 33f),
                        Point(79.1f, 35f),
                        Point(79.1f, 38f),
                        Point(79.1f, 41f),
                        Point(80.1f, 44.1f),
                        Point(80.1f, 47.1f),
                        Point(79.7f, 50f),
                        Point(79.7f, 84f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * o is taller than it is wide (aspect 0.921), so it is fitted by height.
     *
     * One closed ring, written as round letters are taught: counter-clockwise from the top. Its
     * centreline is about 190 canvas units, too long for one pass, and it turns smoothly all the
     * way, so it is split at the top and at the bottom, where the letter sits on the writing line
     * (a's split): `left` from the top down the left side to the bottom, then `right` from the
     * bottom up the right side and back to the top, closing the ring. The skeleton's short spurs at
     * the top, left, bottom and right run into the bulges where Andika's stroke thickens, not paths.
     *
     * For a dot to land on both split points each half must be a whole number of 6-unit dot
     * spacings, so the ring is scaled about its centre by 1.0095 to 192 units (16 + 16 spacings),
     * moving it under half a unit outward, far inside the 9-unit pen half-width.
     */
    Exercise(
        id = "english-small-o",
        title = "o",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 15,
        strokes = listOf(
            Stroke(
                id = "english-small-o-left",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50.7f, 15.8f),
                        Point(47.8f, 16.1f),
                        Point(44.9f, 16.5f),
                        Point(42f, 16.9f),
                        Point(39.4f, 17.8f),
                        Point(36.8f, 18.9f),
                        Point(34.4f, 20.4f),
                        Point(32.2f, 22.3f),
                        Point(30.2f, 24.5f),
                        Point(28.5f, 26.8f),
                        Point(27.2f, 29.3f),
                        Point(26.1f, 31.9f),
                        Point(25.2f, 34.6f),
                        Point(24.5f, 37.3f),
                        Point(23.9f, 40.1f),
                        Point(23.6f, 43f),
                        Point(23.2f, 45.9f),
                        Point(22.9f, 48.8f),
                        Point(23.2f, 51.8f),
                        Point(23.6f, 54.7f),
                        Point(23.9f, 57.6f),
                        Point(24.4f, 60.4f),
                        Point(25.1f, 63.2f),
                        Point(26.1f, 65.8f),
                        Point(27.2f, 68.4f),
                        Point(28.6f, 70.9f),
                        Point(30.2f, 73.1f),
                        Point(32.2f, 75.2f),
                        Point(34.3f, 77.1f),
                        Point(36.8f, 78.5f),
                        Point(39.4f, 79.5f),
                        Point(42.1f, 80.3f),
                        Point(45f, 80.6f),
                        Point(47.9f, 81f),
                        Point(49.3f, 81f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-o-right",
                points = StrokePoints.polyline(
                    listOf(
                        Point(49.3f, 81f),
                        Point(50.8f, 80.9f),
                        Point(53.7f, 80.6f),
                        Point(56.6f, 80.1f),
                        Point(59.4f, 79.5f),
                        Point(62f, 78.6f),
                        Point(64.5f, 77.3f),
                        Point(66.8f, 75.5f),
                        Point(68.8f, 73.5f),
                        Point(70.6f, 71.2f),
                        Point(72.1f, 68.9f),
                        Point(73.3f, 66.3f),
                        Point(74.3f, 63.7f),
                        Point(75.2f, 61f),
                        Point(75.9f, 58.3f),
                        Point(76.3f, 55.4f),
                        Point(76.7f, 52.5f),
                        Point(76.9f, 49.6f),
                        Point(77f, 46.7f),
                        Point(76.8f, 43.8f),
                        Point(76.4f, 40.9f),
                        Point(75.9f, 38f),
                        Point(75.3f, 35.2f),
                        Point(74.4f, 32.6f),
                        Point(73.4f, 29.9f),
                        Point(72.2f, 27.4f),
                        Point(70.8f, 25.1f),
                        Point(69f, 22.8f),
                        Point(67f, 20.8f),
                        Point(64.7f, 19.2f),
                        Point(62.2f, 17.9f),
                        Point(59.6f, 16.9f),
                        Point(56.8f, 16.4f),
                        Point(53.8f, 16.1f),
                        Point(50.7f, 15.8f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * p is much taller than it is wide (aspect 0.687), so it is fitted by height.
     *
     * b turned upside down: a straight stem (ink row centre x 31.8, flat top at y 8.1, flat foot at
     * y 90) with a closed bowl off its upper half, which the skeleton joins to the stem at y 25 and
     * y 54. Written as taught: `stem` top to bottom, then `bowl` out of the stem, up-right over the
     * top, clockwise down the right side and round the bottom back into the stem, in one pass. The
     * skeleton's spur up-left from the upper junction runs into the corner of the stem's top, not a
     * path.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and the bowl's two ends land exactly on
     * stem dots, so each join is closed but reads as one dot rather than a clump of overlapping ones
     * (the first pass, with the bowl starting between two stem dots, drew that clump). The stem is
     * y 13.5 to 85.5 (72 units, nearly centred between the flat ends; 78 would not fit with both end
     * dots' 2.5 radius inside the ink), which puts stem dots at y 25.5 and 55.5, beside the two
     * junctions. The bowl runs between those two dots, leaving the stem straight for the arch (the
     * skeleton's kink at the notch would put its second dot against the stem dot above), and its
     * skeleton body is scaled about its centre by 1.021 so it is a whole 19 spacings (114 units),
     * with every other dot clear of the stem's.
     */
    Exercise(
        id = "english-small-p",
        title = "p",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 16,
        strokes = listOf(
            Stroke(
                id = "english-small-p-stem",
                points = StrokePoints.line(Point(31.8f, 13.5f), Point(31.8f, 85.5f)),
            ),
            Stroke(
                id = "english-small-p-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(31.8f, 25.5f),
                        Point(36.6f, 21.7f),
                        Point(38.6f, 20.7f),
                        Point(41.7f, 19.7f),
                        Point(43.7f, 17.6f),
                        Point(45.8f, 16.6f),
                        Point(48.8f, 15.6f),
                        Point(51.9f, 14.6f),
                        Point(54f, 13.5f),
                        Point(57f, 13.5f),
                        Point(60.1f, 13.5f),
                        Point(63.1f, 14.6f),
                        Point(65.2f, 16.6f),
                        Point(67.2f, 18.6f),
                        Point(68.2f, 21.7f),
                        Point(69.3f, 23.8f),
                        Point(70.3f, 26.8f),
                        Point(70.3f, 29.9f),
                        Point(70.3f, 32.9f),
                        Point(71.3f, 36f),
                        Point(70.3f, 38f),
                        Point(70.3f, 41.1f),
                        Point(69.3f, 44.2f),
                        Point(69.3f, 47.2f),
                        Point(67.2f, 49.3f),
                        Point(66.2f, 52.3f),
                        Point(64.2f, 54.4f),
                        Point(62.1f, 56.4f),
                        Point(59.1f, 57.4f),
                        Point(57f, 58.5f),
                        Point(54f, 58.5f),
                        Point(50.9f, 58.5f),
                        Point(47.8f, 57.4f),
                        Point(45.8f, 57.4f),
                        Point(42.7f, 56.4f),
                        Point(39.7f, 55.4f),
                        Point(36.6f, 55.4f),
                        Point(34.6f, 54.4f),
                        Point(31.8f, 55.5f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * q is much taller than it is wide (aspect 0.652), so it is fitted by height.
     *
     * g with a straight descender: a closed bowl whose arch runs into the stem's slanted top-right
     * corner, and a straight stem (ink row centre x 68.1) down to a flat foot at y 90. Written round
     * first, as a, d and g are: `bowl` from the stem's top, left over the top, counter-clockwise down
     * the left and round the bottom, back into the stem at the lower junction; then `stem` top to
     * bottom. The skeleton's spur from the foot out to its right corner is the corner, not a path.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and the bowl's two ends land exactly on
     * stem dots, so each join is closed but reads as one dot (see p). The stem is y 13 to 85 (72
     * units; 78 would not fit between the slanted top, at y 9.3 above the stem's centre, and the
     * flat foot with both end dots' 2.5 radius inside the ink). The bowl starts on the stem's first
     * dot and ends on its dot at y 49, beside the junction at y 50; its skeleton body is scaled
     * about its centre by 1.026 so the bowl is a whole 18 spacings (108 units), with every other dot
     * clear of the stem's.
     */
    Exercise(
        id = "english-small-q",
        title = "q",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 17,
        strokes = listOf(
            Stroke(
                id = "english-small-q-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(68.1f, 13f),
                        Point(64.5f, 14.5f),
                        Point(61.4f, 14.5f),
                        Point(59.4f, 13.4f),
                        Point(56.3f, 13.4f),
                        Point(53.2f, 13.4f),
                        Point(50.1f, 13.4f),
                        Point(47.1f, 13.4f),
                        Point(45f, 14.5f),
                        Point(41.9f, 15.5f),
                        Point(39.9f, 16.5f),
                        Point(37.8f, 18.6f),
                        Point(35.8f, 20.6f),
                        Point(33.7f, 23.7f),
                        Point(32.7f, 25.7f),
                        Point(31.7f, 28.8f),
                        Point(31.7f, 30.9f),
                        Point(30.6f, 34f),
                        Point(30.6f, 37f),
                        Point(30.6f, 40.1f),
                        Point(30.6f, 43.2f),
                        Point(31.7f, 45.2f),
                        Point(31.7f, 48.3f),
                        Point(33.7f, 51.4f),
                        Point(34.7f, 53.4f),
                        Point(36.8f, 55.5f),
                        Point(38.8f, 57.5f),
                        Point(41.9f, 58.6f),
                        Point(45f, 58.6f),
                        Point(47.1f, 58.6f),
                        Point(50.1f, 57.5f),
                        Point(53.2f, 56.5f),
                        Point(55.3f, 55.5f),
                        Point(58.3f, 54.5f),
                        Point(60.4f, 53.4f),
                        Point(63.5f, 52.4f),
                        Point(65.5f, 51.4f),
                        Point(68.1f, 49f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-q-stem",
                points = StrokePoints.line(Point(68.1f, 13f), Point(68.1f, 85f)),
            ),
        ),
    ),
    /**
     * r is taller than it is wide (aspect 0.857), so it is fitted by height.
     *
     * A straight stem (ink row centre x 29.6, flat top at y 8.6, flat foot at y 90) and an arm that
     * leaves it just under the skeleton junction, rises over the shoulder and comes down to a
     * slanted flat cut. Written as taught: `stem` top to bottom, then `arm` out of the stem, over
     * the shoulder and down towards the cut, as n's arch is. The cut forks the skeleton into a prong
     * per corner and the arm stops short of the prongs' midpoint; the skeleton's spur up into the
     * shoulder's bulge and the one from the stem's top into the corner of its slanted cut are not
     * paths.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and the arm leaves the stem exactly on
     * one of the stem's dots, so the join is closed but reads as one dot (see p). The stem is
     * y 13.5 to 85.5 (72 units, centred between the flat ends; 78 would not fit with both end dots'
     * 2.5 radius inside the ink), and the arm leaves its dot at y 37.5, cutting straight across to
     * the shoulder (the skeleton's first points hug the stem). How far short of the cut the arm stops
     * is what makes it whole spacings without reshaping the skeleton: 5.1 units, a whole 10
     * spacings (60 units), with every other arm dot clear of the stem's. The whole guide is moved
     * 2 units left, as c and e are, so its bbox centre is not at the catalog test's 3-unit limit;
     * the canvas draws only the guide, so this moves placement, not shape.
     */
    Exercise(
        id = "english-small-r",
        title = "r",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 18,
        strokes = listOf(
            Stroke(
                id = "english-small-r-stem",
                points = StrokePoints.line(Point(27.6f, 13.5f), Point(27.6f, 85.5f)),
            ),
            Stroke(
                id = "english-small-r-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(27.6f, 37.5f),
                        Point(38f, 30f),
                        Point(40f, 29f),
                        Point(43f, 28f),
                        Point(45f, 26f),
                        Point(47f, 24f),
                        Point(49f, 22f),
                        Point(51f, 21f),
                        Point(54f, 21f),
                        Point(57f, 20f),
                        Point(59f, 19f),
                        Point(62f, 19f),
                        Point(65f, 19f),
                        Point(67f, 20f),
                        Point(70f, 22f),
                        Point(71f, 24f),
                        Point(72f, 27f),
                        Point(73f, 29f),
                        Point(73.6f, 32.4f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * s is taller than it is wide (aspect 0.808), so it is fitted by height.
     *
     * One movement between two slanted flat cuts: from the top-right cut, left over the top, down
     * the left side, along the diagonal spine, round the bottom bowl and out to the lower-left cut.
     * Each cut forks the skeleton into a prong per corner, and the skeleton's spurs at the top, left,
     * right and bottom run into the bulges where Andika's stroke thickens; none of those is a path.
     * As one stroke it is about 174 canvas units, past what a child holds in one pass, so it is
     * split where the spine crosses the letter's centre, the landmark between its two bowls: `top`
     * from the top cut to the centre (84 units) and `bottom` from the centre to the lower cut
     * (90 units), sharing the dot there.
     *
     * Both ends stop 4.4 units short of their cut's midpoint, the inset that makes the whole path a
     * whole number of 6-unit dot spacings without reshaping the skeleton (the pair of insets whose
     * larger one is smallest, so both ends sit alike), and the split is on the whole spacing
     * nearest the spine's midpoint, so both halves are whole spacings too.
     */
    Exercise(
        id = "english-small-s",
        title = "s",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 19,
        strokes = listOf(
            Stroke(
                id = "english-small-s-top",
                points = StrokePoints.polyline(
                    listOf(
                        Point(73.3f, 20.5f),
                        Point(67f, 18f),
                        Point(65f, 17f),
                        Point(62f, 17f),
                        Point(59f, 16f),
                        Point(56f, 16f),
                        Point(54f, 16f),
                        Point(51f, 16f),
                        Point(48f, 16f),
                        Point(45f, 16f),
                        Point(42f, 17f),
                        Point(39f, 17f),
                        Point(37f, 18f),
                        Point(34f, 20f),
                        Point(32f, 22f),
                        Point(31f, 24f),
                        Point(30f, 27f),
                        Point(30f, 29f),
                        Point(29f, 32f),
                        Point(30f, 35f),
                        Point(30f, 37f),
                        Point(32f, 40f),
                        Point(34f, 42f),
                        Point(36f, 43f),
                        Point(39f, 44f),
                        Point(41f, 45f),
                        Point(44f, 46f),
                        Point(47f, 46f),
                        Point(49f, 47f),
                        Point(50.9f, 47f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-s-bottom",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50.9f, 47f),
                        Point(52f, 47f),
                        Point(55f, 48f),
                        Point(58f, 49f),
                        Point(60f, 50f),
                        Point(63f, 51f),
                        Point(65f, 52f),
                        Point(67f, 54f),
                        Point(69f, 56f),
                        Point(71f, 58f),
                        Point(71f, 61f),
                        Point(72f, 63f),
                        Point(71f, 66f),
                        Point(71f, 69f),
                        Point(70f, 71f),
                        Point(69f, 74f),
                        Point(67f, 76f),
                        Point(64f, 78f),
                        Point(62f, 79f),
                        Point(59f, 79f),
                        Point(57f, 80f),
                        Point(54f, 80f),
                        Point(51f, 81f),
                        Point(48f, 81f),
                        Point(45f, 81f),
                        Point(42f, 80f),
                        Point(40f, 80f),
                        Point(37f, 79f),
                        Point(34f, 79f),
                        Point(31f, 78f),
                        Point(29f, 77f),
                        Point(22.7f, 73.8f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * t is much taller than it is wide (aspect 0.586), so it is fitted by height.
     *
     * A straight stem from a flat top, curling at the foot into a hook that ends in a vertical flat
     * cut, and a crossbar that is separate ink. Written as taught: `stem` top to bottom and round
     * the hook in one pass, then `bar` left to right. The skeleton runs the hook into the cut's top
     * corner and spurs down into the foot's bulge, neither a path, so past the curl the hook follows
     * the ink's column centres, turning up into the cut as the glyph does.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and where the bar crosses the stem one
     * bar dot sits exactly on a stem dot, so the crossing reads as one dot (see p). The stem is at
     * x 46.5, half a unit right of its ink centre, so the bar, x 28.5 to 70.5 (a whole 7 spacings,
     * the most that fits with both end dots on the ink), has a dot on it. The stem starts at y 9.5
     * (before the shift), the end dot's radius under the flat top, which puts its fifth dot at y
     * 33.5, inside the bar's ink, so the bar runs there. The stem is a whole 16 spacings (96
     * units): it stops 2.5 units short of the cut, where the last dot just fits, with its end eased
     * 4 units up the cut. The guide is then moved 4 units down (the coordinates here include it) so
     * its bbox centre is on the canvas's, the vertical counterpart of c's shift; the canvas draws
     * only the guide, so this moves placement, not shape.
     */
    Exercise(
        id = "english-small-t",
        title = "t",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 20,
        strokes = listOf(
            Stroke(
                id = "english-small-t-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(46.5f, 13.5f),
                        Point(46.5f, 66.5f),
                        Point(46.4f, 69.6f),
                        Point(46.5f, 72.6f),
                        Point(46.8f, 75.5f),
                        Point(47.3f, 78.4f),
                        Point(48.2f, 81.1f),
                        Point(49.8f, 83.5f),
                        Point(52.1f, 85.2f),
                        Point(54.9f, 86.1f),
                        Point(57.8f, 86.4f),
                        Point(60f, 86.7f),
                        Point(64f, 86.4f),
                        Point(66f, 84.9f),
                        Point(68f, 83.2f),
                        Point(70f, 81.3f),
                        Point(71f, 80.3f),
                        Point(71.7f, 79.5f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-t-bar",
                points = StrokePoints.line(Point(28.5f, 37.5f), Point(70.5f, 37.5f)),
            ),
        ),
    ),
    /**
     * u is a shade taller than it is wide (aspect 0.990), so it is fitted by height.
     *
     * n upside down: a left arm (ink row centre x 21.6, flat top at y 7) down and round the bottom,
     * rising into a right stem (x 74.8, flat top at y 7) that runs on to a flared foot. Written as
     * taught: `arc` down the left arm, round the bottom and up into the stem, then `stem` top to
     * bottom. The arm is straight to y 50 and follows the skeleton from there; the skeleton's spurs
     * into both tops' slanted corners, the foot's outer corner and the bottom bulge are not paths.
     *
     * Every stroke is a whole number of 6-unit dot spacings, and the arc ends exactly on one of the
     * stem's dots, so the join is closed but reads as one dot (see p). The stem is 72 units (78
     * would not fit between the flat top and foot with both end dots' 2.5 radius inside the ink);
     * the arc starts 3.5 units under its flat top, level with the stem's start, ends on the stem's
     * dot beside the junction, and its curve is scaled about its centre by 1.003 so it is a whole
     * 20 spacings (120 units), with every other arc dot clear of the stem's. The guide is then
     * moved 3.5 units down (the coordinates here include it) so its bbox centre is on the canvas's,
     * as t's is; the canvas draws only the guide, so this moves placement, not shape.
     */
    Exercise(
        id = "english-small-u",
        title = "u",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 21,
        strokes = listOf(
            Stroke(
                id = "english-small-u-arc",
                points = StrokePoints.polyline(
                    listOf(
                        Point(21.6f, 14f),
                        Point(21.5f, 53.4f),
                        Point(21.9f, 56.4f),
                        Point(21.9f, 59.5f),
                        Point(21.9f, 62.5f),
                        Point(22.9f, 65.5f),
                        Point(22.9f, 68.5f),
                        Point(23.9f, 70.5f),
                        Point(23.9f, 73.5f),
                        Point(25f, 76.5f),
                        Point(27f, 78.5f),
                        Point(29f, 80.5f),
                        Point(31f, 82.5f),
                        Point(34f, 83.5f),
                        Point(37f, 83.5f),
                        Point(39f, 83.5f),
                        Point(42f, 82.5f),
                        Point(45f, 82.5f),
                        Point(48f, 81.5f),
                        Point(51f, 80.5f),
                        Point(53f, 79.5f),
                        Point(55f, 77.5f),
                        Point(58.1f, 75.5f),
                        Point(60.1f, 73.5f),
                        Point(62.1f, 72.5f),
                        Point(65.1f, 71.5f),
                        Point(67.1f, 70.5f),
                        Point(70.1f, 69.5f),
                        Point(73.1f, 67.5f),
                        Point(74.8f, 68f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-u-stem",
                points = StrokePoints.line(Point(74.8f, 14f), Point(74.8f, 86f)),
            ),
        ),
    ),
    /**
     * v is a shade wider than it is tall (aspect 1.063), so it is fitted by width.
     *
     * Two straight diagonals from flat tops meeting in a flat bottom 20 units wide. The skeleton
     * forks into every corner of the flat ends, so each diagonal is a line fitted through the ink's
     * row centres (x = 0.364 y + 14.87 on the left, mirrored on the right). Meeting the two lines
     * in a sharp point would put the dots either side of it only 4.5 units apart, since the arms
     * are about 44 degrees apart, so they would overlap. Instead each arm runs straight down its
     * own centreline to the foot (x 47 and 53) and a flat bottom of exactly one dot spacing joins
     * them, as the glyph's flat bottom does.
     *
     * As one movement it is about 174 canvas units, past what a child holds in one pass, so it is
     * split at the foot: `down` the left arm (84 units, a whole 14 spacings; 90 would not fit
     * between the flat top and the foot with both end dots inside the ink), then `up` along the
     * flat bottom and up the right arm (6 + 84 units), the two sharing the dot at the foot's left
     * corner. The arms are a hair over 84 units so float rounding cannot drop the dot at the
     * corner, and the slack along them is split evenly between the top and the foot.
     */
    Exercise(
        id = "english-small-v",
        title = "v",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 22,
        strokes = listOf(
            Stroke(
                id = "english-small-v-down",
                points = StrokePoints.line(Point(18.7f, 10.5f), Point(47f, 89.7f)),
            ),
            Stroke(
                id = "english-small-v-up",
                points = StrokePoints.polyline(
                    listOf(
                        Point(47f, 89.7f),
                        Point(53f, 89.7f),
                        Point(81.3f, 10.5f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * w is much wider than it is tall (aspect 1.470), so it is fitted by width.
     *
     * Two v's side by side: four straight diagonals between flat tops (the middle peak's top is
     * flat too) and two flat feet. The skeleton forks into every corner of the flat ends, so each
     * diagonal is a line fitted through the ink's row centres, and the right half mirrors the left
     * (the glyph is symmetric about x 50). As in v, arms meeting in a sharp corner would crowd the
     * dots either side of it, so every corner is a flat of exactly one dot spacing, as the glyph's
     * flat feet and peak are: the feet run x 27 to 33 and 67 to 73 at the height where the outer
     * and inner centrelines are 6 apart, and the peak runs x 47 to 53, a few units under its flat
     * top, where the inner arms are far enough apart.
     *
     * The whole path is about 246 canvas units, so it is split at the middle peak, the landmark
     * between the two v's: `left` down the outer arm, along the left foot and up to the peak's left
     * corner (120 units); `right` along the peak, down to the right foot, along it and up the outer
     * arm (126 units), the two sharing the dot at the peak. Every diagonal is a whole number of
     * 6-unit spacings (60 outer, 54 inner), over by under 0.06 so float rounding cannot drop a
     * corner dot and the surplus does not build up along a stroke; that needs the corners to two
     * decimals. The outer arm's start slides along its centreline under the flat top to make its
     * length, and the peak's height slides along the flat-topped peak to make the inner arm's.
     */
    Exercise(
        id = "english-small-w",
        title = "w",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 23,
        strokes = listOf(
            Stroke(
                id = "english-small-w-left",
                points = StrokePoints.polyline(
                    listOf(
                        Point(12.88f, 21.13f),
                        Point(27f, 79.5f),
                        Point(33f, 79.5f),
                        Point(47f, 27.29f),
                    ),
                ),
            ),
            Stroke(
                id = "english-small-w-right",
                points = StrokePoints.polyline(
                    listOf(
                        Point(47f, 27.29f),
                        Point(53f, 27.29f),
                        Point(67f, 79.5f),
                        Point(73f, 79.5f),
                        Point(87.12f, 21.13f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * x is a shade wider than it is tall (aspect 1.083), so it is fitted by width.
     *
     * Two straight diagonals between flat tops and flat feet, crossing near the centre. Written as
     * taught: `down` from the top left to the bottom right, then `cross` from the top right to the
     * bottom left. The skeleton forks into every corner of the flat ends and knots into a short bar
     * where the diagonals cross, so each diagonal is a line fitted through the ink's row centres of
     * both its halves; the second diagonal's halves are offset about 5.6 units at the centre, and
     * one straight line through both, as a child draws it, stays on the ink.
     *
     * Where they cross, both strokes put a dot exactly on the crossing point, so it reads as one
     * dot (see p): each diagonal's ends sit a whole number of 6-unit spacings from that point along
     * its line, 8 either side, the furthest that keeps each end dot inside its flat end, making
     * both strokes a whole 16 spacings (96 units), over by 0.02 so float rounding cannot drop the
     * last dot. The diagonals cross at about 72 degrees, so the dots either side of the crossing
     * sit well apart.
     */
    Exercise(
        id = "english-small-x",
        title = "x",
        type = ExerciseType.ENGLISH_SMALL,
        difficulty = Difficulty.BEGINNER,
        order = 24,
        strokes = listOf(
            Stroke(
                id = "english-small-x-down",
                points = StrokePoints.line(Point(21.27f, 10.7f), Point(78.44f, 87.84f)),
            ),
            Stroke(
                id = "english-small-x-cross",
                points = StrokePoints.line(Point(79.81f, 11.76f), Point(19.88f, 86.78f)),
            ),
        ),
    ),
)
