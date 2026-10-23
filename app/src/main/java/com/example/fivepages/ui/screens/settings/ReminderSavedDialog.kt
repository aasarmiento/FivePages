package com.example.fivepages.ui.screens.settings

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
fun ReminderSavedDialog(message: String, onDone: () -> Unit) {
    AppDialog(
        onDismiss = onDone,
        icon = { DialogIcon(Icons.Filled.Verified, MaterialTheme.colorScheme.primary) },
        title = "Reminder saved",
        message = message,
    ) {
        DialogPrimaryButton("Done", onDone)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReminderSavedDialogPreview() {
    FivePagesTheme {
        ReminderSavedDialog(message = "Your Morning coffee reminder is set for 8:00 AM.", onDone = {})
    }
}
