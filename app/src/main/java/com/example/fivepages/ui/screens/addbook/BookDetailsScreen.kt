package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.Book
import com.example.fivepages.model.dailyGoal
import com.example.fivepages.model.percentDone
import com.example.fivepages.model.progressSegments
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.CircleIconButton
import com.example.fivepages.ui.components.PrimaryButton
import com.example.fivepages.ui.components.OutlineButton
import com.example.fivepages.model.remainingDays

@Composable
fun BookDetailsScreen(
    book: Book,
    onBack: () -> Unit,
    onStartSession: () -> Unit,
    onLogPages: () -> Unit,
    onOptions: () -> Unit,
    dayStreak: Int = 0,
) {
    val colors = MaterialTheme.colorScheme
    var showStart by remember { mutableStateOf(false) }
    val pagesLeft = (book.totalPages - book.pagesRead).coerceAtLeast(0)
    val percent = percentDone(book.pagesRead, book.totalPages)
    val filledBars = progressSegments(book.pagesRead, book.totalPages)
    val goal = dailyGoal(pagesLeft, remainingDays(book.targetDateMillis))

    Scaffold(
        containerColor = colors.background,
        topBar = {
            BackTopBar(
                title = book.title,
                onBack = onBack,
                trailing = {
                    CircleIconButton(Icons.Filled.MoreVert, "Book options", onOptions)
                }
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Starts the reading session timer
                PrimaryButton(
                    label = if (pagesLeft > 0) "Start Reading Session" else "Book finished",
                    onClick = { showStart = true }, //on-start session wire
                    enabled = pagesLeft > 0,
                )

                // Manual Page Logging
                if (pagesLeft > 0) {
                    OutlineButton(
                        label = "Log pages manually",
                        onClick = onLogPages, // on-log session wire
                    )
                }
            }
        },
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clearAndSetSemantics { contentDescription = "$percent percent read" },
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(10) { index ->
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                color = if (index < filledBars) colors.primary else colors.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            HorizontalDivider(color = colors.outline)
            Row(Modifier.fillMaxWidth()) {
                Stat("$pagesLeft", "pages left", Modifier.weight(1f))
                Stat("$goal", "pages/day goal", Modifier.weight(1f))
                Stat("$dayStreak", "day streak", Modifier.weight(1f))
            }
        }
    }

    if (showStart) {
        StartSessionDialog(
            onStart = { showStart = false; onStartSession() },
            onCancel = { showStart = false }
        )
    }
}

@Composable
private fun Stat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailsPreview() {
    FivePagesTheme {
        BookDetailsScreen(
            book = Book(
                id = 1,
                title = "Before the Coffee Gets Cold",
                totalPages = 280,
                pagesRead = 120,
                targetDateMillis = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)
            ),
            onBack = {},
            onStartSession = {}, //mock callback in Preview
            onLogPages = {},
            onOptions = {},
            dayStreak = 1
        )
    }
}