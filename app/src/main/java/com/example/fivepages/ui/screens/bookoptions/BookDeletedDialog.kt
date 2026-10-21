package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogIcon
import com.example.fivepages.ui.components.DialogPrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun BookDeletedDialog(bookTitle: String, onDone: () -> Unit) {
    AppDialog(
        onDismiss = onDone,
        icon = { DialogIcon(Icons.Filled.Verified, MaterialTheme.colorScheme.primary) },
        title = "Book deleted",
        message = "\"$bookTitle\" and its reading history have been removed. This can't be undone.",
    ) {
        DialogPrimaryButton("Done", onDone)
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDeletedDialogPreview() {
    FivePagesTheme { BookDeletedDialog(bookTitle = "Atomic Habits", onDone = {}) }
}