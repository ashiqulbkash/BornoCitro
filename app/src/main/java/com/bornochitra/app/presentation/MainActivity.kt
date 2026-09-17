package com.bornochitra.app.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
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
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "বর্ণচিত্র",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BornoChitraAppPreview() {
    BornoChitraTheme {
        BornoChitraApp()
    }
}
