package com.bornochitra.feature.welcome

import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.core.onboarding.OnboardingStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class FakeLanguageStore(private var current: AppLanguage = AppLanguage.BANGLA) : AppLanguageStore {
        var setCount = 0

        override val language: AppLanguage get() = current

        override fun setLanguage(language: AppLanguage) {
            current = language
            setCount++
        }

        override fun applyDefault() = Unit
    }

    private class FakeOnboardingStore(override var hasSeenWelcome: Boolean = false) : OnboardingStore {
        override fun markWelcomeSeen() {
            hasSeenWelcome = true
        }
    }

    @Test
    fun `welcome shows the language the app is in`() {
        val viewModel = WelcomeViewModel(FakeLanguageStore(AppLanguage.ENGLISH), FakeOnboardingStore())

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    @Test
    fun `choosing a language shows it at once and stores it after the switch has slid`() = runTest(dispatcher) {
        val store = FakeLanguageStore()
        val viewModel = WelcomeViewModel(store, FakeOnboardingStore())

        viewModel.onEvent(WelcomeEvent.LanguageSelected(AppLanguage.ENGLISH))
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
        val viewModel = WelcomeViewModel(store, FakeOnboardingStore())

        viewModel.onEvent(WelcomeEvent.LanguageSelected(AppLanguage.BANGLA))
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, store.setCount)
        assertEquals(AppLanguage.BANGLA, viewModel.uiState.value.language)
    }

    @Test
    fun `continuing marks the welcome as seen`() {
        val onboarding = FakeOnboardingStore()
        val viewModel = WelcomeViewModel(FakeLanguageStore(), onboarding)
        assertFalse(onboarding.hasSeenWelcome)

        viewModel.onEvent(WelcomeEvent.ContinueClicked)

        assertTrue(onboarding.hasSeenWelcome)
    }
}
