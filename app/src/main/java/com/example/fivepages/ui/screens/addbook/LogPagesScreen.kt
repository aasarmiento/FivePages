package com.example.fivepages.ui.screens.addbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fivepages.model.Book
import com.example.fivepages.model.pagesToLog
import com.example.fivepages.ui.components.AppTextField
import com.example.fivepages.ui.components.BackTopBar
import com.example.fivepages.ui.components.LabeledField
import com.example.fivepages.ui.components.PrimaryButton

@Composable
fun LogPagesScreen(
    book: Book,
    onBack: () -> Unit,
    onSave: (pages: Int) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val pagesLeft = (book.totalPages - book.pagesRead).coerceAtLeast(0)

    // Saveable so the number survives rotation
    var pagesText by rememberSaveable { mutableStateOf("") }
    var saved by rememberSaveable { mutableStateOf(false) }
    val pageCount = pagesToLog(pagesText, pagesLeft)

    Scaffold(
        containerColor = colors.background,
        topBar = { BackTopBar(title = "Log Reading Session", onBack = onBack) },
        bottomBar = {
            Column(
                Modifier
                    .background(colors.background)
                    .navigationBarsPadding()
                    .padding(20.dp)
            ) {
                PrimaryButton(
                    label = "Save & Log Session",
                    onClick = {
                        // Guard against a quick double tap saving the session twice
                        if (!saved) {
                            saved = true
                            onSave(pageCount)
                        }
                    },
                    enabled = pageCount > 0 && !saved,
                )
            }
        },
    ) { padding ->
        // Scrolls in landscape; in portrait it still fills the screen like before
        BoxWithConstraints(Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "How many pages did you read?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onBackground,
                        textAlign = TextAlign.Center,
                    )
                }

                // Stepper Control
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    OutlinedIconButton(
                        onClick = { pagesText = (pageCount - 1).takeIf { it > 0 }?.toString() ?: "" },
                        enabled = pageCount > 0,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$pageCount",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary,
                            fontSize = 48.sp
                        )
                        Text(
                            text = if (pageCount == 1) "page" else "pages",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.onSurfaceVariant
                        )
                    }

                    OutlinedIconButton(
                        onClick = { pagesText = (pageCount + 1).toString() },
                        enabled = pageCount < pagesLeft,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Typing is faster than tapping plus for big numbers
                LabeledField(label = "Or type the number of pages") {
                    AppTextField(
                        value = pagesText,
                        onChange = { v ->
                            pagesText = if (v.any(Char::isDigit)) pagesToLog(v, pagesLeft).toString() else ""
                        },
                        placeholder = "e.g. 25",
                        keyboard = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }

                Text(
                    text = "$pagesLeft pages remaining in total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}
