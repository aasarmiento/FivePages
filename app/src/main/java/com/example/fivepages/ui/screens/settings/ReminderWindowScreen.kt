package com.example.fivepages.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fivepages.model.ReminderWindow
import com.example.fivepages.model.timeText
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.AppPickerField
import com.example.fivepages.ui.components.AppSwitch
import com.example.fivepages.ui.components.AppTextButton
import com.example.fivepages.ui.components.AppTextField
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.LabeledField
import com.example.fivepages.ui.components.PrimaryButton

//reminder = null means we are adding a new reminder
@Composable
fun ReminderWindowScreen(
    reminder: ReminderWindow?,
    onBack: () -> Unit,
    onSave: (label: String, hour: Int, minute: Int, enabled: Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    //Form State
    var name by rememberSaveable { mutableStateOf(reminder?.label ?: "") }
    var hour by rememberSaveable { mutableStateOf(reminder?.hour ?: 8) }
    var minute by rememberSaveable { mutableStateOf(reminder?.minute ?: 0) }
    var enabled by rememberSaveable { mutableStateOf(reminder?.enabled ?: true) }

    //Popup State
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showSaved by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    var showDeleted by rememberSaveable { mutableStateOf(false) }

    //If the name is left empty, keep the old name (or use "Reminder" for a new one)
    val originalLabel = reminder?.label ?: "Reminder"
    val finalName = name.trim().ifBlank { originalLabel }
    val timeText = ReminderWindow(0, finalName, hour, minute).timeText()

    Scaffold(
        containerColor = colors.background,
        //Title
        topBar = { BackTopBar(title = "Reminder window", onBack = onBack) },
        //Save and Delete Buttons
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    label = "Save",
                    onClick = {
                        onSave(finalName, hour, minute, enabled)
                        showSaved = true
                    },
                )

                //Delete is only for existing reminders
                if (reminder != null) {
                    AppTextButton(
                        label = "Delete this reminder",
                        onClick = { showDeleteConfirm = true },
                        color = colors.error,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            //Name Field
            LabeledField("Name") {
                AppTextField(
                    value = name,
                    onChange = { if (it.length <= 30) name = it },
                    placeholder = "e.g. Morning coffee",
                )
            }

            //Time Field
            LabeledField("Time") {
                AppPickerField(
                    value = timeText,
                    placeholder = "",
                    onClick = { showTimePicker = true },
                )
            }

            //Enabled Switch
            LabeledField("Enabled") {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Send notification at this time",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onBackground
                    )
                    AppSwitch(checked = enabled, onCheckedChange = { enabled = it })
                }
            }
        }
    }

    //Time Picker Popup
    if (showTimePicker) {
        TimePickerDialog(
            initialHour = hour,
            initialMinute = minute,
            onConfirm = { newHour, newMinute ->
                hour = newHour
                minute = newMinute
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    //06.3 Reminder Saved Popup
    if (showSaved) {
        val message = if (reminder == null && name.isBlank()) {
            "Your reminder is set for $timeText."
        } else {
            "Your $finalName reminder is set for $timeText."
        }
        ReminderSavedDialog(message = message, onDone = onBack)
    }

    //06.4 Delete Reminder Popup
    if (showDeleteConfirm) {
        DeleteReminderDialog(
            message = "This turns off your $originalLabel notification. You can add it back later.",
            onKeep = { showDeleteConfirm = false },
            onDelete = {
                showDeleteConfirm = false
                onDelete()
                showDeleted = true
            }
        )
    }

    //06.5 Reminder Deleted Popup
    if (showDeleted) {
        ReminderDeletedDialog(
            message = "$originalLabel - $timeText has been removed. You can add it back anytime.",
            onDone = onBack
        )
    }
}

//Clock Popup for picking the time
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colors.surface,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = state)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    AppTextButton(
                        label = "Cancel",
                        onClick = onDismiss,
                        color = colors.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                    AppTextButton(
                        label = "OK",
                        onClick = { onConfirm(state.hour, state.minute) },
                        color = colors.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun ReminderWindowScreenPreview() {
    FivePagesTheme {
        ReminderWindowScreen(
            reminder = ReminderWindow(1, "Morning coffee", 8, 0),
            onBack = {},
            onSave = { _, _, _, _ -> },
            onDelete = {},
        )
    }
}
