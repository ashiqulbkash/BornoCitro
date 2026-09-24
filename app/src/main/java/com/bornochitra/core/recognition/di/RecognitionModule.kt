package com.bornochitra.core.recognition.di

import com.bornochitra.core.recognition.InkRecognizer
import com.bornochitra.core.recognition.MlKitInkRecognizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecognitionModule {

    @Binds
    abstract fun bindInkRecognizer(impl: MlKitInkRecognizer): InkRecognizer
}
