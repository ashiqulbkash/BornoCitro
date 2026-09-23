package com.bornochitra.core.content

import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke

/** One named stroke of a digit's guide, before it belongs to an exercise. */
internal data class DigitStroke(val name: String, val points: List<Point>)

/**
 * A digit's guide in its own fitted canvas, with where its glyph's ink and advance box sit there, so
 * two digits can be set side by side the way the font sets a number.
 */
internal data class DigitGuide(
    val inkLeft: Float,
    val inkRight: Float,
    val advanceLeft: Float,
    val advanceRight: Float,
    val strokes: List<DigitStroke>,
)

/**
 * Builds the guide of a two-digit number from its digits' guides (plan.md Step 4), instead of deriving
 * each number from its own glyph. The ones digit follows the tens digit at the font's advance, as the
 * number is typeset (Inter does not kern digits), and the pair is scaled by [TWO_DIGIT_SCALE] about the
 * ink's vertical middle and centred on the canvas. Strokes keep writing order — the tens digit, then
 * the ones digit.
 *
 * Scaling a guide shortens every stroke, so a guide derived for full size would end between dots. The
 * digits passed in are therefore derived for this scale, with their spacing divided by it, and every
 * composed stroke comes out a whole number of dot spacings.
 */
internal object NumberComposer {

    /** One size for every two-digit number: the largest at which the widest, 20, fits x 3..97. */
    const val TWO_DIGIT_SCALE = 0.69f

    private const val CANVAS_CENTRE_X = 50f

    /** Every digit is fitted with its ink on y 7..90, so this is its vertical middle. */
    private const val INK_CENTRE_Y = 48.5f

    fun compose(exerciseId: String, tens: DigitGuide, ones: DigitGuide): List<Stroke> {
        val onesShift = tens.advanceRight - ones.advanceLeft
        val inkCentreX = (tens.inkLeft + ones.inkRight + onesShift) / 2

        fun DigitGuide.place(place: String, shift: Float) = strokes.map { stroke ->
            Stroke(
                id = "$exerciseId-$place-${stroke.name}",
                points = stroke.points.map { point ->
                    Point(
                        x = CANVAS_CENTRE_X + (point.x + shift - inkCentreX) * TWO_DIGIT_SCALE,
                        y = INK_CENTRE_Y + (point.y - INK_CENTRE_Y) * TWO_DIGIT_SCALE,
                    )
                },
            )
        }

        return tens.place("tens", 0f) + ones.place("ones", onesShift)
    }
}
