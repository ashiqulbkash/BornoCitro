package com.bornochitra.core.speech

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.bornochitra.core.locale.AppLanguage
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Runs [TextToSpeechPlayer] on the device's real speech engine. Which voices a device has differs,
 * so the tests check that the player agrees with itself rather than expecting a particular voice.
 */
class TextToSpeechPlayerTest {

    private val player = TextToSpeechPlayer(ApplicationProvider.getApplicationContext<Context>())

    @After
    fun tearDown() {
        player.release()
    }

    @Test
    fun speak_succeedsExactlyWhenTheLanguageHasAVoice() = runBlocking {
        AppLanguage.entries.forEach { language ->
            assertEquals(language.name, player.hasVoice(language), player.speak("a", language))
        }
    }

    @Test
    fun calledTogether_shareOneEngine() = runBlocking {
        val first = async { player.hasVoice(AppLanguage.ENGLISH) }
        val second = async { player.hasVoice(AppLanguage.ENGLISH) }
        assertEquals(first.await(), second.await())
    }

    @Test
    fun afterRelease_staysSilent() = runBlocking {
        player.hasVoice(AppLanguage.ENGLISH)
        player.release()

        AppLanguage.entries.forEach { language ->
            assertFalse(player.hasVoice(language))
            assertFalse(player.speak("a", language))
        }
    }

    @Test
    fun releasedBeforeFirstUse_neverStartsTheEngine() = runBlocking {
        player.release()

        assertFalse(player.hasVoice(AppLanguage.ENGLISH))
    }
}
