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
)
