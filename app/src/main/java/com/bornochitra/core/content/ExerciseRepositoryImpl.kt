package com.bornochitra.core.content

import com.bornochitra.core.model.Exercise
import com.bornochitra.core.model.ExerciseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor() : ExerciseRepository {

    private val exercisesById: Map<String, Exercise> = ExerciseCatalog.all.associateBy { it.id }

    override fun observeExercises(type: ExerciseType): Flow<List<Exercise>> =
        flowOf(ExerciseCatalog.all.filter { it.type == type }.sortedBy { it.order })

    override suspend fun getExercise(id: String): Exercise? = exercisesById[id]
}
