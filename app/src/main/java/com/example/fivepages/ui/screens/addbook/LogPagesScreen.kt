package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.Book

@Composable
fun LogPagesScreen(
    book: Book,
    onSave: (pages: Int) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Log Pages for: ${book.title}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onSave(5) }) {
                Text("Save 5 Pages (Test)")
            }
        }
    }
}
