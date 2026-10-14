package com.example.fivepages

import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.ui.theme.FivePagesTheme


//Bottom Tabs
private enum class Tab(val label: String, val icon: ImageVector) {
    Library("Library", Icons.Filled.MenuBook),
    Progress("Progress", Icons.Filled.ShowChart),
    Settings("Settings", Icons.Filled.Settings),
}


@Composable
fun LibraryEmptyScreen(
    onAddBook: () -> Unit,
    onTabSelected: (String) -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme
    var selected by remember { mutableStateOf(Tab.Library) }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Column(Modifier.background(colors.background)) {
                Text(
                    "My books",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.onBackground,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                )
                HorizontalDivider(color = colors.outline)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBook,
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                shape = CircleShape,
            ) { Icon(Icons.Filled.Add, contentDescription = "Add a book") }
        },
        bottomBar = {
            NavigationBar(containerColor = colors.surface) {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == selected,
                        onClick = { selected = tab; onTabSelected(tab.label) },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = {
                            Text(
                                tab.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (tab == selected) FontWeight.Bold else FontWeight.Medium,
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
        EmptyLibraryState(Modifier.padding(padding).fillMaxSize())
    }


}

@Composable
private fun EmptyLibraryState(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier.size(72.dp).background(colors.secondaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.MenuBook,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(20.dp))
        Text("No books yet", style = MaterialTheme.typography.titleLarge, color = colors.onBackground)
        Spacer(Modifier.height(6.dp))
        Text(
            "Add a book and we’ll turn it into a small daily goal.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun LibraryEmptyPreview() {
    FivePagesTheme { LibraryEmptyScreen(onAddBook = {}) }
}
