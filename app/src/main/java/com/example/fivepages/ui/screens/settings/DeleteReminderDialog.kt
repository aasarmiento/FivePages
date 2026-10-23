package com.example.fivepages.ui.screens.settings

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
fun DeleteReminderDialog(message: String, onKeep: () -> Unit, onDelete: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    AppDialog(
        onDismiss = onKeep,
        icon = { DialogIcon(Icons.Outlined.Report, colors.error) },
        title = "Delete this reminder?",
        message = message,
    ) {
        DialogOutlineButton("Keep Reminder", onKeep)
        DialogTextButton("Delete Reminder", colors.error, onDelete)
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteReminderDialogPreview() {
    FivePagesTheme {
        DeleteReminderDialog(
            message = "This turns off your Morning coffee notification. You can add it back later.",
            onKeep = {},
            onDelete = {}
        )
    }
}
