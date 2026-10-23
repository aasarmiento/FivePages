package com.example.fivepages.ui.screens.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.ui.components.FullScreenPage
import com.example.fivepages.ui.components.PageBody
import com.example.fivepages.ui.components.PageDots
import com.example.fivepages.ui.components.PrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun NotificationsScreen(
    onEnableReminders: () -> Unit,
    onNotNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    FullScreenPage(modifier) {

        //Title, Bell and Message
        PageBody {
            Text(
                "Gentle reminders",
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onBackground
            )
            Spacer(Modifier.height(20.dp))
            Icon(
                Icons.Filled.Notifications,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(52.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "We can nudge you during your free time, like your morning coffee, so reading doesn't get lost in the day. You choose the windows, and can turn this off anytime in Settings.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
            Spacer(Modifier.height(28.dp))

            //Page Dots
            PageDots(current = 2)
        }

        //Enable Reminders Button
        PrimaryButton("Enable reminders", onEnableReminders)

        //Not Now Link
        TextButton(onClick = onNotNow) {
            Text(
                text = "Not now",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun NotificationsScreenPreview() {
    FivePagesTheme {
        NotificationsScreen(onEnableReminders = {}, onNotNow = {})
    }
}
