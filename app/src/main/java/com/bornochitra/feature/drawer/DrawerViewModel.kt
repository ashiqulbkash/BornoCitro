package com.bornochitra.feature.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.feature.welcome.LANGUAGE_SWITCH_ANIMATION_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DrawerUiState(
    val language: AppLanguage = AppLanguage.BANGLA,
)

sealed interface DrawerEvent {
    /** The drawer is opening. */
    data object Opened : DrawerEvent

    data class LanguageSelected(val language: AppLanguage) : DrawerEvent
}

/** The navigation drawer's own state: the app's language switch. */
@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val appLanguageStore: AppLanguageStore,
) : ViewModel() {

    // The store applies a language change after the slide, so this keeps the choice the switch shows now.
    private val mutableState = MutableStateFlow(DrawerUiState(language = appLanguageStore.language))
    val uiState: StateFlow<DrawerUiState> = mutableState.asStateFlow()

    private var pendingLanguageChange: Job? = null

    fun onEvent(event: DrawerEvent) {
        when (event) {
            DrawerEvent.Opened -> showStoredLanguage()
            is DrawerEvent.LanguageSelected -> selectLanguage(event.language)
        }
    }

    /**
     * This ViewModel belongs to the activity and outlives the recreation a language change causes, so a
     * change made elsewhere (Welcome, Android's app language settings) is read again on every opening.
     */
    private fun showStoredLanguage() {
        if (pendingLanguageChange?.isActive == true) return
        mutableState.value = DrawerUiState(language = appLanguageStore.language)
    }

    private fun selectLanguage(selected: AppLanguage) {
        if (selected == mutableState.value.language) return
        mutableState.value = DrawerUiState(language = selected)
        pendingLanguageChange = viewModelScope.launch {
            delay(LANGUAGE_SWITCH_ANIMATION_MILLIS)
            appLanguageStore.setLanguage(selected)
        }
    }
}
