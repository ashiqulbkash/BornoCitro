package com.bornochitra.core.database.repository

import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.model.PracticeResult
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {

    fun observeProgress(): Flow<LearningProgress>

    fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>>

    /** Persists [result] and updates its exercise's aggregate progress, returning the new session id. */
    suspend fun savePracticeResult(result: PracticeResult): Long
}
