package com.bornochitra.core.recognition

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.Point
import com.bornochitra.core.tracing.MultiStrokeTracker
import com.bornochitra.core.tracing.TracePoint
import com.bornochitra.core.tracing.TraceResult
import com.bornochitra.core.tracing.TraceScoreCalculator
import kotlin.math.max

/**
 * Scores freehand writing against an exercise's guide. Someone writing from memory puts the letter
 * wherever and at whatever size they like, so the ink is first moved and uniformly scaled onto the
 * guide's own box; only then is it measured with the same coverage and accuracy as tracing, so the
 * score says how closely the shape matches rather than where it was drawn.
 */
object FreehandScorer {

    fun score(
        ink: List<List<TracePoint>>,
        exercise: Exercise,
        calculator: TraceScoreCalculator = TraceScoreCalculator(),
    ): Float {
        val tracker = MultiStrokeTracker(exercise)
        fitOnto(ink, exercise.strokes.flatMap { it.points }).forEach { stroke ->
            tracker.onStart(stroke.first())
            stroke.drop(1).forEach(tracker::onMove)
            tracker.onEnd()
        }
        return calculator.scoreTrace(
            TraceResult(
                exerciseId = exercise.id,
                strokeResults = tracker.strokeResults,
                isCompleted = tracker.isCompleted,
                expectedStrokeCount = exercise.strokes.size,
            ),
        )
    }

    /** [ink] moved and uniformly scaled so its box is centred on, and as large as, [guide]'s. */
    internal fun fitOnto(ink: List<List<TracePoint>>, guide: List<Point>): List<List<TracePoint>> {
        val strokes = ink.filter { it.isNotEmpty() }
        val inkPoints = strokes.flatten()
        if (inkPoints.isEmpty() || guide.isEmpty()) return strokes
        val inkBox = Box.of(inkPoints.map { Point(it.x, it.y) })
        val guideBox = Box.of(guide)
        val scale = if (inkBox.size > 0f) guideBox.size / inkBox.size else 1f
        return strokes.map { stroke ->
            stroke.map {
                it.copy(
                    x = guideBox.centreX + (it.x - inkBox.centreX) * scale,
                    y = guideBox.centreY + (it.y - inkBox.centreY) * scale,
                )
            }
        }
    }

    private data class Box(val minX: Float, val minY: Float, val maxX: Float, val maxY: Float) {
        val centreX: Float get() = (minX + maxX) / 2f
        val centreY: Float get() = (minY + maxY) / 2f

        /** The longer side, so a scaled letter keeps its own proportions. */
        val size: Float get() = max(maxX - minX, maxY - minY)

        companion object {
            fun of(points: List<Point>) = Box(
                minX = points.minOf { it.x },
                minY = points.minOf { it.y },
                maxX = points.maxOf { it.x },
                maxY = points.maxOf { it.y },
            )
        }
    }
}
