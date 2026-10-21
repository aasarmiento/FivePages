package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.SessionLog
import com.example.fivepages.model.formatDuration
import com.example.fivepages.model.formatSessionDate
import com.example.fivepages.model.pagesLabel
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.BackTopBar

@Composable
fun SessionHistoryScreen(
    sessions: List<SessionLog>,
    onBack: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "History", onBack = onBack) },
    ) { padding ->
        if (sessions.isEmpty()) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No sessions yet. Log some pages and they'll show up here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                HistoryRow(
                    date = "Date",
                    pages = "Pages Logged",
                    time = "Time in Session",
                    header = true
                )
                HorizontalDivider(color = colors.outline)
                LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                    items(sessions, key = { it.id }) { session ->
                        HistoryRow(
                            date = formatSessionDate(session.loggedAtMillis),
                            pages = pagesLabel(session.pages),
                            time = formatDuration(session.durationSeconds),
                            header = false
                        )
                        HorizontalDivider(color = colors.outline.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(date: String, pages: String, time: String, header: Boolean) {
    val colors = MaterialTheme.colorScheme
    val style = if (header) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium
    val weight = if (header) FontWeight.Bold else FontWeight.Normal
    val color = if (header) colors.primary else colors.onBackground
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(date, style = style, fontWeight = weight, color = color, modifier = Modifier.weight(1f))
        Text(
            pages,
            style = style,
            fontWeight = weight,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(
            time,
            style = style,
            fontWeight = weight,
            color = color,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun SessionHistoryPreview() {
    FivePagesTheme {
        SessionHistoryScreen(
            sessions = listOf(
                SessionLog(5, 1, 5, 1_758_240_000_000, 4200),
                SessionLog(4, 1, 1, 1_758_100_000_000, 300),
                SessionLog(3, 1, 1, 1_758_000_000_000, 0),
            ),
            onBack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun SessionHistoryEmptyPreview() {
    FivePagesTheme {
        SessionHistoryScreen(sessions = emptyList(), onBack = {})
    }
}