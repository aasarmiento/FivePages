package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fivepages.R
import com.example.fivepages.model.dailyGoal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.PrimaryButton
import com.example.fivepages.model.remainingDays


private val DateFmt = SimpleDateFormat("MM / dd / yyyy", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(onBack: () -> Unit, onSave: (title: String, totalPages: Int, targetDateMillis: Long) -> Unit,
                  isTitleTaken: (String) -> Boolean = { false },
) {

    val colors = MaterialTheme.colorScheme

    // Form State
    var title by remember { mutableStateOf("") }
    var pagesText by remember { mutableStateOf("") }
    var targetDateMillis by remember { mutableStateOf<Long?>(null) }
    var showPicker by remember { mutableStateOf(false) }
    var showAdded by remember { mutableStateOf(false) }
    val formattedDate = targetDateMillis?.let { DateFmt.format(Date(it)) } ?: ""

    val pages = pagesText.toIntOrNull() ?: 0

    // Days Left & Daily Goal Calculation
    val daysLeft = remember(targetDateMillis) { targetDateMillis?.let { remainingDays(it) } ?: 0 }
    val perDay = if (pages > 0 && daysLeft > 0) dailyGoal(pages, daysLeft) else 0
    val titleTaken = !showAdded && title.isNotBlank() && isTitleTaken(title)
    val isFormValid = title.isNotBlank() && !titleTaken && pages > 0 && targetDateMillis != null


    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "Add a book", onBack = onBack) },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(20.dp)
            ) {
                PrimaryButton(
                    label = "Save to Library",
                    onClick = {
                        if (!showAdded) {
                            targetDateMillis?.let { targetMillis ->
                                onSave(title.trim(), pages, targetMillis)
                                showAdded = true
                            }
                        }
                    },
                    enabled = isFormValid && !showAdded,
                )
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
                    placeholder = "e.g. Before the Coffee Gets Cold",
                    isError = titleTaken,
                    errorText = if (titleTaken) "You already have a book with this title" else null
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
            // Target Finish Date Input
            LabeledField(label = "Target finish date") {
                Box {
                    FpTextField(
                        value = formattedDate,
                        onChange = {},
                        placeholder = "mm / dd / yyyy",
                        readOnly = true,
                        trailing = {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Pick a date",
                                tint = colors.primary
                            )
                        }
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable { showPicker = true }
                    )
                }
            }
            // Dynamic Goal Card Calculation
            if (perDay > 0) {
                GoalCard(perDay = perDay, pages = pages, days = daysLeft)
            }
        }
    }

    // Material 3 Date Picker Dialog
    if (showPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = targetDateMillis ?: System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Prevent selecting dates before today (at midnight UTC)
                    val todayMidnight = (System.currentTimeMillis() / 86400000L) * 86400000L
                    return utcTimeMillis >= todayMidnight
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        targetDateMillis = pickerState.selectedDateMillis
                        showPicker = false
                    }
                ) {
                    Text("OK", color = colors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("Cancel", color = colors.onSurfaceVariant)
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (showAdded) {
        BookAddedDialog(
            title = title.trim(),
            perDay = perDay,
            pages = pages,
            days = daysLeft,
            onDone = onBack
        )
    }
}

@Composable
private fun GoalCard(perDay: Int, pages: Int, days: Int) {
    val colors = MaterialTheme.colorScheme
    val subStyle = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)

    Row(
        Modifier
            .fillMaxWidth()
            .background(colors.primaryContainer, RoundedCornerShape(12.dp))
            .border(1.dp, colors.primary, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.fivepages_owl1),
            contentDescription = "FivePages Owl",
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "$perDay pages a day",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onPrimaryContainer,
            )
            Text(
                text = "$pages pages ÷ $days days",
                style = subStyle,
                color = colors.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Text(
                text = "1 micro-sprint of $perDay pages",
                style = subStyle,
                color = colors.onPrimaryContainer.copy(alpha = 0.8f)
            )
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
    readOnly: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    errorText: String? = null,
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        readOnly = readOnly,
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
        trailingIcon = trailing,
        isError = isError,
        supportingText = if (errorText != null) {
            { Text(errorText) }
        } else null,
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