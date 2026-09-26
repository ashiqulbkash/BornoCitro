package com.bornochitra.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.bornochitra.core.model.Point
import com.bornochitra.core.model.Stroke as ExerciseStroke
import com.bornochitra.core.ui.theme.BcDimens
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

/**
 * A drawing's picture (design/DESIGN_SPEC.md 5.9): its own strokes as solid round lines, fitted and centred in a
 * [size] square [inset] from its edges, so the tile shows exactly the shape the child will trace. Decorative; the
 * tile names it.
 */
@Composable
fun BcExerciseShape(
    strokes: List<ExerciseStroke>,
    modifier: Modifier = Modifier,
    size: Dp = BcDimens.drawingShape,
    color: Color = MaterialTheme.colorScheme.secondary,
    strokeWidth: Dp = BcDimens.drawingShapeStroke,
    inset: Dp = BcDimens.drawingShapeInset,
) {
    Canvas(modifier = modifier.size(size)) {
        val points = strokes.flatMap { it.points }
        if (points.isEmpty()) return@Canvas
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val width = points.maxOf { it.x } - minX
        val height = points.maxOf { it.y } - minY
        val lineWidth = strokeWidth.toPx()
        // A straight line has no height, so the shape is scaled by its longer side.
        val scale = (this.size.minDimension - 2 * inset.toPx()) / maxOf(width, height, 1f)
        val left = (this.size.width - width * scale) / 2
        val top = (this.size.height - height * scale) / 2
        fun Point.x() = left + (x - minX) * scale
        fun Point.y() = top + (y - minY) * scale
        val style = Stroke(width = lineWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        strokes.filter { it.points.isNotEmpty() }.forEach { stroke ->
            val path = Path().apply {
                moveTo(stroke.points.first().x(), stroke.points.first().y())
                stroke.points.drop(1).forEach { lineTo(it.x(), it.y()) }
            }
            drawPath(path, color = color, style = style)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BcExerciseShapePreview() {
    BornoChitraTheme {
        Row(modifier = Modifier.padding(BcSpacing.m), horizontalArrangement = Arrangement.spacedBy(BcSpacing.m)) {
            BcExerciseShape(strokes = listOf(ExerciseStroke("line", listOf(Point(15f, 50f), Point(85f, 50f)))))
            BcExerciseShape(
                strokes = listOf(
                    ExerciseStroke("roof", listOf(Point(20f, 45f), Point(50f, 15f), Point(80f, 45f))),
                    ExerciseStroke("body", listOf(Point(25f, 45f), Point(25f, 85f), Point(75f, 85f), Point(75f, 45f))),
                ),
            )
        }
    }
}
