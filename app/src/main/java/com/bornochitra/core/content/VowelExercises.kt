package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Vowel stroke geometry. অ (plan.md Step 10.8), আ (Step 11.1), ই (Step 11.2), ঈ (Step 11.3) and উ
 * (Step 11.4) are read off the rendered glyph's centreline; the letters after them still carry
 * placeholder paths that approximate the letter's silhouette, and are corrected one at a time by
 * plan.md Steps 11.5-11.10.
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
    Exercise(
        id = "vowel-uu",
        title = "ঊ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 6,
        strokes = listOf(
            Stroke(
                id = "vowel-uu-body",
                points = StrokePoints.line(Point(30f, 20f), Point(30f, 65f)),
            ),
            Stroke(
                id = "vowel-uu-hook",
                points = StrokePoints.arc(center = Point(45f, 65f), radius = 15f, startDeg = 180f, sweepDeg = 160f, samples = 16),
            ),
            Stroke(
                id = "vowel-uu-tail",
                points = StrokePoints.line(Point(50f, 25f), Point(70f, 15f)),
            ),
        ),
    ),
    Exercise(
        id = "vowel-ri",
        title = "ঋ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.ADVANCED,
        order = 7,
        strokes = listOf(
            Stroke(
                id = "vowel-ri-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(35f, 15f),
                        Point(35f, 55f),
                        Point(55f, 70f),
                        Point(35f, 85f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-ri-hook",
                points = StrokePoints.arc(center = Point(35f, 15f), radius = 8f, startDeg = 90f, sweepDeg = 270f, samples = 12),
            ),
        ),
    ),
    Exercise(
        id = "vowel-e",
        title = "এ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 8,
        strokes = listOf(
            Stroke(
                id = "vowel-e-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 50f),
                        Point(75f, 50f),
                        Point(55f, 20f),
                        Point(30f, 35f),
                        Point(55f, 80f),
                        Point(75f, 65f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "vowel-oi",
        title = "ঐ",
        type = ExerciseType.VOWEL,
        difficulty = Difficulty.INTERMEDIATE,
        order = 9,
        strokes = listOf(
            Stroke(
                id = "vowel-oi-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(25f, 50f),
                        Point(75f, 50f),
                        Point(55f, 20f),
                        Point(30f, 35f),
                        Point(55f, 80f),
                        Point(75f, 65f),
                    ),
                ),
            ),
            Stroke(
                id = "vowel-oi-mark",
                points = StrokePoints.line(Point(15f, 40f), Point(15f, 60f)),
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
