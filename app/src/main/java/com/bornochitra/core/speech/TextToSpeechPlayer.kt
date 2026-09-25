package com.bornochitra.core.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import com.bornochitra.core.locale.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

private const val UTTERANCE_ID = "bc-speech"

private val AppLanguage.locale: Locale
    get() = when (this) {
        AppLanguage.BANGLA -> Locale.forLanguageTag("bn-BD")
        AppLanguage.ENGLISH -> Locale.US
    }

/**
 * [SpeechPlayer] on Android [TextToSpeech]. The engine is started on first use, off the main thread,
 * and every later call reuses it. Its calls are binder calls to the engine's service, so they run on
 * [dispatcher] too.
 */
class TextToSpeechPlayer(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : SpeechPlayer {

    @Inject
    constructor(@ApplicationContext context: Context) : this(context, Dispatchers.IO)

    private val startMutex = Mutex()
    private var engine: TextToSpeech? = null
    private var released = false

    override suspend fun hasVoice(language: AppLanguage): Boolean {
        val tts = startedEngine() ?: return false
        return withContext(dispatcher) { tts.isLanguageAvailable(language.locale) >= TextToSpeech.LANG_AVAILABLE }
    }

    override suspend fun speak(text: String, language: AppLanguage): Boolean {
        val tts = startedEngine() ?: return false
        return withContext(dispatcher) {
            tts.setLanguage(language.locale) >= TextToSpeech.LANG_AVAILABLE &&
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID) == TextToSpeech.SUCCESS
        }
    }

    override fun release() {
        synchronized(this) {
            released = true
            engine?.shutdown()
            engine = null
        }
    }

    /** The running engine, or null once released or when the device has no speech engine. */
    private suspend fun startedEngine(): TextToSpeech? = startMutex.withLock {
        synchronized(this) {
            if (released) return null
            engine?.let { return it }
        }
        val started = startEngine() ?: return null
        synchronized(this) {
            // Released while the engine was starting: the screen is gone, so nobody will speak.
            if (released) {
                started.shutdown()
                return null
            }
            engine = started
        }
        started
    }

    private suspend fun startEngine(): TextToSpeech? = withContext(dispatcher) {
        val status = CompletableDeferred<Int>()
        val tts = TextToSpeech(context) { status.complete(it) }
        try {
            if (status.await() == TextToSpeech.SUCCESS) {
                tts
            } else {
                tts.shutdown()
                null
            }
        } catch (cancellation: CancellationException) {
            tts.shutdown()
            throw cancellation
        }
    }
}
