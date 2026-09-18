package com.bornochitra.core.database.di

import android.content.Context
import androidx.room.Room
import com.bornochitra.core.database.AppDatabase
import com.bornochitra.core.database.dao.ExerciseProgressDao
import com.bornochitra.core.database.dao.PracticeSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "bornochitra.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideExerciseProgressDao(appDatabase: AppDatabase): ExerciseProgressDao =
        appDatabase.exerciseProgressDao()

    @Provides
    fun providePracticeSessionDao(appDatabase: AppDatabase): PracticeSessionDao =
        appDatabase.practiceSessionDao()
}
