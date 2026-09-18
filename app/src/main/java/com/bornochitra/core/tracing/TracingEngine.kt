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
 * A multi-stroke letter is practised in two passes: first stroke by stroke, then once more with
 * every guide on screen so the child writes the whole letter ([TracingPhase]). Both passes run the
 * same stroke sequence through their own tracker, and the final score averages all of their
 * strokes. A single-stroke exercise has nothing to assemble, so it runs the first pass only.
 */
class TracingEngine(
    private val exercise: Exercise,
    tolerance: TracingTolerance = TracingTolerance(),
    completionThreshold: Float = MultiStrokeTracker.DEFAULT_COMPLETION_THRESHOLD,
    private val scoreCalculator: TraceScoreCalculator = TraceScoreCalculator(tolerance = tolerance),
) {

    val phases: List<TracingPhase> = if (exercise.strokes.size > 1) {
        listOf(TracingPhase.STROKE_BY_STROKE, TracingPhase.FULL_LETTER)
    } else {
        listOf(TracingPhase.STROKE_BY_STROKE)
    }

    private val trackers = phases.map { MultiStrokeTracker(exercise, tolerance, completionThreshold) }

    private var phaseIndex = 0

    private val activeTracker: MultiStrokeTracker? get() = trackers.getOrNull(phaseIndex)

    val totalStrokes: Int = exercise.strokes.size

    /** The pass being worked through, staying on the last one once the exercise is complete. */
    val phase: TracingPhase get() = phases[phaseIndex.coerceAtMost(phases.lastIndex)]

    /** The stroke the child is currently expected to trace, or null once the exercise is complete. */
    val currentStroke: Stroke? get() = activeTracker?.currentStroke

    /** 1-based index of [currentStroke] within the current pass, clamped to [totalStrokes]. */
    val currentStrokeNumber: Int
        get() = ((activeTracker?.currentStrokeIndex ?: totalStrokes) + 1).coerceAtMost(totalStrokes)

    /**
     * Guides to draw right now: only [currentStroke] while practising stroke by stroke, but every
     * stroke once the whole letter is being written.
     */
    val guideStrokes: List<Stroke>
        get() = when {
            isExerciseCompleted -> emptyList()
            phase == TracingPhase.FULL_LETTER -> exercise.strokes
            else -> listOfNotNull(currentStroke)
        }

    val isExerciseCompleted: Boolean get() = phaseIndex >= trackers.size

    fun onStart(point: TracePoint) {
        activeTracker?.onStart(point)
    }

    fun onMove(point: TracePoint) {
        activeTracker?.onMove(point)
    }

    fun onCancel() {
        activeTracker?.onCancel()
    }

    /** Ends the current stroke attempt and reports what happened: retry, advance, or exercise complete with a score. */
    fun onEnd(): TracingAttemptOutcome {
        val tracker = activeTracker ?: return TracingAttemptOutcome.NoAttempt
        tracker.onEnd()

        if (tracker.isSequenceCompleted) {
            phaseIndex += 1
            if (!isExerciseCompleted) return TracingAttemptOutcome.StrokeAttempted(isCompleted = true)

            val score = scoreCalculator.scoreTrace(combinedResult())
            return TracingAttemptOutcome.ExerciseCompleted(score = score, level = scoreCalculator.scoreLevel(score))
        }

        val attempt = tracker.lastAttemptResult ?: return TracingAttemptOutcome.NoAttempt
        return TracingAttemptOutcome.StrokeAttempted(isCompleted = attempt.isCompleted)
    }

    private fun combinedResult() = TraceResult(
        exerciseId = exercise.id,
        strokeResults = trackers.flatMap { it.completedResults },
        isCompleted = true,
    )
}
