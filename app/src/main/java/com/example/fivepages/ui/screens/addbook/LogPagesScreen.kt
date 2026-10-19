package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.model.Book
import com.example.fivepages.model.dailyGoal

@Composable
fun LogPagesScreen(
    book: Book,
    onBack: () -> Unit,
    onSave: (pages: Int) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val pagesLeft = (book.totalPages - book.pagesRead).coerceAtLeast(0)

    val initialGoal = remember(book) {
        val diffMs = book.targetDateMillis - System.currentTimeMillis()
        val days = ((diffMs / (1000 * 60 * 60 * 24)).toInt() + 1).coerceAtLeast(1)
        dailyGoal(pagesLeft, days).coerceIn(1, pagesLeft.coerceAtLeast(1))
    }

    var pagesToLog by remember { mutableStateOf(initialGoal) }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Column(Modifier.background(colors.background)) {
                Row(
                    Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        onClick = onBack,
                        shape = CircleShape,
                        color = colors.surface,
                        border = BorderStroke(1.dp, colors.outline),
                        modifier = Modifier.size(40.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Text(
                        text = "Log Reading Session",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onBackground,
                    )
                }
                HorizontalDivider(color = colors.outline)
            }
        },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = { onSave(pagesToLog) },
                    enabled = pagesToLog > 0,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                ) {
                    Text("Save & Log Session", style = MaterialTheme.typography.titleSmall)
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "How many pages did you read?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center,
                )
            }

            // Stepper Control
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                OutlinedIconButton(
                    onClick = { if (pagesToLog > 1) pagesToLog-- },
                    enabled = pagesToLog > 1,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$pagesToLog",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary,
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (pagesToLog == 1) "page" else "pages",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurfaceVariant
                    )
                }

                OutlinedIconButton(
                    onClick = { if (pagesToLog < pagesLeft) pagesToLog++ },
                    enabled = pagesToLog < pagesLeft,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Text(
                text = "$pagesLeft pages remaining in total",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }
    }
}