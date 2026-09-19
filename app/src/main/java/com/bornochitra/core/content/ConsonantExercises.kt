package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Consonant stroke geometry. Every consonant is read off its rendered glyph's centreline (plan.md
 * Steps 11.11-11.15), never approximated by eye, so each guide sits on the letter shown above it.
 * Not every consonant carries a matra: ক and ঘ reach the headline across their whole width, খ and গ
 * only to the right of the stem, and ঙ has no headline bar at all. Step 23 re-validates every letter
 * before release.
 */
internal val consonantExercises: List<Exercise> = listOf(
    /**
     * ক is read off its rendered glyph's centreline the way the vowels are (plan.md Steps 10.8,
     * 11.1-11.10), not approximated by eye. It is written as the letter is hand-drawn: the left
     * knot first — down-left from the stem, round its blunt point and back down-right to the foot —
     * then the stem, then the right lobe curling into its filled ball terminal, and the matra last,
     * because Bengali draws the headline last. The lobe stops at the centre of the filled ball that
     * ends it, which is where the pen comes to rest. ক is wider than it is tall, so its ink is
     * fitted by width and centred vertically, as অ, আ and ও are; fitting by height would push the
     * matra past the edge of the canvas.
     */
    Exercise(
        id = "consonant-ko",
        title = "ক",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "consonant-ko-knot",
                points = StrokePoints.polyline(
                    listOf(
                        Point(56f, 32f),
                        Point(52f, 32f),
                        Point(49f, 33f),
                        Point(46f, 33f),
                        Point(44f, 34f),
                        Point(41f, 36f),
                        Point(38f, 37f),
                        Point(35f, 38f),
                        Point(32f, 39f),
                        Point(30f, 41f),
                        Point(27f, 42f),
                        Point(24f, 43f),
                        Point(21f, 45f),
                        Point(19f, 46f),
                        Point(18f, 47f),
                        Point(13f, 48f),
                        Point(11f, 49f),
                        Point(12f, 52f),
                        Point(15f, 53f),
                        Point(16f, 54f),
                        Point(18f, 55f),
                        Point(21f, 56f),
                        Point(24f, 57f),
                        Point(27f, 58f),
                        Point(30f, 59f),
                        Point(32f, 61f),
                        Point(35f, 62f),
                        Point(38f, 64f),
                        Point(40f, 66f),
                        Point(43f, 68f),
                        Point(45f, 70f),
                        Point(47f, 72f),
                        Point(49f, 75f),
                        Point(52f, 76f),
                        Point(54f, 77f),
                        Point(55f, 77f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ko-stem",
                points = StrokePoints.line(Point(56f, 19f), Point(56f, 84f), samples = 16),
            ),
            Stroke(
                id = "consonant-ko-lobe",
                points = StrokePoints.polyline(
                    listOf(
                        Point(56f, 31f),
                        Point(59f, 32f),
                        Point(62f, 32f),
                        Point(65f, 32f),
                        Point(68f, 33f),
                        Point(71f, 34f),
                        Point(74f, 35f),
                        Point(76f, 36f),
                        Point(79f, 38f),
                        Point(81f, 40f),
                        Point(83f, 43f),
                        Point(85f, 45f),
                        Point(86f, 48f),
                        Point(86f, 51f),
                        Point(86f, 54f),
                        Point(85f, 57f),
                        Point(82f, 58f),
                        Point(79f, 59f),
                        Point(76f, 59f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ko-matra",
                points = StrokePoints.line(Point(7f, 19f), Point(93f, 19f)),
            ),
        ),
    ),
    /**
     * খ is read off its rendered glyph's centreline, like ক. Its curl comes first: the pen starts at
     * the letter's top peak, carries on down into the wrap and finishes in the filled ball at the
     * top left. The body then leaves that same peak at a corner — which is why it is a second stroke
     * rather than a continuation — descends the long arc, rounds the pointed left terminal and
     * sweeps out to the stem's foot in one movement, as ক's knot does. The stem, which rises a
     * little above the headline, comes next, and the matra last. খ's headline bar is genuinely short
     * and sits only to the right of the stem: the curl occupies the top left, so there is nothing
     * for a bar to cross there. Bars only join into a continuous headline in running text.
     */
    Exercise(
        id = "consonant-kho",
        title = "খ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "consonant-kho-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(50f, 15f),
                        Point(48f, 18f),
                        Point(47f, 20f),
                        Point(47f, 22f),
                        Point(47f, 23f),
                        Point(44f, 24f),
                        Point(42f, 26f),
                        Point(40f, 29f),
                        Point(38f, 31f),
                        Point(35f, 32f),
                        Point(32f, 33f),
                        Point(29f, 34f),
                        Point(26f, 33f),
                        Point(23f, 33f),
                        Point(21f, 30f),
                        Point(20f, 28f),
                        Point(20f, 25f),
                        Point(21f, 22f),
                        Point(21f, 21f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-kho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(47f, 23f),
                        Point(50f, 24f),
                        Point(51f, 27f),
                        Point(52f, 30f),
                        Point(52f, 33f),
                        Point(52f, 36f),
                        Point(51f, 39f),
                        Point(50f, 42f),
                        Point(48f, 44f),
                        Point(45f, 46f),
                        Point(43f, 48f),
                        Point(40f, 49f),
                        Point(37f, 50f),
                        Point(34f, 51f),
                        Point(31f, 52f),
                        Point(30f, 52f),
                        Point(26f, 52f),
                        Point(22f, 51f),
                        Point(23f, 55f),
                        Point(27f, 57f),
                        Point(30f, 58f),
                        Point(32f, 60f),
                        Point(35f, 61f),
                        Point(38f, 62f),
                        Point(41f, 63f),
                        Point(44f, 64f),
                        Point(46f, 66f),
                        Point(49f, 67f),
                        Point(52f, 69f),
                        Point(54f, 71f),
                        Point(56f, 73f),
                        Point(59f, 75f),
                        Point(61f, 77f),
                        Point(63f, 80f),
                        Point(65f, 81f),
                        Point(68f, 82f),
                        Point(70f, 83f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-kho-stem",
                points = StrokePoints.line(Point(69f, 10f), Point(70f, 88f), samples = 16),
            ),
            Stroke(
                id = "consonant-kho-matra",
                points = StrokePoints.line(Point(70f, 19f), Point(83f, 19f)),
            ),
        ),
    ),
    /**
     * গ is read off its rendered glyph's centreline. Its body is a single movement: the pen leaves
     * the stem, arches over the top, comes down the left side to a sharp hairpin, turns back and
     * runs right into the hook, then curves down and round to the hook's flat terminal. The stem is
     * the body's only other end, which is why the body starts there rather than in mid-air. Like খ,
     * গ's headline bar is short and sits only to the right of the stem, and the stem rises a little
     * above it.
     */
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
                        Point(70f, 34f),
                        Point(67f, 32f),
                        Point(65f, 31f),
                        Point(62f, 29f),
                        Point(60f, 27f),
                        Point(58f, 24f),
                        Point(55f, 23f),
                        Point(53f, 21f),
                        Point(50f, 20f),
                        Point(47f, 19f),
                        Point(44f, 19f),
                        Point(41f, 18f),
                        Point(38f, 19f),
                        Point(35f, 19f),
                        Point(32f, 20f),
                        Point(29f, 21f),
                        Point(27f, 23f),
                        Point(24f, 25f),
                        Point(22f, 28f),
                        Point(20f, 30f),
                        Point(19f, 33f),
                        Point(19f, 35f),
                        Point(15f, 37f),
                        Point(18f, 39f),
                        Point(22f, 39f),
                        Point(25f, 39f),
                        Point(28f, 39f),
                        Point(31f, 38f),
                        Point(34f, 38f),
                        Point(37f, 39f),
                        Point(40f, 40f),
                        Point(43f, 41f),
                        Point(45f, 44f),
                        Point(46f, 46f),
                        Point(46f, 49f),
                        Point(46f, 52f),
                        Point(46f, 56f),
                        Point(45f, 58f),
                        Point(43f, 61f),
                        Point(40f, 63f),
                        Point(38f, 65f),
                        Point(35f, 66f),
                        Point(34f, 67f),
                        Point(30f, 70f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-go-stem",
                points = StrokePoints.line(Point(70f, 10f), Point(71f, 88f), samples = 16),
            ),
            Stroke(
                id = "consonant-go-matra",
                points = StrokePoints.line(Point(71f, 19f), Point(84f, 19f)),
            ),
        ),
    ),
    /**
     * ঘ is read off its rendered glyph's centreline. The hook comes first: the pen leaves the
     * headline, drops down the left, wraps round the bowl and carries on out and up the diagonal to
     * the flag's flat terminal — bowl and diagonal are one unbroken mass above the notch that opens
     * under them, so they are one stroke. The body leaves that same node at a corner, which is why
     * it is a second stroke: down the long diagonal, round the blunt left terminal and out along the
     * bottom sweep to the stem's foot, one movement as ক's knot and খ's body are. Then the stem, and
     * the matra last. Unlike খ and গ, ঘ's matra spans the whole letter, because the hook reaches the
     * headline at the left.
     */
    Exercise(
        id = "consonant-gho",
        title = "ঘ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "consonant-gho-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26f, 12f),
                        Point(25f, 16f),
                        Point(25f, 19f),
                        Point(24f, 22f),
                        Point(23f, 25f),
                        Point(23f, 28f),
                        Point(24f, 31f),
                        Point(25f, 34f),
                        Point(27f, 36f),
                        Point(29f, 38f),
                        Point(32f, 39f),
                        Point(35f, 39f),
                        Point(38f, 40f),
                        Point(39f, 41f),
                        Point(42f, 41f),
                        Point(45f, 40f),
                        Point(48f, 38f),
                        Point(51f, 37f),
                        Point(56f, 35f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-gho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(39f, 43f),
                        Point(39f, 42f),
                        Point(38f, 45f),
                        Point(36f, 48f),
                        Point(33f, 50f),
                        Point(31f, 51f),
                        Point(28f, 53f),
                        Point(28f, 54f),
                        Point(24f, 55f),
                        Point(25f, 58f),
                        Point(28f, 59f),
                        Point(28f, 60f),
                        Point(30f, 61f),
                        Point(33f, 62f),
                        Point(36f, 63f),
                        Point(39f, 64f),
                        Point(42f, 65f),
                        Point(45f, 66f),
                        Point(48f, 67f),
                        Point(51f, 69f),
                        Point(53f, 70f),
                        Point(56f, 72f),
                        Point(58f, 74f),
                        Point(61f, 76f),
                        Point(63f, 78f),
                        Point(65f, 80f),
                        Point(68f, 81f),
                        Point(71f, 82f),
                        Point(73f, 83f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-gho-stem",
                points = StrokePoints.line(Point(74f, 12f), Point(74f, 88f), samples = 16),
            ),
            Stroke(
                id = "consonant-gho-matra",
                points = StrokePoints.line(Point(12f, 12f), Point(88f, 12f)),
            ),
        ),
    ),
    /**
     * ঙ is read off its rendered glyph's centreline. It has neither a matra nor a dot — the
     * placeholder's arc-and-circle was wrong on both counts. The glyph is a single unbroken
     * centreline running from the tail's cut terminal, through the neck, round the closed loop, back
     * through the neck, down and out to the hairpin on the right and all the way round the bottom to
     * the left-hand terminal. That is one pen path of roughly 275 canvas units, far too long for a
     * child to hold in one pass, so it is split where the loop closes on itself at the neck — the
     * one landmark in the letter a child can actually see. Both halves are real pen movements: make
     * the loop, then sweep right round. The hairpin under the flat nose on the right stays inside
     * the sweep, as ক's left point stays inside its knot.
     */
    Exercise(
        id = "consonant-ngo",
        title = "ঙ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "consonant-ngo-loop",
                points = StrokePoints.polyline(
                    listOf(
                        Point(24f, 11f),
                        Point(27f, 14f),
                        Point(29f, 17f),
                        Point(31f, 19f),
                        Point(33f, 21f),
                        Point(36f, 24f),
                        Point(38f, 25f),
                        Point(41f, 27f),
                        Point(44f, 27f),
                        Point(47f, 28f),
                        Point(48f, 25f),
                        Point(49f, 22f),
                        Point(50f, 19f),
                        Point(52f, 17f),
                        Point(54f, 15f),
                        Point(56f, 13f),
                        Point(59f, 12f),
                        Point(62f, 12f),
                        Point(65f, 12f),
                        Point(68f, 12f),
                        Point(72f, 13f),
                        Point(74f, 15f),
                        Point(76f, 17f),
                        Point(76f, 20f),
                        Point(76f, 23f),
                        Point(76f, 26f),
                        Point(74f, 29f),
                        Point(72f, 31f),
                        Point(69f, 32f),
                        Point(66f, 33f),
                        Point(63f, 33f),
                        Point(60f, 33f),
                        Point(57f, 33f),
                        Point(54f, 33f),
                        Point(51f, 33f),
                        Point(48f, 32f),
                        Point(47f, 32f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ngo-sweep",
                points = StrokePoints.polyline(
                    listOf(
                        Point(46f, 32f),
                        Point(46f, 35f),
                        Point(46f, 38f),
                        Point(46f, 41f),
                        Point(47f, 44f),
                        Point(47f, 47f),
                        Point(47f, 50f),
                        Point(48f, 53f),
                        Point(50f, 56f),
                        Point(52f, 58f),
                        Point(55f, 58f),
                        Point(58f, 58f),
                        Point(61f, 58f),
                        Point(64f, 57f),
                        Point(67f, 56f),
                        Point(70f, 54f),
                        Point(72f, 52f),
                        Point(75f, 50f),
                        Point(77f, 48f),
                        Point(80f, 47f),
                        Point(81f, 44f),
                        Point(84f, 48f),
                        Point(85f, 50f),
                        Point(86f, 53f),
                        Point(86f, 56f),
                        Point(87f, 59f),
                        Point(87f, 62f),
                        Point(86f, 65f),
                        Point(86f, 68f),
                        Point(85f, 71f),
                        Point(84f, 74f),
                        Point(82f, 76f),
                        Point(80f, 79f),
                        Point(77f, 81f),
                        Point(74f, 82f),
                        Point(72f, 83f),
                        Point(69f, 84f),
                        Point(66f, 84f),
                        Point(62f, 85f),
                        Point(59f, 85f),
                        Point(56f, 85f),
                        Point(53f, 84f),
                        Point(50f, 84f),
                        Point(47f, 83f),
                        Point(44f, 82f),
                        Point(42f, 80f),
                        Point(39f, 79f),
                        Point(36f, 77f),
                        Point(34f, 75f),
                        Point(32f, 72f),
                        Point(30f, 70f),
                        Point(28f, 67f),
                        Point(26f, 65f),
                        Point(25f, 62f),
                        Point(23f, 59f),
                        Point(22f, 56f),
                        Point(21f, 54f),
                        Point(20f, 51f),
                        Point(18f, 48f),
                        Point(17f, 45f),
                        Point(16f, 42f),
                        Point(16f, 39f),
                        Point(15f, 36f),
                        Point(14f, 35f),
                        Point(13f, 31f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * চ is read off its rendered glyph's centreline like the letters before it. The letter below the
     * headline is a single closed bowl whose left edge is the stem, so the pen path a child is
     * taught is one continuous movement: down the left side, round the bottom, up the right and back
     * along the top to where it started. That is roughly 185 canvas units, too long to hold in one
     * pass, so it is split at the stem's foot — the corner where the left edge turns into the bottom
     * curve, and the one landmark in the letter a child can see. The stem is not quite straight, so
     * it is a polyline rather than a line like গ's. The matra is written last, as Bengali is.
     */
    Exercise(
        id = "consonant-cho",
        title = "চ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "consonant-cho-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(31f, 12f),
                        Point(31f, 15f),
                        Point(31f, 18f),
                        Point(31f, 21f),
                        Point(31f, 24f),
                        Point(32f, 27f),
                        Point(31f, 30f),
                        Point(31f, 33f),
                        Point(31f, 36f),
                        Point(31f, 39f),
                        Point(31f, 42f),
                        Point(31f, 45f),
                        Point(31f, 48f),
                        Point(31f, 51f),
                        Point(31f, 54f),
                        Point(31f, 58f),
                        Point(31f, 60f),
                        Point(31f, 64f),
                        Point(31f, 67f),
                        Point(32f, 70f),
                        Point(32f, 73f),
                        Point(32f, 76f),
                        Point(33f, 79f),
                        Point(35f, 81f),
                        Point(36f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-cho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(36f, 82f),
                        Point(38f, 83f),
                        Point(41f, 84f),
                        Point(44f, 85f),
                        Point(47f, 85f),
                        Point(50f, 84f),
                        Point(53f, 83f),
                        Point(56f, 82f),
                        Point(58f, 81f),
                        Point(61f, 79f),
                        Point(63f, 76f),
                        Point(65f, 74f),
                        Point(67f, 72f),
                        Point(69f, 69f),
                        Point(70f, 66f),
                        Point(72f, 63f),
                        Point(73f, 60f),
                        Point(73f, 58f),
                        Point(74f, 54f),
                        Point(74f, 52f),
                        Point(74f, 48f),
                        Point(73f, 45f),
                        Point(71f, 42f),
                        Point(69f, 41f),
                        Point(66f, 40f),
                        Point(63f, 39f),
                        Point(60f, 38f),
                        Point(57f, 38f),
                        Point(54f, 37f),
                        Point(51f, 37f),
                        Point(48f, 36f),
                        Point(45f, 34f),
                        Point(42f, 33f),
                        Point(40f, 31f),
                        Point(37f, 29f),
                        Point(35f, 28f),
                        Point(32f, 27f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-cho-matra",
                points = StrokePoints.line(Point(13f, 11f), Point(87f, 11f)),
            ),
        ),
    ),
    /**
     * ছ is read off its rendered glyph's centreline like the letters before it. It is চ with a large
     * lobe and a tail added on the right — the same shape Bengali ২ has — so it is written in that
     * order: the চ part first (down the stem, round the bowl, up its right side and back along the
     * top edge to the stem, one continuous movement of ~100 canvas units, where চ alone is twice
     * that and has to be split), then the lobe out of the top of the bowl, clockwise down the right
     * and back left along the bottom to its blunt tip, then the tail springing from the bottom of
     * the lobe down to the right, and the matra last.
     *
     * The bottom bar at the lower left belongs to the lobe, not to the tail: the ink there is a
     * single pen width thick, so the tail cannot reach that far left without leaving ink below it,
     * and there is none. The lobe's tip and the tail's end are flat cuts, where the skeleton forks
     * into two prongs; each guide stops two units short of the cut's midpoint, as the matra stops
     * short of its own end caps. ছ is ADVANCED like ঙ and ঔ: the lobe is a long curve that reverses
     * direction at the bottom, and the tail leaves the letter from a point in the middle of it.
     */
    Exercise(
        id = "consonant-chho",
        title = "ছ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 7,
        strokes = listOf(
            Stroke(
                id = "consonant-chho-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(29f, 12f),
                        Point(29f, 14f),
                        Point(29f, 18f),
                        Point(29f, 21f),
                        Point(29f, 22f),
                        Point(29f, 25f),
                        Point(29f, 28f),
                        Point(29f, 31f),
                        Point(29f, 34f),
                        Point(29f, 37f),
                        Point(29f, 40f),
                        Point(30f, 43f),
                        Point(31f, 46f),
                        Point(32f, 49f),
                        Point(35f, 50f),
                        Point(38f, 50f),
                        Point(41f, 50f),
                        Point(44f, 50f),
                        Point(46f, 48f),
                        Point(49f, 46f),
                        Point(51f, 44f),
                        Point(52f, 41f),
                        Point(53f, 38f),
                        Point(53f, 35f),
                        Point(54f, 32f),
                        Point(54f, 29f),
                        Point(53f, 29f),
                        Point(53f, 28f),
                        Point(50f, 28f),
                        Point(47f, 28f),
                        Point(44f, 27f),
                        Point(41f, 27f),
                        Point(38f, 26f),
                        Point(36f, 24f),
                        Point(33f, 23f),
                        Point(30f, 22f),
                        Point(29f, 22f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-chho-lobe",
                points = StrokePoints.polyline(
                    listOf(
                        Point(53f, 28f),
                        Point(56f, 28f),
                        Point(59f, 28f),
                        Point(62f, 28f),
                        Point(65f, 29f),
                        Point(68f, 31f),
                        Point(70f, 33f),
                        Point(71f, 36f),
                        Point(72f, 39f),
                        Point(73f, 42f),
                        Point(73f, 45f),
                        Point(73f, 48f),
                        Point(72f, 51f),
                        Point(71f, 54f),
                        Point(69f, 56f),
                        Point(68f, 59f),
                        Point(65f, 61f),
                        Point(63f, 62f),
                        Point(60f, 64f),
                        Point(57f, 65f),
                        Point(54f, 66f),
                        Point(52f, 68f),
                        Point(49f, 68f),
                        Point(46f, 67f),
                        Point(43f, 67f),
                        Point(40f, 67f),
                        Point(36f, 66f),
                        Point(34f, 66f),
                        Point(33f, 66f),
                        Point(31f, 66f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-chho-tail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(52f, 68f),
                        Point(53f, 71f),
                        Point(56f, 73f),
                        Point(59f, 74f),
                        Point(62f, 75f),
                        Point(65f, 76f),
                        Point(67f, 77f),
                        Point(70f, 79f),
                        Point(73f, 80f),
                        Point(76f, 82f),
                        Point(78f, 83f),
                        Point(80f, 84f),
                        Point(82f, 85f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-chho-matra",
                points = StrokePoints.line(Point(15f, 11f), Point(85f, 11f)),
            ),
        ),
    ),
    /**
     * জ is read off its rendered glyph's centreline like the letters before it. It is wider than it
     * is tall, so its ink is fitted by width and centred vertically, as ক and the wide vowels are;
     * fitting by height would push the headline past the edge of the canvas.
     *
     * Below the headline the letter is a spiral and a hook. The spiral is one movement of ~165
     * canvas units: down from the headline, counter-clockwise round the small inner bowl, up to a
     * sharp point in the middle of the letter, and then — turning back on itself — clockwise round
     * the big outer bowl to the flat cut under the headline on the far left. That is too long to
     * hold in one pass, so it is split at the point, which is both where the pen reverses and the
     * clearest landmark in the letter; the ink narrows to a wedge there and the skeleton grows a
     * spur into it. `curl` runs up into the point and `sweep` starts again just below it, where the
     * outer bowl begins, so the two guides do not retrace the same few units. The hook hangs off the
     * headline to the right and descends to its own flat cut. The matra is written last.
     */
    Exercise(
        id = "consonant-jo",
        title = "জ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 8,
        strokes = listOf(
            Stroke(
                id = "consonant-jo-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(45f, 21f),
                        Point(44f, 24f),
                        Point(42f, 27f),
                        Point(40f, 29f),
                        Point(38f, 31f),
                        Point(36f, 34f),
                        Point(35f, 37f),
                        Point(34f, 40f),
                        Point(33f, 42f),
                        Point(33f, 46f),
                        Point(34f, 48f),
                        Point(36f, 51f),
                        Point(38f, 53f),
                        Point(42f, 53f),
                        Point(44f, 54f),
                        Point(48f, 53f),
                        Point(50f, 52f),
                        Point(53f, 51f),
                        Point(56f, 49f),
                        Point(58f, 48f),
                        Point(59f, 48f),
                        Point(60f, 48f),
                        Point(61f, 45f),
                        Point(62f, 42f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-jo-sweep",
                points = StrokePoints.polyline(
                    listOf(
                        Point(60f, 48f),
                        Point(62f, 50f),
                        Point(62f, 53f),
                        Point(63f, 56f),
                        Point(63f, 59f),
                        Point(62f, 62f),
                        Point(62f, 65f),
                        Point(60f, 68f),
                        Point(59f, 70f),
                        Point(56f, 72f),
                        Point(54f, 73f),
                        Point(51f, 74f),
                        Point(48f, 75f),
                        Point(45f, 75f),
                        Point(42f, 75f),
                        Point(39f, 75f),
                        Point(36f, 74f),
                        Point(33f, 73f),
                        Point(30f, 71f),
                        Point(28f, 69f),
                        Point(26f, 67f),
                        Point(24f, 65f),
                        Point(22f, 62f),
                        Point(20f, 60f),
                        Point(19f, 57f),
                        Point(18f, 54f),
                        Point(17f, 51f),
                        Point(16f, 48f),
                        Point(15f, 45f),
                        Point(14f, 42f),
                        Point(13f, 40f),
                        Point(12f, 37f),
                        Point(12f, 36f),
                        Point(12f, 34f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-jo-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(55f, 21f),
                        Point(56f, 24f),
                        Point(57f, 27f),
                        Point(59f, 29f),
                        Point(61f, 31f),
                        Point(64f, 33f),
                        Point(67f, 34f),
                        Point(70f, 35f),
                        Point(73f, 36f),
                        Point(76f, 36f),
                        Point(79f, 36f),
                        Point(80f, 36f),
                        Point(81f, 36f),
                        Point(82f, 39f),
                        Point(82f, 42f),
                        Point(82f, 45f),
                        Point(81f, 48f),
                        Point(80f, 51f),
                        Point(80f, 54f),
                        Point(80f, 57f),
                        Point(80f, 60f),
                        Point(80f, 63f),
                        Point(80f, 66f),
                        Point(80f, 69f),
                        Point(81f, 72f),
                        Point(81f, 75f),
                        Point(82f, 78f),
                        Point(82f, 79f),
                        Point(82f, 81f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-jo-matra",
                points = StrokePoints.line(Point(5f, 20f), Point(95f, 20f)),
            ),
        ),
    ),
    /**
     * ঝ is read off its rendered glyph's centreline like the letters before it. It is wider than it
     * is tall, so its ink is fitted by width and centred vertically, as ক and জ are.
     *
     * The letter hangs a body off a long headline on the left and a post under a short headline of
     * its own on the right: ঝ's headline is genuinely broken, with fourteen canvas units of blank
     * canvas between the two bars, so one straight guide cannot cover both. The body is ক's shape —
     * the wedge leaves the middle stem, runs down-left along the upper diagonal, rounds the blunt
     * left point and comes back down-right to the stem's foot, one movement of ~105 canvas units —
     * so it is written the way ক's knot is: the wedge first, then the stem. The arm then leaves the
     * stem halfway down and runs down-right into the post, and the post descends past the arm to
     * its own foot; like খ's and গ's stems it rises a little above the headline. The skeleton forks
     * at the blunt left point into a short spur running out to the ink's leftmost tip, so the two
     * diagonals are joined by an arc that rounds the point rather than by the fork's node, as ক's
     * knot rounds its own point.
     *
     * The headline is written last, as Bengali is, in two strokes: the cap over the post, then the
     * matra over the body. Only the long bar carries the `-matra` name, which is the name the
     * catalog orders the headline on, but no part of the letter is drawn after either bar.
     */
    Exercise(
        id = "consonant-jho",
        title = "ঝ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 9,
        strokes = listOf(
            Stroke(
                id = "consonant-jho-wedge",
                points = StrokePoints.polyline(
                    listOf(
                        Point(57f, 35f),
                        Point(54f, 35f),
                        Point(51f, 35f),
                        Point(48f, 36f),
                        Point(45f, 37f),
                        Point(43f, 38f),
                        Point(40f, 39f),
                        Point(37f, 41f),
                        Point(34f, 42f),
                        Point(31f, 43f),
                        Point(28f, 45f),
                        Point(26f, 46f),
                        Point(23f, 48f),
                        Point(20f, 49f),
                        Point(18f, 51f),
                        Point(15f, 53f),
                        Point(12f, 52f),
                        Point(12f, 55f),
                        Point(18f, 59f),
                        Point(20f, 60f),
                        Point(24f, 61f),
                        Point(26f, 62f),
                        Point(29f, 63f),
                        Point(32f, 64f),
                        Point(35f, 66f),
                        Point(37f, 67f),
                        Point(40f, 69f),
                        Point(42f, 71f),
                        Point(45f, 73f),
                        Point(47f, 75f),
                        Point(49f, 78f),
                        Point(51f, 80f),
                        Point(54f, 81f),
                        Point(56f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-jho-stem",
                points = StrokePoints.line(Point(57f, 22f), Point(57f, 87f), samples = 16),
            ),
            Stroke(
                id = "consonant-jho-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(58f, 60f),
                        Point(61f, 60f),
                        Point(64f, 61f),
                        Point(66f, 63f),
                        Point(69f, 65f),
                        Point(71f, 67f),
                        Point(73f, 70f),
                        Point(75f, 72f),
                        Point(78f, 73f),
                        Point(80f, 74f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-jho-post",
                points = StrokePoints.line(Point(80f, 13f), Point(80f, 79f), samples = 16),
            ),
            Stroke(
                id = "consonant-jho-cap",
                points = StrokePoints.line(Point(80f, 22f), Point(95f, 22f)),
            ),
            Stroke(
                id = "consonant-jho-matra",
                points = StrokePoints.line(Point(5f, 22f), Point(59f, 22f)),
            ),
        ),
    ),
    /**
     * ঞ is read off its rendered glyph's centreline like the letters before it. It is wider than it
     * is tall (aspect 1.40), so its ink is fitted by width and centred vertically, as ক, জ and ঝ
     * are. Like ঙ it has no headline bar at all, so no stroke is a matra.
     *
     * The letter is a middle stem carrying a sail on the left, two bowls on the right and a
     * bar-and-base along the bottom. The stem is written first, as চ's is, because everything else
     * hangs off it. The sail then leaves the stem's top, rises over the apex, comes down the left
     * flank and stops in the filled ball that ends it — the ball is where the pen comes to rest, so
     * the sail is drawn into it, the way ক's lobe is drawn into its own ball.
     *
     * The right half is one spiral of ~120 canvas units out of the stem's top: up over the upper
     * bowl, down the right flank, left into a point between the bowls, then back out and round the
     * lower bowl to the stem. The point is a tapering wedge, not a pen terminal — the pen turns back
     * on itself there and the skeleton grows a spur into the wedge — so, as জ is split at its point,
     * the bowl runs into the point and the sweep restarts on the far side of the turn.
     *
     * The base is one movement of ~77 canvas units: the left bar from its flat-cut top, down, round
     * the corner and right along the bottom to the stem's foot. It is written last, in the place a
     * letter with a headline would write its matra.
     */
    Exercise(
        id = "consonant-nio",
        title = "ঞ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 10,
        strokes = listOf(
            Stroke(
                id = "consonant-nio-stem",
                points = StrokePoints.line(Point(62f, 33f), Point(62f, 81f), samples = 12),
            ),
            Stroke(
                id = "consonant-nio-hood",
                points = StrokePoints.polyline(
                    listOf(
                        Point(62f, 33f),
                        Point(62f, 30f),
                        Point(61f, 27f),
                        Point(60f, 24f),
                        Point(58f, 22f),
                        Point(55f, 21f),
                        Point(52f, 20f),
                        Point(49f, 20f),
                        Point(46f, 21f),
                        Point(43f, 22f),
                        Point(41f, 23f),
                        Point(38f, 25f),
                        Point(36f, 27f),
                        Point(34f, 29f),
                        Point(32f, 32f),
                        Point(31f, 35f),
                        Point(30f, 38f),
                        Point(30f, 41f),
                        Point(30f, 44f),
                        Point(33f, 45f),
                        Point(36f, 46f),
                        Point(38f, 47f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-nio-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(62f, 33f),
                        Point(66f, 32f),
                        Point(69f, 31f),
                        Point(71f, 28f),
                        Point(73f, 26f),
                        Point(76f, 25f),
                        Point(79f, 24f),
                        Point(82f, 24f),
                        Point(85f, 24f),
                        Point(88f, 25f),
                        Point(90f, 27f),
                        Point(92f, 30f),
                        Point(92f, 33f),
                        Point(92f, 36f),
                        Point(91f, 39f),
                        Point(90f, 42f),
                        Point(89f, 44f),
                        Point(88f, 45f),
                        Point(85f, 45f),
                        Point(82f, 46f),
                        Point(80f, 46f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-nio-sweep",
                points = StrokePoints.polyline(
                    listOf(
                        Point(80f, 47f),
                        Point(84f, 47f),
                        Point(90f, 48f),
                        Point(92f, 50f),
                        Point(92f, 53f),
                        Point(93f, 56f),
                        Point(92f, 59f),
                        Point(92f, 62f),
                        Point(90f, 65f),
                        Point(88f, 67f),
                        Point(85f, 67f),
                        Point(82f, 68f),
                        Point(79f, 67f),
                        Point(76f, 67f),
                        Point(73f, 65f),
                        Point(71f, 63f),
                        Point(69f, 61f),
                        Point(66f, 60f),
                        Point(62f, 58f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-nio-base",
                points = StrokePoints.polyline(
                    listOf(
                        Point(8f, 46f),
                        Point(7f, 51f),
                        Point(7f, 54f),
                        Point(7f, 57f),
                        Point(8f, 60f),
                        Point(9f, 63f),
                        Point(10f, 66f),
                        Point(12f, 68f),
                        Point(15f, 70f),
                        Point(18f, 71f),
                        Point(21f, 72f),
                        Point(24f, 72f),
                        Point(27f, 72f),
                        Point(30f, 72f),
                        Point(33f, 72f),
                        Point(36f, 71f),
                        Point(39f, 71f),
                        Point(42f, 71f),
                        Point(45f, 70f),
                        Point(48f, 71f),
                        Point(51f, 71f),
                        Point(54f, 72f),
                        Point(56f, 74f),
                        Point(59f, 75f),
                        Point(62f, 77f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * ট is read off its rendered glyph's centreline like the letters before it. It is taller than
     * it is wide (aspect 0.64), so its ink is fitted by height with x centred, the way চ and ছ are.
     *
     * The hook is written first, being the letter's topmost and leftmost stroke: down the short bar
     * from its flat-cut top, then right across the whole letter above the headline and down onto
     * the headline at the far right, where it ends.
     *
     * The stem is straight, from the headline down to the point where its foot starts to curve, and
     * the bowl carries that curve: it leaves the filled ball that heads it, runs down the right
     * flank, round the bottom and back left into the stem's foot, the way চ's body closes onto its
     * own stem. The ball is a blunt filled terminal rather than a pen cut — the skeleton collapses
     * it to a single point at its centre — so the bowl is drawn out of that centre, as ক's lobe is
     * drawn into the centre of its own ball.
     *
     * The matra is the full headline and is written last.
     */
    Exercise(
        id = "consonant-tto",
        title = "ট",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 11,
        strokes = listOf(
            Stroke(
                id = "consonant-tto-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(29f, 9f),
                        Point(29f, 11f),
                        Point(29f, 14f),
                        Point(30f, 17f),
                        Point(31f, 19f),
                        Point(34f, 21f),
                        Point(37f, 22f),
                        Point(40f, 23f),
                        Point(43f, 23f),
                        Point(46f, 23f),
                        Point(49f, 23f),
                        Point(52f, 23f),
                        Point(55f, 23f),
                        Point(58f, 24f),
                        Point(61f, 25f),
                        Point(63f, 27f),
                        Point(64f, 29f),
                        Point(65f, 32f),
                        Point(65f, 35f),
                        Point(65f, 37f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-tto-stem",
                points = StrokePoints.line(Point(37f, 37f), Point(37f, 80f), samples = 12),
            ),
            Stroke(
                id = "consonant-tto-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(58f, 58f),
                        Point(61f, 58f),
                        Point(64f, 59f),
                        Point(67f, 61f),
                        Point(67f, 64f),
                        Point(67f, 67f),
                        Point(66f, 70f),
                        Point(65f, 73f),
                        Point(64f, 76f),
                        Point(62f, 78f),
                        Point(60f, 80f),
                        Point(58f, 82f),
                        Point(56f, 84f),
                        Point(53f, 85f),
                        Point(50f, 86f),
                        Point(47f, 86f),
                        Point(44f, 86f),
                        Point(41f, 85f),
                        Point(39f, 83f),
                        Point(38f, 80f),
                        Point(37f, 80f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-tto-matra",
                points = StrokePoints.line(Point(26f, 37f), Point(74f, 37f)),
            ),
        ),
    ),
    /**
     * ঠ is read off its rendered glyph's centreline like the letters before it. It is taller than
     * it is wide (aspect 0.61), so its ink is fitted by height with x centred, the way ট is.
     *
     * The glyph is one continuous movement broken only by the headline: the pen comes down the
     * flourish at the top, crosses the matra and carries straight on into the bowl's left arm,
     * sweeps out to the left, round the foot, up the right flank and closes back onto the matra.
     *
     * The skeleton does not say that on its own. The matra is seven canvas units thick, so
     * skeletonizing it knots the flourish, the two halves of the headline and the bowl's two arms
     * into a small diamond just below it: the flourish's branch stops short at x=45 and the bowl's
     * loop reports both of its ends at the same junction point. Reading those junctions literally
     * would land the flourish and the bowl on the headline at different places and leave the loop
     * hanging open, so the two strokes are joined at the headline crossing instead — which is also
     * the only landmark in the letter a child can see, and the split keeps each stroke short
     * enough to hold in one pass.
     *
     * The hook therefore runs from the rounded terminal at the top down onto the matra, and the
     * bowl takes over at that same point, ending five units along the headline — under one dot
     * spacing — so the loop reads as closed. The matra is written last.
     */
    Exercise(
        id = "consonant-ttho",
        title = "ঠ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 12,
        strokes = listOf(
            Stroke(
                id = "consonant-ttho-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(42f, 9f),
                        Point(42f, 12f),
                        Point(40f, 14f),
                        Point(39f, 16f),
                        Point(38f, 19f),
                        Point(38f, 22f),
                        Point(39f, 25f),
                        Point(40f, 27f),
                        Point(42f, 30f),
                        Point(43f, 32f),
                        Point(44f, 35f),
                        Point(46f, 37f),
                        Point(47f, 37f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ttho-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(47f, 37f),
                        Point(48f, 40f),
                        Point(49f, 43f),
                        Point(49f, 46f),
                        Point(49f, 48f),
                        Point(50f, 51f),
                        Point(50f, 54f),
                        Point(49f, 57f),
                        Point(48f, 60f),
                        Point(47f, 62f),
                        Point(45f, 65f),
                        Point(43f, 67f),
                        Point(41f, 68f),
                        Point(38f, 69f),
                        Point(36f, 70f),
                        Point(33f, 71f),
                        Point(33f, 74f),
                        Point(33f, 77f),
                        Point(35f, 79f),
                        Point(37f, 81f),
                        Point(39f, 83f),
                        Point(41f, 85f),
                        Point(44f, 86f),
                        Point(46f, 86f),
                        Point(49f, 87f),
                        Point(52f, 86f),
                        Point(55f, 86f),
                        Point(58f, 85f),
                        Point(60f, 84f),
                        Point(62f, 82f),
                        Point(64f, 79f),
                        Point(65f, 77f),
                        Point(66f, 74f),
                        Point(66f, 71f),
                        Point(66f, 68f),
                        Point(66f, 65f),
                        Point(65f, 62f),
                        Point(65f, 60f),
                        Point(64f, 57f),
                        Point(63f, 55f),
                        Point(61f, 52f),
                        Point(60f, 50f),
                        Point(58f, 47f),
                        Point(56f, 45f),
                        Point(54f, 43f),
                        Point(53f, 40f),
                        Point(52f, 37f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ttho-matra",
                points = StrokePoints.line(Point(27f, 37f), Point(73f, 37f)),
            ),
        ),
    ),
    /**
     * ড is read off its rendered glyph's centreline like the letters before it. It is wider than it
     * is tall (aspect 1.23), so its ink is fitted by width with y centred, the way ক, জ and ঝ are.
     *
     * Below the headline the letter is one continuous movement: down the short middle stem, right
     * and down through the shallow V, up to the sharp point at the top right, then back down the
     * right flank, round the bottom and up the long left arm to the flat cut where the pen lifts.
     * That is about 190 canvas units, more than a child can hold in one pass, so it is split at the
     * point — where the arm and the flank converge into one tapered tip and the pen turns back on
     * itself, the same split জ and ঞ take.
     *
     * The arm therefore runs from the matra crossing down into the V and up into the point, and the
     * bowl starts again at that same point, runs down the flank and round the bottom and stops 2
     * units short of the midpoint of the left arm's flat-cut terminal, as ছ's cut ends do. Neither
     * tip touches the headline: below the matra the letter hangs from the stem alone.
     *
     * The pen turns at (77,41), the highest point of the wedge that is still a full pen thick. The
     * ink runs on above it to an apex at (79,35), but only as the taper where the two edges of the
     * turn converge — half the ink's thickness there is 0.3 canvas units against the pen's 4.71 —
     * so no pen centre ever reaches it, and a guide drawn up to it hangs outside the stroke and
     * forks away from where the bowl begins. Both strokes meet at the turn instead, so their dot
     * rows share a dot. That also leaves the arm 72.3 canvas units long: the guide drops a dot
     * every 6 units from a stroke's start, so a stroke that stops just short of a multiple of the
     * spacing draws almost a whole spacing of bare path past its last dot, which shows on the
     * device as a tail hanging off the tip.
     *
     * The matra is the full headline and is written last.
     */
    Exercise(
        id = "consonant-ddo",
        title = "ড",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 13,
        strokes = listOf(
            Stroke(
                id = "consonant-ddo-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(45f, 16f),
                        Point(45f, 20f),
                        Point(45f, 22f),
                        Point(45f, 26f),
                        Point(45f, 28f),
                        Point(45f, 32f),
                        Point(45f, 34f),
                        Point(45f, 38f),
                        Point(45f, 40f),
                        Point(45f, 44f),
                        Point(46f, 46f),
                        Point(46f, 50f),
                        Point(47f, 52f),
                        Point(50f, 54f),
                        Point(53f, 55f),
                        Point(56f, 55f),
                        Point(59f, 55f),
                        Point(62f, 54f),
                        Point(64f, 52f),
                        Point(67f, 50f),
                        Point(69f, 48f),
                        Point(71f, 46f),
                        Point(73f, 44f),
                        Point(76f, 42f),
                        Point(77f, 41f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ddo-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(77f, 41f),
                        Point(79f, 44f),
                        Point(81f, 46f),
                        Point(82f, 49f),
                        Point(83f, 52f),
                        Point(83f, 55f),
                        Point(83f, 58f),
                        Point(83f, 61f),
                        Point(83f, 64f),
                        Point(83f, 67f),
                        Point(81f, 70f),
                        Point(80f, 73f),
                        Point(78f, 76f),
                        Point(76f, 78f),
                        Point(73f, 80f),
                        Point(71f, 81f),
                        Point(68f, 82f),
                        Point(65f, 83f),
                        Point(62f, 83f),
                        Point(59f, 84f),
                        Point(56f, 83f),
                        Point(53f, 83f),
                        Point(50f, 83f),
                        Point(47f, 82f),
                        Point(44f, 81f),
                        Point(41f, 80f),
                        Point(38f, 78f),
                        Point(36f, 76f),
                        Point(33f, 74f),
                        Point(31f, 72f),
                        Point(29f, 69f),
                        Point(27f, 67f),
                        Point(26f, 64f),
                        Point(24f, 61f),
                        Point(23f, 59f),
                        Point(21f, 56f),
                        Point(20f, 53f),
                        Point(19f, 50f),
                        Point(18f, 47f),
                        Point(17f, 44f),
                        Point(16f, 41f),
                        Point(15f, 38f),
                        Point(14f, 35f),
                        Point(13f, 33f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ddo-matra",
                points = StrokePoints.line(Point(5f, 16f), Point(95f, 16f)),
            ),
        ),
    ),
    /**
     * ঢ is read off its rendered glyph's centreline like the letters before it. It is a shade
     * taller than it is wide (aspect 0.95), so its ink is fitted by height with x centred, the way
     * ট and ঠ are.
     *
     * Below the headline the glyph is a single continuous movement of about 156 canvas units: down
     * the long stem, round the bottom bowl, up the right flank and over the top into the filled
     * ball that ends the curl. That is far more than a child can hold in one pass, so it is split
     * at the foot — where the stem stops being straight and the bowl's curve begins — which is the
     * split ট takes and the only landmark below the matra a child can see.
     *
     * The stem is therefore straight from the headline down to the foot, and the bowl carries every
     * curve: round the bottom, up the right flank, over the top and inwards to the centre of the
     * ball. That ball is a blunt filled terminal rather than a pen cut, so the skeleton collapses it
     * to one point and the bowl is drawn into that centre, the way ক's lobe is drawn into its own
     * ball and ট's bowl is drawn out of it.
     *
     * The matra is the full headline, inset 2 units from each end cap, and is written last.
     */
    Exercise(
        id = "consonant-ddho",
        title = "ঢ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 14,
        strokes = listOf(
            Stroke(
                id = "consonant-ddho-stem",
                points = StrokePoints.line(Point(31f, 12f), Point(31f, 72f)),
            ),
            Stroke(
                id = "consonant-ddho-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(31f, 72f),
                        Point(32f, 75f),
                        Point(32f, 78f),
                        Point(34f, 80f),
                        Point(36f, 82f),
                        Point(38f, 84f),
                        Point(41f, 84f),
                        Point(44f, 85f),
                        Point(47f, 85f),
                        Point(50f, 84f),
                        Point(53f, 84f),
                        Point(55f, 83f),
                        Point(58f, 81f),
                        Point(60f, 80f),
                        Point(63f, 78f),
                        Point(65f, 76f),
                        Point(67f, 74f),
                        Point(69f, 72f),
                        Point(70f, 69f),
                        Point(72f, 67f),
                        Point(73f, 64f),
                        Point(74f, 62f),
                        Point(74f, 59f),
                        Point(75f, 56f),
                        Point(75f, 53f),
                        Point(75f, 50f),
                        Point(75f, 47f),
                        Point(73f, 45f),
                        Point(71f, 44f),
                        Point(68f, 43f),
                        Point(65f, 42f),
                        Point(63f, 43f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-ddho-matra",
                points = StrokePoints.line(Point(13f, 12f), Point(87f, 12f)),
            ),
        ),
    ),
    /**
     * ণ is read off its rendered glyph's centreline like the letters before it. It is taller than
     * it is wide (aspect 0.86), so its ink is fitted by height with x centred, the way ট, ঠ and ঢ
     * are.
     *
     * It is built like গ: a long post, a short headline bar that sits only to the right of the
     * post, the post rising a little above that bar, and the whole letter hanging off the post's
     * left. Here what hangs there is a spiral of about 98 canvas units — short enough to hold in
     * one pass, so unlike জ's and ঞ's spirals it is not split.
     *
     * The loop therefore leaves the post at mid-height, where the letter's arm tapers into it,
     * arches over the top, comes down the left flank, runs along the bottom and curls inwards into
     * the centre of the filled ball that ends it. That ball is a blunt filled terminal rather than
     * a pen cut, so the skeleton collapses it to a single point and the loop is drawn into that
     * centre, the way ক's lobe is drawn into its own ball.
     *
     * The stem follows, top to foot and inset 2 units from each end cap, as গ's does, and the
     * matra — the short bar right of the post — is written last.
     */
    Exercise(
        id = "consonant-nno",
        title = "ণ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 15,
        strokes = listOf(
            Stroke(
                id = "consonant-nno-loop",
                points = StrokePoints.polyline(
                    listOf(
                        Point(68f, 33f),
                        Point(65f, 32f),
                        Point(62f, 30f),
                        Point(60f, 28f),
                        Point(58f, 26f),
                        Point(56f, 24f),
                        Point(53f, 22f),
                        Point(50f, 21f),
                        Point(47f, 20f),
                        Point(44f, 19f),
                        Point(41f, 18f),
                        Point(38f, 18f),
                        Point(35f, 18f),
                        Point(32f, 19f),
                        Point(30f, 20f),
                        Point(27f, 21f),
                        Point(24f, 23f),
                        Point(22f, 25f),
                        Point(21f, 28f),
                        Point(20f, 31f),
                        Point(20f, 34f),
                        Point(20f, 37f),
                        Point(20f, 40f),
                        Point(21f, 43f),
                        Point(22f, 46f),
                        Point(24f, 48f),
                        Point(26f, 51f),
                        Point(28f, 52f),
                        Point(31f, 53f),
                        Point(34f, 54f),
                        Point(37f, 54f),
                        Point(40f, 52f),
                        Point(41f, 50f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-nno-stem",
                points = StrokePoints.line(Point(68f, 9f), Point(68f, 88f), samples = 16),
            ),
            Stroke(
                id = "consonant-nno-matra",
                points = StrokePoints.line(Point(68f, 19f), Point(83f, 19f)),
            ),
        ),
    ),
)
