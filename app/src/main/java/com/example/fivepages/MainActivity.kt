package com.example.fivepages

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.fivepages.navigation.AppNavHost
import com.example.fivepages.state.AppState
import com.example.fivepages.ui.theme.FivePagesTheme

class MainActivity : ComponentActivity() {
    private val appState: AppState by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FivePagesTheme {
                AppNavHost(appState)
            }
        }
    }
}