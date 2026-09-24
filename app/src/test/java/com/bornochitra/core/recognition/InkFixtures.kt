package com.bornochitra.core.recognition

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.tracing.TracePoint
import kotlin.math.cos
import kotlin.math.sin

/**
 * [exercise] written the way someone writing from memory might: at [scale] of the guide's size,
 * shifted by [dx]/[dy], with a gentle finger wobble, and optionally with the strokes in reverse
 * order and each drawn from the other end.
 */
internal fun freehandInk(
    exercise: Exercise,
    scale: Float = 0.6f,
    dx: Float = 15f,
    dy: Float = 10f,
    wobble: Float = 1.5f,
    reversed: Boolean = true,
): List<List<TracePoint>> {
    val strokes = exercise.strokes.map { it.points }.let { if (reversed) it.reversed().map { points -> points.reversed() } else it }
    return strokes.map { points ->
        points.mapIndexed { index, point ->
            TracePoint(
                x = point.x * scale + dx + wobble * sin(point.y * 0.15f),
                y = point.y * scale + dy + wobble * cos(point.x * 0.15f),
                timestampMs = index * 16L,
            )
        }
    }
}
