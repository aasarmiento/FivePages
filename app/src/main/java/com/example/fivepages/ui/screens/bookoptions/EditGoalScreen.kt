package com.example.fivepages.ui.screens.bookoptions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.fivepages.ui.components.AppPickerField
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.AppTextField
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.LabeledField
import com.example.fivepages.ui.components.PrimaryButton
import com.example.fivepages.model.remainingDays

private val DateFmt = SimpleDateFormat("MM / dd / yyyy", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    totalPages: Int,
    pagesRead: Int,
    targetDateMillis: Long,
    onBack: () -> Unit,
    onSave: (newTotalPages: Int, newTargetDateMillis: Long) -> Unit,
    onDone: () -> Unit = onBack,
) {
    val colors = MaterialTheme.colorScheme

    var pagesText by rememberSaveable { mutableStateOf(totalPages.toString()) }
    var dateMillis by rememberSaveable { mutableStateOf(targetDateMillis) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var showUpdated by rememberSaveable { mutableStateOf(false) }

    val pages = pagesText.toIntOrNull() ?: 0
    val pagesLeft = (pages - pagesRead).coerceAtLeast(0)
    val daysLeft = remainingDays(dateMillis)
    val perDay = dailyGoal(pagesLeft, daysLeft)
    val pagesError = when {
        pages <= 0 -> "Enter the total number of pages"
        pages < pagesRead -> "You've already read $pagesRead pages"
        else -> null
    }

    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "Edit goal", onBack = onBack) },
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
                            onSave(pages, dateMillis)
                            showUpdated = true
                        }
                    },
                    enabled = pagesError == null && !showUpdated,
                )
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            LabeledField(label = "Total pages") {
                AppTextField(
                    value = pagesText,
                    onChange = { v -> pagesText = v.filter(Char::isDigit).take(5) },
                    placeholder = "e.g. 250",
                    keyboard = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = pagesError != null,
                    errorText = pagesError,
                )
            }

            LabeledField(label = "Target finish date") {
                AppPickerField(
                    value = DateFmt.format(Date(dateMillis)),
                    placeholder = "mm / dd / yyyy",
                    onClick = { showPicker = true },
                    trailing = {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Pick a date",
                            tint = colors.primary
                        )
                    },
                )
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
                AppTextButton(
                    label = "OK",
                    onClick = {
                        pickerState.selectedDateMillis?.let { dateMillis = it }
                        showPicker = false
                    },
                    color = colors.primary,
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            dismissButton = {
                AppTextButton(
                    label = "Cancel",
                    onClick = { showPicker = false },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (showUpdated) {
        GoalUpdatedDialog(perDay = perDay, finished = pagesLeft == 0, onDone = onDone)
    }
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