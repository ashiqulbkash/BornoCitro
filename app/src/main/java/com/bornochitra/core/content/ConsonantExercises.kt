package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/**
 * Consonant stroke geometry. ক is read off its rendered glyph's centreline (plan.md Step 11.11);
 * খ, গ, ঘ and ঙ still carry the placeholder geometry described in [vowelExercises]' history and are
 * corrected in plan.md Steps 11.12-11.15. Step 23 re-validates every letter before release.
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
    Exercise(
        id = "consonant-kho",
        title = "খ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.BEGINNER,
        order = 2,
        strokes = listOf(
            Stroke(
                id = "consonant-kho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 15f),
                        Point(30f, 85f),
                        Point(75f, 85f),
                        Point(75f, 45f),
                        Point(30f, 45f),
                    ),
                ),
            ),
        ),
    ),
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
                        Point(30f, 20f),
                        Point(70f, 20f),
                        Point(30f, 50f),
                        Point(70f, 55f),
                        Point(45f, 85f),
                    ),
                ),
            ),
        ),
    ),
    Exercise(
        id = "consonant-gho",
        title = "ঘ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.INTERMEDIATE,
        order = 4,
        strokes = listOf(
            Stroke(
                id = "consonant-gho-body",
                points = StrokePoints.polyline(
                    listOf(
                        Point(30f, 15f),
                        Point(30f, 85f),
                        Point(70f, 85f),
                        Point(70f, 15f),
                    ),
                ),
            ),
            Stroke(
                id = "consonant-gho-cross",
                points = StrokePoints.line(Point(30f, 50f), Point(70f, 50f)),
            ),
        ),
    ),
    Exercise(
        id = "consonant-ngo",
        title = "ঙ",
        type = ExerciseType.CONSONANT,
        difficulty = Difficulty.ADVANCED,
        order = 5,
        strokes = listOf(
            Stroke(
                id = "consonant-ngo-body",
                points = StrokePoints.arc(center = Point(50f, 50f), radius = 30f, startDeg = -30f, sweepDeg = 300f, samples = 24),
            ),
            Stroke(
                id = "consonant-ngo-dot",
                points = StrokePoints.arc(center = Point(50f, 85f), radius = 4f, startDeg = 0f, sweepDeg = 360f, samples = 12),
            ),
        ),
    ),
)
