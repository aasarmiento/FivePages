package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.theme.FivePagesTheme

@Composable
fun BookOptionsScreen(
    bookTitle: String,
    onBack: () -> Unit,
    onEditTitle: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeletedDone: () -> Unit,
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            OptionRow(
                label = "Edit title",
                onClick = onEditTitle,
                trailing = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant
                    )
                }
            )
            HorizontalDivider(color = colors.outline)
            OptionRow(
                label = "Delete book",
                labelColor = colors.error,
                onClick = { showDeleteConfirm = true },
                trailing = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = colors.error
                    )
                }
            )
            HorizontalDivider(color = colors.outline)
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
private fun OptionRow(
    label: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit,
    labelColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onBackground,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = labelColor)
        trailing()
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
            onDeleteConfirmed = {},
            onDeletedDone = {}
        )
    }
}