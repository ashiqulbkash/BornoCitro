package com.bornochitra.app.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.bornochitra.core.ui.theme.BornoChitraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "BornoChitraBootstrap"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
