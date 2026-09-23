package com.bornochitra.core.content

import com.bornochitra.core.model.Exercise

/** All bundled exercise definitions. Static content ships with the app; see plan.md section 9. */
internal object ExerciseCatalog {
    val all: List<Exercise> = vowelExercises + consonantExercises + englishSmallExercises + englishCapitalExercises +
        mathExercises + banglaNumberExercises + drawingExercises
}
