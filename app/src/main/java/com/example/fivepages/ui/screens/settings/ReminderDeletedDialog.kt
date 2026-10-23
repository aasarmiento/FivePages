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
fun ReminderDeletedDialog(message: String, onDone: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    AppDialog(
        onDismiss = onDone,
        icon = { DialogIcon(Icons.Filled.Verified, colors.primary) },
        title = "Reminder deleted",
        message = message,
        titleColor = colors.primary,
    ) {
        DialogPrimaryButton("Done", onDone)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReminderDeletedDialogPreview() {
    FivePagesTheme {
        ReminderDeletedDialog(
            message = "Morning coffee - 8:00 AM has been removed. You can add it back anytime.",
            onDone = {}
        )
    }
}
