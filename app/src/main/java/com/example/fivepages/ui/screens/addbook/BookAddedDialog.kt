package com.example.fivepages.ui.screens.addbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fivepages.R
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogImage
import com.example.fivepages.ui.components.DialogPrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun BookAddedDialog(title: String, perDay: Int, pages: Int, days: Int, onDone: () -> Unit) {
    AppDialog(
        onDismiss = onDone,
        icon = { DialogImage(R.drawable.fivepages_owl1) },
        title = "Added to your library!",
        message = "$title · $perDay pages/day\n$pages pages over $days days",
    ) {
        DialogPrimaryButton("Go to Library", onDone)
    }
}

@Preview(showBackground = true)
@Composable
private fun BookAddedDialogPreview() {
    FivePagesTheme {
        BookAddedDialog(title = "Atomic Habits", perDay = 5, pages = 250, days = 57, onDone = {})
    }
}