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
fun GoalUpdatedDialog(perDay: Int, finished: Boolean, onDone: () -> Unit) {
    AppDialog(
        onDismiss = onDone,
        icon = { DialogIcon(Icons.Filled.Verified, MaterialTheme.colorScheme.primary) },
        title = "Goal updated",
        message = if (finished) "Your goal is saved." else "You're now aiming for $perDay pages a day.",
    ) {
        DialogPrimaryButton("Done", onDone)
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalUpdatedDialogPreview() {
    FivePagesTheme { GoalUpdatedDialog(perDay = 5, finished = false, onDone = {}) }
}