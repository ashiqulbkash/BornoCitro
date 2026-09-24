package com.bornochitra.core.recognition

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.ScoreLevel
import com.bornochitra.core.tracing.TracePoint
import com.bornochitra.core.tracing.TraceScoreCalculator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class FreehandScore(val score: Float, val level: ScoreLevel)

/**
 * Checks writing done from memory, however the writer forms the letter. The downloaded handwriting
 * model is required: without it nothing passes. With it, two readers look at the ink and either one
 * reading it as the expected item is enough:
 *
 * - the model. One character on its own has no word around it, so its first guess is often a letter
 *   from outside the category (৮ written cleanly reads ষ, t, ৮); only its guesses that are items of
 *   the [category] being practised count, and the first of those must be the expected item.
 * - [ShapeMatcher]: the ink must be closest to the expected item's shape of the whole category, and
 *   close enough.
 *
 * The model cannot veto the shape: on the device it misread cleanly written single Bengali letters
 * (এ read as দ, হ, ও, ...), and on its own it filled 9 of 17 correct blanks where the two together
 * filled 22 of 22.
 *
 * Once the ink is read as the item, [FreehandScorer] says how closely it matches the letter's shape.
 */
class FreehandChecker(
    private val recognizer: InkRecognizer,
    private val dispatcher: CoroutineDispatcher,
    private val calculator: TraceScoreCalculator = TraceScoreCalculator(),
) {

    @Inject
    constructor(recognizer: InkRecognizer) : this(recognizer, Dispatchers.Default)

    /** The score for [ink] if it reads as [expected], or null if it does not (yet). */
    suspend fun check(
        ink: List<List<TracePoint>>,
        expected: Exercise,
        category: List<Exercise>,
    ): FreehandScore? {
        val script = expected.type.writingScript ?: return null
        if (ink.none { it.size >= 2 }) return null
        val candidates = recognizer.recognize(ink, script) ?: return null
        return withContext(dispatcher) {
            val firstReadItem = candidates.firstNotNullOfOrNull { candidate ->
                category.firstOrNull { RecognizedText.matches(candidate, it.title) }
            }
            val isRecognized = firstReadItem?.id == expected.id ||
                ShapeMatcher.isRecognisedAs(ink.map { stroke -> stroke.map { Point(it.x, it.y) } }, expected, category)
            if (!isRecognized) return@withContext null
            val score = FreehandScorer.score(ink, expected, calculator)
            FreehandScore(score = score, level = calculator.scoreLevel(score))
        }
    }
}
