package com.bornochitra.feature.fillblanks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.network.NetworkMonitor
import com.bornochitra.core.recognition.InkRecognizer
import com.bornochitra.core.recognition.WritingScript
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Whether fill-in-the-blanks' handwriting models are on the device. */
enum class HandwritingModelStatus {
    CHECKING,
    MISSING,
    DOWNLOADING,
    READY,
    FAILED,
}

/** What the handwriting-model dialog says, from the models' status and the connection. */
enum class ModelDialog {
    CHECKING,

    /** The models are missing and the device is online: offer the download. */
    OFFER,

    /** The models are missing or failed to download, and the device is offline: ask for the internet. */
    NO_INTERNET,
    DOWNLOADING,

    /** The connection dropped mid-download; ML Kit carries on once it is back. */
    WAITING_FOR_INTERNET,
    FAILED,
}

data class FillBlanksGateState(
    val modelStatus: HandwritingModelStatus = HandwritingModelStatus.CHECKING,
    /** The dialog to show, or null for none. */
    val modelDialog: ModelDialog? = null,
    /**
     * Set once fill-in-the-blanks may open, to the language of the hub it was opened from; the nav host
     * navigates and reports [FillBlanksGateEvent.FillBlanksOpened].
     */
    val openFillBlanks: AppLanguage? = null,
)

sealed interface FillBlanksGateEvent {
    /** A hub's fill-in-the-blanks button was tapped; [language] picks the categories the picker lists. */
    data class FillBlanksClicked(val language: AppLanguage) : FillBlanksGateEvent

    data object FillBlanksOpened : FillBlanksGateEvent

    data object DownloadModelsClicked : FillBlanksGateEvent

    data object ModelDialogDismissed : FillBlanksGateEvent
}

private data class ModelGate(
    val status: HandwritingModelStatus = HandwritingModelStatus.CHECKING,
    val isDialogShown: Boolean = false,
    /** The language of a tap made before the models were ready; fill-in-the-blanks opens in it once they are. */
    val waitingLanguage: AppLanguage? = null,
    val openFillBlanks: AppLanguage? = null,
) {
    /** The gate with the models' [status] known, opening fill-in-the-blanks if a tap was waiting for them. */
    fun withStatus(status: HandwritingModelStatus): ModelGate = when {
        status == HandwritingModelStatus.READY && waitingLanguage != null ->
            copy(status = status, isDialogShown = false, waitingLanguage = null, openFillBlanks = waitingLanguage)
        status == HandwritingModelStatus.READY -> copy(status = status, isDialogShown = false)
        else -> copy(status = status)
    }

    fun dialog(isOnline: Boolean): ModelDialog? {
        if (!isDialogShown) return null
        return when (status) {
            HandwritingModelStatus.CHECKING -> ModelDialog.CHECKING
            HandwritingModelStatus.MISSING -> if (isOnline) ModelDialog.OFFER else ModelDialog.NO_INTERNET
            HandwritingModelStatus.DOWNLOADING ->
                if (isOnline) ModelDialog.DOWNLOADING else ModelDialog.WAITING_FOR_INTERNET
            HandwritingModelStatus.FAILED -> if (isOnline) ModelDialog.FAILED else ModelDialog.NO_INTERNET
            HandwritingModelStatus.READY -> null
        }
    }
}

private const val STATE_SHARING_TIMEOUT_MS = 5_000L

/**
 * The gate in front of fill-in-the-blanks: it reads handwriting with models that are downloaded once,
 * and has no offline reader, so the models are mandatory. A hub's fill-in-the-blanks button opens it
 * only once they are on the device; tapping it while they are missing offers the download, asking for
 * the internet first when the device is offline.
 *
 * It belongs to the nav host, not to a hub, so both hubs share one status and a download keeps running
 * after the hub that started it is left.
 */
@HiltViewModel
class FillBlanksGateViewModel @Inject constructor(
    private val inkRecognizer: InkRecognizer,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val modelGate = MutableStateFlow(ModelGate())

    private val isOnline: StateFlow<Boolean> =
        networkMonitor.isOnline.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = true)

    val uiState: StateFlow<FillBlanksGateState> = combine(modelGate, isOnline) { gate, isOnline ->
        FillBlanksGateState(
            modelStatus = gate.status,
            modelDialog = gate.dialog(isOnline),
            openFillBlanks = gate.openFillBlanks,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
        initialValue = FillBlanksGateState(),
    )

    init {
        viewModelScope.launch {
            val isReady = WritingScript.entries.all { inkRecognizer.isModelReady(it) }
            modelGate.update { gate ->
                if (isReady) {
                    gate.withStatus(HandwritingModelStatus.READY)
                } else {
                    gate.withStatus(HandwritingModelStatus.MISSING)
                }
            }
        }
    }

    fun onEvent(event: FillBlanksGateEvent) {
        when (event) {
            is FillBlanksGateEvent.FillBlanksClicked -> modelGate.update { gate ->
                if (gate.status == HandwritingModelStatus.READY) {
                    gate.copy(openFillBlanks = event.language)
                } else {
                    gate.copy(isDialogShown = true, waitingLanguage = event.language)
                }
            }
            FillBlanksGateEvent.FillBlanksOpened -> modelGate.update { it.copy(openFillBlanks = null) }
            FillBlanksGateEvent.DownloadModelsClicked -> downloadModels()
            FillBlanksGateEvent.ModelDialogDismissed -> modelGate.update {
                it.copy(isDialogShown = false, waitingLanguage = null)
            }
        }
    }

    private fun downloadModels() {
        if (modelGate.value.status == HandwritingModelStatus.DOWNLOADING || !isOnline.value) return
        modelGate.update { it.copy(status = HandwritingModelStatus.DOWNLOADING, isDialogShown = true) }
        viewModelScope.launch {
            val isDownloaded = WritingScript.entries.all { inkRecognizer.downloadModel(it) }
            modelGate.update { gate ->
                gate.withStatus(if (isDownloaded) HandwritingModelStatus.READY else HandwritingModelStatus.FAILED)
            }
        }
    }
}
