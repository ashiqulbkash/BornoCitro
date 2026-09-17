package com.bornochitra.core.tracing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke as PathStrokeStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val GUIDE_CANVAS_UNIT = 100f

/** Visual styling for [DottedPathCanvas]. All sizes are in the exercise's normalized 0..100 units. */
data class DottedPathStyle(
    val dotSpacing: Float = 6f,
    val dotRadius: Float = 2.5f,
    val pathWidth: Float = 1.5f,
    val dotColor: Color? = null,
    val pathColor: Color? = null,
)

/**
 * Renders a [Stroke] as a faint guide line with evenly spaced dots along it — the dotted-path
 * prototype from plan.md Step 10.2. Supports straight and curved strokes since both are plain
 * point sequences; no touch handling or scoring happens here.
 */
@Composable
fun DottedPathCanvas(
    stroke: Stroke,
    modifier: Modifier = Modifier,
    style: DottedPathStyle = DottedPathStyle(),
) {
    val resolvedDotColor = style.dotColor ?: MaterialTheme.colorScheme.primary
    val resolvedPathColor = style.pathColor ?: MaterialTheme.colorScheme.outline
    val dots = remember(stroke, style.dotSpacing) {
        DottedPathSampler.sample(stroke.points, style.dotSpacing)
    }

    Canvas(modifier = modifier) {
        val scale = size.minDimension / GUIDE_CANVAS_UNIT
        fun toOffset(point: Point) = Offset(point.x * scale, point.y * scale)

        if (stroke.points.size >= 2) {
            val guidePath = Path().apply {
                val first = stroke.points.first()
                moveTo(first.x * scale, first.y * scale)
                stroke.points.drop(1).forEach { lineTo(it.x * scale, it.y * scale) }
            }
            drawPath(
                path = guidePath,
                color = resolvedPathColor,
                style = PathStrokeStyle(width = style.pathWidth * scale, cap = StrokeCap.Round),
            )
        }

        dots.forEach { dot ->
            drawCircle(color = resolvedDotColor, radius = style.dotRadius * scale, center = toOffset(dot))
        }
    }
}

@Preview(showBackground = true, name = "straight stroke")
@Composable
private fun DottedPathCanvasStraightPreview() {
    BornoChitraTheme {
        Surface {
            DottedPathCanvas(
                stroke = Stroke(id = "straight", points = listOf(Point(10f, 50f), Point(90f, 50f))),
                modifier = Modifier.aspectRatio(1f),
            )
        }
    }
}

@Preview(showBackground = true, name = "curved stroke")
@Composable
private fun DottedPathCanvasCurvedPreview() {
    BornoChitraTheme {
        Surface {
            DottedPathCanvas(
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
