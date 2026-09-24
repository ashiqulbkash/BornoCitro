package com.bornochitra.core.recognition

import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.tracing.TracePoint

/** The handwriting a category is written in, as a BCP-47 tag for the recognizer's model. */
enum class WritingScript(val languageTag: String) {
    BENGALI("bn"),
    LATIN("en"),
}

/** Null for drawings, which are shapes rather than writing. */
val ExerciseType.writingScript: WritingScript?
    get() = when (this) {
        ExerciseType.VOWEL, ExerciseType.CONSONANT, ExerciseType.BANGLA_NUMBER -> WritingScript.BENGALI
        ExerciseType.ENGLISH_SMALL, ExerciseType.ENGLISH_CAPITAL, ExerciseType.MATH -> WritingScript.LATIN
        ExerciseType.DRAWING -> null
    }

/** Reads handwriting, [ink] being one list of points per stroke on the 0..100 canvas. */
interface InkRecognizer {

    /** Whether [script]'s model is on the device, so [recognize] can read it. */
    suspend fun isModelReady(script: WritingScript): Boolean

    /** Downloads [script]'s model if it is not on the device yet; false if that failed. */
    suspend fun downloadModel(script: WritingScript): Boolean

    /** Candidate texts, best first, or null when [script] cannot be read right now (its model is not downloaded yet). */
    suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String>?
}
