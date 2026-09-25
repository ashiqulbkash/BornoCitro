package com.bornochitra.core.onboarding

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFERENCES_NAME = "onboarding"
private const val KEY_WELCOME_SEEN = "welcome_seen"

@Singleton
class SharedPreferencesOnboardingStore @Inject constructor(
    @ApplicationContext context: Context,
) : OnboardingStore {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override val hasSeenWelcome: Boolean
        get() = preferences.getBoolean(KEY_WELCOME_SEEN, false)

    override fun markWelcomeSeen() {
        preferences.edit { putBoolean(KEY_WELCOME_SEEN, true) }
    }
}
