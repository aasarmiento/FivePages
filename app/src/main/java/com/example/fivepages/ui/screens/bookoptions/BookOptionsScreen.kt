package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun BookOptionsScreen(
    bookTitle: String,
    onBack: () -> Unit,
    onEditTitle: () -> Unit,
    onEditGoal: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeletedDone: () -> Unit,
    onSessionHistory: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme

    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    var showDeleted by rememberSaveable { mutableStateOf(false) }

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
                        text = "Book Options",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onBackground
                    )
                }
                HorizontalDivider(color = colors.outline)
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = colors.surface,
                border = BorderStroke(1.dp, colors.outline),
            ) {
                Column {
                    OptionRow(
                        label = "Edit title",
                        onClick = onEditTitle,
                        trailing = { Chevron() }
                    )
                    HorizontalDivider(color = colors.outline)
                    OptionRow(
                        label = "Edit goal (pages/date)",
                        onClick = onEditGoal,
                        trailing = { Chevron() }
                    )
                    HorizontalDivider(color = colors.outline)
                    OptionRow(
                        label = "Delete book",
                        labelColor = colors.error,
                        onClick = { showDeleteConfirm = true },
                        leading = {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = null,
                                tint = colors.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }

            if (onSessionHistory != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onSessionHistory)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Session History Log",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Chevron()
                }
            }
        }
    }

    if (showDeleteConfirm) {
        DeleteBookDialog(
            bookTitle = bookTitle,
            onKeep = { showDeleteConfirm = false },
            onDelete = {
                showDeleteConfirm = false
                onDeleteConfirmed()
                showDeleted = true
            }
        )
    }

    if (showDeleted) {
        BookDeletedDialog(bookTitle = bookTitle, onDone = onDeletedDone)
    }
}

@Composable
private fun Chevron() {
    Icon(
        Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun OptionRow(
    label: String,
    onClick: () -> Unit,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(10.dp))
            }
            Text(label, style = MaterialTheme.typography.bodyLarge, color = labelColor)
        }
        if (trailing != null) trailing()
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun BookOptionsScreenPreview() {
    FivePagesTheme {
        BookOptionsScreen(
            bookTitle = "Atomic Habits",
            onBack = {},
            onEditTitle = {},
            onEditGoal = {},
            onDeleteConfirmed = {},
            onDeletedDone = {},
            onSessionHistory = {}
        )
    }
}