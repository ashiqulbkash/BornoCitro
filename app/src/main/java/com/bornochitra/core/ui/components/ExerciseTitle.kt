package com.bornochitra.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bornochitra.R

/**
 * An exercise's title in the app's language. Letters and numbers are the glyphs being practised, so
 * they are shown as they are; only a drawing's title is a word, and it is translated by its id.
 */
@Composable
fun exerciseTitle(exerciseId: String, title: String): String {
    val drawingName = when (exerciseId) {
        "drawing-line" -> R.string.drawing_line
        "drawing-circle" -> R.string.drawing_circle
        "drawing-square" -> R.string.drawing_square
        "drawing-triangle" -> R.string.drawing_triangle
        "drawing-house" -> R.string.drawing_house
        else -> return title
    }
    return stringResource(drawingName)
}
