package com.example.fivepages.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun BackTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier.background(colors.background)) {
        Row(
            Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleIconButton(Icons.AutoMirrored.Filled.ArrowBack, "Back", onBack)
            Spacer(Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (trailing != null) {
                Spacer(Modifier.width(14.dp))
                trailing()
            }
        }
        HorizontalDivider(color = colors.outline)
    }
}

@Composable
fun TitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier.background(colors.background)) {
        Text(
            text = title,
            style = style,
            color = colors.onBackground,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        )
        HorizontalDivider(color = colors.outline)
    }
}
