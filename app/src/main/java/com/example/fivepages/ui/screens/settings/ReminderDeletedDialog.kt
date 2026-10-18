package com.example.fivepages.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun ReminderDeletedDialog(message: String, onDone: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    //Popup
    Dialog(onDismissRequest = onDone) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Check Badge
                Icon(
                    Icons.Filled.Verified,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))

                //Title and Message
                Text(
                    text = "Reminder deleted",
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.primary
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))

                //Done Button
                Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    ),
                ) {
                    Text(text = "Done", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
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