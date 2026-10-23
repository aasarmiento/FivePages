package com.example.fivepages.ui.screens.addbook

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fivepages.R
import com.example.fivepages.ui.components.AppDialog
import com.example.fivepages.ui.components.DialogImage
import com.example.fivepages.ui.components.DialogOutlineButton
import com.example.fivepages.ui.components.DialogTextButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun DiscardSessionDialog(onKeep: () -> Unit, onDiscard: () -> Unit) {
    AppDialog(
        onDismiss = onKeep,
        icon = { DialogImage(R.drawable.fivepages_owl1) },
        title = "Discard this session?",
        message = "Your reading time for this session won't be saved.",
    ) {
        DialogOutlineButton("Keep reading", onKeep)
        DialogTextButton("Discard", MaterialTheme.colorScheme.error, onDiscard)
    }
}

@Preview(showBackground = true)
@Composable
private fun DiscardSessionDialogPreview() {
    FivePagesTheme { DiscardSessionDialog(onKeep = {}, onDiscard = {}) }
}
