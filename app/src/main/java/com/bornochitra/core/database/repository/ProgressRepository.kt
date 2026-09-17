package com.bornochitra.core.database.repository

import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.LearningProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    fun observeProgress(): Flow<LearningProgress>

    fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>>
}
