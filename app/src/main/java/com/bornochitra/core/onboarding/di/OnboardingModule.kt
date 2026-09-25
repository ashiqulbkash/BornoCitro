package com.bornochitra.core.onboarding.di

import com.bornochitra.core.onboarding.OnboardingStore
import com.bornochitra.core.onboarding.SharedPreferencesOnboardingStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {

    @Binds
    abstract fun bindOnboardingStore(impl: SharedPreferencesOnboardingStore): OnboardingStore
}
