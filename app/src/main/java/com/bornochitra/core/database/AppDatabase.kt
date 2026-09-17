package com.bornochitra.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bornochitra.core.database.entity.ExerciseProgressEntity
import com.bornochitra.core.database.entity.PracticeSessionEntity

/**
 * Persisted learning progress lives here (see plan.md Section 10).
 * DAOs are added when the progress-tracking feature is implemented; this bootstrap
 * version only proves that Room initializes correctly end to end with its real schema.
 */
@Database(
    entities = [ExerciseProgressEntity::class, PracticeSessionEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase()
