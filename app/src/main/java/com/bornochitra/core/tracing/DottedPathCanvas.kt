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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke as PathStrokeStyle
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke
import com.bornochitra.core.ui.theme.BcTheme
import com.bornochitra.core.ui.theme.BornoChitraTheme

internal const val GUIDE_CANVAS_UNIT = 100f

/** The guide line under the dots is drawn at 60% of the guide colour (design/DESIGN_SPEC.md 4, Tracing canvas). */
internal const val GUIDE_LINE_ALPHA = 0.6f

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
    val resolvedDotColor = style.dotColor ?: BcTheme.colors.guide
    val resolvedPathColor = style.pathColor ?: BcTheme.colors.guide.copy(alpha = GUIDE_LINE_ALPHA)
    val dots = remember(stroke, style.dotSpacing) {
        DottedPathSampler.sample(stroke.points, style.dotSpacing)
    }

    Canvas(modifier = modifier) {
        val scale = size.minDimension / GUIDE_CANVAS_UNIT
        drawDottedGuide(stroke.points, dots, style, scale, resolvedDotColor, resolvedPathColor)
    }
}

/**
 * Draws the guide line and dots for a stroke. Shared by [DottedPathCanvas] and
 * [TracingInputCanvas] so both scale points the same way within a single [DrawScope].
 */
internal fun DrawScope.drawDottedGuide(
    strokePoints: List<Point>,
    dots: List<Point>,
    style: DottedPathStyle,
    scale: Float,
    dotColor: Color,
    pathColor: Color,
) {
    fun toOffset(point: Point) = Offset(point.x * scale, point.y * scale)

    if (strokePoints.size >= 2) {
        val guidePath = Path().apply {
            val first = strokePoints.first()
            moveTo(first.x * scale, first.y * scale)
            strokePoints.drop(1).forEach { lineTo(it.x * scale, it.y * scale) }
        }
        drawPath(
            path = guidePath,
            color = pathColor,
            style = PathStrokeStyle(width = style.pathWidth * scale, cap = StrokeCap.Round),
        )
    }

    dots.forEach { dot ->
        drawCircle(color = dotColor, radius = style.dotRadius * scale, center = toOffset(dot))
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
