package com.example.fivepages.ui.screens.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogIcon
import com.example.fivepages.ui.components.DialogPrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun RemindersOnDialog(onDismiss: () -> Unit) {
    AppDialog(
        onDismiss = onDismiss,
        icon = { DialogIcon(Icons.Filled.Notifications, MaterialTheme.colorScheme.primary) },
        title = "Reminders are on!",
        message = "Don't worry, you can disable and edit this in Settings later.",
    ) {
        DialogPrimaryButton("Okay, got it!", onDismiss)
    }
}

@Preview(showBackground = true)
@Composable
private fun RemindersOnDialogPreview() {
    FivePagesTheme {
        RemindersOnDialog(onDismiss = {})
    }
}
