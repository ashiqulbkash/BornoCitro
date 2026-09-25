package com.bornochitra.feature.learn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.speech.SpeechPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_LANGUAGE = "language"

/** Whether the device can speak the screen's language. */
enum class VoiceStatus {
    CHECKING,
    READY,
    MISSING,
}

data class LearnItem(
    val letter: String,
    val explanation: String,
    val spokenLetter: String = letter,
)

data class LearnUiState(
    val language: AppLanguage,
    val items: List<LearnItem>,
    val voiceStatus: VoiceStatus = VoiceStatus.CHECKING,
)

sealed interface LearnEvent {
    data class LetterClicked(val item: LearnItem) : LearnEvent

    data class ExplanationClicked(val item: LearnItem) : LearnEvent
}

/**
 * Learn with audio (plan.md Step 12), in the language the route asks for: each letter's button speaks
 * the letter, and its explanation button speaks "A for apple" or "অ তে অজগর". The screen owns its
 * [SpeechPlayer] and releases it when it is left.
 */
@HiltViewModel
class LearnViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val speechPlayer: SpeechPlayer,
) : ViewModel() {

    private val language: AppLanguage = AppLanguage.valueOf(checkNotNull(savedStateHandle[ARG_LANGUAGE]))

    private val mutableState = MutableStateFlow(
        LearnUiState(
            language = language,
            items = learnLetters(language).map {
                LearnItem(letter = it.letter, explanation = it.explanation(language), spokenLetter = it.spoken)
            },
        ),
    )
    val uiState: StateFlow<LearnUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            showVoiceStatus(hasVoice = speechPlayer.hasVoice(language))
        }
    }

    fun onEvent(event: LearnEvent) {
        when (event) {
            is LearnEvent.LetterClicked -> speak(event.item.spokenLetter)
            is LearnEvent.ExplanationClicked -> speak(event.item.explanation)
        }
    }

    // A tap tries to speak even when no voice was found, so a voice installed meanwhile is picked up.
    private fun speak(text: String) {
        viewModelScope.launch {
            showVoiceStatus(hasVoice = speechPlayer.speak(text, language))
        }
    }

    private fun showVoiceStatus(hasVoice: Boolean) {
        mutableState.update { it.copy(voiceStatus = if (hasVoice) VoiceStatus.READY else VoiceStatus.MISSING) }
    }

    override fun onCleared() {
        speechPlayer.release()
    }
}
