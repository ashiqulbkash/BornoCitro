package com.bornochitra.feature.onboarding

import androidx.lifecycle.ViewModel
import com.bornochitra.core.onboarding.OnboardingStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface OnboardingEvent {
    /** Welcome is done: it will not be shown again. */
    data object StartClicked : OnboardingEvent
}

/** The first-launch welcome: what the app is, and the one button that leaves it behind. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingStore: OnboardingStore,
) : ViewModel() {

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.StartClicked -> onboardingStore.markWelcomeSeen()
        }
    }
}
