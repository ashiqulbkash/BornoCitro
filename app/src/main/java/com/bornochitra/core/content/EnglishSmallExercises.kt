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
)
