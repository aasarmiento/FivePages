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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.FpColor
import com.example.fivepages.FpSans
import com.example.fivepages.FpSerif

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FpColor.Background)
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
            Text("How it works", fontFamily = FpSerif, fontWeight = FontWeight.Medium, fontSize = 22.sp, color = FpColor.Ink)
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
                            .background(if (i == 1) FpColor.Green else FpColor.Muted.copy(alpha = 0.35f), CircleShape)
                    )
                }
            }
        }

        //Next Button
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FpColor.Green, contentColor = FpColor.White),
        ) {
            Text("Next", fontFamily = FpSans, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

//Step Row
@Composable
private fun StepRow(step: Step) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            Modifier.size(26.dp).background(FpColor.Tint, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(step.number.toString(), fontFamily = FpSans, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = FpColor.Green)
        }
        Column {
            Text(step.title, fontFamily = FpSans, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FpColor.Ink)
            Text(step.body, fontFamily = FpSans, fontSize = 12.sp, color = FpColor.Muted)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun HowItWorksScreenPreview() {
    HowItWorksScreen(onNext = {})
}
