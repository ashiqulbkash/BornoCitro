package com.bornochitra.core.analytics.di

import com.bornochitra.core.analytics.AnalyticsTracker
import com.bornochitra.core.analytics.LogAnalyticsTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(impl: LogAnalyticsTracker): AnalyticsTracker
}
