package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.fivepages.R
import com.example.fivepages.ui.theme.FivePagesTheme


@Composable
fun DiscardSessionDialog(onKeep: () -> Unit, onDiscard: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    Dialog(onDismissRequest = onKeep) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.fivepages_owl1),
                    contentDescription = null,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("Discard this session?", style = MaterialTheme.typography.titleLarge, color = colors.onSurface)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Your reading time for this session won't be saved.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onKeep,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, colors.primary),
                ) {
                    Text("Keep reading", style = MaterialTheme.typography.titleSmall, color = colors.primary)
                }
                TextButton(onClick = onDiscard) {
                    Text("Discard", style = MaterialTheme.typography.labelLarge, color = colors.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiscardSessionDialogPreview() {
    FivePagesTheme { DiscardSessionDialog(onKeep = {}, onDiscard = {}) }
}