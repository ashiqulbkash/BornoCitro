package com.bornochitra.core.recognition

import com.bornochitra.core.recognition.di.RecognitionModule
import com.bornochitra.core.tracing.TracePoint
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Gives every Hilt test handwriting models that are already on the device, so the fill-in-the-blanks
 * download dialog never stands in the way and no test needs the network. Nothing is read by a model, so
 * fill-in-the-blanks falls back to shape matching.
 */
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RecognitionModule::class])
object TestRecognitionModule {

    @Provides
    @Singleton
    fun provideInkRecognizer(): InkRecognizer = object : InkRecognizer {
        override suspend fun isModelReady(script: WritingScript) = true

        override suspend fun downloadModel(script: WritingScript) = true

        override suspend fun recognize(ink: List<List<TracePoint>>, script: WritingScript): List<String>? = null
    }
}
