package com.bornochitra.core.tracing

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Stroke

/**
 * Reusable domain-level façade over [MultiStrokeTracker] and [TraceScoreCalculator] — the
 * "domain engine" deliverable of plan.md Step 10.11. Bundles stroke sequencing and scoring
 * behind one small, UI-agnostic API so a driving ViewModel (plan.md Step 11's PracticeViewModel)
 * can run an exercise attempt without reaching into tracker/calculator internals directly.
 * Stays independent of ViewModel/Room/Hilt/Compose per plan.md section 4.
 *
 * An exercise is practised in a single pass with every stroke's guide on screen, so the child sees
 * the complete letter or shape and traces on top of it (plan.md Step 10.6). The strokes are still
 * traced in their teaching order, and the final score averages all of them.
 */
class TracingEngine(
    private val exercise: Exercise,
    tolerance: TracingTolerance = TracingTolerance(),
    completionThreshold: Float = MultiStrokeTracker.DEFAULT_COMPLETION_THRESHOLD,
    private val scoreCalculator: TraceScoreCalculator = TraceScoreCalculator(tolerance = tolerance),
) {

    private val tracker = MultiStrokeTracker(exercise, tolerance, completionThreshold)

    /** The stroke the child is currently expected to trace, or null once the exercise is complete. */
    val currentStroke: Stroke? get() = tracker.currentStroke

    /** Guides to draw right now: the whole exercise, so the complete letter or shape is visible. */
    val guideStrokes: List<Stroke> get() = if (isExerciseCompleted) emptyList() else exercise.strokes

    val isExerciseCompleted: Boolean get() = tracker.isSequenceCompleted

    fun onStart(point: TracePoint) {
        tracker.onStart(point)
    }

    fun onMove(point: TracePoint) {
        tracker.onMove(point)
    }

    fun onCancel() {
        tracker.onCancel()
    }

    /** Ends the current stroke attempt and reports what happened: retry, advance, or exercise complete with a score. */
    fun onEnd(): TracingAttemptOutcome {
        if (isExerciseCompleted) return TracingAttemptOutcome.NoAttempt
        tracker.onEnd()

        if (tracker.isSequenceCompleted) {
            val score = scoreCalculator.scoreTrace(tracker.toTraceResult())
            return TracingAttemptOutcome.ExerciseCompleted(score = score, level = scoreCalculator.scoreLevel(score))
        }

        val attempt = tracker.lastAttemptResult ?: return TracingAttemptOutcome.NoAttempt
        return TracingAttemptOutcome.StrokeAttempted(isCompleted = attempt.isCompleted)
    }
}
