package com.bornochitra.core.content

import com.bornochitra.core.model.Difficulty
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/** Digits with a loop or two bowls ask more of the hand than the straight and single-curve ones. */
private val intermediateDigits = setOf('3', '6', '8', '9')

private fun digitExercise(value: Int): Exercise {
    val digit = value.digitToChar()
    val id = "math-$value"
    return Exercise(
        id = id,
        title = "$digit",
        type = ExerciseType.MATH,
        difficulty = if (digit in intermediateDigits) Difficulty.INTERMEDIATE else Difficulty.BEGINNER,
        strokes = digitGuides.getValue(digit).strokes.map { Stroke(id = "$id-${it.name}", points = it.points) },
        order = value,
    )
}

private fun numberExercise(value: Int): Exercise {
    val id = "math-$value"
    val (tens, ones) = value.toString().map { twoDigitGuides.getValue(it) }
    return Exercise(
        id = id,
        title = "$value",
        type = ExerciseType.MATH,
        difficulty = Difficulty.INTERMEDIATE,
        strokes = interNumberComposer.compose(exerciseId = id, tens = tens, ones = ones),
        order = value,
    )
}

/**
 * 1 (aspect 0.441) is Inter's flag and a plain stem cut flat at both ends, with no foot. It is
 * written as taught, up the flag and then straight down: `flag` from its tip up to the top of the
 * stem, then `stem` from that shared dot down, a whole 12 spacings on the stem's centre line (x
 * 59.75), 5.5 units inside the flat top and foot. The flag meets the stem at ~54 degrees, so the
 * dots either side of the shared one stand clear of each other.
 */
private val digitOne = DigitGuide(
    inkLeft = 31.71f,
    inkRight = 68.29f,
    advanceLeft = 26.54f,
    advanceRight = 75.7f,
    strokes = listOf(
        DigitStroke(
            name = "flag",
            points = StrokePoints.polyline(
                listOf(
                    Point(35.42f, 28.26f),
                    Point(36.83f, 25.56f),
                    Point(38.63f, 23.17f),
                    Point(40.87f, 21.18f),
                    Point(43.3f, 19.42f),
                    Point(45.81f, 17.78f),
                    Point(48.51f, 16.48f),
                    Point(51.39f, 15.66f),
                    Point(54.29f, 14.88f),
                    Point(57.15f, 13.99f),
                    Point(59.75f, 12.5f),
                ),
            ),
        ),
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(59.75f, 12.5f), Point(59.75f, 84.52f)),
        ),
    ),
)

/**
 * 2 (aspect 0.707): `hook` from the terminal on the left, over the top, down the right and along
 * the diagonal to the bottom-left corner, then `base` along the bottom bar. The base is a whole 8
 * spacings on its midline (y 83), 2.8 inside the flat right end, and the hook ends on the base's
 * first dot, so the corner is one dot.
 */
private val digitTwo = DigitGuide(
    inkLeft = 20.67f,
    inkRight = 79.33f,
    advanceLeft = 14.63f,
    advanceRight = 85.43f,
    strokes = listOf(
        DigitStroke(
            name = "hook",
            points = StrokePoints.polyline(
                listOf(
                    Point(27.93f, 29.21f),
                    Point(29.63f, 26.16f),
                    Point(31.1f, 23.54f),
                    Point(32.8f, 21.08f),
                    Point(34.86f, 18.9f),
                    Point(37.25f, 17.1f),
                    Point(39.9f, 15.7f),
                    Point(42.73f, 14.69f),
                    Point(45.66f, 14.08f),
                    Point(48.65f, 13.83f),
                    Point(51.65f, 13.92f),
                    Point(54.61f, 14.35f),
                    Point(57.51f, 15.14f),
                    Point(60.26f, 16.32f),
                    Point(62.82f, 17.89f),
                    Point(65.09f, 19.84f),
                    Point(66.99f, 22.15f),
                    Point(68.43f, 24.78f),
                    Point(69.36f, 27.63f),
                    Point(69.77f, 30.6f),
                    Point(69.68f, 33.59f),
                    Point(69.12f, 36.54f),
                    Point(68.13f, 39.36f),
                    Point(66.78f, 42.04f),
                    Point(65.16f, 44.56f),
                    Point(63.34f, 46.95f),
                    Point(61.39f, 49.23f),
                    Point(59.34f, 51.42f),
                    Point(57.24f, 53.56f),
                    Point(55.1f, 55.66f),
                    Point(52.92f, 57.73f),
                    Point(50.75f, 59.8f),
                    Point(48.59f, 61.88f),
                    Point(46.43f, 63.96f),
                    Point(44.27f, 66.04f),
                    Point(42.13f, 68.15f),
                    Point(40.05f, 70.31f),
                    Point(38.01f, 72.51f),
                    Point(35.9f, 74.64f),
                    Point(33.7f, 76.68f),
                    Point(31.3f, 77.73f),
                    Point(29.84f, 80.32f),
                    Point(28.5f, 83f),
                ),
            ),
        ),
        DigitStroke(
            name = "base",
            points = StrokePoints.line(Point(28.5f, 83f), Point(76.52f, 83f)),
        ),
    ),
)

/**
 * 3 (aspect 0.737) is two bowls meeting at a short middle bar. `top` runs from the upper terminal
 * over the top and down into the junction, then two spacings left along the bar to its tip;
 * `bottom` starts at that tip, comes back along the same bar and runs round the lower bowl to the
 * lower terminal. The bar is straight and shared, so its dots are the same in both strokes.
 */
private val digitThree = DigitGuide(
    inkLeft = 19.41f,
    inkRight = 80.59f,
    advanceLeft = 14.04f,
    advanceRight = 85.75f,
    strokes = listOf(
        DigitStroke(
            name = "top",
            points = StrokePoints.polyline(
                listOf(
                    Point(25.6f, 24.97f),
                    Point(29.47f, 23.31f),
                    Point(31.89f, 21.55f),
                    Point(34.06f, 19.48f),
                    Point(36.4f, 17.61f),
                    Point(39f, 16.11f),
                    Point(41.78f, 15f),
                    Point(44.68f, 14.24f),
                    Point(47.65f, 13.83f),
                    Point(50.65f, 13.76f),
                    Point(53.63f, 14.03f),
                    Point(56.57f, 14.66f),
                    Point(59.38f, 15.68f),
                    Point(62.01f, 17.12f),
                    Point(64.37f, 18.97f),
                    Point(66.37f, 21.2f),
                    Point(67.9f, 23.77f),
                    Point(68.88f, 26.6f),
                    Point(69.25f, 29.57f),
                    Point(68.95f, 32.55f),
                    Point(67.96f, 35.38f),
                    Point(66.35f, 37.9f),
                    Point(64.32f, 40.1f),
                    Point(62.1f, 42.13f),
                    Point(59.73f, 43.89f),
                    Point(58f, 44.75f),
                    Point(58f, 47.75f),
                    Point(46f, 47.75f),
                ),
            ),
        ),
        DigitStroke(
            name = "bottom",
            points = StrokePoints.polyline(
                listOf(
                    Point(46f, 47.75f),
                    Point(58f, 47.75f),
                    Point(60.86f, 48.64f),
                    Point(62.63f, 50.37f),
                    Point(64.06f, 52.7f),
                    Point(66.2f, 54.8f),
                    Point(68.27f, 56.97f),
                    Point(69.95f, 59.44f),
                    Point(71.09f, 62.21f),
                    Point(71.59f, 65.17f),
                    Point(71.44f, 68.16f),
                    Point(70.68f, 71.05f),
                    Point(69.33f, 73.73f),
                    Point(67.49f, 76.09f),
                    Point(65.27f, 78.11f),
                    Point(62.78f, 79.77f),
                    Point(60.08f, 81.08f),
                    Point(57.24f, 82.03f),
                    Point(54.3f, 82.63f),
                    Point(51.32f, 82.92f),
                    Point(48.32f, 82.93f),
                    Point(45.33f, 82.66f),
                    Point(42.39f, 82.08f),
                    Point(39.53f, 81.17f),
                    Point(36.81f, 79.91f),
                    Point(34.31f, 78.27f),
                    Point(32.05f, 76.3f),
                    Point(29.78f, 74.34f),
                    Point(27.18f, 72.84f),
                    Point(23.62f, 71.29f),
                ),
            ),
        ),
    ),
)

