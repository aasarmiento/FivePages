package com.example.fivepages.ui.screens.progress

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.ProgressSummary
import com.example.fivepages.model.WeekDay
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.AppBottomBar
import com.example.fivepages.ui.components.AppTab
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.TitleTopBar

private val MinBarHeight = 14.dp
private val MaxBarExtra = 76.dp

@Composable
fun ProgressScreen(
    summary: ProgressSummary,
    onTabSelected: (String) -> Unit,
    onPreviewMissedDay: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = { TitleTopBar("Progress") },
        bottomBar = { AppBottomBar(AppTab.Progress, onTabSelected) },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Stat("${summary.dayStreak}", "day streak", Modifier.weight(1f))
                Stat("${summary.booksActive}", "books active", Modifier.weight(1f))
                Stat("${summary.sessionsThisMonth}", "sessions/mo", Modifier.weight(1f))
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Last 7 days",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )
                WeekChart(summary.week)
            }

            if (onPreviewMissedDay != null) {
                AppTextButton(
                    label = "Preview: what happens if I miss a day?",
                    onClick = onPreviewMissedDay,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = colors.error,
                    asLink = true,
                )
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WeekChart(week: List<WeekDay>) {
    val colors = MaterialTheme.colorScheme
    val maxPages = week.maxOfOrNull { it.pages }?.coerceAtLeast(1) ?: 1

    Row(
        Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        week.forEach { day ->
            val barHeight = MinBarHeight + MaxBarExtra * (day.pages.toFloat() / maxPages)
            val description = when {
                day.isFuture -> "${day.name}, not yet"
                day.pages == 0 -> "${day.name}, no reading"
                day.pages == 1 -> "${day.name}, 1 page"
                else -> "${day.name}, ${day.pages} pages"
            }
            Column(
                Modifier
                    .weight(1f)
                    .clearAndSetSemantics { contentDescription = description },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(barHeight)
                        .background(
                            color = if (day.pages > 0) colors.primary else colors.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = day.letter,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (day.isToday) colors.primary else colors.onSurfaceVariant
                )
            }
        }
    }
}

private fun sampleWeek(pages: List<Int>, todayIndex: Int): List<WeekDay> {
    val letters = listOf("M", "T", "W", "T", "F", "S", "S")
    val names = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    return letters.indices.map { i ->
        WeekDay(letters[i], names[i], pages[i], isToday = i == todayIndex, isFuture = i > todayIndex)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun ProgressScreenEmptyPreview() {
    FivePagesTheme {
        ProgressScreen(
            summary = ProgressSummary(0, 0, 0, sampleWeek(List(7) { 0 }, todayIndex = 2)),
            onTabSelected = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun ProgressScreenWithDataPreview() {
    FivePagesTheme {
        ProgressScreen(
            summary = ProgressSummary(
                dayStreak = 3,
                booksActive = 2,
                sessionsThisMonth = 9,
                week = sampleWeek(listOf(12, 0, 25, 8, 0, 0, 0), todayIndex = 3),
            ),
            onTabSelected = {},
        )
    }
}