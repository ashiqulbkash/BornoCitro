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
    /**
     * ত is read off its rendered glyph's centreline like the letters before it. It is wider than it
     * is tall (aspect 1.27), so its ink is fitted by width with y centred, the way ক, জ, ঝ and ড
     * are; fitting by height would push its headline past the edge of the canvas.
     *
     * Its headline does not touch the rest of the letter — the font leaves five canvas units of
     * blank between the bar and the body — so, unlike every consonant before it, nothing below the
     * matra hangs off it. The body is one spiral of about 173 canvas units: in from the flat-cut
     * terminal at the top left, down the long left arm, round the bottom, up the right flank, over
     * the top and curling inwards into the filled ball at the letter's centre.
     *
     * That is far more than a child can hold in one pass, and the spiral turns smoothly from end to
     * end, so it has no corner or neck to break at. It is split at the foot instead — the lowest
     * point of the bowl, where the letter sits on the writing line — which is the one landmark
     * below the headline a child can see, and the split ঢ takes. The arm runs from the tip to the
     * foot and the bowl takes over at that same point, so the two share a dot.
     *
     * The arm starts 2 units short of the midpoint of its flat-cut terminal, as ছ's cut ends do:
     * the skeleton forks into a prong for each corner of the cut, and the midpoint between them is
     * where the pen would come down. The bowl ends at the centre of the ball that closes the
     * spiral — a blunt filled terminal, not a pen cut, whose largest inscribed disc is 11.6 canvas
     * units against the pen's 4.78 — so the skeleton collapses it to a single point and the curl is
     * drawn into that centre, the way ক's lobe is drawn into its own ball.
     *
     * The matra is the full headline, inset 2 units from each end cap, and is written last.
     */
    Exercise(
        id = "consonant-to",
        title = "ত",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 16,
        strokes = listOf(
            Stroke(
                id = "consonant-to-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(14f, 34f),
                        Point(15f, 37f),
                        Point(15f, 40f),
                        Point(16f, 42f),
                        Point(17f, 45f),
                        Point(18f, 48f),
                        Point(19f, 51f),
                        Point(20f, 53f),
                        Point(22f, 56f),
                        Point(23f, 58f),
                        Point(24f, 61f),
                        Point(26f, 63f),
                        Point(27f, 66f),
                        Point(29f, 68f),
                        Point(31f, 70f),
                        Point(33f, 73f),
                        Point(35f, 75f),
                        Point(37f, 76f),
                        Point(40f, 78f),
                        Point(43f, 79f),
                        Point(45f, 80f),
                        Point(48f, 81f),
                        Point(51f, 82f),
                        Point(54f, 82f),
                        Point(56f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-to-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(56f, 82f),
                        Point(59f, 82f),
                        Point(62f, 82f),
                        Point(65f, 81f),
                        Point(67f, 81f),
                        Point(70f, 80f),
                        Point(73f, 78f),
                        Point(75f, 77f),
                        Point(77f, 75f),
                        Point(79f, 72f),
                        Point(81f, 70f),
                        Point(82f, 67f),
                        Point(83f, 64f),
                        Point(83f, 62f),
                        Point(83f, 59f),
                        Point(83f, 56f),
                        Point(83f, 53f),
                        Point(82f, 50f),
                        Point(82f, 47f),
                        Point(80f, 45f),
                        Point(79f, 42f),
                        Point(77f, 40f),
                        Point(75f, 38f),
                        Point(72f, 36f),
                        Point(70f, 35f),
                        Point(67f, 34f),
                        Point(64f, 34f),
                        Point(61f, 34f),
                        Point(58f, 34f),
                        Point(57f, 37f),
                        Point(55f, 39f),
                        Point(53f, 42f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-to-matra",
                points = StrokePoints.line(Point(5f, 18f), Point(95f, 18f)),
            ),
        ),
    ),
    /**
     * থ is read off its rendered glyph's centreline like the letters before it. It is taller than
     * it is wide (aspect 0.89), so its ink is fitted by height with x centred, the way ট, ঠ, ঢ and
     * ণ are.
     *
     * Structurally it is গ and ণ once more: a long post that rises above the headline, a matra bar
     * only to the right of the post, and the rest of the letter hanging off the post's left. Here
     * that rest is a spiral with a filled ball curled inside it, whose outer end sweeps down and
     * right into the post's foot — about 147 canvas units in all, more than a child can hold in
     * one pass.
     *
     * The two parts meet at the loop's leftmost point, where the two arms leave in a 54° fork and a
     * pen running through swings round by 126°, so that corner is both the only landmark below
     * the loop a child can see and the natural place to break. The loop is written first, from the
     * corner round counter-clockwise — along the bottom, up the right flank, back left over the
     * top, down the left and curling inwards into the ball — the way ও's bowl is written, and the
     * tail then restarts at that same corner and runs down to the post's foot.
     *
     * The corner is taken at (25,54), the point on the skeleton's spur farthest from the junction
     * where the ink is still a full pen thick. The spur runs on to (19,50), but the ink there has
     * thinned to 0.27 canvas units against the pen's 4.39 — that is the round cap of the turn, not
     * a path the pen's centre ever travels — and a guide taken to the spur's tip would hang outside
     * the stroke, which is what ড's first pass got wrong.
     *
     * The loop ends at (22,34), the centre of the ball that closes the spiral: a blunt filled
     * terminal, not a pen cut, whose largest inscribed disc is 7.09 canvas units against the pen's
     * 4.39. The skeleton collapses it to a point two units past the centre, so the curl is
     * truncated at its closest approach and drawn straight in, the way ত's and ণ's are.
     *
     * The stem is inset 2 units from each end cap as গ's and ণ's are, and the matra — the short bar
     * right of the post — is inset 2 units from its own cap and written last.
     */
    Exercise(
        id = "consonant-tho",
        title = "থ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 17,
        strokes = listOf(
            Stroke(
                id = "consonant-tho-loop",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 54f),
                        Point(28f, 55f),
                        Point(29f, 53f),
                        Point(32f, 52f),
                        Point(35f, 51f),
                        Point(37f, 50f),
                        Point(40f, 49f),
                        Point(42f, 48f),
                        Point(44f, 47f),
                        Point(47f, 45f),
                        Point(48f, 43f),
                        Point(50f, 40f),
                        Point(50f, 37f),
                        Point(51f, 35f),
                        Point(51f, 32f),
                        Point(50f, 29f),
                        Point(50f, 26f),
                        Point(48f, 24f),
                        Point(47f, 22f),
                        Point(44f, 20f),
                        Point(42f, 19f),
                        Point(39f, 18f),
                        Point(36f, 18f),
                        Point(33f, 18f),
                        Point(30f, 18f),
                        Point(28f, 19f),
                        Point(25f, 20f),
                        Point(23f, 21f),
                        Point(21f, 23f),
                        Point(20f, 26f),
                        Point(19f, 29f),
                        Point(19f, 31f),
                        Point(21f, 33f),
                        Point(22f, 34f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-tho-tail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 54f),
                        Point(28f, 55f),
                        Point(29f, 57f),
                        Point(31f, 60f),
                        Point(33f, 60f),
                        Point(36f, 61f),
                        Point(39f, 62f),
                        Point(41f, 63f),
                        Point(44f, 64f),
                        Point(46f, 66f),
                        Point(48f, 67f),
                        Point(51f, 69f),
                        Point(53f, 70f),
                        Point(55f, 72f),
                        Point(57f, 74f),
                        Point(59f, 76f),
                        Point(61f, 79f),
                        Point(63f, 81f),
                        Point(65f, 82f),
                        Point(68f, 83f),
                        Point(69f, 83f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-tho-stem",
                points = StrokePoints.line(Point(70f, 9f), Point(70f, 88f), samples = 16),
            ),
            Stroke(
                id = "consonant-tho-matra",
                points = StrokePoints.line(Point(70f, 19f), Point(85f, 19f)),
            ),
        ),
    ),
    /**
     * দ is read off its rendered glyph's centreline like the letters before it. It is a shade
     * taller than it is wide (aspect 0.91), so its ink is fitted by height with x centred, the way
     * ট, ঠ, ঢ, ণ and থ are.
     *
     * Below the full headline the glyph is one continuous movement of ~148 canvas units: down the
     * left stem, into the sharp V at the bottom, up the long diagonal to the peak at the top right,
     * then back down the post to the baseline. That is more than a child can hold in one pass, so
     * it is split at the peak — the letter's highest point below the headline, where the pen turns
     * back on itself — the same split ড, জ and ঞ take. The arm runs from the headline into the V
     * and up into the peak, the post starts again at that same point and descends.
     *
     * The V's vertex is taken at (31,55), where the stem's and the diagonal's centrelines cross.
     * The ink runs on to a tip at (31,66), but that overhang is the corner's outer taper, not a
     * path the pen's centre travels: the skeleton's spur into it thins from 4.9 to 1.87 canvas
     * units against the pen's 4.88, so a guide taken to the spur's tip would hang outside the
     * stroke — the mistake ড's first pass made — and would double back over itself for no ink.
     *
     * The peak is taken at (68,30), the highest point of the wedge between the diagonal and the
     * post that is still a full pen thick. The skeleton's own arch tops out two units lower,
     * dragged down by the ink filled in below the peak, and stopping there would leave the glyph's
     * pointed cap uncovered.
     *
     * The post ends 1 unit short of the midpoint of its slanted end cap, whose two corners are
     * where the skeleton forks at (69,84), as ছ's flat-cut ends do — closer than the usual 2 units
     * because the guide drops a dot every 6 canvas units from a stroke's start, so a stroke ending
     * just short of a multiple of that spacing hangs a whole spacing of bare path past its last
     * dot (ড's grey tail). At 60.2 units the post clears ten spacings with 0.2 to spare.
     *
     * The matra is the full headline, inset 2 units from each end cap, and is written last.
     */
    Exercise(
        id = "consonant-do",
        title = "দ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 18,
        strokes = listOf(
            Stroke(
                id = "consonant-do-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 12f),
                        Point(30f, 15f),
                        Point(30f, 18f),
                        Point(30f, 21f),
                        Point(30f, 24f),
                        Point(30f, 27f),
                        Point(30f, 30f),
                        Point(30f, 33f),
                        Point(30f, 36f),
                        Point(30f, 39f),
                        Point(30f, 42f),
                        Point(30f, 45f),
                        Point(30f, 48f),
                        Point(30f, 51f),
                        Point(30f, 54f),
                        Point(31f, 55f),
                        Point(34f, 54f),
                        Point(37f, 54f),
                        Point(39f, 52f),
                        Point(41f, 50f),
                        Point(43f, 48f),
                        Point(45f, 46f),
                        Point(47f, 44f),
                        Point(49f, 42f),
                        Point(51f, 41f),
                        Point(54f, 39f),
                        Point(56f, 37f),
                        Point(58f, 36f),
                        Point(61f, 34f),
                        Point(63f, 33f),
                        Point(66f, 33f),
                        Point(68f, 31f),
                        Point(68f, 30f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-do-post",
                points = StrokePoints.polyline(
                    listOf(
                        Point(68f, 30f),
                        Point(68f, 33f),
                        Point(70f, 35f),
                        Point(70f, 37f),
                        Point(69f, 40f),
                        Point(69f, 43f),
                        Point(68f, 46f),
                        Point(67f, 48f),
                        Point(67f, 51f),
                        Point(66f, 54f),
                        Point(66f, 57f),
                        Point(66f, 60f),
                        Point(66f, 63f),
                        Point(66f, 66f),
                        Point(66f, 69f),
                        Point(67f, 72f),
                        Point(67f, 74f),
                        Point(67f, 77f),
                        Point(68f, 80f),
                        Point(69f, 83f),
                        Point(69f, 85f),
                        Point(70f, 88f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-do-matra",
                points = StrokePoints.line(Point(14f, 12f), Point(86f, 12f)),
            ),
        ),
    ),
    /**
     * ধ is read off its rendered glyph's centreline like the letters before it. It is a shade
     * taller than it is wide (aspect 0.90), so its ink is fitted by height with x centred, the way
     * ট, ঠ, ঢ, ণ, থ and দ are.
     *
     * Structurally it is গ, ণ and থ once more: a post on the right, a matra bar only to the right
     * of the post, and the body hanging off the post's left. Here the body is a wide "<" whose two
     * ends both run into the post — an arm leaving the post's side at (68,29) where it tapers in,
     * down-left to the corner at (20,51), then a long sweep down-right into the post's foot at
     * (68,82) — with a loop spiralling up out of the arm's middle, over the top-left of the letter
     * and inwards to the blunt tongue that ends it.
     *
     * The body is one movement of ~116 canvas units. At the junction (35,39) the arm and the sweep
     * leave in a 15° fork, so a pen running through barely changes heading, while the loop leaves
     * at 96° to the arm and 69° to the sweep. The glyph itself therefore says arm-and-sweep is the
     * pen's path and the loop is joined onto it, so the body is written first and the loop starts
     * on it, the way ক's knot starts on a stem it has yet to draw.
     *
     * The corner is the junction where the arm's and the sweep's centrelines cross. The ink runs on
     * up-left to a tip at (13,49) and the skeleton grows a spur into it, but that is the corner's
     * outer mitre rather than a path the pen's centre travels: it thins from 4.9 to 0.92 canvas
     * units against the pen's 4.89, exactly the taper দ's V has.
     *
     * The loop ends at (42,20), the lowest point inside the tongue whose ink is still a full pen
     * thick. The tongue is wider than it is tall, so the skeleton stops at its top edge instead of
     * running into it, the way it collapses ক's and ণ's filled balls to a point; the curl is drawn
     * into it as ক's lobe is drawn into its own ball.
     *
     * The post runs from the matra's centreline to its end cap and the matra is the bar right of
     * the post, each stopping 1 to 3 units short of its cap — whichever leaves the least bare path
     * past the stroke's last dot, since the guide drops a dot every 6 canvas units from a stroke's
     * start and a longer remainder hangs off the tip as a grey tail (ড's, then দ's). The matra is
     * written last.
     */
    Exercise(
        id = "consonant-dho",
        title = "ধ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 19,
        strokes = listOf(
            Stroke(
                id = "consonant-dho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(68f, 29f),
                        Point(65f, 30f),
                        Point(63f, 30f),
                        Point(60f, 31f),
                        Point(57f, 31f),
                        Point(55f, 32f),
                        Point(52f, 33f),
                        Point(50f, 34f),
                        Point(47f, 35f),
                        Point(45f, 36f),
                        Point(42f, 37f),
                        Point(39f, 38f),
                        Point(36f, 38f),
                        Point(35f, 40f),
                        Point(33f, 42f),
                        Point(31f, 43f),
                        Point(28f, 45f),
                        Point(26f, 46f),
                        Point(24f, 48f),
                        Point(21f, 49f),
                        Point(20f, 51f),
                        Point(21f, 54f),
                        Point(21f, 57f),
                        Point(24f, 57f),
                        Point(26f, 58f),
                        Point(29f, 59f),
                        Point(32f, 60f),
                        Point(35f, 60f),
                        Point(37f, 61f),
                        Point(40f, 62f),
                        Point(42f, 64f),
                        Point(44f, 65f),
                        Point(47f, 66f),
                        Point(49f, 68f),
                        Point(51f, 69f),
                        Point(53f, 71f),
                        Point(56f, 73f),
                        Point(58f, 75f),
                        Point(60f, 77f),
                        Point(62f, 79f),
                        Point(64f, 80f),
                        Point(67f, 81f),
                        Point(68f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-dho-loop",
                points = StrokePoints.polyline(
                    listOf(
                        Point(36f, 39f),
                        Point(34f, 36f),
                        Point(32f, 34f),
                        Point(30f, 33f),
                        Point(28f, 30f),
                        Point(27f, 28f),
                        Point(26f, 25f),
                        Point(26f, 22f),
                        Point(26f, 20f),
                        Point(27f, 17f),
                        Point(29f, 15f),
                        Point(32f, 14f),
                        Point(34f, 13f),
                        Point(37f, 13f),
                        Point(40f, 13f),
                        Point(42f, 15f),
                        Point(43f, 17f),
                        Point(42f, 20f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-dho-post",
                points = StrokePoints.line(Point(68f, 13f), Point(68f, 87f)),
            ),
            Stroke(
                id = "consonant-dho-matra",
                points = StrokePoints.line(Point(68f, 13f), Point(86f, 13f)),
            ),
        ),
    ),
    /**
     * ন is read off its rendered glyph's centreline like the letters before it, but off the
     * phone's own font rather than the scratchpad's: Noto Sans Bengali redrew ন between v2.001,
     * which Android ships and the app therefore shows above the canvas, and the newer v3.011. v3
     * ends the curl early in a filled ball; v2 carries the spiral a half-turn further, round a
     * closed counter, and ends it in a flat slanted cut. The guide follows the letter the child
     * actually sees.
     *
     * It renders square (aspect 1.004), so its ink is fitted by height with x centred, the way ঢ,
     * দ and ধ are. Above the baseline it is built like ঢ — a full headline with a post running
     * from it down to the foot — and below it like ণ: one curl hangs off the post's middle-left.
     * That curl is 86 canvas units, inside what a child can hold in one pass, so unlike জ's, ঞ's
     * and ত's spirals it is not split.
     *
     * The curl therefore leaves the post at mid-height, where the letter's arm tapers into it,
     * arches up over the crest, comes down the left flank and sweeps along the bottom back to the
     * right. It stops 2 units short of the midpoint of the flat cut that ends it, as ছ's and ত's
     * cut ends do: the skeleton forks into a prong for each corner of the cut — here (38,66) and
     * (33,74) — and the midpoint between them is where the pen lifts.
     *
     * The post follows, from the headline down to the foot and stopping 3 units short of the end
     * cap, and the matra — the full headline, inset from each cap so the bar is a whole 13 dot
     * spacings — is written last, as Bengali writes it.
     */
    Exercise(
        id = "consonant-no",
        title = "ন",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 20,
        strokes = listOf(
            Stroke(
                id = "consonant-no-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(72f, 55f),
                        Point(69f, 54f),
                        Point(67f, 53f),
                        Point(64f, 52f),
                        Point(62f, 50f),
                        Point(60f, 48f),
                        Point(58f, 46f),
                        Point(55f, 45f),
                        Point(53f, 43f),
                        Point(51f, 42f),
                        Point(48f, 41f),
                        Point(46f, 40f),
                        Point(43f, 39f),
                        Point(40f, 39f),
                        Point(37f, 38f),
                        Point(34f, 39f),
                        Point(32f, 39f),
                        Point(29f, 40f),
                        Point(27f, 42f),
                        Point(25f, 44f),
                        Point(23f, 46f),
                        Point(22f, 49f),
                        Point(22f, 51f),
                        Point(22f, 54f),
                        Point(22f, 57f),
                        Point(23f, 60f),
                        Point(24f, 62f),
                        Point(26f, 64f),
                        Point(28f, 66f),
                        Point(30f, 68f),
                        Point(33f, 69f),
                        Point(34f, 69f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-no-post",
                points = StrokePoints.line(Point(72f, 12f), Point(72f, 87f)),
            ),
            Stroke(
                id = "consonant-no-matra",
                points = StrokePoints.line(Point(11f, 12f), Point(89f, 12f)),
            ),
        ),
    ),
    /**
     * প is read off the glyph the phone renders, as ন is. It is a shade wider than it is tall
     * (aspect 1.023), but height-fitting leaves its ink at x 7..92, comfortably inside the canvas
     * and the same size as every other consonant, where a width fit would stretch it to y 4..96 —
     * so it is fitted by height, like ন, ঢ, দ and ধ.
     *
     * Structurally it is গ, ণ, থ and ধ once more: a post rising above the headline, a matra bar
     * only to its right, and the body hanging off the post's left. Here that body is a triangular
     * sail. The hood leaves the post at mid-height, arches up over the top and comes down to a
     * sharp turn at the far left; the tongue comes back right out of that turn and down to the
     * sail's bottom corner; and the arm — the diagonal — hangs off the hood and runs down-left
     * past that corner to a flat cut.
     *
     * Which bands the pen runs through is the glyph's own answer, taken from the angles the
     * branches leave each junction at. Where the hood reaches the post, the hood and the link into
     * the post leave at 43 degrees and the arm at 75, so the hood flows into the post and the arm
     * is joined onto it; at the sail's bottom corner the arm and its tail leave at 34 degrees and
     * the tongue at 59, so the arm runs through and the tongue ends on it. That makes `sail` one
     * movement of 107 canvas units — post, hood, turn, tongue — inside what a child can hold in
     * one pass, and `arm` a second of 43.
     *
     * The far-left corner is a real turn rather than a terminal: the skeleton crosses it at full
     * pen thickness, and its two spurs, to (11,35) and (19,36), are the corner's outer mitre and
     * the ink's wedge into the counter — thin places the pen's centre never travels, as ধ's mitre
     * at (13,49) is. The mitre is why the guide turns nine units short of the letter's leftmost
     * ink: at a turn this sharp the outer corner runs out to twice the pen's half-width.
     *
     * `arm` ends 2 units short of the midpoint of the flat cut that closes it, as ছ's, ত's and ন's
     * cut ends do; the cut runs from (25,59.5) to (31.5,66). The post follows, inset 2 units from
     * each end cap as ণ's is, and the matra — the short bar right of the post — is last.
     */
    Exercise(
        id = "consonant-po",
        title = "প",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 21,
        strokes = listOf(
            Stroke(
                id = "consonant-po-sail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(74f, 39f),
                        Point(72f, 38f),
                        Point(69f, 36f),
                        Point(67f, 35f),
                        Point(65f, 34f),
                        Point(62f, 33f),
                        Point(61f, 30f),
                        Point(60f, 27f),
                        Point(58f, 26f),
                        Point(56f, 24f),
                        Point(54f, 22f),
                        Point(51f, 21f),
                        Point(49f, 20f),
                        Point(46f, 19f),
                        Point(43f, 19f),
                        Point(40f, 19f),
                        Point(37f, 19f),
                        Point(35f, 19f),
                        Point(32f, 20f),
                        Point(30f, 21f),
                        Point(27f, 22f),
                        Point(25f, 24f),
                        Point(23f, 26f),
                        Point(20f, 28f),
                        Point(18f, 30f),
                        Point(16f, 32f),
                        Point(16f, 34f),
                        Point(16f, 37f),
                        Point(18f, 39f),
                        Point(21f, 39f),
                        Point(24f, 38f),
                        Point(27f, 38f),
                        Point(30f, 39f),
                        Point(32f, 40f),
                        Point(34f, 42f),
                        Point(35f, 45f),
                        Point(35f, 48f),
                        Point(35f, 51f),
                        Point(36f, 53f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-po-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(62f, 33f),
                        Point(60f, 34f),
                        Point(58f, 36f),
                        Point(56f, 38f),
                        Point(53f, 40f),
                        Point(51f, 42f),
                        Point(49f, 44f),
                        Point(47f, 46f),
                        Point(45f, 48f),
                        Point(43f, 50f),
                        Point(40f, 52f),
                        Point(38f, 52f),
                        Point(36f, 54f),
                        Point(34f, 56f),
                        Point(32f, 58f),
                        Point(30f, 61f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-po-post",
                points = StrokePoints.line(Point(75f, 9f), Point(75f, 88f)),
            ),
            Stroke(
                id = "consonant-po-matra",
                points = StrokePoints.line(Point(75f, 19f), Point(90f, 19f)),
            ),
        ),
    ),
    /**
     * ফ is read off the glyph the phone renders, like ন and প. It is wider than it is tall
     * (aspect 1.379), so its ink is fitted by width with y centred, the way জ, ঝ, ড and ত are;
     * height-fitting it would push the headline past the edge of the canvas.
     *
     * It is a full headline, a post hanging free below it, a bowl off the post's top, and a
     * zigzag sail on the left. Like ত, the right half does not touch the bar: the post's top is a
     * flat cut five canvas units clear of it.
     *
     * The sail is one movement of 107 canvas units, inside what a child can hold in one pass —
     * down the short stem from the matra, down-right to the first corner, back down-left to the
     * second, then the long diagonal down-right into the post's foot. Each of its three sharp
     * corners grows a skeleton spur into the ink's outer mitre, to (10,29), (40,42) and (10,51);
     * those thin to 0.2-1.4 canvas units against the pen's 3.93, so the pen's centre turns at the
     * junction and never travels out along the spur, as ধ's and প's corners do.
     *
     * The bowl is joined onto the post rather than continuous with it, which is what the angles at
     * the post's top say: the two halves of the post leave at 34 degrees and the bowl at 57, so
     * the post runs straight through and the bowl starts on it, the way ণ's loop starts on its own
     * post. The bowl ends 2 units short of the midpoint of the vertical flat cut that closes it —
     * the cut runs from (69,58) to (69,66.5) and grows a skeleton prong into each corner, as ছ's,
     * ত's and ন's cut ends do.
     *
     * The sail and the bowl come first, then the post from its top cut to its foot, then the
     * matra, inset 2 units from each end cap so the bar is a whole 15 dot spacings.
     */
    Exercise(
        id = "consonant-pho",
        title = "ফ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 22,
        strokes = listOf(
            Stroke(
                id = "consonant-pho-sail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(16f, 20f),
                        Point(16f, 23f),
                        Point(16f, 26f),
                        Point(18f, 27f),
                        Point(21f, 28f),
                        Point(23f, 29f),
                        Point(26f, 31f),
                        Point(28f, 32f),
                        Point(31f, 33f),
                        Point(33f, 35f),
                        Point(35f, 37f),
                        Point(36f, 40f),
                        Point(34f, 42f),
                        Point(32f, 43f),
                        Point(29f, 44f),
                        Point(27f, 46f),
                        Point(24f, 47f),
                        Point(22f, 48f),
                        Point(19f, 49f),
                        Point(17f, 51f),
                        Point(16f, 53f),
                        Point(16f, 56f),
                        Point(18f, 58f),
                        Point(21f, 58f),
                        Point(23f, 59f),
                        Point(26f, 60f),
                        Point(28f, 61f),
                        Point(31f, 62f),
                        Point(34f, 63f),
                        Point(36f, 64f),
                        Point(38f, 66f),
                        Point(41f, 67f),
                        Point(43f, 69f),
                        Point(45f, 71f),
                        Point(47f, 72f),
                        Point(49f, 75f),
                        Point(52f, 76f),
                        Point(54f, 77f),
                        Point(57f, 78f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-pho-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(57f, 33f),
                        Point(60f, 33f),
                        Point(63f, 33f),
                        Point(66f, 33f),
                        Point(68f, 34f),
                        Point(71f, 35f),
                        Point(74f, 36f),
                        Point(76f, 37f),
                        Point(78f, 38f),
                        Point(81f, 40f),
                        Point(83f, 42f),
                        Point(84f, 44f),
                        Point(85f, 47f),
                        Point(86f, 50f),
                        Point(86f, 52f),
                        Point(86f, 55f),
                        Point(85f, 58f),
                        Point(83f, 60f),
                        Point(81f, 61f),
                        Point(78f, 62f),
                        Point(75f, 62f),
                        Point(72f, 62f),
                        Point(71f, 62f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-pho-post",
                points = StrokePoints.line(Point(57f, 30f), Point(57f, 84f)),
            ),
            Stroke(
                id = "consonant-pho-matra",
                points = StrokePoints.line(Point(5f, 20f), Point(95f, 20f)),
            ),
        ),
    ),
    /**
     * ব is read off the glyph the phone renders, like ন, প and ফ. It comes out all but square
     * (aspect 0.989), so its ink is fitted by height with x centred, the way ন, ঢ, দ and ধ are.
     *
     * It is ধ's body without the loop: a full headline, a post from the headline down to the foot,
     * and one wide "<" hanging off the post's left whose two ends both run back into the post. The
     * arm leaves the post at (72,26), where it tapers in, and runs down-left to the corner at
     * (23,48); the sweep carries on from there down-right into the post's foot at (72,81). That is
     * 119 canvas units in one movement, inside what a child can hold in one pass, as ধ's 116-unit
     * body is, and the corner is the one landmark in it a child can see.
     *
     * The corner is the junction where the two centrelines cross. The ink runs on up-left to a tip
     * and the skeleton grows a spur into it, but that is the corner's outer mitre — it thins from
     * 4.94 to 0.74 canvas units against the pen's 4.93 — so the pen's centre turns at the junction
     * and never travels out along the spur, exactly as ধ's and প's corners do.
     *
     * The post follows, from the headline to 3 units short of its end cap, and the matra — the full
     * headline, inset 2 units from each cap so the bar is a whole 13 dot spacings — is last.
     */
    Exercise(
        id = "consonant-bo",
        title = "ব",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 23,
        strokes = listOf(
            Stroke(
                id = "consonant-bo-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(72f, 26f),
                        Point(69f, 27f),
                        Point(66f, 27f),
                        Point(63f, 27f),
                        Point(61f, 28f),
                        Point(58f, 29f),
                        Point(56f, 30f),
                        Point(53f, 31f),
                        Point(51f, 32f),
                        Point(48f, 33f),
                        Point(46f, 34f),
                        Point(43f, 35f),
                        Point(41f, 37f),
                        Point(38f, 38f),
                        Point(36f, 39f),
                        Point(33f, 40f),
                        Point(31f, 42f),
                        Point(29f, 43f),
                        Point(26f, 44f),
                        Point(24f, 46f),
                        Point(23f, 48f),
                        Point(24f, 51f),
                        Point(24f, 54f),
                        Point(27f, 54f),
                        Point(30f, 55f),
                        Point(32f, 56f),
                        Point(35f, 57f),
                        Point(38f, 58f),
                        Point(40f, 59f),
                        Point(42f, 60f),
                        Point(45f, 61f),
                        Point(47f, 63f),
                        Point(50f, 64f),
                        Point(52f, 66f),
                        Point(54f, 68f),
                        Point(56f, 69f),
                        Point(58f, 71f),
                        Point(60f, 73f),
                        Point(62f, 76f),
                        Point(64f, 78f),
                        Point(66f, 79f),
                        Point(69f, 80f),
                        Point(71f, 81f),
                        Point(72f, 81f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-bo-post",
                points = StrokePoints.line(Point(72f, 12f), Point(72f, 87f)),
            ),
            Stroke(
                id = "consonant-bo-matra",
                points = StrokePoints.line(Point(11f, 12f), Point(89f, 12f)),
            ),
        ),
    ),
    /**
     * ভ is read off the glyph the phone renders, like ন, প, ফ and ব. It is wider than it is tall
     * (aspect 1.280), so its ink is fitted by width with y centred, the way ত, জ, ঝ, ড and ফ are.
     *
     * Like ত and ফ, the headline stands clear of the rest of the letter. Below it ভ is a single
     * spiral of 191 canvas units: in from the flat cut at the top left, down the long left flank,
     * round the foot, up the right flank, round the sharp corner at the top right, then back
     * down-left into the inner bowl and up to the flat cut at the top middle.
     *
     * That is far past one pass, so it is split twice, both times at a landmark the child can see.
     * The outer arc alone is 139 canvas units — at the ceiling — so it breaks at the foot, the
     * lowest point of the bowl where the letter sits on the writing line, the split ত and ঢ take.
     * The second break is the corner at the top right, where the outer arc and the inner hook leave
     * at 66 degrees and the pen turns back on itself, the split জ, ঞ and ড take. The skeleton's
     * spur there runs up-right into the corner's outer mitre and thins from 5.01 to 0.56 canvas
     * units against the pen's 4.49, so the pen's centre turns at the junction and never travels
     * out along it.
     *
     * Both free ends are flat cuts with a skeleton prong into each corner, as ছ's, ত's, ন's and
     * ফ's are. `arm` starts 2 units short of the midpoint between its prongs; `hook` ends 3 units
     * short of its own, which is what leaves the least bare path past the stroke's last dot — it
     * is the only one of the three whose end is a free tip, the other two handing straight over to
     * the next stroke. The matra, inset 2 units from each end cap for a whole 15 dot spacings, is
     * written last.
     */
    Exercise(
        id = "consonant-bho",
        title = "ভ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 24,
        strokes = listOf(
            Stroke(
                id = "consonant-bho-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(13f, 34f),
                        Point(14f, 37f),
                        Point(15f, 39f),
                        Point(16f, 42f),
                        Point(17f, 45f),
                        Point(18f, 47f),
                        Point(19f, 50f),
                        Point(20f, 52f),
                        Point(21f, 55f),
                        Point(22f, 57f),
                        Point(23f, 60f),
                        Point(25f, 62f),
                        Point(26f, 64f),
                        Point(28f, 66f),
                        Point(29f, 69f),
                        Point(31f, 71f),
                        Point(33f, 73f),
                        Point(36f, 75f),
                        Point(38f, 76f),
                        Point(40f, 78f),
                        Point(43f, 79f),
                        Point(45f, 80f),
                        Point(48f, 81f),
                        Point(51f, 81f),
                        Point(54f, 82f),
                        Point(56f, 82f),
                        Point(57f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-bho-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(57f, 82f),
                        Point(60f, 82f),
                        Point(63f, 82f),
                        Point(66f, 81f),
                        Point(68f, 80f),
                        Point(71f, 79f),
                        Point(73f, 78f),
                        Point(76f, 76f),
                        Point(78f, 74f),
                        Point(80f, 72f),
                        Point(81f, 70f),
                        Point(82f, 67f),
                        Point(83f, 65f),
                        Point(83f, 62f),
                        Point(84f, 59f),
                        Point(83f, 56f),
                        Point(83f, 53f),
                        Point(82f, 50f),
                        Point(82f, 48f),
                        Point(81f, 45f),
                        Point(80f, 43f),
                        Point(78f, 40f),
                        Point(76f, 39f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-bho-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(75f, 39f),
                        Point(73f, 40f),
                        Point(71f, 42f),
                        Point(69f, 44f),
                        Point(67f, 46f),
                        Point(65f, 48f),
                        Point(62f, 49f),
                        Point(60f, 50f),
                        Point(57f, 51f),
                        Point(54f, 51f),
                        Point(52f, 51f),
                        Point(49f, 51f),
                        Point(46f, 50f),
                        Point(44f, 48f),
                        Point(42f, 46f),
                        Point(42f, 43f),
                        Point(42f, 41f),
                        Point(42f, 38f),
                        Point(43f, 35f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-bho-matra",
                points = StrokePoints.line(Point(5f, 18f), Point(95f, 18f)),
            ),
        ),
    ),
    /**
     * ম is read off the glyph the phone renders, like ন, প, ফ, ব and ভ. It is a shade wider than
     * it is tall (aspect 1.032); as with প, height-fitting leaves its ink at x 7..93, comfortably
     * inside the canvas and the same size as every other consonant, where a width fit would
     * stretch it to y 4.5..95.5 — so it is fitted by height, like ন, ব, ঢ, দ and ধ.
     *
     * It is a full headline, a post from the headline to the foot, and a body of two bands meeting
     * at a junction under the middle of the letter, at (43,47): an arc that hangs from the matra at
     * x=20 and curves down-right into that junction, and a long band that runs from the post at
     * (73,67) up-left through it, round the big left bowl and back down to a flat cut at the
     * bottom.
     *
     * Which two of the three bands the pen runs through is measured over a 6-unit baseline rather
     * than the skeleton's last few pixels, which are noisy at a junction — read from three points
     * the arm angles come out 51 and 59 degrees apart and say almost nothing. Over 6 units the
     * bowl and the sweep into the post turn only 27 degrees into each other, against 65 for
     * arc-to-sweep and 87 for arc-to-bowl, so the bowl and the sweep are one movement of 90 canvas
     * units and the arc, 48 units, is joined onto it — the same reading ধ's and প's junctions got.
     *
     * Where the arc leaves the matra is a turn, not a terminal: the skeleton's spur there runs
     * down-left to (13.5,24), into the ink's outer mitre, and thins from 5.25 to 0.15 canvas units
     * against the pen's 4.93, as ফ's (10,29) mitre does, so the guide turns at the junction. The
     * bowl ends 2 units short of the midpoint of the flat cut that closes it, whose corners carry
     * a skeleton prong each, (37.5,85) and (40,75.6).
     *
     * The arc is written first, hanging from the headline, then the bowl, then the post, and the
     * matra — inset from each end cap — last.
     */
    Exercise(
        id = "consonant-mo",
        title = "ম",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 25,
        strokes = listOf(
            Stroke(
                id = "consonant-mo-arc",
                points = StrokePoints.polyline(
                    listOf(
                        Point(20f, 12f),
                        Point(20f, 15f),
                        Point(20f, 18f),
                        Point(20f, 20f),
                        Point(23f, 21f),
                        Point(26f, 22f),
                        Point(29f, 22f),
                        Point(31f, 24f),
                        Point(33f, 25f),
                        Point(36f, 26f),
                        Point(38f, 29f),
                        Point(39f, 31f),
                        Point(40f, 33f),
                        Point(41f, 36f),
                        Point(42f, 39f),
                        Point(42f, 41f),
                        Point(43f, 44f),
                        Point(43f, 47f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-mo-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(74f, 67f),
                        Point(71f, 66f),
                        Point(69f, 65f),
                        Point(66f, 64f),
                        Point(64f, 62f),
                        Point(62f, 60f),
                        Point(61f, 58f),
                        Point(59f, 56f),
                        Point(56f, 54f),
                        Point(54f, 52f),
                        Point(52f, 51f),
                        Point(49f, 50f),
                        Point(46f, 49f),
                        Point(44f, 48f),
                        Point(41f, 48f),
                        Point(38f, 48f),
                        Point(35f, 48f),
                        Point(33f, 49f),
                        Point(30f, 50f),
                        Point(28f, 51f),
                        Point(26f, 53f),
                        Point(24f, 56f),
                        Point(24f, 58f),
                        Point(23f, 61f),
                        Point(23f, 64f),
                        Point(24f, 67f),
                        Point(24f, 69f),
                        Point(26f, 72f),
                        Point(28f, 74f),
                        Point(30f, 76f),
                        Point(32f, 77f),
                        Point(35f, 78f),
                        Point(37f, 80f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-mo-post",
                points = StrokePoints.line(Point(74f, 12f), Point(74f, 87f)),
            ),
            Stroke(
                id = "consonant-mo-matra",
                points = StrokePoints.line(Point(10f, 12f), Point(90f, 12f)),
            ),
        ),
    ),
    /**
     * য — অন্তঃস্থ য — is read off the glyph the phone renders, like ন, প, ফ, ব, ভ and ম. জ already
     * holds `consonant-jo`, so this one is `consonant-yo`. It is a shade wider than it is tall
     * (aspect 1.038), and as with প and ম height-fitting leaves its ink at x 7..93, inside the
     * canvas and the same size as every other consonant, where a width fit would stretch it past
     * y 4..96 — so it is fitted by height, like ন, ব, ম, ঢ, দ and ধ.
     *
     * It is ফ's zigzag sail under a full headline, with a post hanging from the bar rather than ফ's
     * free one, and no bowl: down a short stem from the matra, down-right to the first corner, back
     * down-left to the second, then a long diagonal down-right into the post's foot.
     *
     * All of that is 139 canvas units in one movement — at the ceiling, where ভ's outer arc was —
     * so it is broken at the second corner, (22.5,52.3). That is both a landmark the child can see
     * and the point where the zigzag stops and the long sweep to the post begins; it leaves `sail`
     * 69 units and `tail` 64. ফ keeps the same shape in one stroke because its own sail is only
     * 107 units long.
     *
     * Each of the three corners grows a skeleton spur into the ink's outer mitre — to (15.3,22.7),
     * (52.1,38.5) and (15.1,49.6), thinning to 0.33-1.63 canvas units against the pen's 4.85 — so
     * the pen's centre turns at the junction and never travels out along the spur, as ফ's, ধ's and
     * প's corners do.
     *
     * The post follows, from the headline to 3 units short of its end cap, and the matra is last.
     */
    Exercise(
        id = "consonant-yo",
        title = "য",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 26,
        strokes = listOf(
            Stroke(
                id = "consonant-yo-sail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(23f, 12f),
                        Point(23f, 15f),
                        Point(23f, 18f),
                        Point(24f, 20f),
                        Point(27f, 21f),
                        Point(29f, 22f),
                        Point(32f, 23f),
                        Point(34f, 25f),
                        Point(37f, 26f),
                        Point(39f, 27f),
                        Point(41f, 29f),
                        Point(44f, 30f),
                        Point(46f, 32f),
                        Point(46f, 35f),
                        Point(46f, 37f),
                        Point(44f, 39f),
                        Point(41f, 40f),
                        Point(39f, 41f),
                        Point(36f, 43f),
                        Point(34f, 44f),
                        Point(31f, 45f),
                        Point(29f, 47f),
                        Point(27f, 48f),
                        Point(24f, 49f),
                        Point(23f, 52f),
                        Point(22f, 52f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-yo-tail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(22f, 52f),
                        Point(23f, 55f),
                        Point(24f, 58f),
                        Point(27f, 58f),
                        Point(29f, 59f),
                        Point(32f, 59f),
                        Point(35f, 60f),
                        Point(37f, 61f),
                        Point(40f, 62f),
                        Point(43f, 63f),
                        Point(45f, 64f),
                        Point(48f, 65f),
                        Point(50f, 67f),
                        Point(52f, 68f),
                        Point(55f, 70f),
                        Point(57f, 71f),
                        Point(59f, 73f),
                        Point(61f, 75f),
                        Point(63f, 77f),
                        Point(65f, 79f),
                        Point(68f, 80f),
                        Point(70f, 81f),
                        Point(73f, 82f),
                        Point(74f, 83f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-yo-post",
                points = StrokePoints.line(Point(74f, 12f), Point(74f, 87f)),
            ),
            Stroke(
                id = "consonant-yo-matra",
                points = StrokePoints.line(Point(10f, 12f), Point(90f, 12f)),
            ),
        ),
    ),
)