/**
 * 4 (aspect 0.800): `slant` down the diagonal from the apex to the corner and along the crossbar,
 * then `stem` straight down, as the digit is taught. The stem is on its centre line (x 64.75) and
 * the crossbar on its midline (y 68.5); the corner sits 7 spacings left of the stem, so the
 * crossbar has a dot exactly on the stem's (the crossing rule of 3.24), 1.9 units right of where
 * the diagonal's own centre line meets the crossbar.
 */
private val digitFour = DigitGuide(
    inkLeft = 16.81f,
    inkRight = 83.19f,
    advanceLeft = 11.18f,
    advanceRight = 88.3f,
    strokes = listOf(
        DigitStroke(
            name = "slant",
            points = StrokePoints.polyline(
                listOf(
                    Point(56.66f, 11.86f),
                    Point(22.75f, 68.5f),
                    Point(76.77f, 68.5f),
                ),
            ),
        ),
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(64.75f, 14.5f), Point(64.75f, 86.52f)),
        ),
    ),
)

/**
 * 5 (aspect 0.720), written down, round, then the flag: `stem` 5 spacings down the left side from
 * the top corner, `bowl` from there over, down the right and round to the lower terminal, and
 * `flag` from the same top corner along the top bar. The flag is a whole 6 spacings, 6.6 units
 * short of the flat right end (7 would leave its last dot hanging off the cut).
 */
private val digitFive = DigitGuide(
    inkLeft = 20.12f,
    inkRight = 79.88f,
    advanceLeft = 15.01f,
    advanceRight = 85.05f,
    strokes = listOf(
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(32.75f, 14f), Point(30.75f, 43.95f)),
        ),
        DigitStroke(
            name = "bowl",
            points = StrokePoints.polyline(
                listOf(
                    Point(30.75f, 43.95f),
                    Point(33.35f, 42.45f),
                    Point(34.5f, 43.35f),
                    Point(37.08f, 44.67f),
                    Point(40.04f, 44.44f),
                    Point(42.84f, 43.35f),
                    Point(45.64f, 42.29f),
                    Point(48.55f, 41.58f),
                    Point(51.54f, 41.31f),
                    Point(54.53f, 41.48f),
                    Point(57.47f, 42.07f),
                    Point(60.28f, 43.1f),
                    Point(62.9f, 44.56f),
                    Point(65.25f, 46.42f),
                    Point(67.27f, 48.64f),
                    Point(68.91f, 51.14f),
                    Point(70.16f, 53.87f),
                    Point(71.01f, 56.74f),
                    Point(71.45f, 59.71f),
                    Point(71.52f, 62.71f),
                    Point(71.22f, 65.69f),
                    Point(70.53f, 68.61f),
                    Point(69.45f, 71.4f),
                    Point(67.95f, 74f),
                    Point(66.08f, 76.34f),
                    Point(63.88f, 78.38f),
                    Point(61.41f, 80.07f),
                    Point(58.72f, 81.4f),
                    Point(55.88f, 82.36f),
                    Point(52.94f, 82.94f),
                    Point(49.95f, 83.16f),
                    Point(46.96f, 83.05f),
                    Point(43.99f, 82.6f),
                    Point(41.11f, 81.79f),
                    Point(38.35f, 80.61f),
                    Point(35.8f, 79.04f),
                    Point(33.52f, 77.09f),
                    Point(31.41f, 74.96f),
                    Point(27.71f, 71.95f),
                ),
            ),
        ),
        DigitStroke(
            name = "flag",
            points = StrokePoints.line(Point(32.75f, 14f), Point(68.77f, 14f)),
        ),
    ),
)

/**
 * 6 (aspect 0.747): `down` from the upper terminal over the top, down the left side and round to
 * the bottom, then `loop` up the right, over the counter and back into the left side. The upper
 * terminal's inset is slid until the loop's last dot lands on one of `down`'s dots (0.14 off before
 * the loop is bent 0.04% to meet it exactly), so the join is a single dot.
 */
