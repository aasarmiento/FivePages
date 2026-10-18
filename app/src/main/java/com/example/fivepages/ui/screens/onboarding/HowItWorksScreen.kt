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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.theme.FivePagesTheme


//Step Data
private data class Step(val number: Int, val title: String, val body: String)

//Steps List
private val steps = listOf(
    Step(1, "Set a small daily goal", "Tell us your book and deadline, we do the math."),
    Step(2, "Read in short sprints", "A few minutes counts. Log it when you're done."),
    Step(3, "Build a streak", "See your progress add up, even on busy days.")
)

@Composable
fun HowItWorksScreen(
    onNext: () -> Unit,
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

        //Title and Steps
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("How it works",
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onBackground)
            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                steps.forEach { StepRow(it) }
            }
            Spacer(Modifier.height(28.dp))

            //Page Dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { i ->
                    Box(
                        Modifier
                            .size(width = if (i == 1) 18.dp else 5.dp, height = 5.dp)
                            .background(
                                color = if (i == 1) colors.primary else colors.onSurfaceVariant.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        //Next Button
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            ),
        ) {
            Text(text = "Next",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

//Step Row
@Composable
private fun StepRow(step: Step) {
    val colors = MaterialTheme.colorScheme

    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            Modifier.size(26.dp).background(colors.secondaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step.number.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
            )
        }
        Column {
            Text(
                text = step.title,
                style = MaterialTheme.typography.labelLarge,
                color = colors.onBackground
            )

            Text(
                text = step.body,
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun HowItWorksScreenPreview() {
    FivePagesTheme {
        HowItWorksScreen(onNext = {})
    }
}
