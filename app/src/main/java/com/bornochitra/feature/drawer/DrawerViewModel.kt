package com.bornochitra.feature.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.feature.welcome.LANGUAGE_SWITCH_ANIMATION_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onEvent(event: DrawerEvent) {
        when (event) {
            is DrawerEvent.LanguageSelected -> selectLanguage(event.language)
        }
    }

    private fun selectLanguage(selected: AppLanguage) {
        if (selected == mutableState.value.language) return
        mutableState.value = DrawerUiState(language = selected)
        viewModelScope.launch {
            delay(LANGUAGE_SWITCH_ANIMATION_MILLIS)
            appLanguageStore.setLanguage(selected)
        }
    }
}
