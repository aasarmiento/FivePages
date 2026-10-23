package com.example.fivepages.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.R
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.FullScreenPage
import com.example.fivepages.ui.components.PageBody
import com.example.fivepages.ui.components.PageDots
import com.example.fivepages.ui.components.PrimaryButton
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    FullScreenPage(modifier) {

        //Skip Link
        AppTextButton("Skip to app →", onSkip)

        //Logo and Welcome Text
        PageBody {
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
            PageDots(current = 0)
        }

        //Get Started Button
        PrimaryButton("Get Started", onGetStarted)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun WelcomeScreenPreview() {
    FivePagesTheme {
        WelcomeScreen(onGetStarted = {}, onSkip = {})
    }
}
