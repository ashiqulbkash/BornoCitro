package com.bornochitra.core.tracing

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Stroke

/**
 * Tracks one attempt at a whole letter or drawing — the multi-stroke tracking subsystem from
 * plan.md Step 10.6, reworked for plan.md Step 2: the exercise is one shape, not a sequence of
 * steps. Every point the child traces is kept for the whole attempt, across as many touches as
 * they take, and on each lift every stroke's coverage is measured against all of them via
 * [PathCoverageCalculator]/[PathDistanceCalculator].
 *
 * Two things follow from measuring the shape rather than a step. Lifting the finger keeps the
 * progress made so far, so a new touch continues the attempt instead of restarting it; and the
 * strokes may be traced in any order and split across touches however the child likes, because
 * nothing here expects a particular stroke next.
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
    private val attemptPoints = mutableListOf<TracePoint>()
    private var isTouchInProgress = false

    /** Everything traced so far in this attempt, from every touch that has been lifted. */
    val tracedPoints: List<TracePoint> get() = attemptPoints.toList()

    /** Per-stroke measurements from every point traced so far; empty until the first lift. */
    var strokeResults: List<StrokeTraceResult> = emptyList()
        private set

    /** Whether every stroke of the shape has now been covered, however it was traced. */
    val isCompleted: Boolean get() = strokeResults.isNotEmpty() && strokeResults.all { it.isCompleted }

    fun onStart(point: TracePoint) {
        if (isCompleted) return
        isTouchInProgress = true
        session.onStart(point)
    }

    fun onMove(point: TracePoint) {
        if (!isTouchInProgress) return
        session.onMove(point)
    }

    /** Ends the touch in progress and remeasures the shape; false if there was no touch to end. */
    fun onEnd(): Boolean {
        if (!isTouchInProgress) return false
        isTouchInProgress = false
        session.onEnd()
        return true
    }

    /** The system cancelled the touch in progress; the attempt keeps what earlier touches traced. */
    fun onCancel() {
        if (!isTouchInProgress) return
        isTouchInProgress = false
        session.onCancel()
    }

    /** Builds the aggregate result once the whole shape has been covered. */
    fun toTraceResult(): TraceResult {
        check(isCompleted) { "the shape is not fully traced yet" }
        return TraceResult(
            exerciseId = exercise.id,
            strokeResults = strokeResults,
            isCompleted = true,
            expectedStrokeCount = exercise.strokes.size,
        )
    }

    private fun onPointerEvent(event: TracingPointerEvent) {
        if (event !is TracingPointerEvent.End) return
        attemptPoints += session.tracedPoints
        strokeResults = measureStrokes()
    }

    /**
     * Each stroke's coverage comes from the whole attempt, since a point lands on the guide path it
     * is near whatever the child meant to draw. Accuracy is per stroke, so each point is measured
     * against the stroke it actually falls closest to rather than against all of them.
     */
    private fun measureStrokes(): List<StrokeTraceResult> {
        val pointsByStroke = attemptPoints.groupBy(::nearestStrokeIndex)
        return exercise.strokes.mapIndexed { index, stroke ->
            val strokePoints = pointsByStroke[index].orEmpty()
            val coverage = PathCoverageCalculator.coverage(stroke.points, attemptPoints, tolerance)
            StrokeTraceResult(
                strokeId = stroke.id,
                tracePoints = strokePoints,
                coverage = coverage,
                averageDistance = averageDistance(stroke, strokePoints),
                isCompleted = coverage >= completionThreshold,
            )
        }
    }

    private fun nearestStrokeIndex(point: TracePoint): Int =
        exercise.strokes.indices.minBy { PathDistanceCalculator.distanceToPath(point, exercise.strokes[it].points) }

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
