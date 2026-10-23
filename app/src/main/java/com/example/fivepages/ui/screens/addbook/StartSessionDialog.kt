package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogDangerButton
import com.example.fivepages.ui.components.DialogIcon
import com.example.fivepages.ui.components.DialogPrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun StartSessionDialog(onStart: () -> Unit, onCancel: () -> Unit) {
    AppDialog(
        onDismiss = onCancel,
        icon = { DialogIcon(Icons.AutoMirrored.Filled.MenuBook, MaterialTheme.colorScheme.primary) },
        title = "Start Reading Session?",
        message = "The timer starts right away. Log your pages when you're done.",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DialogPrimaryButton("Start", onStart, Modifier.weight(1f))
            DialogDangerButton("Discard", onCancel, Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartSessionDialogPreview() {
    FivePagesTheme { StartSessionDialog(onStart = {}, onCancel = {}) }
}
