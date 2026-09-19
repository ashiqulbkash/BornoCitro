package com.bornochitra.core.database

import android.content.Context
import androidx.room.Room
import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import com.bornochitra.core.database.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/** Gives every Hilt test a fresh in-memory database, so tests never touch or depend on the device's real progress. */
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

    @Provides
    fun provideExerciseProgressDao(appDatabase: AppDatabase): ExerciseProgressDao =
        appDatabase.exerciseProgressDao()

    @Provides
    fun providePracticeSessionDao(appDatabase: AppDatabase): PracticeSessionDao =
        appDatabase.practiceSessionDao()
}
