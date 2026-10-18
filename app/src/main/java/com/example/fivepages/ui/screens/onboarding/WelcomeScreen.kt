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
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import com.example.fivepages.R
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Skip Link
        TextButton(onClick = onSkip) {
            Text(text = "Skip to app →",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant)
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
            Text(
                text = "FivePages",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            Text(
                text = "habit-enforcer app",
                style = MaterialTheme.typography.bodySmall,
                color = colors.primary
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Welcome to FivePages",
                style = MaterialTheme.typography.titleLarge,
                color = colors.onBackground
            )
            Spacer(Modifier.height(6.dp))

            Text(
                text = "A little reading, every day. No marathon sessions required.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
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
                            .background(
                                color = if (i == 0) colors.primary else colors.onSurfaceVariant.copy(alpha = 0.35f),
                                shape = CircleShape)
                    )
                }
            }
        }

        //Get Started Button
        Button(
            onClick = onGetStarted,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun WelcomeScreenPreview() {
    FivePagesTheme {
        WelcomeScreen(onGetStarted = {}, onSkip = {})
    }
}
