package com.example.fivepages.ui.screens.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.R
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.PrimaryButton

@Composable
fun MissedYesterdayScreen(
    streak: Int,
    freezesLeft: Int,
    onBack: () -> Unit,
    onUseFreeze: () -> Unit,
    onStartFresh: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "Missed yesterday", onBack = onBack) },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    label = buildAnnotatedString {
                        append("Use Streak Freeze ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Normal)) {
                            append("($freezesLeft left)")
                        }
                    },
                    onClick = onUseFreeze,
                    enabled = freezesLeft > 0,
                )
                AppTextButton("Start a new streak today", onStartFresh)
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your streak is safe.",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.streak_shield),
                contentDescription = null,
                modifier = Modifier.size(110.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Life happens.",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.primary
            )
            Spacer(Modifier.height(16.dp))
            BodyLines(
                buildAnnotatedString {
                    append("Use a streak freeze to keep your\n")
                    bold("${streak}-day streak")
                    append(" going, or start fresh today.")
                }
            )
            Spacer(Modifier.height(20.dp))
            BodyLines(AnnotatedString("Either way, your progress\non the book itself is untouched."))
        }
    }
}

@Composable
fun GraceDayUsedScreen(
    streak: Int,
    freezesLeft: Int,
    onBackToProgress: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val freezeWord = if (freezesLeft == 1) "freeze" else "freezes"

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                PrimaryButton("Back to Progress", onBackToProgress)
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.streak_comet),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Streak Grace Day Used!",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )
            Spacer(Modifier.height(12.dp))
            BodyLines(
                buildAnnotatedString {
                    append("Your ")
                    bold("${streak}-day streak")
                    append(" is safe.\nYou have ")
                    bold("$freezesLeft $freezeWord")
                    append(" left this month.\nThey refill on the 1st.\nYour book progress and goal weren't affected.")
                }
            )
        }
    }
}

@Composable
fun FreshStartScreen(
    onStartSession: () -> Unit,
    onBackToProgress: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton("Start a Session Now", onStartSession)
                AppTextButton("Back to Progress", onBackToProgress)
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.streak_fresh_start),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Fresh Start",
                style = MaterialTheme.typography.headlineMedium,
                color = colors.primary
            )
            Spacer(Modifier.height(12.dp))
            BodyLines(
                buildAnnotatedString {
                    append("Your streak is now ")
                    bold("1 day")
                    append(".\nYesterday doesn't count against you.\nEvery day is a new chance to read.")
                }
            )
        }
    }
}

@Composable
private fun BodyLines(text: AnnotatedString) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}

private fun AnnotatedString.Builder.bold(text: String) {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text) }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun MissedYesterdayPreview() {
    FivePagesTheme {
        MissedYesterdayScreen(streak = 5, freezesLeft = 1, onBack = {}, onUseFreeze = {}, onStartFresh = {})
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun GraceDayUsedPreview() {
    FivePagesTheme {
        GraceDayUsedScreen(streak = 5, freezesLeft = 0, onBackToProgress = {})
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun FreshStartPreview() {
    FivePagesTheme {
        FreshStartScreen(onStartSession = {}, onBackToProgress = {})
    }
}