private val digitSix = DigitGuide(
    inkLeft = 19.01f,
    inkRight = 80.99f,
    advanceLeft = 13.92f,
    advanceRight = 86.06f,
    strokes = listOf(
        DigitStroke(
            name = "down",
            points = StrokePoints.polyline(
                listOf(
                    Point(70.29f, 24.18f),
                    Point(68.29f, 21.95f),
                    Point(66.2f, 19.78f),
                    Point(63.86f, 17.9f),
                    Point(61.27f, 16.39f),
                    Point(58.48f, 15.28f),
                    Point(55.55f, 14.59f),
                    Point(52.57f, 14.27f),
                    Point(49.56f, 14.32f),
                    Point(46.6f, 14.76f),
                    Point(43.71f, 15.59f),
                    Point(40.98f, 16.83f),
                    Point(38.46f, 18.45f),
                    Point(36.2f, 20.41f),
                    Point(34.22f, 22.66f),
                    Point(32.52f, 25.13f),
                    Point(31.11f, 27.78f),
                    Point(29.95f, 30.54f),
                    Point(29.03f, 33.4f),
                    Point(28.32f, 36.31f),
                    Point(27.8f, 39.27f),
                    Point(27.4f, 42.25f),
                    Point(27.05f, 45.23f),
                    Point(26.86f, 48.22f),
                    Point(27.11f, 51.21f),
                    Point(27.82f, 54.12f),
                    Point(28.41f, 57.06f),
                    Point(28.56f, 60.05f),
                    Point(28.75f, 63.05f),
                    Point(29.28f, 66.01f),
                    Point(30.15f, 68.88f),
                    Point(31.37f, 71.62f),
                    Point(32.91f, 74.19f),
                    Point(34.81f, 76.52f),
                    Point(37.02f, 78.56f),
                    Point(39.5f, 80.25f),
                    Point(42.19f, 81.57f),
                ),
            ),
        ),
        DigitStroke(
            name = "loop",
            points = StrokePoints.polyline(
                listOf(
                    Point(42.19f, 81.57f),
                    Point(45.19f, 81.76f),
                    Point(48.18f, 81.96f),
                    Point(51.17f, 82.15f),
                    Point(54.17f, 82.34f),
                    Point(57.16f, 82.52f),
                    Point(59.99f, 81.52f),
                    Point(62.66f, 80.15f),
                    Point(65.12f, 78.44f),
                    Point(67.3f, 76.39f),
                    Point(69.17f, 74.04f),
                    Point(70.66f, 71.44f),
                    Point(71.76f, 68.64f),
                    Point(72.43f, 65.72f),
                    Point(72.71f, 62.74f),
                    Point(72.6f, 59.74f),
                    Point(72.11f, 56.79f),
                    Point(71.2f, 53.93f),
                    Point(69.86f, 51.25f),
                    Point(68.13f, 48.8f),
                    Point(66.01f, 46.68f),
                    Point(63.58f, 44.93f),
                    Point(60.9f, 43.58f),
                    Point(58.05f, 42.68f),
                    Point(55.08f, 42.24f),
                    Point(52.09f, 42.23f),
                    Point(49.12f, 42.67f),
                    Point(46.27f, 43.58f),
                    Point(43.6f, 44.95f),
                    Point(41.16f, 46.7f),
                    Point(38.81f, 48.56f),
                    Point(36.14f, 49.89f),
                    Point(33.18f, 50.38f),
                    Point(30.2f, 50.74f),
                    Point(27.15f, 51.23f),
                ),
            ),
        ),
    ),
)

/**
 * 7 (aspect 0.699) is one movement, `body`: along the top bar from the left and down the diagonal
 * (row-centre line x = 66.78 - 0.504(y - 28)), 21 spacings and 126 units in all. The corner turns
 * through ~63 degrees, so its neighbouring dots stand clear with no flat.
 */
private val digitSeven = DigitGuide(
    inkLeft = 21f,
    inkRight = 79f,
    advanceLeft = 16.83f,
    advanceRight = 83.14f,
    strokes = listOf(
        DigitStroke(
            name = "body",
            points = StrokePoints.polyline(
                listOf(
                    Point(25.75f, 14f),
                    Point(73.75f, 14f),
                    Point(38.63f, 83.67f),
                ),
            ),
        ),
    ),
)

/**
 * 8 (aspect 0.749) is two laps through the waist, each centred on the ink and scaled about the
 * waist point (50, 47.5) to whole spacings (upper 19, lower 21), so the waist is a dot of both
 * strokes. `s` starts at the top, goes down the upper left, through the waist and round the lower
 * right to the bottom; `back` returns up the lower left, through the waist and up the upper right
 * to the top.
 */
private val digitEight = DigitGuide(
    inkLeft = 18.93f,
    inkRight = 81.07f,
    advanceLeft = 13.84f,
    advanceRight = 86.14f,
    strokes = listOf(
        DigitStroke(
            name = "s",
            points = StrokePoints.polyline(
                listOf(
                    Point(52.92f, 14.38f),
                    Point(50f, 14.2f),
                    Point(47.08f, 14.34f),
                    Point(44.21f, 14.8f),
                    Point(41.4f, 15.62f),
                    Point(38.76f, 16.8f),
                    Point(36.3f, 18.35f),
                    Point(34.15f, 20.25f),
                    Point(32.36f, 22.51f),
                    Point(31.08f, 25.07f),
                    Point(30.38f, 27.83f),
                    Point(30.3f, 30.68f),
                    Point(30.85f, 33.48f),
                    Point(31.99f, 36.12f),
                    Point(33.63f, 38.48f),
                    Point(35.67f, 40.53f),
                    Point(37.96f, 42.32f),
                    Point(40.44f, 43.85f),
                    Point(43.22f, 44.51f),
                    Point(45.4f, 43.96f),
                    Point(47.69f, 45.73f),
                    Point(50f, 47.5f),
                    Point(52.53f, 49.2f),
                    Point(55.07f, 50.89f),
                    Point(57.38f, 51.07f),
                    Point(60.36f, 51.4f),
                    Point(63.17f, 52.59f),
                    Point(65.77f, 54.21f),
                    Point(68.05f, 56.2f),
                    Point(69.93f, 58.57f),
                    Point(71.34f, 61.23f),
                    Point(72.15f, 64.12f),
                    Point(72.32f, 67.11f),
                    Point(71.82f, 70.06f),
                    Point(70.65f, 72.83f),
                    Point(68.94f, 75.33f),
                    Point(66.81f, 77.5f),
                    Point(64.36f, 79.33f),
                    Point(61.66f, 80.79f),
                    Point(58.8f, 81.89f),
                    Point(55.8f, 82.64f),
                    Point(52.75f, 83.07f),
                ),
            ),
        ),
        DigitStroke(
            name = "back",
            points = StrokePoints.polyline(
                listOf(
                    Point(52.75f, 83.07f),
                    Point(49.81f, 83.2f),
                    Point(46.87f, 83.06f),
                    Point(43.95f, 82.66f),
                    Point(41.11f, 81.95f),
                    Point(38.36f, 80.93f),
                    Point(35.77f, 79.58f),
                    Point(33.39f, 77.9f),
                    Point(31.29f, 75.9f),
                    Point(29.56f, 73.58f),
                    Point(28.31f, 71f),
                    Point(27.63f, 68.22f),
                    Point(27.58f, 65.35f),
                    Point(28.13f, 62.54f),
                    Point(29.22f, 59.87f),
                    Point(30.8f, 57.45f),
                    Point(32.78f, 55.33f),
                    Point(35.09f, 53.54f),
                    Point(37.62f, 52.05f),
                    Point(40.33f, 50.94f),
                    Point(42.89f, 51.36f),
                    Point(45.14f, 50.76f),
                    Point(47.57f, 49.14f),
                    Point(50f, 47.5f),
                    Point(52.29f, 45.75f),
                    Point(54.59f, 44f),
                    Point(56.76f, 44.53f),
                    Point(59.54f, 43.9f),
                    Point(62.01f, 42.39f),
                    Point(64.27f, 40.61f),
                    Point(66.31f, 38.58f),
                    Point(67.95f, 36.24f),
                    Point(69.1f, 33.64f),
                    Point(69.67f, 30.86f),
                    Point(69.6f, 28.03f),
                    Point(68.91f, 25.26f),
                    Point(67.63f, 22.71f),
                    Point(65.84f, 20.45f),
                    Point(63.68f, 18.53f),
                    Point(61.25f, 16.95f),
                    Point(58.6f, 15.73f),
                    Point(55.8f, 14.89f),
                    Point(52.92f, 14.38f),
                ),
            ),
        ),
    ),
)

