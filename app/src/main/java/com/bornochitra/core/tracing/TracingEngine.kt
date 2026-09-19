package com.bornochitra.core.tracing

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Stroke

/**
 * Reusable domain-level façade over [MultiStrokeTracker] and [TraceScoreCalculator] — the
 * "domain engine" deliverable of plan.md Step 10.11. Bundles whole-shape tracking and scoring
 * behind one small, UI-agnostic API so a driving ViewModel (plan.md Step 11's PracticeViewModel)
 * can run an exercise attempt without reaching into tracker/calculator internals directly.
 * Stays independent of ViewModel/Room/Hilt/Compose per plan.md section 4.
 *
 * An exercise is one letter or drawing, practised with every stroke's guide on screen and traced
 * in a single attempt (plan.md Steps 10.6 and 2): touches may be lifted and resumed freely, the
 * strokes may be traced in any order, and the exercise finishes once the whole shape is covered.
 */
class TracingEngine(
    private val exercise: Exercise,
    tolerance: TracingTolerance = TracingTolerance(),
    completionThreshold: Float = MultiStrokeTracker.DEFAULT_COMPLETION_THRESHOLD,
    private val scoreCalculator: TraceScoreCalculator = TraceScoreCalculator(tolerance = tolerance),
) {

    private val tracker = MultiStrokeTracker(exercise, tolerance, completionThreshold)

    /** Guides to draw right now: the whole exercise, so the complete letter or shape is visible. */
    val guideStrokes: List<Stroke> get() = if (isExerciseCompleted) emptyList() else exercise.strokes

    val isExerciseCompleted: Boolean get() = tracker.isCompleted

    fun onStart(point: TracePoint) {
        tracker.onStart(point)
    }

    fun onMove(point: TracePoint) {
        tracker.onMove(point)
    }

    fun onCancel() {
        tracker.onCancel()
    }

    /** Ends the touch and reports what happened: nothing traced, the shape still unfinished, or done with a score. */
    fun onEnd(): TracingAttemptOutcome {
        if (!tracker.onEnd()) return TracingAttemptOutcome.NoAttempt

        if (tracker.isCompleted) {
            val score = scoreCalculator.scoreTrace(tracker.toTraceResult())
            return TracingAttemptOutcome.ExerciseCompleted(score = score, level = scoreCalculator.scoreLevel(score))
        }

        return TracingAttemptOutcome.Unfinished(
            coverage = tracker.strokeResults.map { it.coverage }.average().toFloat(),
        )
    }
}
