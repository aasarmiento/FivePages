package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.AppTextField
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.LabeledField
import com.example.fivepages.ui.components.PrimaryButton

@Composable
fun EditTitleScreen(
    currentTitle: String,
    onBack: () -> Unit,
    onSave: (newTitle: String) -> Unit,
    isTitleTaken: (String) -> Boolean = { false },
    onDone: () -> Unit = onBack,
) {
    val colors = MaterialTheme.colorScheme

    var title by rememberSaveable { mutableStateOf(currentTitle) }
    var showUpdated by rememberSaveable { mutableStateOf(false) }
    val taken = title.isNotBlank() && isTitleTaken(title)

    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "Edit title", onBack = onBack) },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(20.dp)
            ) {
                PrimaryButton(
                    label = "Save",
                    onClick = {
                        if (!showUpdated) {
                            onSave(title.trim())
                            showUpdated = true
                        }
                    },
                    enabled = title.isNotBlank() && !taken && !showUpdated,
                )
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            LabeledField(label = "Book title") {
                AppTextField(
                    value = title,
                    onChange = { if (it.length <= 60) title = it },
                    placeholder = "e.g. Before the Coffee Gets Cold",
                    isError = title.isBlank() || taken,
                    errorText = when {
                        title.isBlank() -> "Title can't be empty"
                        taken -> "You already have a book with this title"
                        else -> null
                    },
                )
            }
        }
    }

    if (showUpdated) {
        TitleUpdatedDialog(newTitle = title.trim(), onDone = onDone)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun EditTitleScreenPreview() {
    FivePagesTheme {
        EditTitleScreen(currentTitle = "Atomic Habits", onBack = {}, onSave = {})
    }
}