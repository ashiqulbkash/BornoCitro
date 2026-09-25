package com.bornochitra.core.speech

import com.bornochitra.core.locale.AppLanguage

/** Has a voice for the languages in [voices] and records what it was asked to say, in order. */
class FakeSpeechPlayer(var voices: Set<AppLanguage> = AppLanguage.entries.toSet()) : SpeechPlayer {

    val spoken = mutableListOf<Pair<String, AppLanguage>>()
    var released = false
        private set

    override suspend fun hasVoice(language: AppLanguage): Boolean = !released && language in voices

    override suspend fun speak(text: String, language: AppLanguage): Boolean {
        if (!hasVoice(language)) return false
        spoken += text to language
        return true
    }

    override fun release() {
        released = true
    }
}
