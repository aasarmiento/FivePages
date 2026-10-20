package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun EditTitleScreen(
    currentTitle: String,
    onBack: () -> Unit,
    onSave: (newTitle: String) -> Unit,
    isTitleTaken: (String) -> Boolean = { false },
) {
    val colors = MaterialTheme.colorScheme

    var title by rememberSaveable { mutableStateOf(currentTitle) }
    var showUpdated by rememberSaveable { mutableStateOf(false) }
    val taken = title.isNotBlank() && isTitleTaken(title)

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
                        text = "Edit title",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onBackground
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
                    .imePadding()
                    .padding(20.dp)
            ) {
                Button(
                    onClick = {
                        if (!showUpdated) {
                            onSave(title.trim())
                            showUpdated = true
                        }
                    },
                    enabled = title.isNotBlank() && !taken && !showUpdated,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                        disabledContainerColor = colors.surfaceVariant,
                        disabledContentColor = colors.onSurfaceVariant.copy(alpha = 0.5f)
                    ),
                ) {
                    Text("Save", style = MaterialTheme.typography.titleSmall)
                }
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Book title", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { if (it.length <= 60) title = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                isError = title.isBlank() || taken,
                supportingText = {
                    if (title.isBlank()) Text("Title can't be empty")
                    else if (taken) Text("You already have a book with this title")
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                ),
            )
        }
    }

    if (showUpdated) {
        TitleUpdatedDialog(newTitle = title.trim(), onDone = onBack)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun EditTitleScreenPreview() {
    FivePagesTheme {
        EditTitleScreen(currentTitle = "Atomic Habits", onBack = {}, onSave = {})
    }
}