/**
 * 9 (aspect 0.744): `loop` from the top of the right side over the top, down the left and round the
 * bottom of the counter, then back up the right side to where it started; `tail` from that dot down
 * the right side and round to the lower terminal. The loop rejoins the right side on the tail's
 * third dot and runs back up the tail's own vertices, so that stretch is the same polyline in both
 * strokes and the join is one dot; only the curve before it is bent (1.3%) to make the loop a whole
 * 22 spacings.
 */
private val digitNine = DigitGuide(
    inkLeft = 19.13f,
    inkRight = 80.87f,
    advanceLeft = 14.05f,
    advanceRight = 85.92f,
    strokes = listOf(
        DigitStroke(
            name = "loop",
            points = StrokePoints.polyline(
                listOf(
                    Point(66.8f, 23f),
                    Point(65.14f, 20.57f),
                    Point(62.98f, 18.59f),
                    Point(60.62f, 16.86f),
                    Point(58.02f, 15.51f),
                    Point(55.26f, 14.54f),
                    Point(52.41f, 13.95f),
                    Point(49.5f, 13.72f),
                    Point(46.6f, 13.85f),
                    Point(43.73f, 14.34f),
                    Point(40.95f, 15.19f),
                    Point(38.3f, 16.42f),
                    Point(35.86f, 18.01f),
                    Point(33.67f, 19.93f),
                    Point(31.77f, 22.15f),
                    Point(30.22f, 24.62f),
                    Point(29.05f, 27.32f),
                    Point(28.26f, 30.14f),
                    Point(27.86f, 33.06f),
                    Point(27.84f, 36f),
                    Point(28.2f, 38.92f),
                    Point(28.95f, 41.77f),
                    Point(30.09f, 44.49f),
                    Point(31.64f, 46.99f),
                    Point(33.55f, 49.22f),
                    Point(35.79f, 51.11f),
                    Point(38.31f, 52.61f),
                    Point(41.03f, 53.69f),
                    Point(43.87f, 54.35f),
                    Point(46.77f, 54.57f),
                    Point(49.67f, 54.36f),
                    Point(52.5f, 53.7f),
                    Point(55.19f, 52.61f),
                    Point(57.7f, 51.12f),
                    Point(60.01f, 49.34f),
                    Point(62.25f, 47.48f),
                    Point(64.5f, 45.61f),
                    Point(66.76f, 43.75f),
                    Point(69f, 41.9f),
                    Point(71.25f, 40.04f),
                    Point(71.25f, 40.03f),
                    Point(71.33f, 37.03f),
                    Point(71.13f, 34.04f),
                    Point(70.6f, 31.09f),
                    Point(69.72f, 28.22f),
                    Point(68.44f, 25.51f),
                    Point(66.8f, 23f),
                ),
            ),
        ),
        DigitStroke(
            name = "tail",
            points = StrokePoints.polyline(
                listOf(
                    Point(66.8f, 23f),
                    Point(68.44f, 25.51f),
                    Point(69.72f, 28.22f),
                    Point(70.6f, 31.09f),
                    Point(71.13f, 34.04f),
                    Point(71.33f, 37.03f),
                    Point(71.25f, 40.03f),
                    Point(71.25f, 40.04f),
                    Point(71.25f, 43.03f),
                    Point(71.69f, 45.99f),
                    Point(72.27f, 48.93f),
                    Point(72.5f, 51.92f),
                    Point(72.35f, 54.92f),
                    Point(71.98f, 57.89f),
                    Point(71.43f, 60.84f),
                    Point(70.69f, 63.75f),
                    Point(69.74f, 66.6f),
                    Point(68.57f, 69.35f),
                    Point(67.13f, 71.99f),
                    Point(65.42f, 74.44f),
                    Point(63.41f, 76.68f),
                    Point(61.13f, 78.62f),
                    Point(58.6f, 80.22f),
                    Point(55.85f, 81.43f),
                    Point(52.96f, 82.22f),
                    Point(49.99f, 82.61f),
                    Point(46.99f, 82.63f),
                    Point(44.02f, 82.28f),
                    Point(41.11f, 81.53f),
                    Point(38.35f, 80.37f),
                    Point(35.81f, 78.78f),
                    Point(33.52f, 76.85f),
                    Point(31.33f, 74.8f),
                    Point(28.85f, 73.12f),
                    Point(26.29f, 71.56f),
                    Point(23.65f, 69.97f),
                ),
            ),
        ),
    ),
)

/**
 * 0 is only needed inside 10 and 20, so it has no exercise of its own. It is one oval ring (aspect
 * 0.780), written like o: `left` counter-clockwise from the top to the bottom, then `right` on up
 * to the top. The centreline is the midpoint of the ink along each normal, scaled about its centre
 * so the ring is whole spacings once composed.
 */
private val twoDigitZero = DigitGuide(
    inkLeft = 17.63f,
    inkRight = 82.37f,
    advanceLeft = 12.53f,
    advanceRight = 87.44f,
    strokes = listOf(
        DigitStroke(
            name = "left",
            points = StrokePoints.polyline(
                listOf(
                    Point(49.86f, 13.52f),
                    Point(47.09f, 13.7f),
                    Point(44.12f, 14.25f),
                    Point(41.27f, 15.2f),
                    Point(38.58f, 16.55f),
                    Point(36.13f, 18.28f),
                    Point(33.94f, 20.34f),
                    Point(32.06f, 22.67f),
                    Point(30.46f, 25.2f),
                    Point(29.15f, 27.89f),
                    Point(28.08f, 30.69f),
                    Point(27.24f, 33.56f),
                    Point(26.6f, 36.48f),
                    Point(26.14f, 39.43f),
                    Point(25.82f, 42.4f),
                    Point(25.63f, 45.39f),
                    Point(25.56f, 48.37f),
                    Point(25.61f, 51.36f),
                    Point(25.79f, 54.35f),
                    Point(26.11f, 57.32f),
                    Point(26.56f, 60.27f),
                    Point(27.18f, 63.2f),
                    Point(28.01f, 66.07f),
                    Point(29.06f, 68.87f),
                    Point(30.36f, 71.57f),
                    Point(31.95f, 74.11f),
                    Point(33.82f, 76.45f),
                    Point(35.98f, 78.53f),
                    Point(38.43f, 80.27f),
                    Point(41.1f, 81.63f),
                    Point(43.96f, 82.6f),
                    Point(46.91f, 83.17f),
                    Point(49.92f, 83.36f),
                ),
            ),
        ),
        DigitStroke(
            name = "right",
            points = StrokePoints.polyline(
                listOf(
                    Point(49.92f, 83.36f),
                    Point(52.92f, 83.17f),
                    Point(55.85f, 82.61f),
                    Point(58.69f, 81.65f),
                    Point(61.34f, 80.29f),
                    Point(63.78f, 78.55f),
                    Point(65.94f, 76.49f),
                    Point(67.82f, 74.15f),
                    Point(69.39f, 71.61f),
                    Point(70.69f, 68.92f),
                    Point(71.75f, 66.12f),
                    Point(72.57f, 63.25f),
                    Point(73.19f, 60.33f),
                    Point(73.64f, 57.37f),
                    Point(73.95f, 54.4f),
                    Point(74.14f, 51.42f),
                    Point(74.2f, 48.43f),
                    Point(74.13f, 45.44f),
                    Point(73.95f, 42.46f),
                    Point(73.64f, 39.48f),
                    Point(73.17f, 36.53f),
                    Point(72.53f, 33.61f),
                    Point(71.7f, 30.74f),
                    Point(70.64f, 27.95f),
                    Point(69.33f, 25.26f),
                    Point(67.75f, 22.73f),
                    Point(65.87f, 20.4f),
                    Point(63.71f, 18.33f),
                    Point(61.28f, 16.59f),
                    Point(58.62f, 15.22f),
                    Point(55.78f, 14.26f),
                    Point(52.85f, 13.7f),
                    Point(49.86f, 13.52f),
                ),
            ),
        ),
    ),
)

