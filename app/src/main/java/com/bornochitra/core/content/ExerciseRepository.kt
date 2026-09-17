package com.bornochitra.core.content

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import kotlinx.coroutines.flow.Flow

/** Read access to exercise definitions. See plan.md section 11. */
interface ExerciseRepository {

    fun observeExercises(type: ExerciseType): Flow<List<Exercise>>

    suspend fun getExercise(id: String): Exercise?
}
