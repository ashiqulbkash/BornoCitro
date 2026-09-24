package com.bornochitra.core.recognition

import com.bornochitra.core.content.ExerciseCatalog
import com.bornochitra.core.model.ExerciseType
import com.bornochitra.core.tracing.TracePoint
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FreehandCheckerTest {

    private class FakeRecognizer(var candidates: List<String>?) : InkRecognizer {
        var recognizeCalls = 0

        override suspend fun isModelReady(script: WritingScript) = candidates != null

        override suspend fun downloadModel(script: WritingScript) = true

        override suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String>? {
            recognizeCalls++
            return candidates
        }
    }

    private val dispatcher = StandardTestDispatcher()
    private val consonants = ExerciseCatalog.all.filter { it.type == ExerciseType.CONSONANT }
    private val ko = consonants.single { it.title == "ক" }
    private val kho = consonants.single { it.title == "খ" }

    private fun checker(recognizer: InkRecognizer) = FreehandChecker(recognizer, dispatcher)

    @Test
    fun `ink the model reads as the item passes with its shape score`() = runTest(dispatcher) {
        val ink = freehandInk(ko)
        val result = checker(FakeRecognizer(listOf("ক", "ব"))).check(ink, ko, consonants)
        assertNotNull(result)
        assertEquals(FreehandScorer.score(ink, ko), result!!.score, 0.001f)
    }

    @Test
    fun `guesses from outside the category are skipped`() = runTest(dispatcher) {
        assertNotNull(checker(FakeRecognizer(listOf("t", "৮", "ক"))).check(freehandInk(ko), ko, consonants))
    }

    @Test
    fun `the model alone is enough`() = runTest(dispatcher) {
        // A straight line has nothing of ক's shape, so only the model's reading can pass it.
        val line = listOf(listOf(TracePoint(10f, 10f, 0L), TracePoint(90f, 90f, 16L)))
        assertNotNull(checker(FakeRecognizer(listOf("ক"))).check(line, ko, consonants))
    }

    @Test
    fun `the shape passes ink the model misreads`() = runTest(dispatcher) {
        // A clean ক that the model reads as খ, or as nothing from the category.
        assertNotNull(checker(FakeRecognizer(listOf("খ", "ক"))).check(freehandInk(ko), ko, consonants))
        assertNotNull(checker(FakeRecognizer(listOf("t", "E"))).check(freehandInk(ko), ko, consonants))
    }

    @Test
    fun `ink neither reader takes for the item does not pass`() = runTest(dispatcher) {
        assertNull(checker(FakeRecognizer(listOf("খ", "ক"))).check(freehandInk(kho), ko, consonants))
        assertNull(checker(FakeRecognizer(emptyList())).check(freehandInk(ko), kho, consonants))
    }

    @Test
    fun `without the model nothing passes, even a clean shape`() = runTest(dispatcher) {
        assertNull(checker(FakeRecognizer(null)).check(freehandInk(ko), ko, consonants))
    }

    @Test
    fun `no ink with any length is not sent to the model`() = runTest(dispatcher) {
        val recognizer = FakeRecognizer(listOf("ক"))
        assertNull(checker(recognizer).check(listOf(listOf(TracePoint(1f, 1f, 0L))), ko, consonants))
        assertEquals(0, recognizer.recognizeCalls)
    }
}
