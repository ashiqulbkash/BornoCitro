package com.bornochitra.app.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.bornochitra.app.navigation.BcDestination
import com.bornochitra.app.navigation.BcNavHost
import com.bornochitra.core.database.AppDatabase
import com.bornochitra.core.locale.AppLanguageEntryPoint
import com.bornochitra.core.locale.AppLanguageStore
import com.bornochitra.core.onboarding.OnboardingStore
import com.bornochitra.core.ui.theme.BornoChitraTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "BornoChitraBootstrap"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var onboardingStore: OnboardingStore

    // Needed before super.onCreate, where field injection has not happened yet.
    private val appLanguageStore: AppLanguageStore by lazy {
        EntryPointAccessors.fromApplication<AppLanguageEntryPoint>(applicationContext).appLanguageStore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // BrandSplash draws the same icon in the same place, so the system splash's default fade-out
        // would only dim the icon for a moment: remove it at once instead.
        installSplashScreen().setOnExitAnimationListener { it.remove() }
        // No language chosen yet means Bangla. Below Android 13 AppCompat applies it in place when it is
        // set before the activity is created (on Android 12 a recreate asked for during onCreate never
        // came); from 13 the framework needs the created activity, and recreates it once.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) appLanguageStore.applyDefault()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) appLanguageStore.applyDefault()
        enableEdgeToEdge()
        val startDestination = if (onboardingStore.hasSeenWelcome) BcDestination.Home.route else BcDestination.Welcome.route
        setContent {
            BornoChitraTheme {
                BornoChitraApp(startDestination = startDestination)
            }
        }
        verifyDatabaseInitializes()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Below Android 13 AppCompat applies a new app language in place (the manifest handles locale
        // changes) but tells only the activity, so Compose kept showing the old strings. From 13 the
        // framework owns per-app languages and already sends the change to the views.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) window.decorView.dispatchConfigurationChanged(newConfig)
    }

    /** Bootstrap smoke check: confirms Room opens successfully, off the main thread. */
    private fun verifyDatabaseInitializes() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                appDatabase.openHelper.writableDatabase
            }
            Log.i(TAG, "Room database initialized successfully")
        }
    }
}

@Composable
private fun BornoChitraApp(startDestination: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        // Saved, so the language recreate on first start and configuration changes do not replay it.
        var showBrandSplash by rememberSaveable { mutableStateOf(true) }
        Box {
            val navController = rememberNavController()
            BcNavHost(navController = navController, startDestination = startDestination, modifier = Modifier.fillMaxSize())
            if (showBrandSplash) BrandSplash(onFinished = { showBrandSplash = false })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BornoChitraAppPreview() {
    BornoChitraTheme {
        BornoChitraApp(startDestination = BcDestination.Home.route)
    }
}
