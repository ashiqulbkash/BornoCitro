package com.bornochitra.feature.about

import androidx.annotation.StringRes
import com.bornochitra.R

/** What the app offers, in order. Shared by onboarding's second page and the Grown-ups tab. */
enum class AboutFeature(@StringRes val text: Int) {
    LETTERS(R.string.about_feature_letters),
    NUMBERS(R.string.about_feature_numbers),
    DRAWING(R.string.about_feature_drawing),
    PROGRESS(R.string.about_feature_progress),
}
