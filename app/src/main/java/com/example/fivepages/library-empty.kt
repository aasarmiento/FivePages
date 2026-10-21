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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.model.Book
import com.example.fivepages.model.dailyGoal
import com.example.fivepages.model.percentDone
import com.example.fivepages.model.progressSegments
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
    streakFor: (Int) -> Int = { 0 },
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
                    SearchField(query = query, onQueryChange = { query = it })
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(visibleBooks, key = { it.id }) { book ->
                            BookCard(
                                book = book,
                                streak = streakFor(book.id),
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
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurface),
        cursorBrush = SolidColor(colors.primary),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.surface, shape)
                    .border(1.dp, colors.outline, shape)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Box(Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            "Search Books",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Clear search",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun BookCard(
    book: Book,
    streak: Int,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    val nowMs = System.currentTimeMillis()
    val diffMs = book.targetDateMillis - nowMs
    val daysLeft = ((diffMs / (1000 * 60 * 60 * 24)).toInt() + 1).coerceAtLeast(1)
    val pagesRemaining = (book.totalPages - book.pagesRead).coerceAtLeast(0)
    val perDay = dailyGoal(pagesRemaining, daysLeft)
    val percent = percentDone(book.pagesRead, book.totalPages)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() }
            .background(colors.surface, shape)
            .border(1.dp, colors.outline, shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverTile(book.title)

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                fontWeight = FontWeight.Bold,
                color = colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            SegmentedBar(
                filled = progressSegments(book.pagesRead, book.totalPages),
                percent = percent
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val streakText = if (streak > 0) " · $streak" else ""
                    Text(
                        text = "$percent% done · $perDay pages/day$streakText",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (streak > 0) {
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = "$streak day streak",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                ReadButton(onClick = onClick)
            }
        }
    }
}

@Composable
private fun CoverTile(title: String) {
    val colors = MaterialTheme.colorScheme
    val initials = title.trim()
        .split(Regex("\\s+"))
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 76.dp)
            .background(colors.primaryContainer, RoundedCornerShape(6.dp))
            .border(1.dp, colors.outline, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.primary
        )
    }
}

@Composable
private fun SegmentedBar(filled: Int, percent: Int, segments: Int = 10) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = "$percent percent read" },
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(segments) { index ->
            Box(
                Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (index < filled) colors.primary else colors.surfaceVariant,
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
private fun ReadButton(onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = colors.primary,
        contentColor = colors.onPrimary
    ) {
        Text(
            text = "Read",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
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

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun LibraryWithBookPreview() {
    FivePagesTheme {
        LibraryEmptyScreen(
            books = listOf(
                Book(
                    id = 1,
                    title = "Atomic Habits",
                    totalPages = 250,
                    targetDateMillis = System.currentTimeMillis() + (57L * 24 * 60 * 60 * 1000),
                    pagesRead = 12
                )
            ),
            onAddBook = {},
            streakFor = { 1 }
        )
    }
}