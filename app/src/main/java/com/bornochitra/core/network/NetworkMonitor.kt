package com.bornochitra.core.network

import kotlinx.coroutines.flow.Flow

/**
 * Whether the device can reach the internet. The app works offline; only fill-in-the-blanks needs
 * the internet, once, to download its handwriting models.
 */
interface NetworkMonitor {
    /** The current state first, then every change. */
    val isOnline: Flow<Boolean>
}