/**
 * 1 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitOne = DigitGuide(
    inkLeft = 31.71f,
    inkRight = 68.29f,
    advanceLeft = 26.54f,
    advanceRight = 75.7f,
    strokes = listOf(
        DigitStroke(
            name = "flag",
            points = StrokePoints.polyline(
                listOf(
                    Point(37.06f, 25.15f),
                    Point(38.93f, 22.85f),
                    Point(41.14f, 20.98f),
                    Point(43.49f, 19.28f),
                    Point(45.92f, 17.7f),
                    Point(48.53f, 16.46f),
                    Point(51.35f, 15.78f),
                    Point(54.21f, 15.34f),
                    Point(57.05f, 14.75f),
                    Point(59.75f, 13.75f),
                ),
            ),
        ),
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(59.75f, 13.75f), Point(59.75f, 83.34f)),
        ),
    ),
)

/**
 * 2 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitTwo = DigitGuide(
    inkLeft = 20.67f,
    inkRight = 79.33f,
    advanceLeft = 14.63f,
    advanceRight = 85.43f,
    strokes = listOf(
        DigitStroke(
            name = "hook",
            points = StrokePoints.polyline(
                listOf(
                    Point(30f, 25.49f),
                    Point(31.76f, 22.5f),
                    Point(33.58f, 20.17f),
                    Point(35.76f, 18.15f),
                    Point(38.23f, 16.52f),
                    Point(40.93f, 15.28f),
                    Point(43.77f, 14.43f),
                    Point(46.7f, 13.95f),
                    Point(49.66f, 13.83f),
                    Point(52.63f, 14.02f),
                    Point(55.54f, 14.56f),
                    Point(58.37f, 15.46f),
                    Point(61.05f, 16.75f),
                    Point(63.5f, 18.41f),
                    Point(65.66f, 20.44f),
                    Point(67.42f, 22.83f),
                    Point(68.72f, 25.49f),
                    Point(69.51f, 28.35f),
                    Point(69.79f, 31.3f),
                    Point(69.6f, 34.26f),
                    Point(68.94f, 37.16f),
                    Point(67.88f, 39.93f),
                    Point(66.48f, 42.54f),
                    Point(64.84f, 45.01f),
                    Point(63.01f, 47.36f),
                    Point(61.06f, 49.59f),
                    Point(59.02f, 51.76f),
                    Point(56.94f, 53.87f),
                    Point(54.81f, 55.94f),
                    Point(52.66f, 57.98f),
                    Point(50.51f, 60.04f),
                    Point(48.37f, 62.1f),
                    Point(46.23f, 64.15f),
                    Point(44.09f, 66.22f),
                    Point(41.98f, 68.3f),
                    Point(39.93f, 70.44f),
                    Point(37.9f, 72.62f),
                    Point(35.82f, 74.73f),
                    Point(33.63f, 76.74f),
                    Point(31.26f, 77.76f),
                    Point(29.83f, 80.34f),
                    Point(28.5f, 83f),
                ),
            ),
        ),
        DigitStroke(
            name = "base",
            points = StrokePoints.line(Point(28.5f, 83f), Point(72.01f, 83f)),
        ),
    ),
)

/**
 * 3 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitThree = DigitGuide(
    inkLeft = 19.41f,
    inkRight = 80.59f,
    advanceLeft = 14.04f,
    advanceRight = 85.75f,
    strokes = listOf(
        DigitStroke(
            name = "top",
            points = StrokePoints.polyline(
                listOf(
                    Point(25.29f, 25.08f),
                    Point(29.15f, 23.48f),
                    Point(31.54f, 21.86f),
                    Point(33.65f, 19.87f),
                    Point(35.86f, 17.99f),
                    Point(38.31f, 16.46f),
                    Point(40.96f, 15.28f),
                    Point(43.73f, 14.45f),
                    Point(46.58f, 13.94f),
                    Point(49.47f, 13.75f),
                    Point(52.37f, 13.87f),
                    Point(55.23f, 14.32f),
                    Point(58.01f, 15.12f),
                    Point(60.65f, 16.31f),
                    Point(63.09f, 17.88f),
                    Point(65.23f, 19.83f),
                    Point(67f, 22.12f),
                    Point(68.3f, 24.71f),
                    Point(69.06f, 27.5f),
                    Point(69.23f, 30.39f),
                    Point(68.77f, 33.24f),
                    Point(67.68f, 35.92f),
                    Point(66.03f, 38.3f),
                    Point(64.02f, 40.38f),
                    Point(61.87f, 42.33f),
                    Point(59.58f, 43.78f),
                    Point(58f, 44.85f),
                    Point(58f, 47.75f),
                    Point(49.3f, 47.75f),
                ),
            ),
        ),
        DigitStroke(
            name = "bottom",
            points = StrokePoints.polyline(
                listOf(
                    Point(49.3f, 47.75f),
                    Point(58f, 47.75f),
                    Point(60.77f, 48.61f),
                    Point(62.73f, 50.2f),
                    Point(63.85f, 52.47f),
                    Point(65.9f, 54.52f),
                    Point(67.94f, 56.59f),
                    Point(69.65f, 58.92f),
                    Point(70.88f, 61.54f),
                    Point(71.52f, 64.36f),
                    Point(71.55f, 67.25f),
                    Point(71f, 70.09f),
                    Point(69.89f, 72.76f),
                    Point(68.28f, 75.17f),
                    Point(66.29f, 77.27f),
                    Point(63.99f, 79.03f),
                    Point(61.47f, 80.46f),
                    Point(58.79f, 81.56f),
                    Point(56f, 82.32f),
                    Point(53.14f, 82.78f),
                    Point(50.24f, 82.96f),
                    Point(47.35f, 82.88f),
                    Point(44.47f, 82.52f),
                    Point(41.65f, 81.88f),
                    Point(38.91f, 80.93f),
                    Point(36.32f, 79.63f),
                    Point(33.94f, 77.98f),
                    Point(31.78f, 76.05f),
                    Point(28.94f, 73.64f),
                ),
            ),
        ),
    ),
)

/**
 * 4 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitFour = DigitGuide(
    inkLeft = 16.81f,
    inkRight = 83.19f,
    advanceLeft = 11.18f,
    advanceRight = 88.3f,
    strokes = listOf(
        DigitStroke(
            name = "slant",
            points = StrokePoints.polyline(
                listOf(
                    Point(53.54f, 16.85f),
                    Point(21.27f, 68.5f),
                    Point(73.47f, 68.5f),
                ),
            ),
        ),
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(64.75f, 16.33f), Point(64.75f, 85.92f)),
        ),
    ),
)

/**
 * 5 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitFive = DigitGuide(
    inkLeft = 20.12f,
    inkRight = 79.88f,
    advanceLeft = 15.01f,
    advanceRight = 85.05f,
    strokes = listOf(
        DigitStroke(
            name = "stem",
            points = StrokePoints.line(Point(32.75f, 14f), Point(31.01f, 40.06f)),
        ),
        DigitStroke(
            name = "bowl",
            points = StrokePoints.polyline(
                listOf(
                    Point(31.01f, 40.06f),
                    Point(33.81f, 41.07f),
                    Point(34.66f, 42.84f),
                    Point(36.89f, 44.74f),
                    Point(39.81f, 44.62f),
                    Point(42.56f, 43.49f),
                    Point(45.32f, 42.38f),
                    Point(48.2f, 41.63f),
                    Point(51.15f, 41.31f),
                    Point(54.12f, 41.43f),
                    Point(57.05f, 41.96f),
                    Point(59.86f, 42.92f),
                    Point(62.49f, 44.3f),
                    Point(64.87f, 46.07f),
                    Point(66.94f, 48.21f),
                    Point(68.64f, 50.65f),
                    Point(69.95f, 53.32f),
                    Point(70.86f, 56.14f),
                    Point(71.39f, 59.07f),
                    Point(71.54f, 62.04f),
                    Point(71.33f, 65f),
                    Point(70.74f, 67.92f),
                    Point(69.76f, 70.72f),
                    Point(68.37f, 73.35f),
                    Point(66.61f, 75.75f),
                    Point(64.52f, 77.85f),
                    Point(62.14f, 79.63f),
                    Point(59.53f, 81.06f),
                    Point(56.75f, 82.11f),
                    Point(53.86f, 82.79f),
                    Point(50.9f, 83.13f),
                    Point(47.93f, 83.12f),
                    Point(44.97f, 82.78f),
                    Point(42.08f, 82.1f),
                    Point(39.29f, 81.07f),
                    Point(36.68f, 79.65f),
                    Point(34.31f, 77.85f),
                    Point(32.19f, 75.76f),
                    Point(29.14f, 72.9f),
                ),
            ),
        ),
        DigitStroke(
            name = "flag",
            points = StrokePoints.line(Point(32.75f, 14f), Point(67.56f, 14f)),
        ),
    ),
)

/**
 * 6 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitSix = DigitGuide(
    inkLeft = 19.01f,
    inkRight = 80.99f,
    advanceLeft = 13.92f,
    advanceRight = 86.06f,
    strokes = listOf(
        DigitStroke(
            name = "down",
            points = StrokePoints.polyline(
                listOf(
                    Point(69.09f, 22.84f),
                    Point(67.09f, 20.64f),
                    Point(64.86f, 18.65f),
                    Point(62.39f, 16.98f),
                    Point(59.69f, 15.7f),
                    Point(56.83f, 14.84f),
                    Point(53.88f, 14.37f),
                    Point(50.91f, 14.25f),
                    Point(47.93f, 14.51f),
                    Point(45.02f, 15.16f),
                    Point(42.22f, 16.21f),
                    Point(39.61f, 17.64f),
                    Point(37.24f, 19.43f),
                    Point(35.14f, 21.54f),
                    Point(33.3f, 23.9f),
                    Point(31.76f, 26.45f),
                    Point(30.49f, 29.14f),
                    Point(29.47f, 31.94f),
                    Point(28.66f, 34.81f),
                    Point(28.05f, 37.73f),
                    Point(27.6f, 40.67f),
                    Point(27.23f, 43.64f),
                    Point(26.93f, 46.61f),
                    Point(26.9f, 49.59f),
                    Point(27.4f, 52.52f),
                    Point(28.14f, 55.41f),
                    Point(28.51f, 58.37f),
                    Point(28.62f, 61.35f),
                    Point(28.93f, 64.31f),
                    Point(29.6f, 67.22f),
                    Point(30.62f, 70.03f),
                    Point(31.95f, 72.69f),
                    Point(33.63f, 75.15f),
                    Point(35.65f, 77.35f),
                    Point(37.95f, 79.26f),
                    Point(40.51f, 80.8f),
                ),
            ),
        ),
        DigitStroke(
            name = "loop",
            points = StrokePoints.polyline(
                listOf(
                    Point(40.51f, 80.8f),
                    Point(43.49f, 81.1f),
                    Point(46.48f, 81.39f),
                    Point(49.46f, 81.69f),
                    Point(52.46f, 81.99f),
                    Point(55.44f, 82.29f),
                    Point(58.39f, 82.15f),
                    Point(61.16f, 80.98f),
                    Point(63.76f, 79.48f),
                    Point(66.13f, 77.64f),
                    Point(68.23f, 75.48f),
                    Point(69.97f, 73.05f),
                    Point(71.34f, 70.38f),
                    Point(72.31f, 67.55f),
                    Point(72.86f, 64.62f),
                    Point(73.02f, 61.63f),
                    Point(72.8f, 58.66f),
                    Point(72.18f, 55.75f),
                    Point(71.15f, 52.94f),
                    Point(69.69f, 50.35f),
                    Point(67.85f, 48.01f),
                    Point(65.64f, 46f),
                    Point(63.13f, 44.39f),
                    Point(60.4f, 43.18f),
                    Point(57.5f, 42.41f),
                    Point(54.52f, 42.08f),
                    Point(51.52f, 42.2f),
                    Point(48.57f, 42.78f),
                    Point(45.74f, 43.8f),
                    Point(43.13f, 45.28f),
                    Point(40.74f, 47.09f),
                    Point(38.33f, 48.9f),
                    Point(35.57f, 50.03f),
                    Point(32.58f, 50.43f),
                    Point(29.6f, 50.81f),
                    Point(27.05f, 50.6f),
                ),
            ),
        ),
    ),
)

/**
 * 7 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitSeven = DigitGuide(
    inkLeft = 21f,
    inkRight = 79f,
    advanceLeft = 16.83f,
    advanceRight = 83.14f,
    strokes = listOf(
        DigitStroke(
            name = "body",
            points = StrokePoints.polyline(
                listOf(
                    Point(30.27f, 14f),
                    Point(73.75f, 14f),
                    Point(38.52f, 83.91f),
                ),
            ),
        ),
    ),
)

/**
 * 8 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitEight = DigitGuide(
    inkLeft = 18.93f,
    inkRight = 81.07f,
    advanceLeft = 13.84f,
    advanceRight = 86.14f,
    strokes = listOf(
        DigitStroke(
            name = "s",
            points = StrokePoints.polyline(
                listOf(
                    Point(54.25f, 14.86f),
                    Point(51.31f, 14.52f),
                    Point(48.36f, 14.51f),
                    Point(45.42f, 14.83f),
                    Point(42.55f, 15.51f),
                    Point(39.81f, 16.57f),
                    Point(37.25f, 18.01f),
                    Point(34.96f, 19.82f),
                    Point(33.03f, 22.01f),
                    Point(31.57f, 24.51f),
                    Point(30.69f, 27.27f),
                    Point(30.47f, 30.15f),
                    Point(30.89f, 33.03f),
                    Point(31.93f, 35.74f),
                    Point(33.52f, 38.18f),
                    Point(35.53f, 40.31f),
                    Point(37.82f, 42.16f),
                    Point(40.3f, 43.75f),
                    Point(43.12f, 44.59f),
                    Point(45.34f, 43.91f),
                    Point(47.67f, 45.7f),
                    Point(50f, 47.5f),
                    Point(52.45f, 49.13f),
                    Point(54.89f, 50.77f),
                    Point(57.28f, 51.68f),
                    Point(59.88f, 51.29f),
                    Point(62.66f, 52.3f),
                    Point(65.23f, 53.72f),
                    Point(67.58f, 55.48f),
                    Point(69.6f, 57.6f),
                    Point(71.22f, 60.02f),
                    Point(72.37f, 62.67f),
                    Point(72.97f, 65.5f),
                    Point(72.98f, 68.4f),
                    Point(72.39f, 71.24f),
                    Point(71.2f, 73.9f),
                    Point(69.54f, 76.31f),
                    Point(67.49f, 78.41f),
                    Point(65.16f, 80.21f),
                    Point(62.59f, 81.69f),
                    Point(59.86f, 82.83f),
                    Point(57.01f, 83.65f),
                    Point(54.09f, 84.18f),
                ),
            ),
        ),
        DigitStroke(
            name = "back",
            points = StrokePoints.polyline(
                listOf(
                    Point(54.09f, 84.18f),
                    Point(51.14f, 84.43f),
                    Point(48.17f, 84.41f),
                    Point(45.23f, 84.14f),
                    Point(42.32f, 83.59f),
                    Point(39.5f, 82.72f),
                    Point(36.81f, 81.55f),
                    Point(34.28f, 80.05f),
                    Point(31.99f, 78.21f),
                    Point(30.01f, 76.08f),
                    Point(28.42f, 73.64f),
                    Point(27.35f, 70.95f),
                    Point(26.85f, 68.1f),
                    Point(26.97f, 65.2f),
                    Point(27.67f, 62.39f),
                    Point(28.9f, 59.74f),
                    Point(30.57f, 57.35f),
                    Point(32.64f, 55.27f),
                    Point(35f, 53.52f),
                    Point(37.56f, 52.05f),
                    Point(40.32f, 50.99f),
                    Point(42.73f, 51.71f),
                    Point(45.11f, 50.79f),
                    Point(47.55f, 49.15f),
                    Point(50f, 47.5f),
                    Point(52.34f, 45.72f),
                    Point(54.67f, 43.93f),
                    Point(56.91f, 44.63f),
                    Point(59.73f, 43.8f),
                    Point(62.2f, 42.2f),
                    Point(64.48f, 40.36f),
                    Point(66.49f, 38.23f),
                    Point(68.07f, 35.79f),
                    Point(69.09f, 33.08f),
                    Point(69.49f, 30.21f),
                    Point(69.23f, 27.33f),
                    Point(68.32f, 24.56f),
                    Point(66.81f, 22.06f),
                    Point(64.83f, 19.88f),
                    Point(62.51f, 18.06f),
                    Point(59.92f, 16.61f),
                    Point(57.15f, 15.55f),
                    Point(54.25f, 14.86f),
                ),
            ),
        ),
    ),
)

/**
 * 9 for the two-digit numbers: the same derivation as the full-size digit with its dot spacing and
 * radius divided by NumberComposer's scale (0.69), so every stroke is whole spacings once the
 * number is composed.
 */
