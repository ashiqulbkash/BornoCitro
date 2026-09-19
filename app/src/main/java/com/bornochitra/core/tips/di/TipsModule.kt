package com.bornochitra.core.tips.di

import com.bornochitra.core.tips.TipRules
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TipsModule {

    /** One place to move when contextual tips appear (plan.md section 43). */
    @Provides
    @Singleton
    fun provideTipRules(): TipRules = TipRules()
}
