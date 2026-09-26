package com.bornochitra.feature.learn

import androidx.lifecycle.SavedStateHandle
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

    private fun viewModel(player: FakeSpeechPlayer, language: AppLanguage = AppLanguage.ENGLISH) =
        LearnViewModel(SavedStateHandle(mapOf("language" to language.name)), player)

    private fun startedViewModel(player: FakeSpeechPlayer, language: AppLanguage = AppLanguage.ENGLISH): LearnViewModel =
        viewModel(player, language).also { dispatcher.scheduler.advanceUntilIdle() }

    @Test
    fun listsEveryEnglishLetterWithItsExplanation() {
        val state = startedViewModel(FakeSpeechPlayer()).uiState.value

        assertEquals(AppLanguage.ENGLISH, state.language)
        assertEquals(englishLearnLetters.map { it.letter }, state.items.map { it.letter })
        assertEquals(
            LearnItem(letter = "A", explanation = "A for apple", word = "apple", group = LearnGroup.ENGLISH_LETTERS),
            state.items.first(),
        )
        assertTrue(state.items.all { it.group == LearnGroup.ENGLISH_LETTERS })
    }

    @Test
    fun checksTheVoiceWhenOpened() {
        val player = FakeSpeechPlayer()
        val viewModel = viewModel(player)
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
    fun listsEveryBanglaVowelAndConsonantWithItsExplanation() {
        val state = startedViewModel(FakeSpeechPlayer(), AppLanguage.BANGLA).uiState.value

        assertEquals(AppLanguage.BANGLA, state.language)
        assertEquals(banglaLearnLetters.map { it.letter }, state.items.map { it.letter })
        assertEquals(
            LearnItem(letter = "অ", explanation = "অ তে অজগর", word = "অজগর", group = LearnGroup.VOWELS, spokenLetter = "স্বরে অ"),
            state.items.first(),
        )
        // The 11 vowels come first, under their own heading, then the 39 consonants under theirs.
        assertEquals(List(11) { LearnGroup.VOWELS } + List(39) { LearnGroup.CONSONANTS }, state.items.map { it.group })
    }

    @Test
    fun banglaButtons_speakTheLetterAndItsWordInBangla() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player, AppLanguage.BANGLA)
        val kho = viewModel.uiState.value.items.first { it.letter == "খ" }

        viewModel.onEvent(LearnEvent.LetterClicked(kho))
        viewModel.onEvent(LearnEvent.ExplanationClicked(kho))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf("খ", "খ তে খরগোশ").map { it to AppLanguage.BANGLA }, player.spoken)
    }

    @Test
    fun banglaVowelButton_speaksTheVowelsPrimerName() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player, AppLanguage.BANGLA)
        val items = viewModel.uiState.value.items

        viewModel.onEvent(LearnEvent.LetterClicked(items.first { it.letter == "ই" }))
        viewModel.onEvent(LearnEvent.LetterClicked(items.first { it.letter == "ঈ" }))
        viewModel.onEvent(LearnEvent.ExplanationClicked(items.first { it.letter == "ঈ" }))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf("হ্রস্ব ই", "দীর্ঘ ঈ", "ঈ তে ঈগল").map { it to AppLanguage.BANGLA }, player.spoken)
    }

    @Test
    fun banglaSibilantButtons_speakNamesThatTellThemApart() {
        val player = FakeSpeechPlayer()
        val viewModel = startedViewModel(player, AppLanguage.BANGLA)
        val items = viewModel.uiState.value.items

        listOf("শ", "ষ", "স").forEach { letter -> viewModel.onEvent(LearnEvent.LetterClicked(items.first { it.letter == letter })) }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf("তালব্য শ", "মূর্ধন্য ষ", "দন্ত্য স").map { it to AppLanguage.BANGLA }, player.spoken)
    }

    @Test
    fun noBanglaVoice_showsItAsMissing() {
        val viewModel = startedViewModel(FakeSpeechPlayer(voices = setOf(AppLanguage.ENGLISH)), AppLanguage.BANGLA)

        assertEquals(VoiceStatus.MISSING, viewModel.uiState.value.voiceStatus)
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
        ViewModelProvider.create(store, viewModelFactory { initializer { viewModel(player) } })[LearnViewModel::class]
        dispatcher.scheduler.advanceUntilIdle()

        store.clear()

        assertTrue(player.released)
    }
}
