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
)
