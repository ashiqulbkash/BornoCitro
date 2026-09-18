package com.bornochitra.core.tracing

import com.bornochitra.core.content.consonantExercises
import com.bornochitra.core.content.drawingExercises
import com.bornochitra.core.content.vowelExercises
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.hypot

/**
 * Validates plan.md Steps 10.8-10.10's checklist for real exercise content — the Bengali letters
 * every vowel (plan.md Steps 11.1-11.10) and "ক" (plan.md Step 11.11), plus the non-letter
 * "Circle" drawing — by running each through [TracingEngine], the same reusable component
 * [LetterTracingPrototype] drives (plan.md Step 10.11). "ক" is structurally different from "অ" — a
 * knot that reverses at a sharp point, a straight stem and a curled lobe rather than a closed loop —
 * and "Circle" is not a letter at all, so passing them all proves the engine is not accidentally
 * specialized for one shape or for letters specifically. Dotted rendering and live finger interaction can only be checked visually/manually
 * via [LetterTracingPrototype]'s previews and a physical device — see the post-task brief.
 */
class LetterTracingPrototypeTest {

    private val vowelO = vowelExercises.first { it.id == "vowel-o" }
    private val vowelAa = vowelExercises.first { it.id == "vowel-aa" }
    private val vowelI = vowelExercises.first { it.id == "vowel-i" }
    private val vowelIi = vowelExercises.first { it.id == "vowel-ii" }
    private val vowelU = vowelExercises.first { it.id == "vowel-u" }
    private val vowelUu = vowelExercises.first { it.id == "vowel-uu" }
    private val vowelRi = vowelExercises.first { it.id == "vowel-ri" }
    private val vowelE = vowelExercises.first { it.id == "vowel-e" }
    private val vowelOi = vowelExercises.first { it.id == "vowel-oi" }
    private val vowelOa = vowelExercises.first { it.id == "vowel-oa" }
    private val vowelAu = vowelExercises.first { it.id == "vowel-au" }
    private val consonantKo = consonantExercises.first { it.id == "consonant-ko" }
    private val consonantKho = consonantExercises.first { it.id == "consonant-kho" }
    private val consonantGo = consonantExercises.first { it.id == "consonant-go" }
    private val drawingCircle = drawingExercises.first { it.id == "drawing-circle" }

    private fun tracePoint(point: Point) = TracePoint(point.x, point.y, timestampMs = 0L)

    private fun Point.distanceTo(other: Point) = hypot(x - other.x, y - other.y)

    @Test
    fun `vowel-o is traced as bowl, stem and matra, in that order`() {
        assertEquals(
            listOf("vowel-o-body", "vowel-o-stem", "vowel-o-matra"),
            vowelO.strokes.map { it.id },
        )
        assertEquals(Point(10f, 40f), vowelO.strokes.first().points.first())
    }

