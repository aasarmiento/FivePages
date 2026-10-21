package com.example.fivepages.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.fivepages.ui.components.BackTopBar
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
                    TextButton(onClick = { showDeleteConfirm = true }) {
                        Text(
                            "Delete this reminder",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.error
                        )
                    }
                }
            }
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            //Name Field
            Text("Name", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 30) name = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        "e.g. Morning coffee",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    cursorColor = colors.primary,
                ),
            )

            Spacer(Modifier.height(24.dp))

            //Time Field
            Text("Time", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(colors.surface, RoundedCornerShape(10.dp))
                    .border(1.dp, colors.outline, RoundedCornerShape(10.dp))
                    .clickable { showTimePicker = true }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(timeText, style = MaterialTheme.typography.bodyLarge, color = colors.onSurface)
            }

            Spacer(Modifier.height(24.dp))

            //Enabled Switch
            Text("Enabled", style = MaterialTheme.typography.labelLarge, color = colors.onBackground)
            Spacer(Modifier.height(8.dp))
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
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.surface,
                        checkedTrackColor = colors.primary,
                        checkedBorderColor = colors.primary,
                        uncheckedThumbColor = colors.surface,
                        uncheckedTrackColor = colors.outline,
                        uncheckedBorderColor = colors.outline,
                    ),
                )
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
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = { onConfirm(state.hour, state.minute) }) { Text("OK") }
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