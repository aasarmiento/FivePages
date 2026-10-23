package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.R
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.FullScreenPage
import com.example.fivepages.ui.components.PageBody
import com.example.fivepages.ui.components.PrimaryButton
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

    FullScreenPage {
        PageBody {
            Image(
                painter = painterResource(R.drawable.fivepages_owl1),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text("Nice one!", style = MaterialTheme.typography.headlineSmall, color = colors.onBackground)
            Spacer(Modifier.height(6.dp))
            Text("Session logged", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "$pagesText$streakText",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                if (streakDays != null) {
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        PrimaryButton("Return to Library", onReturnToLibrary)

        if (onUndo != null) {
            Spacer(Modifier.height(8.dp))
            AppTextButton(
                label = "Logged the wrong number? Undo",
                onClick = onUndo,
                color = colors.error,
                asLink = true,
            )
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