private val twoDigitNine = DigitGuide(
    inkLeft = 19.13f,
    inkRight = 80.87f,
    advanceLeft = 14.05f,
    advanceRight = 85.92f,
    strokes = listOf(
        DigitStroke(
            name = "loop",
            points = StrokePoints.polyline(
                listOf(
                    Point(66.8f, 23f),
                    Point(65.15f, 20.55f),
                    Point(63.01f, 18.56f),
                    Point(60.67f, 16.81f),
                    Point(58.11f, 15.45f),
                    Point(55.38f, 14.48f),
                    Point(52.56f, 13.88f),
                    Point(49.7f, 13.65f),
                    Point(46.84f, 13.77f),
                    Point(44.02f, 14.26f),
                    Point(41.28f, 15.11f),
                    Point(38.7f, 16.34f),
                    Point(36.3f, 17.92f),
                    Point(34.17f, 19.85f),
                    Point(32.32f, 22.08f),
                    Point(30.83f, 24.57f),
                    Point(29.71f, 27.27f),
                    Point(28.96f, 30.11f),
                    Point(28.61f, 33.03f),
                    Point(28.63f, 35.98f),
                    Point(29.02f, 38.91f),
                    Point(29.8f, 41.76f),
                    Point(30.98f, 44.47f),
                    Point(32.54f, 46.97f),
                    Point(34.47f, 49.19f),
                    Point(36.71f, 51.06f),
                    Point(39.21f, 52.54f),
                    Point(41.91f, 53.6f),
                    Point(44.72f, 54.23f),
                    Point(47.58f, 54.43f),
                    Point(50.44f, 54.18f),
                    Point(53.22f, 53.5f),
                    Point(55.85f, 52.36f),
                    Point(58.28f, 50.84f),
                    Point(60.49f, 49.01f),
                    Point(62.64f, 47.09f),
                    Point(64.8f, 45.17f),
                    Point(66.96f, 43.26f),
                    Point(69.1f, 41.35f),
                    Point(71.26f, 39.43f),
                    Point(71.33f, 36.97f),
                    Point(71.13f, 33.99f),
                    Point(70.59f, 31.05f),
                    Point(69.71f, 28.2f),
                    Point(68.44f, 25.5f),
                    Point(66.8f, 23f),
                ),
            ),
        ),
        DigitStroke(
            name = "tail",
            points = StrokePoints.polyline(
                listOf(
                    Point(66.8f, 23f),
                    Point(68.44f, 25.5f),
                    Point(69.71f, 28.2f),
                    Point(70.59f, 31.05f),
                    Point(71.13f, 33.99f),
                    Point(71.33f, 36.97f),
                    Point(71.26f, 39.43f),
                    Point(71.25f, 39.96f),
                    Point(71.24f, 42.95f),
                    Point(71.68f, 45.9f),
                    Point(72.25f, 48.84f),
                    Point(72.5f, 51.81f),
                    Point(72.36f, 54.8f),
                    Point(72f, 57.76f),
                    Point(71.46f, 60.7f),
                    Point(70.73f, 63.6f),
                    Point(69.8f, 66.44f),
                    Point(68.64f, 69.2f),
                    Point(67.23f, 71.83f),
                    Point(65.54f, 74.29f),
                    Point(63.56f, 76.53f),
                    Point(61.31f, 78.49f),
                    Point(58.8f, 80.11f),
                    Point(56.08f, 81.35f),
                    Point(53.21f, 82.17f),
                    Point(50.25f, 82.6f),
                    Point(47.27f, 82.64f),
                    Point(44.29f, 82.33f),
                    Point(41.39f, 81.62f),
                    Point(38.62f, 80.5f),
                    Point(36.06f, 78.97f),
                    Point(33.76f, 77.07f),
                    Point(31.58f, 75.02f),
                    Point(29.07f, 73.25f),
                ),
            ),
        ),
    ),
)

