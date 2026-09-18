package com.bornochitra.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity

/**
 * Write path for a finished practice attempt. Owns both the session log and the exercise's
 * aggregate progress row so [recordPracticeResult] can update them in a single transaction (see
 * plan.md section 10: "Keep transactions around operations that must be atomic").
 */
@Dao
interface PracticeSessionDao {

    @Insert
    suspend fun insertSession(session: PracticeSessionEntity): Long

    @Query("SELECT * FROM exercise_progress WHERE exerciseId = :exerciseId")
    suspend fun getProgress(exerciseId: String): ExerciseProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: ExerciseProgressEntity)

    /** Inserts [session] and writes its already-computed [progress] row, returning the new session id. */
    @Transaction
    suspend fun recordPracticeResult(session: PracticeSessionEntity, progress: ExerciseProgressEntity): Long {
        val sessionId = insertSession(session)
        upsertProgress(progress)
        return sessionId
    }
}
