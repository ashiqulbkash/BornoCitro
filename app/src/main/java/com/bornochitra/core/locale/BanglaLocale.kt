package com.bornochitra.core.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * The app's UI is Bangla only. Applying it on every start also replaces an English choice kept by an
 * older build, whether the framework holds it (Android 13+) or AppCompat does.
 */
object BanglaLocale {

    private val locales = LocaleListCompat.forLanguageTags(AppLanguage.BANGLA.tag)

    fun apply() {
        if (AppCompatDelegate.getApplicationLocales() != locales) AppCompatDelegate.setApplicationLocales(locales)
    }
}
