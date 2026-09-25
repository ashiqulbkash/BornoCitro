package com.bornochitra.feature.fillblanks

import com.bornochitra.core.locale.AppLanguage
import com.bornochitra.core.network.NetworkMonitor
import com.bornochitra.core.recognition.InkRecognizer
import com.bornochitra.core.recognition.WritingScript
import com.bornochitra.core.tracing.TracePoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FillBlanksGateViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Has the models in [ready] on the device; [downloadSucceeds] decides every download. */
    private class FakeRecognizer(
        ready: Set<WritingScript> = emptySet(),
        var downloadSucceeds: Boolean = true,
    ) : InkRecognizer {
        val ready = ready.toMutableSet()

        /** Downloads wait for this, so a test can look at the state while one runs. */
        var downloadGate = CompletableDeferred(Unit)

        /** The same for the check whether a model is on the device. */
        var checkGate = CompletableDeferred(Unit)

        override suspend fun isModelReady(script: WritingScript): Boolean {
            checkGate.await()
            return script in ready
        }

        override suspend fun downloadModel(script: WritingScript): Boolean {
            downloadGate.await()
            if (downloadSucceeds) ready += script
            return downloadSucceeds
        }

        override suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String>? = null
    }

    private class FakeNetworkMonitor(online: Boolean = true) : NetworkMonitor {
        override val isOnline = MutableStateFlow(online)
    }

    private fun kotlinx.coroutines.test.TestScope.startedViewModel(
        recognizer: FakeRecognizer = FakeRecognizer(ready = WritingScript.entries.toSet()),
        network: FakeNetworkMonitor = FakeNetworkMonitor(),
    ): FillBlanksGateViewModel {
        val viewModel = FillBlanksGateViewModel(recognizer, network)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
        return viewModel
    }

    @Test
    fun `with the models on the device fill in the blanks opens and no dialog shows`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeRecognizer(ready = WritingScript.entries.toSet()))
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksOpened)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a missing model shows no dialog on start and offers the download when fill in the blanks is tapped`() = runTest(dispatcher) {
        // Only one of the two models is there.
        val viewModel = startedViewModel(FakeRecognizer(ready = setOf(WritingScript.LATIN)))
        assertEquals(HandwritingModelStatus.MISSING, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.openFillBlanks)
        assertEquals(ModelDialog.OFFER, viewModel.uiState.value.modelDialog)

        viewModel.onEvent(FillBlanksGateEvent.ModelDialogDismissed)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.modelDialog)
    }

    @Test
    fun `a finished download closes the dialog and opens the way to fill in the blanks`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.runCurrent()
        assertEquals(HandwritingModelStatus.DOWNLOADING, viewModel.uiState.value.modelStatus)
        assertNotNull(viewModel.uiState.value.modelDialog)
        recognizer.downloadGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(WritingScript.entries.toSet(), recognizer.ready)
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)
        // Downloaded without a tap on fill in the blanks, so it does not open by itself.
        assertNull(viewModel.uiState.value.openFillBlanks)
        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a failed download keeps the dialog up to try again`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(downloadSucceeds = false)
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.FAILED, viewModel.uiState.value.modelStatus)
        assertNotNull(viewModel.uiState.value.modelDialog)
        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.openFillBlanks)

        recognizer.downloadSucceeds = true
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
    }

    @Test
    fun `a tap while the models are being looked for waits for the answer`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(ready = WritingScript.entries.toSet()).apply { checkGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)
        assertEquals(HandwritingModelStatus.CHECKING, viewModel.uiState.value.modelStatus)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.modelDialog)
        assertNull(viewModel.uiState.value.openFillBlanks)

        recognizer.checkGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.modelDialog)
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a tap that asked for the download opens fill in the blanks once it is done`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeRecognizer())
        viewModel.onEvent(FillBlanksGateEvent.ModelDialogDismissed)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `dismissing the dialog forgets the waiting tap`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        viewModel.onEvent(FillBlanksGateEvent.ModelDialogDismissed)
        recognizer.downloadGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `offline the dialog asks for the internet and nothing downloads`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer()
        val network = FakeNetworkMonitor(online = false)
        val viewModel = startedViewModel(recognizer, network)
        assertNull(viewModel.uiState.value.modelDialog)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.NO_INTERNET, viewModel.uiState.value.modelDialog)
        assertEquals(HandwritingModelStatus.MISSING, viewModel.uiState.value.modelStatus)
        assertTrue(recognizer.ready.isEmpty())
        assertNull(viewModel.uiState.value.openFillBlanks)

        network.isOnline.value = true
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.OFFER, viewModel.uiState.value.modelDialog)
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        // The tap made while offline was still waiting, so fill in the blanks opens.
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `losing the internet mid-download says so and the download carries on`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val network = FakeNetworkMonitor()
        val viewModel = startedViewModel(recognizer, network)

        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.runCurrent()
        assertEquals(ModelDialog.DOWNLOADING, viewModel.uiState.value.modelDialog)
        network.isOnline.value = false
        dispatcher.scheduler.runCurrent()
        assertEquals(ModelDialog.WAITING_FOR_INTERNET, viewModel.uiState.value.modelDialog)

        network.isOnline.value = true
        recognizer.downloadGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)
    }

    @Test
    fun `a failed download offline asks for the internet before offering a retry`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(downloadSucceeds = false)
        val network = FakeNetworkMonitor()
        val viewModel = startedViewModel(recognizer, network)

        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.FAILED, viewModel.uiState.value.modelDialog)
        network.isOnline.value = false
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.NO_INTERNET, viewModel.uiState.value.modelDialog)
    }

    @Test
    fun `the picker opens in the language of the hub that was tapped`() = runTest(dispatcher) {
        val viewModel = startedViewModel()

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.BANGLA))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.BANGLA, viewModel.uiState.value.openFillBlanks)
        viewModel.onEvent(FillBlanksGateEvent.FillBlanksOpened)

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.ENGLISH))
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a tap waiting for the download opens the picker in its own language`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeRecognizer())

        viewModel.onEvent(FillBlanksGateEvent.FillBlanksClicked(AppLanguage.BANGLA))
        viewModel.onEvent(FillBlanksGateEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(AppLanguage.BANGLA, viewModel.uiState.value.openFillBlanks)
    }
}
