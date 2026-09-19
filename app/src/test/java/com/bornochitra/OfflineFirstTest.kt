package com.bornochitra

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Test

class OfflineFirstTest {

    @Test
    fun `manifest does not request network permissions`() {
        val manifest = File("src/main/AndroidManifest.xml").readText()

        assertFalse(manifest.contains("android.permission.INTERNET"))
        assertFalse(manifest.contains("android.permission.ACCESS_NETWORK_STATE"))
    }

    @Test
    fun `no networking library is a dependency`() {
        val buildScript = File("build.gradle.kts").readText()

        listOf("retrofit", "okhttp", "ktor", "firebase", "coil").forEach { library ->
            assertFalse("$library must not be added: the app works fully offline", buildScript.contains(library, ignoreCase = true))
        }
    }
}
