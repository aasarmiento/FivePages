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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.fivepages.R

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit,
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

        //Skip Link
        TextButton(onClick = onSkip) {
            Text("Skip to app →", fontFamily = FpSans, fontSize = 11.sp, color = FpColor.Muted)
        }

        //Logo and Welcome Text
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.fivepages_logo),
                contentDescription = "FivePages logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(8.dp))
            Text("FivePages", fontFamily = FpSerif, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = FpColor.Green)
            Text("habit-enforcer app", fontFamily = FpSerif, fontSize = 13.sp, color = FpColor.Green)
            Spacer(Modifier.height(20.dp))
            Text("Welcome to FivePages", fontFamily = FpSerif, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = FpColor.Ink)
            Spacer(Modifier.height(6.dp))
            Text(
                "A little reading, every day. No marathon sessions required.",
                fontFamily = FpSans,
                fontSize = 13.sp,
                color = FpColor.Muted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
            Spacer(Modifier.height(20.dp))

            //Page Dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { i ->
                    Box(
                        Modifier
                            .size(width = if (i == 0) 18.dp else 5.dp, height = 5.dp)
                            .background(if (i == 0) FpColor.Green else FpColor.Muted.copy(alpha = 0.35f), CircleShape)
                    )
                }
            }
        }

        //Get Started Button
        Button(
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FpColor.Green, contentColor = FpColor.White),
        ) {
            Text("Get Started", fontFamily = FpSans, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onGetStarted = {}, onSkip = {})
}
