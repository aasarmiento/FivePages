package com.example.fivepages.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.ReminderWindow
import com.example.fivepages.model.timeText
import com.example.fivepages.ui.theme.FivePagesTheme

//Bottom Tabs (same as Library)
private enum class SettingsTab(val label: String, val icon: ImageVector) {
    Library("Library", Icons.Filled.MenuBook),
    Progress("Progress", Icons.Filled.ShowChart),
    Settings("Settings", Icons.Filled.Settings),
}

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
        topBar = {
            Column(Modifier.background(colors.background)) {
                Text(
                    "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.onBackground,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                )
                HorizontalDivider(color = colors.outline)
            }
        },
        //Bottom Tabs
        bottomBar = {
            NavigationBar(containerColor = colors.surface) {
                SettingsTab.entries.forEach { tab ->
                    val isSelected = tab == SettingsTab.Settings
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onTabSelected(tab.label) },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = {
                            Text(
                                tab.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.primary,
                            selectedTextColor = colors.primary,
                            unselectedIconColor = colors.onSurfaceVariant,
                            unselectedTextColor = colors.onSurfaceVariant,
                            indicatorColor = Color.Transparent,
                        ),
                    )
                }
            }
        },
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