package com.example.fivepages.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.ReminderWindow
import com.example.fivepages.model.timeText
import com.example.fivepages.ui.theme.FivePagesTheme
import com.example.fivepages.ui.components.AppBottomBar
import com.example.fivepages.ui.components.AppTab
import com.example.fivepages.ui.components.TitleTopBar

@Composable
fun SettingsScreen(
    reminderWindows: List<ReminderWindow>,
    onToggleReminder: (id: Int, enabled: Boolean) -> Unit,
    onAddReminderWindow: () -> Unit,
    onEditReminder: (id: Int) -> Unit,
    onTabSelected: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        //Title
        topBar = { TitleTopBar("Settings") },
        //Bottom Tabs
        bottomBar = { AppBottomBar(AppTab.Settings, onTabSelected) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
        ) {
            //Section Title
            item {
                Text(
                    "Reminder windows",
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.onBackground,
                )
                Spacer(Modifier.height(8.dp))
            }

            //Reminder List
            items(reminderWindows, key = { it.id }) { window ->
                ReminderRow(
                    window = window,
                    onClick = { onEditReminder(window.id) },
                    onToggle = { onToggleReminder(window.id, it) }
                )
                HorizontalDivider(color = colors.outline)
            }

            //Add Reminder Window Button
            item {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onAddReminderWindow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.5.dp, colors.primary),
                ) {
                    Text(
                        "+ Add reminder window",
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderRow(window: ReminderWindow, onClick: () -> Unit, onToggle: (Boolean) -> Unit) {
    val colors = MaterialTheme.colorScheme
    //Name, Time and Switch
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "${window.label} · ${window.timeText()}",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onBackground,
            modifier = Modifier.weight(1f),
        )
        //On/Off Switch
        Switch(
            checked = window.enabled,
            onCheckedChange = onToggle,
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

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun SettingsScreenPreview() {
    FivePagesTheme {
        SettingsScreen(
            reminderWindows = listOf(
                ReminderWindow(1, "Morning coffee", 8, 0),
                ReminderWindow(2, "Post-lunch", 13, 0),
            ),
            onToggleReminder = { _, _ -> },
            onAddReminderWindow = {},
            onEditReminder = {},
            onTabSelected = {},
        )
    }
}