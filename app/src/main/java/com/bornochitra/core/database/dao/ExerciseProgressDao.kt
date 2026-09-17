package com.bornochitra.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgressDao {

    @Query("SELECT * FROM exercise_progress")
    fun observeAll(): Flow<List<ExerciseProgressEntity>>
}
