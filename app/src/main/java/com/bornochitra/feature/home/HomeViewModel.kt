package com.bornochitra.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bornochitra.core.database.repository.ProgressRepository
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

data class HomeState(
    val overallProgress: Float = 0f,
    val vowelProgress: Float = 0f,
    val consonantProgress: Float = 0f,
    val englishSmallProgress: Float = 0f,
    val englishCapitalProgress: Float = 0f,
    val mathProgress: Float = 0f,
    val banglaNumberProgress: Float = 0f,
    val drawingProgress: Float = 0f,
    val continueExerciseId: String? = null,
    val modelStatus: HandwritingModelStatus = HandwritingModelStatus.CHECKING,
    /** The dialog to show, or null for none. */
    val modelDialog: ModelDialog? = null,
    /** Set once fill-in-the-blanks may open; the screen navigates and reports [HomeEvent.FillBlanksOpened]. */
    val openFillBlanks: Boolean = false,
)

sealed interface HomeEvent {
    data object FillBlanksClicked : HomeEvent

    data object FillBlanksOpened : HomeEvent

    data object DownloadModelsClicked : HomeEvent

    data object ModelDialogDismissed : HomeEvent
}

/** What Home shows besides progress: the handwriting-model gate in front of fill-in-the-blanks. */
private data class ModelGate(
    val status: HandwritingModelStatus = HandwritingModelStatus.CHECKING,
    val isDialogShown: Boolean = false,
    /** Fill-in-the-blanks was tapped before the models were ready; it opens once they are. */
    val isOpenWaitingForModels: Boolean = false,
    val openFillBlanks: Boolean = false,
) {
    /** The gate with the models' [status] known, opening fill-in-the-blanks if a tap was waiting for them. */
    fun withStatus(status: HandwritingModelStatus): ModelGate = when {
        status == HandwritingModelStatus.READY && isOpenWaitingForModels ->
            copy(status = status, isDialogShown = false, isOpenWaitingForModels = false, openFillBlanks = true)
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
 * Home's progress, plus the gate in front of fill-in-the-blanks: it reads handwriting with models
 * that are downloaded once, and has no offline reader, so the models are mandatory. The
 * fill-in-the-blanks button opens it only once they are on the device; tapping it while they are
 * missing offers the download, asking for the internet first when the device is offline.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    progressRepository: ProgressRepository,
    private val inkRecognizer: InkRecognizer,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val modelGate = MutableStateFlow(ModelGate())

    private val isOnline: StateFlow<Boolean> =
        networkMonitor.isOnline.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = true)

    val uiState: StateFlow<HomeState> = combine(
        progressRepository.observeProgress(),
        modelGate,
        isOnline,
    ) { progress, gate, isOnline ->
        HomeState(
            overallProgress = progress.overallProgress,
            vowelProgress = progress.vowelProgress,
            consonantProgress = progress.consonantProgress,
            englishSmallProgress = progress.englishSmallProgress,
            englishCapitalProgress = progress.englishCapitalProgress,
            mathProgress = progress.mathProgress,
            banglaNumberProgress = progress.banglaNumberProgress,
            drawingProgress = progress.drawingProgress,
            continueExerciseId = progress.continueExerciseId,
            modelStatus = gate.status,
            modelDialog = gate.dialog(isOnline),
            openFillBlanks = gate.openFillBlanks,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_SHARING_TIMEOUT_MS),
        initialValue = HomeState(),
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

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FillBlanksClicked -> modelGate.update { gate ->
                if (gate.status == HandwritingModelStatus.READY) {
                    gate.copy(openFillBlanks = true)
                } else {
                    gate.copy(isDialogShown = true, isOpenWaitingForModels = true)
                }
            }
            HomeEvent.FillBlanksOpened -> modelGate.update { it.copy(openFillBlanks = false) }
            HomeEvent.DownloadModelsClicked -> downloadModels()
            HomeEvent.ModelDialogDismissed -> modelGate.update {
                it.copy(isDialogShown = false, isOpenWaitingForModels = false)
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
