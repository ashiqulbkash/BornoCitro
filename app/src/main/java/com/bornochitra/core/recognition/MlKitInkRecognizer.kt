package com.bornochitra.core.recognition

import android.util.Log
import com.bornochitra.core.tracing.TracePoint
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.recognition.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.recognition.Ink
import com.google.mlkit.vision.digitalink.recognition.RecognitionContext
import com.google.mlkit.vision.digitalink.recognition.WritingArea
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "MlKitInkRecognizer"

/** The writing area is the exercise canvas, which is always 100 units square. */
private const val CANVAS_UNITS = 100f

/**
 * [InkRecognizer] on ML Kit Digital Ink Recognition. Its models are downloaded once per script and
 * then run on the device; until one is there, [recognize] returns null and nothing is read.
 * [readyScripts] remembers the models known to be on the device, so each is asked about only once.
 */
@Singleton
class MlKitInkRecognizer @Inject constructor() : InkRecognizer {

    private val modelManager = RemoteModelManager.getInstance()
    private val readyScripts = ConcurrentHashMap.newKeySet<WritingScript>()
    private val recognizers = ConcurrentHashMap<WritingScript, DigitalInkRecognizer>()

    override suspend fun isModelReady(script: WritingScript): Boolean {
        if (script in readyScripts) return true
        val model = modelFor(script) ?: return false
        return try {
            (modelManager.isModelDownloaded(model).await() == true).also { if (it) readyScripts += script }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            Log.w(TAG, "Could not check the ${script.languageTag} handwriting model", exception)
            false
        }
    }

    override suspend fun downloadModel(script: WritingScript): Boolean {
        if (script in readyScripts) return true
        val model = modelFor(script) ?: return false
        return try {
            modelManager.download(model, DownloadConditions.Builder().build()).await()
            readyScripts += script
            true
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            Log.w(TAG, "Could not download the ${script.languageTag} handwriting model", exception)
            false
        }
    }

    override suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String>? {
        if (!isModelReady(script)) return null
        val model = modelFor(script) ?: return null
        return try {
            val recognizer = recognizers.getOrPut(script) {
                DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
            }
            val context = RecognitionContext.builder()
                .setPreContext("")
                .setWritingArea(WritingArea(CANVAS_UNITS, CANVAS_UNITS))
                .build()
            recognizer.recognize(ink.toMlKitInk(), context).await().candidates.map { it.text }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (exception: Exception) {
            // Not being able to read the ink is not fatal: the blank simply stays unfilled.
            Log.w(TAG, "Handwriting recognition failed", exception)
            null
        }
    }

    private fun modelFor(script: WritingScript): DigitalInkRecognitionModel? {
        val identifier = try {
            DigitalInkRecognitionModelIdentifier.fromLanguageTag(script.languageTag)
        } catch (exception: MlKitException) {
            Log.w(TAG, "No handwriting model for ${script.languageTag}", exception)
            null
        } ?: return null
        return DigitalInkRecognitionModel.builder(identifier).build()
    }
}

private fun List<List<TracePoint>>.toMlKitInk(): Ink {
    val builder = Ink.builder()
    filter { it.isNotEmpty() }.forEach { stroke ->
        val strokeBuilder = Ink.Stroke.builder()
        stroke.forEach { strokeBuilder.addPoint(Ink.Point.create(it.x, it.y, it.timestampMs)) }
        builder.addStroke(strokeBuilder.build())
    }
    return builder.build()
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { continuation.resume(it) }
    addOnFailureListener { continuation.resumeWithException(it) }
}
