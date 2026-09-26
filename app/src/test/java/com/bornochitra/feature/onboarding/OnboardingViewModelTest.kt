package com.bornochitra.feature.onboarding

import com.bornochitra.core.onboarding.OnboardingStore
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingViewModelTest {

    private class FakeOnboardingStore(override var hasSeenWelcome: Boolean = false) : OnboardingStore {
        override fun markWelcomeSeen() {
            hasSeenWelcome = true
        }
    }

    @Test
    fun `starting marks the welcome as seen`() {
        val onboarding = FakeOnboardingStore()
        val viewModel = OnboardingViewModel(onboarding)
        assertFalse(onboarding.hasSeenWelcome)

        viewModel.onEvent(OnboardingEvent.StartClicked)

        assertTrue(onboarding.hasSeenWelcome)
    }
}
