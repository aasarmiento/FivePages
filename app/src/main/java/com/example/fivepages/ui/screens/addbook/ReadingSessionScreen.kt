package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.ui.theme.FivePagesTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun ReadingSessionScreen(
    onComplete: () -> Unit,
    onDiscard: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    var accumulatedMs by rememberSaveable { mutableLongStateOf(0L) }   // time banked before the current run
    var runStartedAt by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) } // starts running immediately
    var running by rememberSaveable { mutableStateOf(true) }
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(running) {
        while (running) {
            now = System.currentTimeMillis()
            delay(250)
        }
    }

    val elapsedMs = accumulatedMs + if (running) (now - runStartedAt).coerceAtLeast(0) else 0L

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Column {
                Text(
                    "Reading Session",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.onBackground,
                    modifier = Modifier.statusBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp),
                )
                HorizontalDivider(color = colors.outline)
            }
        },
        bottomBar = {
            Column(Modifier.navigationBarsPadding().padding(20.dp)) {
                Button(
                    onClick = {

                        if (running) accumulatedMs = elapsedMs
                        running = false
                        onComplete()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) { Text("Complete Session", style = MaterialTheme.typography.titleSmall) }
            }
        },
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                Modifier.size(200.dp).border(8.dp, colors.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        formatElapsed(elapsedMs),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 44.sp, fontWeight = FontWeight.Bold),
                        color = colors.onSurface,
                    )
                    Text(
                        if (running) "elapsed" else "paused",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            OutlinedButton(
                onClick = {
                    if (running) {
                        accumulatedMs = elapsedMs
                        running = false
                    } else {
                        runStartedAt = System.currentTimeMillis()
                        now = runStartedAt
                        running = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, colors.primary),
                modifier = Modifier.width(160.dp).height(48.dp),
            ) {
                Text(
                    if (running) "Pause" else "Resume",
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.primary,
                )
            }
            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onDiscard) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline, fontStyle = FontStyle.Italic)) {
                            append("Discard this session")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.error,
                )
            }
        }
    }
}


private fun formatElapsed(ms: Long): String {
    val total = ms / 1000
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun ReadingSessionPreview() {
    FivePagesTheme { ReadingSessionScreen(onComplete = {}, onDiscard = {}) }
}