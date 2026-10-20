package com.example.fivepages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fivepages.model.Book
import com.example.fivepages.model.dailyGoal
import com.example.fivepages.ui.theme.FivePagesTheme


//Bottom Tabs
private enum class Tab(val label: String, val icon: ImageVector) {
    Library("Library", Icons.Filled.MenuBook),
    Progress("Progress", Icons.Filled.ShowChart),
    Settings("Settings", Icons.Filled.Settings),
}

private enum class SortOrder(val label: String) {
    Newest("Newest"),
    Oldest("Oldest"),
    AToZ("A to Z"),
}


@Composable
fun LibraryEmptyScreen(
    books: List<Book> = emptyList(),
    onAddBook: () -> Unit,
    onReadBook: (Int) -> Unit = {},
    onTabSelected: (String) -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme
    var selected by remember { mutableStateOf(Tab.Library) }
    var query by rememberSaveable { mutableStateOf("") }
    var sort by rememberSaveable { mutableStateOf(SortOrder.Newest) }

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
        if (books.isEmpty()) {
            EmptyLibraryState(Modifier.padding(padding).fillMaxSize())
        } else {
            val visibleBooks = books
                .filter { it.title.contains(query.trim(), ignoreCase = true) }
                .let { list ->
                    when (sort) {
                        SortOrder.Newest -> list.sortedByDescending { it.id }
                        SortOrder.Oldest -> list.sortedBy { it.id }
                        SortOrder.AToZ -> list.sortedBy { it.title.lowercase() }
                    }
                }
            val countText = if (query.isBlank()) {
                "${books.size} ${if (books.size == 1) "book" else "books"}"
            } else {
                "${visibleBooks.size} found"
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        placeholder = {
                            Text(
                                "Search Books",
                                style = MaterialTheme.typography.bodyLarge,
                                color = colors.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = colors.onSurfaceVariant)
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Clear search",
                                        tint = colors.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.outline,
                            cursorColor = colors.primary,
                        ),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            countText,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                        TextButton(
                            onClick = { sort = SortOrder.entries[(sort.ordinal + 1) % SortOrder.entries.size] }
                        ) {
                            Text(
                                "Sort: ${sort.label}",
                                style = MaterialTheme.typography.labelLarge,
                                color = colors.primary
                            )
                        }
                    }
                }

                if (visibleBooks.isEmpty()) {
                    Text(
                        "No books match \"${query.trim()}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(visibleBooks, key = { it.id }) { book ->
                            BookCard(
                                book = book,
                                onClick = { onReadBook(book.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCard(book: Book,
                     onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val progress = if (book.totalPages > 0) book.pagesRead.toFloat() / book.totalPages else 0f

    val nowMs = System.currentTimeMillis()
    val diffMs = book.targetDateMillis - nowMs
    val daysLeft = ((diffMs / (1000 * 60 * 60 * 24)).toInt() + 1).coerceAtLeast(1)
    val pagesRemaining = (book.totalPages - book.pagesRead).coerceAtLeast(0)
    val perDay = dailyGoal(pagesRemaining, daysLeft)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(1.dp, colors.outline, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${book.pagesRead} of ${book.totalPages} pages",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
            Text(
                text = "$perDay pages/day",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
            )
        }

        Spacer(Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = colors.primary,
            trackColor = colors.surfaceVariant,
        )
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