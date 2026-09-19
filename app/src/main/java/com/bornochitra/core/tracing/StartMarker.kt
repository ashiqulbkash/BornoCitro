package com.bornochitra.core.tracing

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke as RingStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Point
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val PULSE_DURATION_MS = 1_400
private const val START_DOT_RADIUS_FACTOR = 1.8f
private const val PULSE_RING_MIN_RADIUS_FACTOR = 2f
private const val PULSE_RING_MAX_RADIUS_FACTOR = 5f
private const val PULSE_RING_START_ALPHA = 0.85f

/** One frame of the ring that pulses around a stroke's starting dot, from a 0..1 [progress]. */
internal data class PulseFrame(val radiusFactor: Float, val alpha: Float)

/**
 * The ring grows from just outside the start dot and fades as it does, then starts over. Pure so the
 * motion can be tested without a screen.
 */
internal fun pulseFrame(progress: Float): PulseFrame {
    val t = progress.coerceIn(0f, 1f)
    return PulseFrame(
        radiusFactor = PULSE_RING_MIN_RADIUS_FACTOR + (PULSE_RING_MAX_RADIUS_FACTOR - PULSE_RING_MIN_RADIUS_FACTOR) * t,
        alpha = PULSE_RING_START_ALPHA * (1f - t),
    )
}

/**
 * Marks where to start tracing a stroke: a solid dot with a ring pulsing out of it, so the child
 * can see where to put their finger (plan.md section 44). Drawn as its own layer over
 * [TracingInputCanvas] so the animation only redraws this small canvas rather than rebuilding every
 * guide and ink path each frame. It handles no touches, so the tracing canvas underneath still
 * receives them all.
 *
 * The solid dot alone already marks the start, so the pulse is only an extra cue: with animations
 * turned off in system settings the marker simply stays still.
 */
@Composable
internal fun StartMarker(
    start: Point,
    style: DottedPathStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val progress by rememberInfiniteTransition(label = "startMarker").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = PULSE_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "startMarkerPulse",
    )

    Canvas(modifier = modifier) {
        val scale = size.minDimension / GUIDE_CANVAS_UNIT
        val center = Offset(start.x * scale, start.y * scale)
        val frame = pulseFrame(progress)

        drawCircle(
            color = color.copy(alpha = frame.alpha),
            radius = style.dotRadius * scale * frame.radiusFactor,
            center = center,
            style = RingStyle(width = style.pathWidth * scale * 1.5f),
        )
        drawCircle(color = color, radius = style.dotRadius * scale * START_DOT_RADIUS_FACTOR, center = center)
    }
}

@Preview(showBackground = true, name = "start marker")
@Composable
private fun StartMarkerPreview() {
    BornoChitraTheme {
        Surface {
            StartMarker(
                start = Point(30f, 40f),
                style = DottedPathStyle(),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.aspectRatio(1f).fillMaxSize(),
            )
        }
    }
}
