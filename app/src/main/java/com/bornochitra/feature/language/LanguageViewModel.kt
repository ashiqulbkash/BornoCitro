package com.bornochitra.feature.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Long enough for the language switch's thumb to finish sliding before the whole UI changes language. */
const val LANGUAGE_SWITCH_ANIMATION_MILLIS = 350L

data class LanguageUiState(
    val language: AppLanguage = AppLanguage.BANGLA,
)

sealed interface LanguageEvent {
    /** A screen showing the language appeared; a change made elsewhere is read again. */
    data object Shown : LanguageEvent

    data class LanguageSelected(val language: AppLanguage) : LanguageEvent
}

/**
 * The app's language as the Home language chip, the language sheet and the Grown-ups switch show it. Every one of
 * them changes the same stored setting.
 */
@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val appLanguageStore: AppLanguageStore,
) : ViewModel() {

    // The store applies a language change after the slide, so this keeps the choice the switch shows now.
    private val mutableState = MutableStateFlow(LanguageUiState(language = appLanguageStore.language))
    val uiState: StateFlow<LanguageUiState> = mutableState.asStateFlow()

    private var pendingLanguageChange: Job? = null

    fun onEvent(event: LanguageEvent) {
        when (event) {
            LanguageEvent.Shown -> showStoredLanguage()
            is LanguageEvent.LanguageSelected -> selectLanguage(event.language)
        }
    }

    /**
     * This ViewModel belongs to the activity and outlives the recreation a language change causes, so a change made
     * elsewhere (onboarding, Android's app language settings) is read again whenever a language control is shown.
     */
    private fun showStoredLanguage() {
        if (pendingLanguageChange?.isActive == true) return
        mutableState.value = LanguageUiState(language = appLanguageStore.language)
    }

    private fun selectLanguage(selected: AppLanguage) {
        if (selected == mutableState.value.language) return
        mutableState.value = LanguageUiState(language = selected)
        pendingLanguageChange = viewModelScope.launch {
            delay(LANGUAGE_SWITCH_ANIMATION_MILLIS)
            appLanguageStore.setLanguage(selected)
        }
    }
}
