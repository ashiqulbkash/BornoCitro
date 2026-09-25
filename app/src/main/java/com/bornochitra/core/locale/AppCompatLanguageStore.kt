package com.bornochitra.core.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import javax.inject.Inject

/**
 * Per-app language through AppCompat: the framework's per-app locale on Android 13+ (also shown in
 * the system's app language settings), AppCompat's own storage (`autoStoreLocales`) below it.
 * Changing it recreates the running activities in the new language.
 */
class AppCompatLanguageStore @Inject constructor() : AppLanguageStore {

    override val language: AppLanguage
        get() = when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
            AppLanguage.ENGLISH.tag -> AppLanguage.ENGLISH
            else -> AppLanguage.BANGLA
        }

    override fun setLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.tag))
    }

    override fun applyDefault() {
        if (AppCompatDelegate.getApplicationLocales().isEmpty) setLanguage(AppLanguage.BANGLA)
    }
}
