package com.bornochitra.core.onboarding

/** Whether the child has been through the first-launch Welcome screen. */
interface OnboardingStore {
    val hasSeenWelcome: Boolean

    fun markWelcomeSeen()
}
