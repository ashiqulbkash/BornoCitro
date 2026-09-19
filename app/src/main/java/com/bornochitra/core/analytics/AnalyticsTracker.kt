package com.bornochitra.core.analytics

import android.util.Log
import javax.inject.Inject

/** Where analytics events go. Callers only say what happened; the destination is decided in one place. */
interface AnalyticsTracker {

    fun track(event: AnalyticsEvent)
}

private const val TAG = "BornoChitraAnalytics"

/**
 * Keeps events on the device, in Logcat. The app is offline-only (plan.md Step 20), so nothing
 * leaves the device; a backend, when one is chosen, is a new [AnalyticsTracker] bound in place of this one.
 */
class LogAnalyticsTracker @Inject constructor() : AnalyticsTracker {

    override fun track(event: AnalyticsEvent) {
        Log.i(TAG, "${event.name} ${event.params}")
    }
}
