package com.bornochitra.feature.home

import com.bornochitra.core.database.repository.ProgressRepository
import com.bornochitra.core.model.ExerciseProgress
import com.bornochitra.core.model.LearningProgress
import com.bornochitra.core.network.NetworkMonitor
import com.bornochitra.core.recognition.InkRecognizer
import com.bornochitra.core.recognition.WritingScript
import com.bornochitra.core.tracing.TracePoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ui state mirrors the repository's learning progress`() = runTest(dispatcher) {
        val progressFlow = MutableStateFlow(
            LearningProgress(
                overallProgress = 0.5f,
                vowelProgress = 1f,
                consonantProgress = 0.25f,
                englishSmallProgress = 0.75f,
                englishCapitalProgress = 0.5f,
                mathProgress = 0.2f,
                banglaNumberProgress = 0.4f,
                drawingProgress = 0f,
                continueExerciseId = "vowel-a",
            ),
        )
        val repository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = progressFlow
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(emptyMap())
            override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? =
                null
            override suspend fun getRecentResults(
                exerciseId: String,
                limit: Int,
            ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
        }

        val viewModel = HomeViewModel(repository, FakeRecognizer(ready = WritingScript.entries.toSet()), FakeNetworkMonitor())
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(0.5f, state.overallProgress)
        assertEquals(1f, state.vowelProgress)
        assertEquals(0.25f, state.consonantProgress)
        assertEquals(0.75f, state.englishSmallProgress)
        assertEquals(0.5f, state.englishCapitalProgress)
        assertEquals(0.2f, state.mathProgress)
        assertEquals(0.4f, state.banglaNumberProgress)
        assertEquals(0f, state.drawingProgress)
        assertEquals("vowel-a", state.continueExerciseId)
    }

    @Test
    fun `default ui state has zero progress and no continue exercise`() = runTest(dispatcher) {
        val repository = object : ProgressRepository {
            override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
            override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
                flowOf(emptyMap())
            override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
            override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? =
                null
            override suspend fun getRecentResults(
                exerciseId: String,
                limit: Int,
            ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
        }

        val viewModel = HomeViewModel(repository, FakeRecognizer(ready = WritingScript.entries.toSet()), FakeNetworkMonitor())

        assertEquals(HomeState(), viewModel.uiState.value)
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

    private val emptyProgressRepository = object : ProgressRepository {
        override fun observeProgress(): Flow<LearningProgress> = MutableStateFlow(LearningProgress())
        override fun observeExerciseProgress(exerciseIds: List<String>): Flow<Map<String, ExerciseProgress>> =
            flowOf(emptyMap())
        override suspend fun savePracticeResult(result: com.bornochitra.core.model.PracticeResult): Long = 0L
        override suspend fun getPracticeResult(sessionId: Long): com.bornochitra.core.model.PracticeResult? = null
        override suspend fun getRecentResults(
            exerciseId: String,
            limit: Int,
        ): List<com.bornochitra.core.model.PracticeResult> = emptyList()
    }

    private class FakeNetworkMonitor(online: Boolean = true) : NetworkMonitor {
        override val isOnline = MutableStateFlow(online)
    }

    private fun kotlinx.coroutines.test.TestScope.startedViewModel(
        recognizer: FakeRecognizer = FakeRecognizer(ready = WritingScript.entries.toSet()),
        network: FakeNetworkMonitor = FakeNetworkMonitor(),
    ): HomeViewModel {
        val viewModel = HomeViewModel(emptyProgressRepository, recognizer, network)
        backgroundScope.launch(dispatcher) { viewModel.uiState.collect {} }
        dispatcher.scheduler.advanceUntilIdle()
        return viewModel
    }

    @Test
    fun `with the models on the device fill in the blanks opens and no dialog shows`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeRecognizer(ready = WritingScript.entries.toSet()))
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)

        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.openFillBlanks)

        viewModel.onEvent(HomeEvent.FillBlanksOpened)
        dispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a missing model offers the download on start and keeps fill in the blanks shut`() = runTest(dispatcher) {
        // Only one of the two models is there.
        val viewModel = startedViewModel(FakeRecognizer(ready = setOf(WritingScript.LATIN)))
        assertEquals(HandwritingModelStatus.MISSING, viewModel.uiState.value.modelStatus)
        assertEquals(ModelDialog.OFFER, viewModel.uiState.value.modelDialog)

        viewModel.onEvent(HomeEvent.ModelDialogDismissed)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.modelDialog)

        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.openFillBlanks)
        assertNotNull(viewModel.uiState.value.modelDialog)
    }

    @Test
    fun `a finished download closes the dialog and opens the way to fill in the blanks`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.runCurrent()
        assertEquals(HandwritingModelStatus.DOWNLOADING, viewModel.uiState.value.modelStatus)
        assertNotNull(viewModel.uiState.value.modelDialog)
        recognizer.downloadGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(WritingScript.entries.toSet(), recognizer.ready)
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertNull(viewModel.uiState.value.modelDialog)
        // Downloaded from the start-up offer, not from a tap, so it does not open by itself.
        assertFalse(viewModel.uiState.value.openFillBlanks)
        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a failed download keeps the dialog up to try again`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(downloadSucceeds = false)
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.FAILED, viewModel.uiState.value.modelStatus)
        assertNotNull(viewModel.uiState.value.modelDialog)
        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.openFillBlanks)

        recognizer.downloadSucceeds = true
        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
    }

    @Test
    fun `a tap while the models are being looked for waits for the answer`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(ready = WritingScript.entries.toSet()).apply { checkGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)
        assertEquals(HandwritingModelStatus.CHECKING, viewModel.uiState.value.modelStatus)

        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.modelDialog)
        assertFalse(viewModel.uiState.value.openFillBlanks)

        recognizer.checkGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.uiState.value.modelDialog)
        assertTrue(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `a tap that asked for the download opens fill in the blanks once it is done`() = runTest(dispatcher) {
        val viewModel = startedViewModel(FakeRecognizer())
        viewModel.onEvent(HomeEvent.ModelDialogDismissed)

        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `dismissing the dialog forgets the waiting tap`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val viewModel = startedViewModel(recognizer)

        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        viewModel.onEvent(HomeEvent.ModelDialogDismissed)
        recognizer.downloadGate.complete(Unit)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        assertFalse(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `offline the dialog asks for the internet and nothing downloads`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer()
        val network = FakeNetworkMonitor(online = false)
        val viewModel = startedViewModel(recognizer, network)
        assertEquals(ModelDialog.NO_INTERNET, viewModel.uiState.value.modelDialog)

        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        viewModel.onEvent(HomeEvent.FillBlanksClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.MISSING, viewModel.uiState.value.modelStatus)
        assertTrue(recognizer.ready.isEmpty())
        assertFalse(viewModel.uiState.value.openFillBlanks)

        network.isOnline.value = true
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.OFFER, viewModel.uiState.value.modelDialog)
        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(HandwritingModelStatus.READY, viewModel.uiState.value.modelStatus)
        // The tap made while offline was still waiting, so fill in the blanks opens.
        assertTrue(viewModel.uiState.value.openFillBlanks)
    }

    @Test
    fun `losing the internet mid-download says so and the download carries on`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer().apply { downloadGate = CompletableDeferred() }
        val network = FakeNetworkMonitor()
        val viewModel = startedViewModel(recognizer, network)

        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
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

        viewModel.onEvent(HomeEvent.DownloadModelsClicked)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.FAILED, viewModel.uiState.value.modelDialog)
        network.isOnline.value = false
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(ModelDialog.NO_INTERNET, viewModel.uiState.value.modelDialog)
    }
}