private val mathOperatorExercises: List<Exercise> = listOf(
    /**
     * + (aspect 0.999) is two equal bars crossing at (50, 48.5): `bar` left to right, then `stem`
     * top to bottom, each a whole 6 spacings either side of the crossing so both put a dot on it,
     * 5.4 units inside the flat ends.
     */
    Exercise(
        id = "math-plus",
        title = "+",
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        order = 21,
        strokes = listOf(
            Stroke(
                id = "math-plus-bar",
                points = StrokePoints.line(Point(14f, 48.5f), Point(86f, 48.5f)),
            ),
            Stroke(
                id = "math-plus-stem",
                points = StrokePoints.line(Point(50f, 12.5f), Point(50f, 84.5f)),
            ),
        ),
    ),
    /**
     * − (U+2212, aspect 3.977) is far wider than tall, so it is fitted by width. One `bar` on its
     * midline, a whole 14 spacings, 5 units inside each flat end.
     */
    Exercise(
        id = "math-minus",
        title = "−",
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        order = 22,
        strokes = listOf(
            Stroke(
                id = "math-minus-bar",
                points = StrokePoints.line(Point(8f, 50f), Point(92f, 50f)),
            ),
        ),
    ),
    /**
     * × (aspect 1.000) is two 45-degree diagonals crossing at (50, 48.5): `down` from the top left,
     * then `cross` from the top right, each 7 spacings either side of the crossing, so both put a
     * dot on it.
     */
    Exercise(
        id = "math-times",
        title = "×",
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        order = 23,
        strokes = listOf(
            Stroke(
                id = "math-times-down",
                points = StrokePoints.line(Point(20.3f, 18.8f), Point(79.7f, 78.2f)),
            ),
            Stroke(
                id = "math-times-cross",
                points = StrokePoints.line(Point(79.7f, 18.8f), Point(20.3f, 78.2f)),
            ),
        ),
    ),
    /**
     * ÷ (aspect 0.880): `bar` on its midline (y 48.5), a whole 11 spacings, then `top-dot` and
     * `bottom-dot`, each a ring inside its disc (the dot rule of 1.22), 8 spacings round.
     */
    Exercise(
        id = "math-divide",
        title = "÷",
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        order = 24,
        strokes = listOf(
            Stroke(
                id = "math-divide-bar",
                points = StrokePoints.line(Point(17f, 48.5f), Point(83f, 48.5f)),
            ),
            Stroke(
                id = "math-divide-top-dot",
                points = StrokePoints.arc(
                    center = Point(50f, 19.41f),
                    radius = 7.66f,
                    startDeg = -90f,
                    sweepDeg = -360f,
                    samples = 32,
                ),
            ),
            Stroke(
                id = "math-divide-bottom-dot",
                points = StrokePoints.arc(
                    center = Point(50f, 77.59f),
                    radius = 7.66f,
                    startDeg = -90f,
                    sweepDeg = -360f,
                    samples = 32,
                ),
            ),
        ),
    ),
    /**
     * = (aspect 1.367) is wider than tall, so it is fitted by width: `top` then `bottom`, each on
     * its midline and a whole 14 spacings, 5 units inside the flat ends.
     */
    Exercise(
        id = "math-equals",
        title = "=",
        type = ExerciseType.MATH,
        difficulty = Difficulty.BEGINNER,
        order = 25,
        strokes = listOf(
            Stroke(
                id = "math-equals-top",
                points = StrokePoints.line(Point(8f, 27.5f), Point(92f, 27.5f)),
            ),
            Stroke(
                id = "math-equals-bottom",
                points = StrokePoints.line(Point(8f, 72.5f), Point(92f, 72.5f)),
            ),
        ),
    ),
)

