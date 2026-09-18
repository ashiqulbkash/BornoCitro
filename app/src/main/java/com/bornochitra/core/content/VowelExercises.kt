package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Vowel stroke geometry. অ (plan.md Step 10.8), আ (Step 11.1), ই (Step 11.2), ঈ (Step 11.3), উ
 * (Step 11.4), ঊ (Step 11.5), ঋ (Step 11.6), এ (Step 11.7) and ঐ (Step 11.8) are read off the
 * rendered glyph's centreline; the letters after them still carry placeholder paths that approximate
 * the letter's silhouette, and are corrected one at a time by plan.md Steps 11.9-11.10.
 */
internal val vowelExercises: List<Exercise> = listOf(
    /**
     * অ is traced from the rendered glyph's centreline rather than approximated, so the guide sits
     * on the letter shown above it. Written the way the letter is hand-drawn: the bowl first, then
     * the stem, then the matra — Bengali draws the headline last.
     */
    Exercise(
        id = "vowel-o",
        title = "অ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 1,
        strokes = listOf(
            Stroke(
                id = "vowel-o-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(10f, 40f),
                        Point(14f, 44f),
                        Point(16f, 50f),
                        Point(21f, 57f),
                        Point(27f, 62f),
                        Point(33f, 66f),
                        Point(41f, 68f),
                        Point(47f, 67f),
                        Point(53f, 65f),
                        Point(57f, 60f),
                        Point(60f, 55f),
                        Point(60f, 48f),
                        Point(58f, 43f),
                        Point(55f, 40f),
                        Point(50f, 38f),
                        Point(44f, 38f),
                        Point(40f, 39f),
                        Point(37f, 43f),
                        Point(36f, 46f),
                        Point(39f, 50f),
                        Point(42f, 51f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-o-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(81f, 29f),
                        Point(81f, 43f),
                        Point(81f, 57f),
                        Point(81f, 73f),
                        Point(74f, 70f),
                        Point(67f, 65f),
                        Point(60f, 62f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-o-matra",
                points = StrokePoints.line(Point(8f, 27f), Point(92f, 27f)),
            ),
        ),
    ),
    /**
     * আ is অ plus the আ-কার, so it is written the same way and then given its own vertical: the
     * bowl, অ's stem with the tail that joins it, the আ-কার, then the matra last. Geometry is read
     * off the rendered glyph's centreline, like অ's.
     */
    Exercise(
        id = "vowel-aa",
        title = "আ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "vowel-aa-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(10f, 35f),
                        Point(13f, 46f),
                        Point(18f, 56f),
                        Point(25f, 64f),
                        Point(32f, 69f),
                        Point(39f, 71f),
                        Point(43f, 71f),
                        Point(47f, 71f),
                        Point(52f, 68f),
                        Point(56f, 63f),
                        Point(58f, 62f),
                        Point(59f, 53f),
                        Point(58f, 46f),
                        Point(55f, 39f),
                        Point(51f, 36f),
                        Point(48f, 34f),
                        Point(41f, 33f),
                        Point(38f, 36f),
                        Point(35f, 40f),
                        Point(35f, 43f),
                        Point(35f, 47f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-aa-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(72f, 24f),
                        Point(72f, 76f),
                        Point(67f, 74f),
                        Point(60f, 64f),
                        Point(58f, 62f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-aa-kar",
                points = StrokePoints.line(Point(88f, 24f), Point(88f, 75f)),
            ),
            Stroke(
                id = "vowel-aa-matra",
                points = StrokePoints.line(Point(6f, 24f), Point(94f, 24f)),
            ),
        ),
    ),
    /**
     * ই is the top hook, then the body below the matra — a small curl that opens into the bowl and
     * runs out into the long diagonal tail — and the matra last. Geometry is read off the rendered
     * glyph's centreline. Unlike অ and আ the letter is taller than it is wide, so it is fitted by
     * height and centred.
     */
    Exercise(
        id = "vowel-i",
        title = "ই",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.BEGINNER,
        order = 3,
        strokes = listOf(
            Stroke(
                id = "vowel-i-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(23f, 6f),
                        Point(24f, 12f),
                        Point(29f, 16f),
                        Point(35f, 17f),
                        Point(55f, 18f),
                        Point(61f, 21f),
                        Point(65f, 25f),
                        Point(66f, 33f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-i-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(41f, 52f),
                        Point(43f, 46f),
                        Point(49f, 41f),
                        Point(59f, 42f),
                        Point(65f, 45f),
                        Point(67f, 51f),
                        Point(67f, 56f),
                        Point(66f, 61f),
                        Point(62f, 65f),
                        Point(57f, 68f),
                        Point(52f, 69f),
                        Point(48f, 70f),
                        Point(38f, 69f),
                        Point(49f, 77f),
                        Point(77f, 94f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-i-matra",
                points = StrokePoints.line(Point(35f, 34f), Point(72f, 34f)),
            ),
        ),
    ),
    /**
     * ঈ shares ই's hook, bowl and bottom sweep, so it is written the same way up to that point; what
     * makes it ঈ is the tail, which instead of running out to the bottom right rises back over the
     * bowl, hooks at its apex and drops into a long descender. Geometry is read off the centreline
     * of the glyph as Noto Sans Bengali renders it — the font Android draws the character with above
     * the tracing canvas. Like ই the letter is taller than it is wide, so it is fitted by height and
     * centred.
     */
    Exercise(
        id = "vowel-ii",
        title = "ঈ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "vowel-ii-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26f, 6f),
                        Point(25f, 10f),
                        Point(26f, 15f),
                        Point(29f, 20f),
                        Point(35f, 22f),
                        Point(43f, 23f),
                        Point(51f, 23f),
                        Point(58f, 23f),
                        Point(63f, 25f),
                        Point(66f, 29f),
                        Point(66f, 37f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ii-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(28f, 61f),
                        Point(27f, 57f),
                        Point(27f, 54f),
                        Point(29f, 51f),
                        Point(32f, 49f),
                        Point(36f, 49f),
                        Point(41f, 49f),
                        Point(45f, 51f),
                        Point(48f, 55f),
                        Point(50f, 59f),
                        Point(50f, 62f),
                        Point(51f, 64f),
                        Point(50f, 67f),
                        Point(48f, 71f),
                        Point(45f, 76f),
                        Point(42f, 79f),
                        Point(38f, 80f),
                        Point(34f, 80f),
                        Point(31f, 78f),
                        Point(28f, 74f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ii-tail",
                points = StrokePoints.polyline(
                    listOf(
                        Point(51f, 64f),
                        Point(55f, 62f),
                        Point(58f, 60f),
                        Point(61f, 57f),
                        Point(64f, 55f),
                        Point(67f, 53f),
                        Point(70f, 53f),
                        Point(70f, 58f),
                        Point(69f, 63f),
                        Point(68f, 68f),
                        Point(68f, 73f),
                        Point(68f, 79f),
                        Point(69f, 84f),
                        Point(70f, 89f),
                        Point(71f, 92f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ii-matra",
                points = StrokePoints.line(Point(19f, 37f), Point(81f, 37f)),
            ),
        ),
    ),
    /**
     * উ hangs two pieces off the matra: the stem, which drops and curls right into the u-kar hook,
     * and the big bowl, which sweeps from the upper left around the bottom and back up the right.
     * Both finish at the same blunt tip above the hook, the way the glyph butts them together.
     * Geometry is read off the centreline of the glyph as Noto Sans Bengali renders it, the font
     * Android draws the character with above the tracing canvas.
     */
    Exercise(
        id = "vowel-u",
        title = "উ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "vowel-u-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26f, 7f),
                        Point(26f, 11f),
                        Point(27f, 16f),
                        Point(29f, 20f),
                        Point(33f, 22f),
                        Point(38f, 23f),
                        Point(45f, 23f),
                        Point(53f, 24f),
                        Point(61f, 24f),
                        Point(65f, 26f),
                        Point(68f, 30f),
                        Point(68f, 38f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-u-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(46f, 39f),
                        Point(46f, 45f),
                        Point(46f, 51f),
                        Point(46f, 57f),
                        Point(46f, 61f),
                        Point(47f, 64f),
                        Point(49f, 67f),
                        Point(53f, 68f),
                        Point(57f, 68f),
                        Point(60f, 66f),
                        Point(64f, 63f),
                        Point(67f, 60f),
                        Point(70f, 56f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-u-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(21f, 50f),
                        Point(23f, 55f),
                        Point(25f, 61f),
                        Point(27f, 67f),
                        Point(30f, 73f),
                        Point(34f, 79f),
                        Point(40f, 85f),
                        Point(46f, 89f),
                        Point(53f, 90f),
                        Point(59f, 90f),
                        Point(66f, 88f),
                        Point(72f, 84f),
                        Point(75f, 77f),
                        Point(76f, 71f),
                        Point(75f, 64f),
                        Point(72f, 59f),
                        Point(70f, 56f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-u-matra",
                points = StrokePoints.line(Point(17f, 38f), Point(83f, 38f)),
            ),
        ),
    ),
    /**
     * ঊ is built the way উ is — the same top hook, the same stem curling right into the u-kar, and
     * the same bowl sweeping round the bottom to the blunt tip the stem also ends on. What makes it
     * ঊ is the second arm hanging inside the bowl: it starts below the matra like the bowl's own arm
     * does, runs down parallel to it and joins the bowl where the bottom sweep begins. Geometry is
     * read off the centreline of the glyph as Noto Sans Bengali renders it, the font Android draws
     * the character with above the tracing canvas.
     */
    Exercise(
        id = "vowel-uu",
        title = "ঊ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "vowel-uu-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(26f, 7f),
                        Point(28f, 11f),
                        Point(29f, 16f),
                        Point(32f, 20f),
                        Point(36f, 22f),
                        Point(41f, 23f),
                        Point(49f, 24f),
                        Point(57f, 24f),
                        Point(65f, 24f),
                        Point(71f, 26f),
                        Point(74f, 30f),
                        Point(75f, 38f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-uu-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(51f, 39f),
                        Point(51f, 45f),
                        Point(51f, 51f),
                        Point(51f, 57f),
                        Point(52f, 62f),
                        Point(53f, 66f),
                        Point(56f, 68f),
                        Point(59f, 68f),
                        Point(63f, 67f),
                        Point(66f, 66f),
                        Point(69f, 63f),
                        Point(72f, 60f),
                        Point(75f, 58f),
                        Point(77f, 54f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-uu-bowl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(17f, 53f),
                        Point(19f, 58f),
                        Point(21f, 63f),
                        Point(24f, 68f),
                        Point(27f, 73f),
                        Point(30f, 78f),
                        Point(34f, 82f),
                        Point(38f, 85f),
                        Point(43f, 87f),
                        Point(47f, 87f),
                        Point(51f, 89f),
                        Point(56f, 90f),
                        Point(61f, 90f),
                        Point(66f, 90f),
                        Point(71f, 88f),
                        Point(75f, 86f),
                        Point(78f, 82f),
                        Point(80f, 77f),
                        Point(80f, 71f),
                        Point(79f, 65f),
                        Point(76f, 59f),
                        Point(77f, 54f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-uu-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(31f, 52f),
                        Point(32f, 56f),
                        Point(33f, 60f),
                        Point(34f, 63f),
                        Point(36f, 66f),
                        Point(37f, 69f),
                        Point(38f, 72f),
                        Point(40f, 76f),
                        Point(42f, 79f),
                        Point(45f, 82f),
                        Point(46f, 85f),
                        Point(47f, 87f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-uu-matra",
                points = StrokePoints.line(Point(13f, 38f), Point(87f, 38f)),
            ),
        ),
    ),
    /**
     * ঋ is the first vowel with no matra — nothing runs along the top to tie its parts together, so
     * every piece is derived on its own. The left knot is a single movement in handwriting: it
     * starts inside the round terminal at the top left, sweeps right and up to the peak, drops to
     * the vertex where it meets the stem, runs back down-left to the sharp point, then sweeps
     * down-right to the stem's foot. It is written here as three strokes, split at the knot's two
     * reversals, so a child can finish each one in a single finger pass. The stem follows, then the
     * hook that leaves it and the arm the hook lands on. Geometry is read off the centreline of the
     * glyph as Noto Sans Bengali renders it, the font Android draws the character with above the
     * tracing canvas.
     */
    Exercise(
        id = "vowel-ri",
        title = "ঋ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.ADVANCED,
        order = 7,
        strokes = listOf(
            Stroke(
                id = "vowel-ri-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(16f, 25f),
                        Point(17f, 27f),
                        Point(19f, 30f),
                        Point(21f, 31f),
                        Point(23f, 31f),
                        Point(26f, 30f),
                        Point(28f, 30f),
                        Point(31f, 29f),
                        Point(34f, 28f),
                        Point(36f, 26f),
                        Point(39f, 24f),
                        Point(41f, 22f),
                        Point(44f, 20f),
                        Point(46f, 16f),
                        Point(48f, 22f),
                        Point(50f, 23f),
                        Point(50f, 25f),
                        Point(51f, 26f),
                        Point(52f, 27f),
                        Point(53f, 29f),
                        Point(53f, 30f),
                        Point(54f, 31f),
                        Point(54f, 33f),
                        Point(55f, 34f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-diagonal",
                points = StrokePoints.polyline(
                    listOf(
                        Point(55f, 34f),
                        Point(52f, 36f),
                        Point(50f, 37f),
                        Point(46f, 39f),
                        Point(44f, 40f),
                        Point(41f, 41f),
                        Point(38f, 43f),
                        Point(35f, 44f),
                        Point(32f, 46f),
                        Point(29f, 48f),
                        Point(26f, 49f),
                        Point(23f, 51f),
                        Point(21f, 53f),
                        Point(18f, 52f),
                        Point(15f, 52f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-sweep",
                points = StrokePoints.polyline(
                    listOf(
                        Point(15f, 52f),
                        Point(18f, 54f),
                        Point(20f, 56f),
                        Point(21f, 58f),
                        Point(24f, 60f),
                        Point(28f, 61f),
                        Point(32f, 62f),
                        Point(36f, 64f),
                        Point(40f, 66f),
                        Point(44f, 68f),
                        Point(47f, 71f),
                        Point(51f, 74f),
                        Point(55f, 78f),
                        Point(59f, 80f),
                        Point(63f, 82f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(62f, 8f),
                        Point(62f, 14f),
                        Point(63f, 20f),
                        Point(63f, 30f),
                        Point(63f, 40f),
                        Point(63f, 50f),
                        Point(64f, 58f),
                        Point(64f, 66f),
                        Point(64f, 74f),
                        Point(63f, 82f),
                        Point(63f, 88f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-hook",
                points = StrokePoints.polyline(
                    listOf(
                        Point(64f, 58f),
                        Point(66f, 59f),
                        Point(68f, 59f),
                        Point(70f, 60f),
                        Point(72f, 61f),
                        Point(74f, 63f),
                        Point(76f, 65f),
                        Point(78f, 67f),
                        Point(79f, 69f),
                        Point(81f, 71f),
                        Point(83f, 72f),
                        Point(85f, 72f),
                        Point(87f, 74f),
                        Point(88f, 75f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-arm",
                points = StrokePoints.polyline(
                    listOf(
                        Point(86f, 8f),
                        Point(87f, 16f),
                        Point(88f, 30f),
                        Point(88f, 45f),
                        Point(88f, 60f),
                        Point(88f, 72f),
                        Point(88f, 80f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * এ has no matra either — what closes its top is the arch of the curl, not a headline. The
     * letter is written from the inside out: the pen starts in the round terminal at the letter's
     * centre, spirals up and to the left, arches over the top and turns down into the right-hand
     * stem. That turn is where the guide splits, so a child can finish each stroke in a single
     * finger pass. The base is written last, from its cut terminal on the left, down and along the
     * bottom and up again to the stem's foot. Geometry is read off the centreline of the glyph as
     * Noto Sans Bengali renders it, the font Android draws the character with above the tracing
     * canvas.
     */
    Exercise(
        id = "vowel-e",
        title = "এ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 8,
        strokes = listOf(
            Stroke(
                id = "vowel-e-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(54f, 45f),
                        Point(49f, 43f),
                        Point(46f, 42f),
                        Point(44f, 38f),
                        Point(45f, 35f),
                        Point(45f, 31f),
                        Point(47f, 28f),
                        Point(49f, 24f),
                        Point(52f, 20f),
                        Point(55f, 17f),
                        Point(59f, 14f),
                        Point(62f, 13f),
                        Point(66f, 12f),
                        Point(70f, 12f),
                        Point(73f, 12f),
                        Point(77f, 13f),
                        Point(80f, 16f),
                        Point(83f, 19f),
                        Point(83f, 23f),
                        Point(84f, 26f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-e-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(84f, 26f),
                        Point(84f, 34f),
                        Point(84f, 42f),
                        Point(84f, 50f),
                        Point(84f, 58f),
                        Point(84f, 66f),
                        Point(84f, 74f),
                        Point(84f, 81f),
                        Point(84f, 89f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-e-base",
                points = StrokePoints.polyline(
                    listOf(
                        Point(17f, 42f),
                        Point(16f, 49f),
                        Point(16f, 54f),
                        Point(16f, 59f),
                        Point(17f, 61f),
                        Point(18f, 64f),
                        Point(20f, 68f),
                        Point(22f, 71f),
                        Point(25f, 73f),
                        Point(28f, 74f),
                        Point(32f, 75f),
                        Point(37f, 76f),
                        Point(42f, 76f),
                        Point(47f, 75f),
                        Point(52f, 75f),
                        Point(57f, 74f),
                        Point(62f, 74f),
                        Point(67f, 74f),
                        Point(70f, 75f),
                        Point(72f, 76f),
                        Point(75f, 77f),
                        Point(77f, 79f),
                        Point(80f, 80f),
                        Point(84f, 82f),
                    ),
                ),
            ),
        ),
    ),
    /**
     * ঐ is এ with the ঐ-কার above it, so the body is written the way এ's is — the curl out of the
     * round terminal, the stem, then the base — and the কার is added last, the way a mark is. It is
     * not এ redrawn smaller, though: the কার takes the top of the square, so the body sits lower and
     * tighter, and every point here is read off ঐ's own glyph. The কার starts at its thin top
     * terminal, drops, runs right and curls back down to land on the head of the stem, where the
     * curl ends too. Like ই and ঈ the letter is taller than it is wide, so it is fitted by height
     * and centred. Geometry is read off the centreline of the glyph as Noto Sans Bengali renders it,
     * the font Android draws the character with above the tracing canvas.
     */
    Exercise(
        id = "vowel-oi",
        title = "ঐ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 9,
        strokes = listOf(
            Stroke(
                id = "vowel-oi-curl",
                points = StrokePoints.polyline(
                    listOf(
                        Point(45f, 59f),
                        Point(44f, 58f),
                        Point(42f, 58f),
                        Point(40f, 57f),
                        Point(39f, 55f),
                        Point(39f, 52f),
                        Point(40f, 48f),
                        Point(41f, 46f),
                        Point(43f, 43f),
                        Point(45f, 41f),
                        Point(46f, 39f),
                        Point(48f, 38f),
                        Point(50f, 37f),
                        Point(52f, 37f),
                        Point(54f, 36f),
                        Point(56f, 36f),
                        Point(59f, 36f),
                        Point(61f, 37f),
                        Point(63f, 38f),
                        Point(65f, 41f),
                        Point(66f, 45f),
                        Point(66f, 49f),
                        Point(66f, 52f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-oi-stem",
                points = StrokePoints.polyline(
                    listOf(
                        Point(66f, 52f),
                        Point(66f, 60f),
                        Point(66f, 68f),
                        Point(66f, 76f),
                        Point(66f, 84f),
                        Point(66f, 88f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-oi-base",
                points = StrokePoints.polyline(
                    listOf(
                        Point(20f, 57f),
                        Point(20f, 62f),
                        Point(19f, 66f),
                        Point(20f, 69f),
                        Point(21f, 73f),
                        Point(23f, 76f),
                        Point(26f, 78f),
                        Point(30f, 80f),
                        Point(35f, 80f),
                        Point(39f, 80f),
                        Point(43f, 80f),
                        Point(47f, 79f),
                        Point(51f, 79f),
                        Point(54f, 79f),
                        Point(58f, 80f),
                        Point(61f, 82f),
                        Point(64f, 83f),
                        Point(66f, 84f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-oi-kar",
                points = StrokePoints.polyline(
                    listOf(
                        Point(40f, 8f),
                        Point(40f, 13f),
                        Point(41f, 15f),
                        Point(42f, 18f),
                        Point(44f, 20f),
                        Point(46f, 21f),
                        Point(49f, 22f),
                        Point(53f, 23f),
                        Point(58f, 23f),
                        Point(62f, 24f),
                        Point(67f, 25f),
                        Point(72f, 26f),
                        Point(76f, 28f),
                        Point(79f, 31f),
                        Point(80f, 35f),
                        Point(81f, 38f),
                        Point(80f, 42f),
                        Point(78f, 47f),
                        Point(75f, 49f),
                        Point(71f, 52f),
                        Point(66f, 52f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "vowel-oa",
        title = "ও",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 10,
        strokes = listOf(
            Stroke(
                id = "vowel-oa-stem",
                points = StrokePoints.line(Point(50f, 20f), Point(50f, 85f)),
            ),
            Stroke(
                id = "vowel-oa-loop",
                points = StrokePoints.arc(center = Point(50f, 30f), radius = 15f, startDeg = 90f, sweepDeg = 300f, samples = 20),
            ),
        ),
    ),
    Exercise(
        id = "vowel-au",
        title = "ঔ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.ADVANCED,
        order = 11,
        strokes = listOf(
            Stroke(
                id = "vowel-au-stem",
                points = StrokePoints.line(Point(50f, 20f), Point(50f, 85f)),
            ),
            Stroke(
                id = "vowel-au-loop",
                points = StrokePoints.arc(center = Point(50f, 30f), radius = 15f, startDeg = 90f, sweepDeg = 300f, samples = 20),
            ),
            Stroke(
                id = "vowel-au-tail",
                points = StrokePoints.line(Point(65f, 15f), Point(80f, 35f)),
            ),
        ),
    ),
)
