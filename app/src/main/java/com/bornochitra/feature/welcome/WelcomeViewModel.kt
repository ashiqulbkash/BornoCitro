package com.bornochitra.feature.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.core.onboarding.OnboardingStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WelcomeUiState(
    val language: AppLanguage = AppLanguage.BANGLA,
)

sealed interface WelcomeEvent {
    data class LanguageSelected(val language: AppLanguage) : WelcomeEvent

    /** Welcome is done: it will not be shown again. */
    data object ContinueClicked : WelcomeEvent
}

/** Long enough for the language switch's thumb to finish sliding before the whole UI changes language. */
const val LANGUAGE_SWITCH_ANIMATION_MILLIS = 350L

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val appLanguageStore: AppLanguageStore,
    private val onboardingStore: OnboardingStore,
) : ViewModel() {

    // The store applies a language change after the slide, so this keeps the choice the switch shows now.
    private val mutableState = MutableStateFlow(WelcomeUiState(language = appLanguageStore.language))
    val uiState: StateFlow<WelcomeUiState> = mutableState.asStateFlow()

    fun onEvent(event: WelcomeEvent) {
        when (event) {
            is WelcomeEvent.LanguageSelected -> selectLanguage(event.language)
            WelcomeEvent.ContinueClicked -> onboardingStore.markWelcomeSeen()
        }
    }

    private fun selectLanguage(selected: AppLanguage) {
        if (selected == mutableState.value.language) return
        mutableState.value = WelcomeUiState(language = selected)
        viewModelScope.launch {
            delay(LANGUAGE_SWITCH_ANIMATION_MILLIS)
            appLanguageStore.setLanguage(selected)
        }
    }
}
