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
fun SessionLoggedScreen(
    pagesAdded: Int,
    onReturnToLibrary: () -> Unit,
    streakDays: Int? = null,
    onUndo: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    val pagesText = if (pagesAdded == 1) "1 page added" else "$pagesAdded pages added"
    val streakText = streakDays?.let { " · $it-day streak" } ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.fivepages_owl1),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text("Nice one!", style = MaterialTheme.typography.headlineSmall, color = colors.onBackground)
            Spacer(Modifier.height(6.dp))
            Text("Session logged", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Text(
                "$pagesText$streakText",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onReturnToLibrary,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
        ) {
            Text("Return to Library", style = MaterialTheme.typography.titleSmall)
        }

        if (onUndo != null) {
            TextButton(onClick = onUndo) {
                Text(
                    "Logged the wrong number? Undo",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun SessionLoggedScreenPreview() {
    FivePagesTheme {
        SessionLoggedScreen(pagesAdded = 5, streakDays = 1, onReturnToLibrary = {}, onUndo = {})
    }
}