package com.bornochitra.core.speech.di

import com.bornochitra.core.speech.SpeechPlayer
import com.bornochitra.core.speech.TextToSpeechPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/** One player per ViewModel, so each screen starts its own and releases it when it is left. */
@Module
@InstallIn(ViewModelComponent::class)
abstract class SpeechModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSpeechPlayer(impl: TextToSpeechPlayer): SpeechPlayer
}
