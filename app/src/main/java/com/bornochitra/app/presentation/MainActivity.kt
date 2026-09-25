package com.bornochitra.app.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.bornochitra.app.navigation.BcNavHost
import com.bornochitra.core.database.AppDatabase
import com.bornochitra.core.locale.AppLanguageEntryPoint
import com.bornochitra.core.locale.AppLanguageStore
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

    // Needed before super.onCreate, where field injection has not happened yet.
    private val appLanguageStore: AppLanguageStore by lazy {
        EntryPointAccessors.fromApplication<AppLanguageEntryPoint>(applicationContext).appLanguageStore()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // No language chosen yet means Bangla. Below Android 13 AppCompat applies it in place when it is
        // set before the activity is created (on Android 12 a recreate asked for during onCreate never
        // came); from 13 the framework needs the created activity, and recreates it once.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) appLanguageStore.applyDefault()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) appLanguageStore.applyDefault()
        enableEdgeToEdge()
        setContent {
            BornoChitraTheme {
                BornoChitraApp()
            }
        }
        verifyDatabaseInitializes()
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
private fun BornoChitraApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val navController = rememberNavController()
        BcNavHost(navController = navController, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
private fun BornoChitraAppPreview() {
    BornoChitraTheme {
        BornoChitraApp()
    }
}
