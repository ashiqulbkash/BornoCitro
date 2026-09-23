package com.bornochitra.core.content

import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.model.Point
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NumberComposerTest {

    private fun digit(inkLeft: Float, inkRight: Float, vararg names: String) = DigitGuide(
        inkLeft = inkLeft,
        inkRight = inkRight,
        advanceLeft = inkLeft - 5f,
        advanceRight = inkRight + 5f,
        strokes = names.map { name ->
            DigitStroke(name = name, points = StrokePoints.line(Point(inkLeft + 3f, 10f), Point(inkRight - 3f, 87f)))
        },
    )

    @Test
    fun `strokes keep writing order, tens digit then ones digit, with unique ids`() {
        val strokes = NumberComposer.compose(
            exerciseId = "math-11",
            tens = digit(30f, 70f, "flag", "stem"),
            ones = digit(30f, 70f, "flag", "stem"),
        )

        assertEquals(
            listOf("math-11-tens-flag", "math-11-tens-stem", "math-11-ones-flag", "math-11-ones-stem"),
            strokes.map { it.id },
        )
        val tensRight = strokes.take(2).flatMap { it.points }.maxOf { it.x }
        val onesLeft = strokes.drop(2).flatMap { it.points }.minOf { it.x }
        assertTrue("the ones digit is not to the right of the tens digit", onesLeft > tensRight)
    }

    @Test
    fun `a pair is scaled by the two-digit factor about the ink's middle and centred on the canvas`() {
        val strokes = NumberComposer.compose(
            exerciseId = "math-20",
            tens = digit(20f, 80f, "hook"),
            ones = digit(20f, 80f, "left"),
        )
        val points = strokes.flatMap { it.points }

        assertEquals(50f, (points.minOf { it.x } + points.maxOf { it.x }) / 2, 0.01f)
        assertEquals(48.5f + (10f - 48.5f) * NumberComposer.TWO_DIGIT_SCALE, points.minOf { it.y }, 0.001f)
        assertEquals(48.5f + (87f - 48.5f) * NumberComposer.TWO_DIGIT_SCALE, points.maxOf { it.y }, 0.001f)
        val tensWidth = strokes.first().points.let { it.maxOf { p -> p.x } - it.minOf { p -> p.x } }
        assertEquals((80f - 3f - (20f + 3f)) * NumberComposer.TWO_DIGIT_SCALE, tensWidth, 0.001f)
    }

    @Test
    fun `every composed number in the catalog stays on the canvas and lists the tens strokes first`() {
        ExerciseCatalog.all.filter { it.type == ExerciseType.MATH && it.title.length == 2 }.forEach { number ->
            val places = number.strokes.map { it.id.removePrefix("${number.id}-").substringBefore('-') }

            assertEquals(number.id, places.sortedBy { it != "tens" }, places)
            assertTrue(number.id, places.first() == "tens" && places.last() == "ones")
            number.strokes.flatMap { it.points }.forEach { point ->
                assertTrue("${number.id} leaves the canvas", point.x in 0f..100f && point.y in 0f..100f)
            }
        }
    }
}
