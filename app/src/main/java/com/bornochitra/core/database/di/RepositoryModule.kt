package com.bornochitra.core.database.di

import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.database.repository.ProgressRepositoryImpl
import com.bornochitra.core.model.MasteryRule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    companion object {

        /** One place to move the mastery bar for the whole app (plan.md section 41). */
        @Provides
        @Singleton
        fun provideMasteryRule(): MasteryRule = MasteryRule()
    }
}
