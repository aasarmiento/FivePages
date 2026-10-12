package com.example.fivepages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.example.fivepages.navigation.AppNavHost
import com.example.fivepages.state.AppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = remember { AppState() }
            AppNavHost(appState)
        }
    }
}