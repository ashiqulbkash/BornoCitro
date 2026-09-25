package com.bornochitra.feature.learn

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.speech.FakeSpeechPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LearnViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun startedViewModel(player: FakeSpeechPlayer): LearnViewModel =
        LearnViewModel(player).also { dispatcher.scheduler.advanceUntilIdle() }

    @Test
    fun listsEveryEnglishLetterWithItsExplanation() {
        val state = startedViewModel(FakeSpeechPlayer()).uiState.value

        assertEquals(AppLanguage.ENGLISH, state.language)
        assertEquals(englishLearnLetters.map { it.letter }, state.items.map { it.letter })
        assertEquals(LearnItem(letter = "A", explanation = "A for apple"), state.items.first())
    }

    @Test
    fun checksTheVoiceWhenOpened() {
        val player = FakeSpeechPlayer()
        val viewModel = LearnViewModel(player)
        assertEquals(VoiceStatus.CHECKING, viewModel.uiState.value.voiceStatus)

        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(VoiceStatus.READY, viewModel.uiState.value.voiceStatus)
    }

    @Test
    fun letterButton_speaksTheLetterInEnglish() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player)

        viewModel.onEvent(LearnEvent.LetterClicked(viewModel.uiState.value.items[1]))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf("B" to AppLanguage.ENGLISH), player.spoken)
    }

    @Test
    fun explanationButton_speaksTheLetterAndItsWord() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player)

        viewModel.onEvent(LearnEvent.ExplanationClicked(viewModel.uiState.value.items.first()))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf("A for apple" to AppLanguage.ENGLISH), player.spoken)
    }

    @Test
    fun noEnglishVoice_showsItAsMissing() {
        val viewModel = startedViewModel(FakeSpeechPlayer(voices = setOf(AppLanguage.BANGLA)))

        assertEquals(VoiceStatus.MISSING, viewModel.uiState.value.voiceStatus)
    }

    @Test
    fun aTapThatCannotBeSpoken_showsTheVoiceAsMissing() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player)

        player.voices = emptySet()
        viewModel.onEvent(LearnEvent.LetterClicked(viewModel.uiState.value.items.first()))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(VoiceStatus.MISSING, viewModel.uiState.value.voiceStatus)
    }

    @Test
    fun aVoiceInstalledLater_isPickedUpOnTheNextTap() {
        val player = FakeSpeechPlayer(voices = emptySet())
        val viewModel = startedViewModel(player)

        player.voices = setOf(AppLanguage.ENGLISH)
        viewModel.onEvent(LearnEvent.LetterClicked(viewModel.uiState.value.items.first()))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(VoiceStatus.READY, viewModel.uiState.value.voiceStatus)
        assertEquals(listOf("A" to AppLanguage.ENGLISH), player.spoken)
    }

    @Test
    fun leavingTheScreen_releasesThePlayer() {
        val player = FakeSpeechPlayer()
        val store = ViewModelStore()
        ViewModelProvider.create(store, viewModelFactory { initializer { LearnViewModel(player) } })[LearnViewModel::class]
        dispatcher.scheduler.advanceUntilIdle()

        store.clear()

        assertTrue(player.released)
    }
}
