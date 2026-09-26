package com.bornochitra.core.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bornochitra.core.model.Exercise
import com.bornochitra.core.ui.theme.BcSpacing
import com.bornochitra.core.ui.theme.BornoChitraTheme

private const val CANVAS_UNIT = 100f
private const val DOT_RADIUS = 3f

/**
 * Renders an exercise's raw stroke points as plain dots, with no scoring or interaction, purely
 * to sanity-check that content definitions produce sensible coordinates. The real dotted guide
 * renderer with configurable spacing/size is built in plan.md Step 10.2.
 */
@Composable
private fun ExerciseDotPreviewCanvas(exercise: Exercise, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val scale = size.minDimension / CANVAS_UNIT
        exercise.strokes.forEach { stroke ->
            stroke.points.forEach { point ->
                drawCircle(
                    color = Color.DarkGray,
                    radius = DOT_RADIUS,
                    center = Offset(point.x * scale, point.y * scale),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "vowel-o dotted preview")
@Composable
private fun VowelOPreview() {
    BornoChitraTheme {
        Surface {
            ExerciseDotPreviewCanvas(
                exercise = vowelExercises.first { it.id == "vowel-o" },
                modifier = Modifier.fillMaxWidth().padding(BcSpacing.m),
            )
        }
    }
}

@Preview(showBackground = true, name = "consonant-ko dotted preview")
@Composable
private fun ConsonantKoPreview() {
    BornoChitraTheme {
        Surface {
            ExerciseDotPreviewCanvas(
                exercise = consonantExercises.first { it.id == "consonant-ko" },
                modifier = Modifier.fillMaxWidth().padding(BcSpacing.m),
            )
        }
    }
}

@Preview(showBackground = true, name = "drawing-house dotted preview")
@Composable
private fun DrawingHousePreview() {
    BornoChitraTheme {
        Surface {
            ExerciseDotPreviewCanvas(
                exercise = drawingExercises.first { it.id == "drawing-house" },
                modifier = Modifier.fillMaxWidth().padding(BcSpacing.m),
            )
        }
    }
}
