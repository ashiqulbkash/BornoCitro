package com.bornochitra.core.tracing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke as PathStrokeStyle
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * Renders dotted guides and lets a child trace over them with their finger, showing the live
 * traced path for immediate visual feedback. This is the pointer-tracking prototype from
 * plan.md Step 10.3: raw touch capture only — no distance/coverage scoring and no persistence.
 *
 * [stroke] is the one being traced now; [guideStrokes] is everything drawn as a guide — the whole
 * letter or shape, so the child always sees the complete exercise. Ink from strokes already
 * finished against the current [guideStrokes] stays on screen, so the letter builds up as it is
 * written; a retry of the same stroke replaces its own ink rather than layering on it.
 */
@Composable
fun TracingInputCanvas(
    stroke: Stroke,
    modifier: Modifier = Modifier,
    guideStrokes: List<Stroke> = listOf(stroke),
    style: DottedPathStyle = DottedPathStyle(),
    tracedColor: Color? = null,
    onPointerEvent: (TracingPointerEvent) -> Unit = {},
) {
    val resolvedDotColor = style.dotColor ?: MaterialTheme.colorScheme.primary
    val resolvedPathColor = style.pathColor ?: MaterialTheme.colorScheme.outline
    val resolvedTracedColor = tracedColor ?: MaterialTheme.colorScheme.secondary
    val guides = remember(guideStrokes, style.dotSpacing) {
        guideStrokes.map { guide -> guide.points to DottedPathSampler.sample(guide.points, style.dotSpacing) }
    }
    val session = remember(stroke, onPointerEvent) { TracingSession(onEvent = onPointerEvent) }
    var tracedPoints by remember(stroke) { mutableStateOf<List<TracePoint>>(emptyList()) }
    var finishedTraces by remember(guideStrokes) { mutableStateOf<Map<String, List<TracePoint>>>(emptyMap()) }

    Canvas(
        modifier = modifier.pointerInput(stroke) {
            val scale = minOf(size.width, size.height) / GUIDE_CANVAS_UNIT
            fun toTracePoint(offset: Offset) = TracePoint(
                x = offset.x / scale,
                y = offset.y / scale,
                timestampMs = System.currentTimeMillis(),
            )

            detectDragGestures(
                onDragStart = { offset ->
                    finishedTraces = finishedTraces - stroke.id
                    session.onStart(toTracePoint(offset))
                    tracedPoints = session.tracedPoints
                },
                onDrag = { change, _ ->
                    change.consume()
                    session.onMove(toTracePoint(change.position))
                    tracedPoints = session.tracedPoints
                },
                onDragEnd = {
                    finishedTraces = finishedTraces + (stroke.id to session.tracedPoints)
                    session.onEnd()
                },
                onDragCancel = {
                    session.onCancel()
                    tracedPoints = session.tracedPoints
                },
            )
        },
    ) {
        val scale = size.minDimension / GUIDE_CANVAS_UNIT
        guides.forEach { (guidePoints, guideDots) ->
            drawDottedGuide(guidePoints, guideDots, style, scale, resolvedDotColor, resolvedPathColor)
        }

        (finishedTraces.values + listOf(tracedPoints)).forEach { trace ->
            if (trace.size < 2) return@forEach
            val tracedPath = Path().apply {
                val first = trace.first()
                moveTo(first.x * scale, first.y * scale)
                trace.drop(1).forEach { lineTo(it.x * scale, it.y * scale) }
            }
            drawPath(
                path = tracedPath,
                color = resolvedTracedColor,
                style = PathStrokeStyle(
                    width = style.pathWidth * scale * 1.5f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Preview(showBackground = true, name = "tracing input (idle)")
@Composable
private fun TracingInputCanvasPreview() {
    BornoChitraTheme {
        Surface {
            TracingInputCanvas(
                stroke = Stroke(
                    id = "curve",
                    points = listOf(
                        Point(20f, 80f),
                        Point(20f, 30f),
                        Point(50f, 15f),
                        Point(80f, 30f),
                        Point(80f, 80f),
                    ),
                ),
                modifier = Modifier.aspectRatio(1f),
            )
        }
    }
}
