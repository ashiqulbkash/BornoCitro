package com.bornochitra.core.tracing

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Stroke

/**
 * Coordinates tracing across all of an exercise's ordered strokes — the multi-stroke tracking
 * subsystem from plan.md Step 10.6. Wraps a single [TracingSession] and, on every stroke's End
 * event, scores it via [PathCoverageCalculator]/[PathDistanceCalculator]. A stroke only advances
 * the sequence once its coverage meets [completionThreshold]; an attempt that misses the
 * threshold ("incorrect stroke behavior") is discarded without advancing, so the same stroke can
 * simply be retried — starting a new attempt already clears prior points ([TracingSession.onStart]).
 *
 * Independent of ViewModel/Room/Hilt/Compose per plan.md section 4. Turning these results into a
 * learning score is the Score Calculator's job (plan.md section 31), not this class's.
 */
class MultiStrokeTracker(
    private val exercise: Exercise,
    private val tolerance: TracingTolerance = TracingTolerance(),
    private val completionThreshold: Float = DEFAULT_COMPLETION_THRESHOLD,
) {

    init {
        require(exercise.strokes.isNotEmpty()) { "exercise must have at least one stroke" }
    }

    private val session = TracingSession(onEvent = ::onPointerEvent)
    private val mutableCompletedResults = mutableListOf<StrokeTraceResult>()

    var currentStrokeIndex = 0
        private set

    /** The most recently scored attempt, whether or not it advanced the sequence — reflects incomplete/incorrect strokes too. */
    var lastAttemptResult: StrokeTraceResult? = null
        private set

    val completedResults: List<StrokeTraceResult> get() = mutableCompletedResults.toList()

    val currentStroke: Stroke? get() = exercise.strokes.getOrNull(currentStrokeIndex)

    val isSequenceCompleted: Boolean get() = currentStrokeIndex >= exercise.strokes.size

    fun onStart(point: TracePoint) {
        if (isSequenceCompleted) return
        session.onStart(point)
    }

    fun onMove(point: TracePoint) {
        if (isSequenceCompleted) return
        session.onMove(point)
    }

    fun onEnd() {
        if (isSequenceCompleted) return
        session.onEnd()
    }

    fun onCancel() {
        if (isSequenceCompleted) return
        session.onCancel()
    }

    /** Builds the aggregate result once every stroke has met the completion threshold. */
    fun toTraceResult(): TraceResult {
        check(isSequenceCompleted) { "sequence is not complete yet" }
        return TraceResult(
            exerciseId = exercise.id,
            strokeResults = completedResults,
            isCompleted = true,
        )
    }

    private fun onPointerEvent(event: TracingPointerEvent) {
        if (event is TracingPointerEvent.End) scoreCurrentAttempt()
    }

    private fun scoreCurrentAttempt() {
        val stroke = currentStroke ?: return
        val tracedPoints = session.tracedPoints
        val coverage = PathCoverageCalculator.coverage(stroke.points, tracedPoints, tolerance)
        val isCompleted = coverage >= completionThreshold

        val result = StrokeTraceResult(
            strokeId = stroke.id,
            tracePoints = tracedPoints,
            coverage = coverage,
            averageDistance = averageDistance(stroke, tracedPoints),
            isCompleted = isCompleted,
        )
        lastAttemptResult = result

        if (isCompleted) {
            mutableCompletedResults += result
            currentStrokeIndex += 1
        }
    }

    private fun averageDistance(stroke: Stroke, tracedPoints: List<TracePoint>): Float {
        if (tracedPoints.isEmpty()) return 0f
        val total = tracedPoints.sumOf { PathDistanceCalculator.distanceToPath(it, stroke.points).toDouble() }
        return (total / tracedPoints.size).toFloat()
    }

    companion object {
        /** Coverage fraction (see [PathCoverageCalculator]) required to consider a stroke correctly traced. */
        const val DEFAULT_COMPLETION_THRESHOLD = 0.8f
    }
}
