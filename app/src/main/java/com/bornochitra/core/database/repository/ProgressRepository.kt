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

    /** The attempt saved under [sessionId], or null if no such session was recorded. */
    suspend fun getPracticeResult(sessionId: Long): PracticeResult?

    /** Up to [limit] of the exercise's latest attempts, newest first. */
    suspend fun getRecentResults(exerciseId: String, limit: Int): List<PracticeResult>
}