private val digitGuides: Map<Char, DigitGuide> = mapOf(
    '1' to digitOne, '2' to digitTwo, '3' to digitThree, '4' to digitFour, '5' to digitFive,
    '6' to digitSix, '7' to digitSeven, '8' to digitEight, '9' to digitNine,
)

/**
 * Inter's two-digit numbers: 0.69 is the largest one size at which the widest, 20, fits x 3..97, and
 * every digit is fitted with its ink on y 7..90, so 48.5 is its vertical middle.
 */
private val interNumberComposer = NumberComposer(twoDigitScale = 0.69f, inkCentreY = 48.5f)

/** The digits as the two-digit numbers draw them, at [interNumberComposer]'s scale. */
private val twoDigitGuides: Map<Char, DigitGuide> = mapOf(
    '0' to twoDigitZero, '1' to twoDigitOne, '2' to twoDigitTwo, '3' to twoDigitThree, '4' to twoDigitFour,
    '5' to twoDigitFive, '6' to twoDigitSix, '7' to twoDigitSeven, '8' to twoDigitEight, '9' to twoDigitNine,
)

/**
 * Math stroke geometry (plan.md Step 4): the numbers 1-20 and the operators + − × ÷ =, ordered by
 * value so a run of numbers can be read straight off the catalog.
 *
 * Font: **Inter Bold** — the same pinned instance (wght 700, opsz 14) as the English capitals,
 * `BcLatinCapitalFontFamily` — so the reference glyph above the canvas is the face every guide is
 * read from. Each digit and operator is read off its rendered glyph's centreline, never drawn by eye,
 * and fitted on its own (y 7..90, or by width for a glyph wider than tall). 10-20 are not derived:
 * [NumberComposer] sets two digit guides side by side as Inter sets the number, from a second set of
 * guides derived for its scale, so 0 exists only as a [DigitGuide]. Declared last, because a file's
 * top-level values are initialised in order.
 */
internal val mathExercises: List<Exercise> =
    (1..9).map(::digitExercise) + (10..20).map(::numberExercise) + mathOperatorExercises
