package com.bornochitra.core.locale

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** The app's own UI language, kept apart from the phone's language. */
interface AppLanguageStore {
    /** The language the app is shown in: Bangla until another one is chosen. */
    val language: AppLanguage

    /** Shows the whole app in [language] from now on, including after a restart. */
    fun setLanguage(language: AppLanguage)

    /** Chooses Bangla when no language has been chosen yet, so the app does not follow a phone set to English. */
    fun applyDefault()
}

/** The store for code that runs before Hilt injects it, such as an activity before super.onCreate. */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppLanguageEntryPoint {
    fun appLanguageStore(): AppLanguageStore
}