    @Test
    fun `faithfully tracing vowel-o's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelO)
    }

    @Test
    fun `tracing far from vowel-o's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelO)
    }

    @Test
    fun `vowel-aa is traced as bowl, stem, kar and matra, in that order`() {
        assertEquals(
            listOf("vowel-aa-body", "vowel-aa-stem", "vowel-aa-kar", "vowel-aa-matra"),
            vowelAa.strokes.map { it.id },
        )
        assertEquals(Point(10f, 35f), vowelAa.strokes.first().points.first())
    }

    @Test
    fun `vowel-aa shows every stroke's guide at once`() {
        assertEquals(vowelAa.strokes, TracingEngine(vowelAa).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-aa's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelAa)
    }

    @Test
    fun `tracing far from vowel-aa's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelAa)
    }

    @Test
    fun `vowel-i is traced as hook, body and matra, in that order`() {
        assertEquals(
            listOf("vowel-i-hook", "vowel-i-body", "vowel-i-matra"),
            vowelI.strokes.map { it.id },
        )
        assertEquals(Point(23f, 6f), vowelI.strokes.first().points.first())
    }

    @Test
    fun `vowel-i shows every stroke's guide at once`() {
        assertEquals(vowelI.strokes, TracingEngine(vowelI).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-i's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelI)
    }

    @Test
    fun `tracing far from vowel-i's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelI)
    }

    @Test
    fun `vowel-ii is traced as hook, body, tail and matra, in that order`() {
        assertEquals(
            listOf("vowel-ii-hook", "vowel-ii-body", "vowel-ii-tail", "vowel-ii-matra"),
            vowelIi.strokes.map { it.id },
        )
        assertEquals(Point(26f, 6f), vowelIi.strokes.first().points.first())
    }

    @Test
    fun `vowel-ii's tail leaves the body's bottom sweep and ends below it`() {
        // What separates ঈ from ই: the tail starts where the body's sweep passes, rises to its
        // apex and then drops into the descender, rather than running out to the bottom right.
        val body = vowelIi.strokes.first { it.id == "vowel-ii-body" }.points
        val tail = vowelIi.strokes.first { it.id == "vowel-ii-tail" }.points
        assertTrue(tail.first() in body)
        assertTrue(tail.minOf { it.y } < body.maxOf { it.y })
        assertTrue(tail.last().y > body.maxOf { it.y })
    }

    @Test
    fun `vowel-ii shows every stroke's guide at once`() {
        assertEquals(vowelIi.strokes, TracingEngine(vowelIi).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-ii's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelIi)
    }

    @Test
    fun `tracing far from vowel-ii's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelIi)
    }

    @Test
    fun `vowel-u is traced as hook, stem, bowl and matra, in that order`() {
        assertEquals(
            listOf("vowel-u-hook", "vowel-u-stem", "vowel-u-bowl", "vowel-u-matra"),
            vowelU.strokes.map { it.id },
        )
        assertEquals(Point(26f, 7f), vowelU.strokes.first().points.first())
    }

    @Test
    fun `vowel-u's stem and bowl finish at the same tip`() {
        // The glyph butts the two together there, so the guides have to meet rather than overlap.
        val stem = vowelU.strokes.first { it.id == "vowel-u-stem" }.points
        val bowl = vowelU.strokes.first { it.id == "vowel-u-bowl" }.points
        assertEquals(stem.last(), bowl.last())
    }

    @Test
    fun `vowel-u shows every stroke's guide at once`() {
        assertEquals(vowelU.strokes, TracingEngine(vowelU).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-u's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelU)
    }

    @Test
    fun `tracing far from vowel-u's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelU)
    }

    @Test
    fun `vowel-uu is traced as hook, stem, bowl, arm and matra, in that order`() {
        assertEquals(
            listOf("vowel-uu-hook", "vowel-uu-stem", "vowel-uu-bowl", "vowel-uu-arm", "vowel-uu-matra"),
            vowelUu.strokes.map { it.id },
        )
        assertEquals(Point(26f, 7f), vowelUu.strokes.first().points.first())
    }

    @Test
    fun `vowel-uu's stem and bowl finish at the same tip`() {
        // ঊ is built like উ, so the glyph butts the two together at the same blunt tip.
        val stem = vowelUu.strokes.first { it.id == "vowel-uu-stem" }.points
        val bowl = vowelUu.strokes.first { it.id == "vowel-uu-bowl" }.points
        assertEquals(stem.last(), bowl.last())
    }

    @Test
    fun `vowel-uu's arm hangs inside the bowl and ends on it`() {
        // What separates ঊ from উ: a second arm below the matra, running down inside the bowl's own
        // arm and joining it where the bottom sweep begins.
        val bowl = vowelUu.strokes.first { it.id == "vowel-uu-bowl" }.points
        val arm = vowelUu.strokes.first { it.id == "vowel-uu-arm" }.points
        assertTrue(arm.last() in bowl)
        assertTrue(arm.first().x > bowl.first().x)
        assertTrue(arm.maxOf { it.y } < bowl.maxOf { it.y })
    }

    @Test
    fun `vowel-uu shows every stroke's guide at once`() {
        assertEquals(vowelUu.strokes, TracingEngine(vowelUu).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-uu's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelUu)
    }

    @Test
    fun `tracing far from vowel-uu's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelUu)
    }

    @Test
    fun `vowel-ri is traced as curl, diagonal, sweep, stem, hook and arm, in that order`() {
        assertEquals(
            listOf(
                "vowel-ri-curl",
                "vowel-ri-diagonal",
                "vowel-ri-sweep",
                "vowel-ri-stem",
                "vowel-ri-hook",
                "vowel-ri-arm",
            ),
            vowelRi.strokes.map { it.id },
        )
        assertEquals(Point(16f, 25f), vowelRi.strokes.first().points.first())
    }

    @Test
    fun `vowel-ri has no matra`() {
        // The first vowel whose glyph carries no headline, so nothing ties its parts along the top.
        assertTrue(vowelRi.strokes.none { it.id.endsWith("-matra") })
    }

    @Test
    fun `vowel-ri's knot is one path split at its two reversals`() {
        // Curl, diagonal and sweep are a single handwriting movement; each picks up where the last
        // one stopped, and the split points are the peak's vertex and the knot's sharp left point.
        val curl = vowelRi.strokes.first { it.id == "vowel-ri-curl" }.points
        val diagonal = vowelRi.strokes.first { it.id == "vowel-ri-diagonal" }.points
        val sweep = vowelRi.strokes.first { it.id == "vowel-ri-sweep" }.points
        assertEquals(curl.last(), diagonal.first())
        assertEquals(diagonal.last(), sweep.first())
        assertEquals(diagonal.last().x, vowelRi.strokes.minOf { stroke -> stroke.points.minOf { it.x } }, 0f)
    }

    @Test
    fun `vowel-ri's knot and hook both end on the stroke that follows them`() {
        val sweep = vowelRi.strokes.first { it.id == "vowel-ri-sweep" }.points
        val stem = vowelRi.strokes.first { it.id == "vowel-ri-stem" }.points
        val hook = vowelRi.strokes.first { it.id == "vowel-ri-hook" }.points
        val arm = vowelRi.strokes.first { it.id == "vowel-ri-arm" }.points
        assertTrue(sweep.last() in stem)
        assertTrue(hook.first() in stem)
        assertEquals(arm.last().x, hook.last().x, 1f)
        assertTrue(hook.last().y < arm.last().y)
    }

    @Test
    fun `vowel-ri shows every stroke's guide at once`() {
        assertEquals(vowelRi.strokes, TracingEngine(vowelRi).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-ri's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelRi)
    }

    @Test
    fun `tracing far from vowel-ri's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelRi)
    }

    @Test
    fun `vowel-e is traced as curl, stem and base, in that order`() {
        assertEquals(
            listOf("vowel-e-curl", "vowel-e-stem", "vowel-e-base"),
            vowelE.strokes.map { it.id },
        )
        assertEquals(Point(54f, 45f), vowelE.strokes.first().points.first())
    }

    @Test
    fun `vowel-e has no matra`() {
        // Like ঋ, the glyph carries no headline — the curl's own arch closes the letter's top.
        assertTrue(vowelE.strokes.none { it.id.endsWith("-matra") })
    }

    @Test
    fun `vowel-e's curl spirals outward from the terminal and hands over to the stem`() {
        // The curl starts inside the round terminal, so its first point is the innermost one, and
        // it ends where the arch turns down — exactly where the stem picks up.
        val curl = vowelE.strokes.first { it.id == "vowel-e-curl" }.points
        val stem = vowelE.strokes.first { it.id == "vowel-e-stem" }.points
        assertEquals(curl.last(), stem.first())
        assertTrue(curl.first().y > curl.minOf { it.y })
        assertTrue(curl.maxOf { it.x } > curl.first().x)
    }

    @Test
    fun `vowel-e's base runs from its own terminal to the stem's foot`() {
        val stem = vowelE.strokes.first { it.id == "vowel-e-stem" }.points
        val base = vowelE.strokes.first { it.id == "vowel-e-base" }.points
        assertEquals(base.first().x, vowelE.strokes.minOf { stroke -> stroke.points.minOf { it.x } }, 1f)
        assertTrue(base.last() in stem)
    }

    @Test
    fun `vowel-e shows every stroke's guide at once`() {
        assertEquals(vowelE.strokes, TracingEngine(vowelE).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-e's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelE)
    }

    @Test
    fun `tracing far from vowel-e's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelE)
    }

    @Test
    fun `vowel-oi is traced as curl, stem, base and kar, in that order`() {
        assertEquals(
            listOf("vowel-oi-curl", "vowel-oi-stem", "vowel-oi-base", "vowel-oi-kar"),
            vowelOi.strokes.map { it.id },
        )
        assertEquals(Point(45f, 59f), vowelOi.strokes.first().points.first())
    }

    @Test
    fun `vowel-oi's kar is written last and lands on the head of the stem`() {
        // ঐ is এ plus its কার, and a mark goes on after the body it belongs to.
        val stem = vowelOi.strokes.first { it.id == "vowel-oi-stem" }.points
        val curl = vowelOi.strokes.first { it.id == "vowel-oi-curl" }.points
        val kar = vowelOi.strokes.last()
        assertEquals("vowel-oi-kar", kar.id)
        assertEquals(stem.first(), kar.points.last())
        assertEquals(stem.first(), curl.last())
        assertTrue(kar.points.first().y < curl.minOf { it.y })
    }

    @Test
    fun `vowel-oi carries its own geometry rather than এ's`() {
        // The কার takes the top of the square, so ঐ's body sits lower than এ's — sharing a shape
        // between the two is the defect plan.md Step 11.8 exists to remove.
        val oiBody = vowelOi.strokes.filterNot { it.id == "vowel-oi-kar" }
        assertTrue(oiBody.map { it.points } != vowelE.strokes.map { it.points })
        assertTrue(oiBody.minOf { stroke -> stroke.points.minOf { it.y } } > vowelE.strokes.minOf { stroke -> stroke.points.minOf { it.y } })
    }

    @Test
    fun `vowel-oi's base runs from its own terminal to the stem's foot`() {
        val stem = vowelOi.strokes.first { it.id == "vowel-oi-stem" }.points
        val base = vowelOi.strokes.first { it.id == "vowel-oi-base" }.points
        assertEquals(base.first().x, vowelOi.strokes.minOf { stroke -> stroke.points.minOf { it.x } }, 1f)
        assertTrue(base.last() in stem)
    }

    @Test
    fun `vowel-oi shows every stroke's guide at once`() {
        assertEquals(vowelOi.strokes, TracingEngine(vowelOi).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-oi's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelOi)
    }

    @Test
    fun `tracing far from vowel-oi's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelOi)
    }

    @Test
    fun `vowel-oa is traced as curl then sweep`() {
        assertEquals(
            listOf("vowel-oa-curl", "vowel-oa-sweep"),
            vowelOa.strokes.map { it.id },
        )
        assertEquals(Point(46f, 39f), vowelOa.strokes.first().points.first())
    }

    @Test
    fun `vowel-oa has no matra`() {
        // The upper lobe's own arch closes the letter's top; there is no headline to add.
        assertTrue(vowelOa.strokes.none { it.id.endsWith("-matra") })
    }

    @Test
    fun `vowel-oa's curl ends where the sweep passes between the two lobes`() {
        val curl = vowelOa.strokes.first { it.id == "vowel-oa-curl" }.points
        val sweep = vowelOa.strokes.first { it.id == "vowel-oa-sweep" }.points
        assertTrue(curl.last() in sweep)
    }

    @Test
    fun `vowel-oa's sweep starts at the cut tail and finishes inside the lower lobe`() {
        // The tail's tip is the letter's top-left corner, and the sweep ends at the ball terminal —
        // left of, and below, the point where it turned in off the right-hand side.
        val sweep = vowelOa.strokes.first { it.id == "vowel-oa-sweep" }.points
        assertEquals(Point(9f, 24f), sweep.first())
        assertEquals(Point(73f, 48f), sweep.last())
        assertTrue(sweep.last().x < sweep.maxOf { it.x })
        assertTrue(sweep.last().y < sweep.maxOf { it.y })
    }

    @Test
    fun `vowel-oa shows every stroke's guide at once`() {
        assertEquals(vowelOa.strokes, TracingEngine(vowelOa).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-oa's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelOa)
    }

    @Test
    fun `tracing far from vowel-oa's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelOa)
    }

    @Test
    fun `vowel-au is traced as curl, sweep and kar, in that order`() {
        assertEquals(
            listOf("vowel-au-curl", "vowel-au-sweep", "vowel-au-kar"),
            vowelAu.strokes.map { it.id },
        )
        assertEquals(Point(40f, 56f), vowelAu.strokes.first().points.first())
    }

    @Test
    fun `vowel-au's kar is written last and ends where the curl ends`() {
        // ঔ is ও plus its কার, and a mark goes on after the body it belongs to.
        val curl = vowelAu.strokes.first { it.id == "vowel-au-curl" }.points
        val kar = vowelAu.strokes.last()
        assertEquals("vowel-au-kar", kar.id)
        assertEquals(curl.last(), kar.points.last())
        assertTrue(kar.points.first().y < curl.minOf { it.y })
    }

    @Test
    fun `vowel-au carries its own geometry rather than ও's`() {
        // The কার takes the top of the square, so ঔ's body sits lower than ও's — sharing a shape
        // between the two is the defect plan.md Step 11.10 exists to remove.
        val auBody = vowelAu.strokes.filterNot { it.id == "vowel-au-kar" }
        assertTrue(auBody.map { it.points } != vowelOa.strokes.map { it.points })
        assertTrue(auBody.minOf { stroke -> stroke.points.minOf { it.y } } > vowelOa.strokes.minOf { stroke -> stroke.points.minOf { it.y } })
    }

    @Test
    fun `vowel-au's sweep starts at the cut tail and finishes inside the lower lobe`() {
        val sweep = vowelAu.strokes.first { it.id == "vowel-au-sweep" }.points
        assertEquals(Point(16f, 46f), sweep.first())
        assertEquals(Point(58f, 62f), sweep.last())
        assertTrue(sweep.last().x < sweep.maxOf { it.x })
        assertTrue(sweep.last().y < sweep.maxOf { it.y })
    }

    @Test
    fun `vowel-au shows every stroke's guide at once`() {
        assertEquals(vowelAu.strokes, TracingEngine(vowelAu).guideStrokes)
    }

    @Test
    fun `faithfully tracing vowel-au's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(vowelAu)
    }

    @Test
    fun `tracing far from vowel-au's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(vowelAu)
    }

    @Test
    fun `every vowel is traced with geometry derived from its own glyph`() {
        // plan.md Steps 11.1-11.10 close out the placeholder content: no two vowels share a body,
        // and no vowel is still a handful of eyeballed points.
        val bodies = vowelExercises.map { exercise -> exercise.strokes.map { it.points } }
        assertEquals(bodies.size, bodies.distinct().size)
        vowelExercises.forEach { exercise ->
            assertTrue(
                "${exercise.id} has too few waypoints to be glyph-derived",
                exercise.strokes.sumOf { it.points.size } >= 60,
            )
        }
    }

    @Test
    fun `consonant-ko is traced as knot, stem, lobe and matra, in that order`() {
        assertEquals(
            listOf("consonant-ko-knot", "consonant-ko-stem", "consonant-ko-lobe", "consonant-ko-matra"),
            consonantKo.strokes.map { it.id },
        )
        assertEquals(Point(56f, 32f), consonantKo.strokes.first().points.first())
    }

    @Test
    fun `consonant-ko's knot doubles back at the letter's blunt left point`() {
        // The knot is one movement: down-left from the stem to the point, then down-right to the
        // foot. Splitting it in two would teach a pen lift the letter does not have.
        val knot = consonantKo.strokes.first().points
        val leftmost = knot.minByOrNull { it.x }!!
        assertTrue(knot.first().x > leftmost.x && knot.last().x > leftmost.x)
        assertTrue(knot.first().y < leftmost.y && knot.last().y > leftmost.y)
    }

    @Test
    fun `consonant-ko's matra is written last and spans the whole letter`() {
        val matra = consonantKo.strokes.last()
        assertEquals("consonant-ko-matra", matra.id)
        val body = consonantKo.strokes.dropLast(1).flatMap { it.points }
        assertEquals(matra.points.map { it.y }.distinct(), listOf(body.minOf { it.y }))
        assertTrue(matra.points.first().x < body.minOf { it.x })
        assertTrue(matra.points.last().x > body.maxOf { it.x })
    }

    @Test
    fun `consonant-ko shows every stroke's guide at once`() {
        assertEquals(consonantKo.strokes, TracingEngine(consonantKo).guideStrokes)
    }

    @Test
    fun `consonant-ko fits the canvas despite being wider than it is tall`() {
        // Fitting ক by height the way the tall vowels are fitted would run its matra past x=100.
        val points = consonantKo.strokes.flatMap { it.points }
        assertTrue(points.all { it.x in 0f..100f && it.y in 0f..100f })
        val width = points.maxOf { it.x } - points.minOf { it.x }
        val height = points.maxOf { it.y } - points.minOf { it.y }
        assertTrue("expected a wide letter, was ${width}x$height", width > height)
    }

    @Test
    fun `faithfully tracing consonant-ko's real stroke path completes the sequence with a perfect score`() {
        // ক pairs a sharp reversal at the left point and a straight stem with a curled lobe, a
        // different structure from vowel-o's loop, so this proves the engine is not accidentally
        // specialized for one stroke shape.
        assertFaithfulTraceIsPerfect(consonantKo)
    }

    @Test
    fun `tracing far from consonant-ko's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(consonantKo)
    }

    @Test
    fun `consonant-kho is traced as curl, body, stem and matra, in that order`() {
        assertEquals(
            listOf(
                "consonant-kho-curl",
                "consonant-kho-body",
                "consonant-kho-stem",
                "consonant-kho-matra",
            ),
            consonantKho.strokes.map { it.id },
        )
        assertEquals(Point(50f, 15f), consonantKho.strokes.first().points.first())
    }

    @Test
    fun `consonant-kho's curl ends in the ball terminal at the top left`() {
        // The ball is where the pen comes to rest, so the curl wraps inward and stops there rather
        // than starting from it.
        val curl = consonantKho.strokes.first().points
        assertTrue(curl.last().x < curl.first().x)
        assertTrue(curl.last().x < curl.maxOf { it.x } - 20f)
        assertTrue(curl.last().y < curl.maxOf { it.y })
    }

    @Test
    fun `consonant-kho's body starts at the same peak as the curl and ends at the stem's foot`() {
        val curl = consonantKho.strokes.first().points
        val body = consonantKho.strokes[1].points
        val stem = consonantKho.strokes[2].points
        assertTrue(body.first().distanceTo(curl.first()) < 10f)
        assertTrue(body.last().distanceTo(stem.last()) < 10f)
    }

    @Test
    fun `consonant-kho's matra is written last and covers only the stem's right side`() {
        // খ's headline bar really is short — the curl takes the top left, so no bar crosses there.
        val matra = consonantKho.strokes.last()
        assertEquals("consonant-kho-matra", matra.id)
        val stem = consonantKho.strokes[2].points
        assertTrue(matra.points.minOf { it.x } >= stem.minOf { it.x } - 1f)
        assertTrue(matra.points.maxOf { it.x } > stem.maxOf { it.x })
        assertEquals(matra.points.map { it.y }.distinct().size, 1)
    }

    @Test
    fun `consonant-kho shows every stroke's guide at once`() {
        assertEquals(consonantKho.strokes, TracingEngine(consonantKho).guideStrokes)
    }

    @Test
    fun `faithfully tracing consonant-kho's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(consonantKho)
    }

    @Test
    fun `tracing far from consonant-kho's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(consonantKho)
    }

    @Test
    fun `consonant-go is traced as body, stem and matra, in that order`() {
        assertEquals(
            listOf("consonant-go-body", "consonant-go-stem", "consonant-go-matra"),
            consonantGo.strokes.map { it.id },
        )
        assertEquals(Point(70f, 34f), consonantGo.strokes.first().points.first())
    }

    @Test
    fun `consonant-go's body is one movement from the stem round to the hook's terminal`() {
        // The hairpin at the sharp left vertex is a turn, not a pen lift, so the arch and the hook
        // belong to the same stroke.
        val body = consonantGo.strokes.first().points
        val stem = consonantGo.strokes[1].points
        assertTrue(stem.any { it.distanceTo(body.first()) < 6f })
        val vertex = body.minByOrNull { it.x }!!
        assertTrue(body.first().x > vertex.x && body.last().x > vertex.x)
        assertTrue(body.last().y > vertex.y)
    }

    @Test
    fun `consonant-go's arch reaches the top of the letter before the hook drops below it`() {
        val body = consonantGo.strokes.first().points
        val apex = body.minByOrNull { it.y }!!
        val vertexIndex = body.indexOf(body.minByOrNull { it.x }!!)
        assertTrue(body.indexOf(apex) < vertexIndex)
        assertTrue(body.last().y > body.maxOf { it.y } - 1f)
    }

    @Test
    fun `consonant-go's matra is written last and covers only the stem's right side`() {
        val matra = consonantGo.strokes.last()
        assertEquals("consonant-go-matra", matra.id)
        val stem = consonantGo.strokes[1].points
        assertTrue(matra.points.minOf { it.x } >= stem.minOf { it.x } - 1f)
        assertTrue(matra.points.maxOf { it.x } > stem.maxOf { it.x })
        assertEquals(1, matra.points.map { it.y }.distinct().size)
    }

    @Test
    fun `consonant-go shows every stroke's guide at once`() {
        assertEquals(consonantGo.strokes, TracingEngine(consonantGo).guideStrokes)
    }

    @Test
    fun `faithfully tracing consonant-go's real stroke path completes the sequence with a perfect score`() {
        assertFaithfulTraceIsPerfect(consonantGo)
    }

    @Test
    fun `tracing far from consonant-go's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(consonantGo)
    }

    @Test
    fun `drawing-circle has exactly one stroke starting at its documented point`() {
        assertEquals(1, drawingCircle.strokes.size)
        assertEquals(Point(85f, 50f), drawingCircle.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing drawing-circle's real stroke path completes the sequence with a perfect score`() {
        // A closed arc loop, not a letter at all - proves the engine generalizes beyond handwriting.
        assertFaithfulTraceIsPerfect(drawingCircle)
    }

    @Test
    fun `tracing far from drawing-circle's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(drawingCircle)
    }

    private fun assertFaithfulTraceIsPerfect(exercise: Exercise) {
        val engine = TracingEngine(exercise)
        var outcome: TracingAttemptOutcome = TracingAttemptOutcome.NoAttempt

        // The whole letter is on screen throughout; its strokes are traced in their teaching order.
        for (stroke in exercise.strokes) {
            engine.onStart(tracePoint(stroke.points.first()))
            stroke.points.drop(1).forEach { engine.onMove(tracePoint(it)) }
            outcome = engine.onEnd()
        }

        assertTrue(engine.isExerciseCompleted)
        require(outcome is TracingAttemptOutcome.ExerciseCompleted) { "expected ExerciseCompleted, was $outcome" }
        assertTrue("score was ${outcome.score}", outcome.score >= 90f)
        assertEquals(ScoreLevel.PERFECT, outcome.level)
    }

    private fun assertOffPathTraceDoesNotComplete(exercise: Exercise) {
        val engine = TracingEngine(exercise)

        engine.onStart(tracePoint(Point(0f, 0f)))
        engine.onMove(tracePoint(Point(5f, 5f)))
        val outcome = engine.onEnd()

        assertEquals(TracingAttemptOutcome.StrokeAttempted(isCompleted = false), outcome)
        assertFalse(engine.isExerciseCompleted)
    }
}
