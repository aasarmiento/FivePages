package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun DeleteBookDialog(bookTitle: String, onKeep: () -> Unit, onDelete: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    Dialog(onDismissRequest = onKeep) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.Report,
                    contentDescription = null,
                    tint = colors.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("Delete this book?", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
                Spacer(Modifier.height(6.dp))
                Text(
                    "This removes \"$bookTitle\" and its reading history. There's no cloud backup, so this can't be undone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onKeep,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, colors.primary),
                ) {
                    Text("Keep Book", style = MaterialTheme.typography.titleSmall, color = colors.primary)
                }
                TextButton(onClick = onDelete) {
                    Text("Delete Book", style = MaterialTheme.typography.labelLarge, color = colors.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteBookDialogPreview() {
    FivePagesTheme { DeleteBookDialog(bookTitle = "Atomic Habits", onKeep = {}, onDelete = {}) }
}