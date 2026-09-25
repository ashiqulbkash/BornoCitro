package com.bornochitra.core.locale.di

import com.bornochitra.core.locale.AppCompatLanguageStore
import com.bornochitra.core.locale.AppLanguageStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {

    @Binds
    abstract fun bindAppLanguageStore(impl: AppCompatLanguageStore): AppLanguageStore
}
