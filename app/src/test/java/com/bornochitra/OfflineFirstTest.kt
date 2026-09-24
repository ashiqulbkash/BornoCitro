package com.bornochitra

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineFirstTest {

    /**
     * The one exception to working offline: fill-in-the-blanks has no offline reader, so it checks
     * for a connection and downloads ML Kit's handwriting models once. Those two permissions are
     * there for that alone, and only while ML Kit is a dependency; no other network permission is.
     */
    @Test
    fun `the network is requested only for the handwriting models`() {
        val manifest = File("src/main/AndroidManifest.xml").readText()
        val buildScript = File("build.gradle.kts").readText()
        val requested = Regex("""android\.permission\.([A-Z_]+)""").findAll(manifest).map { it.groupValues[1] }.toSet()
        val networkPermissions = requested.filter { "NETWORK" in it || "INTERNET" in it || "WIFI" in it }.toSet()

        assertTrue(
            "only fill-in-the-blanks' model download may use the network, not $networkPermissions",
            setOf("INTERNET", "ACCESS_NETWORK_STATE").containsAll(networkPermissions),
        )
        if (networkPermissions.isNotEmpty()) {
            assertTrue("the network is only for the handwriting models", buildScript.contains("mlkit.digital.ink"))
        }
    }

    @Test
    fun `no networking library is a dependency`() {
        val buildScript = File("build.gradle.kts").readText()

        listOf("retrofit", "okhttp", "ktor", "firebase", "coil").forEach { library ->
            assertFalse("$library must not be added: the app works fully offline", buildScript.contains(library, ignoreCase = true))
        }
    }
}
