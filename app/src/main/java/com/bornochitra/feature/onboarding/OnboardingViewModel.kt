package com.bornochitra.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.core.onboarding.OnboardingStore
import com.bornochitra.feature.language.LANGUAGE_SWITCH_ANIMATION_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val language: AppLanguage = AppLanguage.BANGLA,
)

sealed interface OnboardingEvent {
    data class LanguageSelected(val language: AppLanguage) : OnboardingEvent

    /** Onboarding is done: it will not be shown again. */
    data object StartClicked : OnboardingEvent
}

/** The first-launch onboarding: the app's language on page 1, what the app is on page 2. */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appLanguageStore: AppLanguageStore,
    private val onboardingStore: OnboardingStore,
) : ViewModel() {

    // The store applies a language change after a short delay, so this keeps the choice the cards show now.
    private val mutableState = MutableStateFlow(OnboardingUiState(language = appLanguageStore.language))
    val uiState: StateFlow<OnboardingUiState> = mutableState.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.LanguageSelected -> selectLanguage(event.language)
            OnboardingEvent.StartClicked -> onboardingStore.markWelcomeSeen()
        }
    }

    private fun selectLanguage(selected: AppLanguage) {
        if (selected == mutableState.value.language) return
        mutableState.value = OnboardingUiState(language = selected)
        viewModelScope.launch {
            delay(LANGUAGE_SWITCH_ANIMATION_MILLIS)
            appLanguageStore.setLanguage(selected)
        }
    }
}
