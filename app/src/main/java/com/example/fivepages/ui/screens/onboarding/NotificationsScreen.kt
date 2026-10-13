package com.example.fivepages.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.FpColor
import com.example.fivepages.FpSans
import com.example.fivepages.FpSerif

@Composable
fun NotificationsScreen(
    onEnableReminders: () -> Unit,
    onNotNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FpColor.Background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Title, Bell and Message
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Gentle reminders", fontFamily = FpSerif, fontWeight = FontWeight.Medium, fontSize = 22.sp, color = FpColor.Ink)
            Spacer(Modifier.height(20.dp))
            Icon(
                Icons.Filled.Notifications,
                contentDescription = null,
                tint = FpColor.Green,
                modifier = Modifier.size(52.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "We can nudge you during your free time, like your morning coffee, so reading doesn't get lost in the day. You choose the windows, and can turn this off anytime in Settings.",
                fontFamily = FpSans,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = FpColor.Muted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
            Spacer(Modifier.height(28.dp))

            //Page Dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { i ->
                    Box(
                        Modifier
                            .size(width = if (i == 2) 18.dp else 5.dp, height = 5.dp)
                            .background(if (i == 2) FpColor.Green else FpColor.Muted.copy(alpha = 0.35f), CircleShape)
                    )
                }
            }
        }

        //Enable Reminders Button
        Button(
            onClick = onEnableReminders,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FpColor.Green, contentColor = FpColor.White),
        ) {
            Text("Enable reminders", fontFamily = FpSans, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        //Not Now Link
        TextButton(onClick = onNotNow) {
            Text("Not now", fontFamily = FpSans, fontSize = 12.sp, color = FpColor.Muted)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun NotificationsScreenPreview() {
    NotificationsScreen(onEnableReminders = {}, onNotNow = {})
}
