package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogIcon
import com.example.fivepages.ui.components.DialogOutlineButton
import com.example.fivepages.ui.components.DialogTextButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun DeleteBookDialog(bookTitle: String, onKeep: () -> Unit, onDelete: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    AppDialog(
        onDismiss = onKeep,
        icon = { DialogIcon(Icons.Outlined.Report, colors.error) },
        title = "Delete this book?",
        message = "This removes \"$bookTitle\" and its reading history. There's no cloud backup, so this can't be undone.",
    ) {
        DialogOutlineButton("Keep Book", onKeep)
        DialogTextButton("Delete Book", colors.error, onDelete)
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteBookDialogPreview() {
    FivePagesTheme { DeleteBookDialog(bookTitle = "Atomic Habits", onKeep = {}, onDelete = {}) }
}