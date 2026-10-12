package com.example.fivepages.ui.screens.addbook
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddBookScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) {
        Text("Add a book")
        Button(onClick = onBack) { Text("Back") }
    }
}