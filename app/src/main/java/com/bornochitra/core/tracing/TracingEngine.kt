package com.bornochitra.core.tracing

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Stroke

/**
 * Reusable domain-level façade over [MultiStrokeTracker] and [TraceScoreCalculator] — the
 * "domain engine" deliverable of plan.md Step 10.11. Bundles stroke sequencing and scoring
 * behind one small, UI-agnostic API so a driving ViewModel (plan.md Step 11's PracticeViewModel)
 * can run an exercise attempt without reaching into tracker/calculator internals directly.
 * Stays independent of ViewModel/Room/Hilt/Compose per plan.md section 4.
 */
class TracingEngine(
    exercise: Exercise,
    tolerance: TracingTolerance = TracingTolerance(),
    completionThreshold: Float = MultiStrokeTracker.DEFAULT_COMPLETION_THRESHOLD,
    private val scoreCalculator: TraceScoreCalculator = TraceScoreCalculator(tolerance = tolerance),
) {

    private val tracker = MultiStrokeTracker(exercise, tolerance, completionThreshold)

    val totalStrokes: Int = exercise.strokes.size

    /** The stroke the child is currently expected to trace, or null once the exercise is complete. */
    val currentStroke: Stroke? get() = tracker.currentStroke

    /** 1-based index of [currentStroke] within the exercise, clamped to [totalStrokes]. */
    val currentStrokeNumber: Int get() = (tracker.currentStrokeIndex + 1).coerceAtMost(totalStrokes)

    val isExerciseCompleted: Boolean get() = tracker.isSequenceCompleted

    fun onStart(point: TracePoint) = tracker.onStart(point)

    fun onMove(point: TracePoint) = tracker.onMove(point)

    fun onCancel() = tracker.onCancel()

    /** Ends the current stroke attempt and reports what happened: retry, advance, or exercise complete with a score. */
    fun onEnd(): TracingAttemptOutcome {
        tracker.onEnd()

        if (tracker.isSequenceCompleted) {
            val score = scoreCalculator.scoreTrace(tracker.toTraceResult())
            return TracingAttemptOutcome.ExerciseCompleted(score = score, level = scoreCalculator.scoreLevel(score))
        }

        val attempt = tracker.lastAttemptResult ?: return TracingAttemptOutcome.NoAttempt
        return TracingAttemptOutcome.StrokeAttempted(isCompleted = attempt.isCompleted)
    }
}
