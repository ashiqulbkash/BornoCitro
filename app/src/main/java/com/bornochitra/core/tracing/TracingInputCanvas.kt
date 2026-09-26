package com.bornochitra.core.tracing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke as PathStrokeStyle
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * Renders dotted guides and lets a child trace over them with their finger, showing the live
 * traced path for immediate visual feedback. This is the pointer-tracking prototype from
 * plan.md Step 10.3: raw touch capture only — no distance/coverage scoring and no persistence.
 *
 * [guideStrokes] is everything drawn as a guide — the whole letter or shape, so the child always
 * sees the complete exercise and traces it as one thing. Ink from every touch that has been lifted
 * stays on screen, so stopping and starting again continues the drawing instead of wiping it
 * (plan.md Step 2); it is cleared by handing the canvas a new [guideStrokes] or by recomposing it
 * under a new key, which is how Practice resets an attempt.
 */
@Composable
fun TracingInputCanvas(
    guideStrokes: List<Stroke>,
    modifier: Modifier = Modifier,
    style: DottedPathStyle = DottedPathStyle(),
    tracedColor: Color? = null,
    onPointerEvent: (TracingPointerEvent) -> Unit = {},
) {
    val resolvedDotColor = style.dotColor ?: BcTheme.colors.guide
    val resolvedPathColor = style.pathColor ?: BcTheme.colors.guide.copy(alpha = GUIDE_LINE_ALPHA)
    val resolvedTracedColor = tracedColor ?: MaterialTheme.colorScheme.primary
    val guides = remember(guideStrokes, style.dotSpacing) {
        guideStrokes.map { guide -> guide.points to DottedPathSampler.sample(guide.points, style.dotSpacing) }
    }
    val session = remember(guideStrokes, onPointerEvent) { TracingSession(onEvent = onPointerEvent) }
    // Bumped on every pointer event so the ink redraws from the session's live points without a copy.
    var traceRevision by remember(guideStrokes) { mutableIntStateOf(0) }
    var finishedTraces by remember(guideStrokes) { mutableStateOf<List<List<TracePoint>>>(emptyList()) }

    Box(modifier = modifier) {
        // The guides never change while a finger moves, so they sit on their own layer and are not
        // redrawn for every pointer event.
        Canvas(modifier = Modifier.fillMaxSize().graphicsLayer()) {
            val scale = size.minDimension / GUIDE_CANVAS_UNIT
            guides.forEach { (guidePoints, guideDots) ->
                drawDottedGuide(guidePoints, guideDots, style, scale, resolvedDotColor, resolvedPathColor)
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize().graphicsLayer().pointerInput(guideStrokes) {
                val scale = minOf(size.width, size.height) / GUIDE_CANVAS_UNIT
                fun toTracePoint(offset: Offset) = TracePoint(
                    x = offset.x / scale,
                    y = offset.y / scale,
                    timestampMs = System.currentTimeMillis(),
                )

                detectDragGestures(
                    onDragStart = { offset ->
                        session.onStart(toTracePoint(offset))
                        traceRevision++
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        session.onMove(toTracePoint(change.position))
                        traceRevision++
                    },
                    onDragEnd = {
                        finishedTraces = finishedTraces + listOf(session.tracedPoints)
                        session.onEnd()
                    },
                    onDragCancel = {
                        session.onCancel()
                        traceRevision++
                    },
                )
            },
        ) {
            val scale = size.minDimension / GUIDE_CANVAS_UNIT
            // Read so this layer is redrawn when the live points change.
            traceRevision

            (finishedTraces + listOf(session.livePoints)).forEach { trace ->
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
}

@Preview(showBackground = true, name = "tracing input (idle)")
@Composable
private fun TracingInputCanvasPreview() {
    BornoChitraTheme {
        Surface {
            TracingInputCanvas(
                guideStrokes = listOf(
                    Stroke(
                        id = "curve",
                        points = listOf(
                            Point(20f, 80f),
                            Point(20f, 30f),
                            Point(50f, 15f),
                            Point(80f, 30f),
                            Point(80f, 80f),
                        ),
                    ),
                ),
                modifier = Modifier.aspectRatio(1f),
            )
        }
    }
}
