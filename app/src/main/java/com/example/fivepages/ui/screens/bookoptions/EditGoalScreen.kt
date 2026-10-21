package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.R
import com.example.fivepages.model.dailyGoal
import com.example.fivepages.ui.theme.FivePagesTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val DateFmt = SimpleDateFormat("MM / dd / yyyy", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun daysUntil(targetMillis: Long): Int {
    val days = ((targetMillis - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt() + 1
    return if (days > 0) days else 1
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    totalPages: Int,
    pagesRead: Int,
    targetDateMillis: Long,
    onBack: () -> Unit,
    onSave: (newTotalPages: Int, newTargetDateMillis: Long) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    var pagesText by rememberSaveable { mutableStateOf(totalPages.toString()) }
    var dateMillis by rememberSaveable { mutableStateOf(targetDateMillis) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var showUpdated by rememberSaveable { mutableStateOf(false) }

    val pages = pagesText.toIntOrNull() ?: 0
    val pagesLeft = (pages - pagesRead).coerceAtLeast(0)
    val daysLeft = daysUntil(dateMillis)
    val perDay = dailyGoal(pagesLeft, daysLeft)
    val pagesError = when {
        pages <= 0 -> "Enter the total number of pages"
        pages < pagesRead -> "You've already read $pagesRead pages"
        else -> null
    }

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
                        text = "Edit goal",
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
                            onSave(pages, dateMillis)
                            showUpdated = true
                        }
                    },
                    enabled = pagesError == null && !showUpdated,
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            GoalField(label = "Total pages") {
                OutlinedTextField(
                    value = pagesText,
                    onValueChange = { v -> pagesText = v.filter(Char::isDigit).take(5) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    isError = pagesError != null,
                    supportingText = pagesError?.let { { Text(it) } },
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                )
            }

            GoalField(label = "Target finish date") {
                Box {
                    OutlinedTextField(
                        value = DateFmt.format(Date(dateMillis)),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Pick a date",
                                tint = colors.primary
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = fieldColors(),
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .clickable { showPicker = true }
                    )
                }
            }

            if (pagesError == null && perDay > 0) {
                GoalPreviewCard(perDay = perDay, pagesLeft = pagesLeft, days = daysLeft)
            }
        }
    }

    if (showPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
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
                        pickerState.selectedDateMillis?.let { dateMillis = it }
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

    if (showUpdated) {
        GoalUpdatedDialog(perDay = perDay, finished = pagesLeft == 0, onDone = onBack)
    }
}

@Composable
private fun GoalField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        content()
    }
}

@Composable
private fun fieldColors(): TextFieldColors {
    val colors = MaterialTheme.colorScheme
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colors.surface,
        unfocusedContainerColor = colors.surface,
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.outline,
        cursorColor = colors.primary,
    )
}

@Composable
private fun GoalPreviewCard(perDay: Int, pagesLeft: Int, days: Int) {
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
            contentDescription = null,
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
                text = "$pagesLeft pages left ÷ $days days",
                style = subStyle,
                color = colors.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun EditGoalScreenPreview() {
    FivePagesTheme {
        EditGoalScreen(
            totalPages = 250,
            pagesRead = 40,
            targetDateMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
            onBack = {},
            onSave = { _, _ -> }
        )
    }
}