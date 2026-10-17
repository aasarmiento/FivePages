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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddBookScreen(onBack: () -> Unit) {

    val colors = MaterialTheme.colorScheme

    // Form State
    var title by remember { mutableStateOf("") }
    var pagesText by remember { mutableStateOf("") }

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
                        text = "Add a book",
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onBackground
                    )
                }
                HorizontalDivider(color = colors.outline)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),

            ) {
            // Book Title Input
            LabeledField(label = "Book title") {
                FpTextField(
                    value = title,
                    onChange = { title = it },
                    placeholder = "e.g. Atomic Habits"
                )
            }

            // Total Pages Input
            LabeledField(label = "Total pages") {
                FpTextField(
                    value = pagesText,
                    onChange = { v -> pagesText = v.filter(Char::isDigit).take(5) },
                    placeholder = "e.g. 250",
                    keyboard = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
private fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
    }
}

@Composable
private fun FpTextField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    keyboard: KeyboardOptions = KeyboardOptions.Default,
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        singleLine = true,
        keyboardOptions = keyboard,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = colors.onSurfaceVariant.copy(alpha = 0.7f),
            )
        },
        shape = RoundedCornerShape(14.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.onSurface),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colors.surface,
            unfocusedContainerColor = colors.surface,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline,
            cursorColor = colors.primary,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}


