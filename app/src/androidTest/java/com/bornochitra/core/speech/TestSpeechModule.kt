package com.bornochitra.core.speech

import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.speech.di.SpeechModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import java.util.Collections

/**
 * Gives every Hilt test a player that has a voice for each language and speaks nothing aloud, so the
 * tests do not depend on the device's voices. What the screens asked it to say is in [spoken].
 */
@Module
@TestInstallIn(components = [ViewModelComponent::class], replaces = [SpeechModule::class])
object TestSpeechModule {

    val spoken: MutableList<Pair<String, AppLanguage>> = Collections.synchronizedList(mutableListOf())

    @Provides
    fun provideSpeechPlayer(): SpeechPlayer = object : SpeechPlayer {
        override suspend fun hasVoice(language: AppLanguage) = true

        override suspend fun speak(text: String, language: AppLanguage): Boolean {
            spoken += text to language
            return true
        }

        override fun release() = Unit
    }
}
