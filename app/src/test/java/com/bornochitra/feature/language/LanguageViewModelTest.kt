package com.bornochitra.feature.language

import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Remembers the language like the real store; [setCount] counts the changes asked for. */
    private class FakeLanguageStore(private var current: AppLanguage = AppLanguage.BANGLA) : AppLanguageStore {
        var setCount = 0

        override val language: AppLanguage get() = current

        override fun setLanguage(language: AppLanguage) {
            current = language
            setCount++
        }

        override fun applyDefault() = Unit
    }

    @Test
    fun `the language controls show the language the app is in`() {
        val viewModel = LanguageViewModel(FakeLanguageStore(AppLanguage.ENGLISH))

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    @Test
    fun `choosing a language shows it at once and stores it after the switch has slid`() = runTest(dispatcher) {
        val store = FakeLanguageStore()
        val viewModel = LanguageViewModel(store)

        viewModel.onEvent(LanguageEvent.LanguageSelected(AppLanguage.ENGLISH))
        dispatcher.scheduler.runCurrent()

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
        assertEquals(AppLanguage.BANGLA, store.language)

        dispatcher.scheduler.advanceTimeBy(LANGUAGE_SWITCH_ANIMATION_MILLIS + 1)
        dispatcher.scheduler.runCurrent()

        assertEquals(AppLanguage.ENGLISH, store.language)
        assertEquals(1, store.setCount)
    }

    @Test
    fun `choosing the language already in use changes nothing`() = runTest(dispatcher) {
        val store = FakeLanguageStore()
        val viewModel = LanguageViewModel(store)

        viewModel.onEvent(LanguageEvent.LanguageSelected(AppLanguage.BANGLA))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, store.setCount)
        assertEquals(AppLanguage.BANGLA, viewModel.uiState.value.language)
    }

    @Test
    fun `showing a language control reads a language chosen elsewhere`() {
        val store = FakeLanguageStore(AppLanguage.BANGLA)
        val viewModel = LanguageViewModel(store)

        // e.g. chosen on onboarding, or in Android's app language settings, while this ViewModel lived on.
        store.setLanguage(AppLanguage.ENGLISH)
        viewModel.onEvent(LanguageEvent.Shown)

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    @Test
    fun `showing a language control during the slide keeps the language just chosen`() = runTest(dispatcher) {
        val store = FakeLanguageStore(AppLanguage.BANGLA)
        val viewModel = LanguageViewModel(store)

        viewModel.onEvent(LanguageEvent.LanguageSelected(AppLanguage.ENGLISH))
        dispatcher.scheduler.runCurrent()
        viewModel.onEvent(LanguageEvent.Shown)

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.ENGLISH, store.language)
    }
}
