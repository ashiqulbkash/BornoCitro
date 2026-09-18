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

/**
 * Validates plan.md Steps 10.8-10.10's checklist for real exercise content — the Bengali letters
 * "অ", "আ", "ই", "ঈ", "উ" and "ঊ" (plan.md Steps 11.1-11.5) and "ক", plus the non-letter "Circle"
 * drawing — by running each through [TracingEngine], the same reusable component
 * [LetterTracingPrototype] drives (plan.md Step 10.11). "ক" has straight/angular strokes
 * structurally different from "অ"'s curved loop, and "Circle" is not a letter at all, so passing
 * them all proves the engine is not accidentally specialized for one shape or for letters
 * specifically. Dotted rendering and live finger interaction can only be checked visually/manually
 * via [LetterTracingPrototype]'s previews and a physical device — see the post-task brief.
 */
class LetterTracingPrototypeTest {

    private val vowelO = vowelExercises.first { it.id == "vowel-o" }
    private val vowelAa = vowelExercises.first { it.id == "vowel-aa" }
    private val vowelI = vowelExercises.first { it.id == "vowel-i" }
    private val vowelIi = vowelExercises.first { it.id == "vowel-ii" }
    private val vowelU = vowelExercises.first { it.id == "vowel-u" }
    private val vowelUu = vowelExercises.first { it.id == "vowel-uu" }
    private val consonantKo = consonantExercises.first { it.id == "consonant-ko" }
    private val drawingCircle = drawingExercises.first { it.id == "drawing-circle" }

    private fun tracePoint(point: Point) = TracePoint(point.x, point.y, timestampMs = 0L)

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
    fun `vowel-aa ends with a whole-letter pass`() {
        assertEquals(
            listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER),
            TracingEngine(vowelAa).phases,
        )
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
    fun `vowel-i ends with a whole-letter pass`() {
        assertEquals(
            listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER),
            TracingEngine(vowelI).phases,
        )
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
    fun `vowel-ii ends with a whole-letter pass`() {
        assertEquals(
            listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER),
            TracingEngine(vowelIi).phases,
        )
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
    fun `vowel-u ends with a whole-letter pass`() {
        assertEquals(
            listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER),
            TracingEngine(vowelU).phases,
        )
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
    fun `vowel-uu ends with a whole-letter pass`() {
        assertEquals(
            listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER),
            TracingEngine(vowelUu).phases,
        )
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
    fun `consonant-ko has exactly one stroke starting at its documented point`() {
        assertEquals(1, consonantKo.strokes.size)
        assertEquals(Point(30f, 15f), consonantKo.strokes.single().points.first())
    }

    @Test
    fun `faithfully tracing consonant-ko's real stroke path completes the sequence with a perfect score`() {
        // consonant-ko is built from straight diagonal/vertical segments, unlike vowel-o's curved
        // loop, so this proves the engine is not accidentally specialized for one stroke shape.
        assertFaithfulTraceIsPerfect(consonantKo)
    }

    @Test
    fun `tracing far from consonant-ko's guide path does not complete the exercise`() {
        assertOffPathTraceDoesNotComplete(consonantKo)
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

        // Every pass traces the same stroke sequence: stroke by stroke, then the whole letter.
        repeat(engine.phases.size) {
            for (stroke in exercise.strokes) {
                engine.onStart(tracePoint(stroke.points.first()))
                stroke.points.drop(1).forEach { engine.onMove(tracePoint(it)) }
                outcome = engine.onEnd()
            }
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
