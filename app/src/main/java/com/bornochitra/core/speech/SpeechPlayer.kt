package com.bornochitra.core.speech

import com.bornochitra.core.locale.AppLanguage

/**
 * Speaks letters and words aloud. One player belongs to one screen, which calls [release] when it is
 * left; after that the player stays silent.
 */
interface SpeechPlayer {

    /** Whether the device has a voice for [language]; false also when it has no speech engine at all. */
    suspend fun hasVoice(language: AppLanguage): Boolean

    /** Speaks [text] in [language], cutting off anything still being said; false if it could not be spoken. */
    suspend fun speak(text: String, language: AppLanguage): Boolean

    fun release()
}
