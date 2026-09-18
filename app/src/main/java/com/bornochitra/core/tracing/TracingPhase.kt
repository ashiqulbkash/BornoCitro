package com.bornochitra.core.tracing

/** Which pass of an exercise attempt the child is working through. */
enum class TracingPhase {

    /** One stroke is guided and traced at a time, in order. */
    STROKE_BY_STROKE,

    /** Every stroke's guide is shown at once so the whole letter is written in one go. */
    FULL_LETTER,
}

/** Caption telling the child which stroke of which pass they are on. */
fun tracingStepLabel(phase: TracingPhase, strokeNumber: Int, totalStrokes: Int): String = when (phase) {
    TracingPhase.STROKE_BY_STROKE -> "Stroke $strokeNumber of $totalStrokes"
    TracingPhase.FULL_LETTER -> "Whole letter — stroke $strokeNumber of $totalStrokes"
}
