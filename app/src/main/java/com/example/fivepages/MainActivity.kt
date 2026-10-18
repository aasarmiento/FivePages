package com.example.fivepages

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import com.example.fivepages.navigation.AppNavHost
import com.example.fivepages.state.AppState
import com.example.fivepages.ui.theme.FivePagesTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FivePagesTheme {
                val appState = remember { AppState() }
                AppNavHost(appState)
            }
        }
    }
}