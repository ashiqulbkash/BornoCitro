package com.bornochitra.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity

/** Persisted learning progress lives here (see plan.md Section 10). */
@Database(
    entities = [ExerciseProgressEntity::class, PracticeSessionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseProgressDao(): ExerciseProgressDao
}
