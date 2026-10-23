package com.example.fivepages.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

private val DialogButtonShape = RoundedCornerShape(24.dp)
private val DialogButtonHeight = 48.dp

@Composable
fun AppDialog(
    onDismiss: () -> Unit,
    icon: @Composable () -> Unit,
    title: String,
    message: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    actions: @Composable ColumnScope.() -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon()
                Spacer(Modifier.height(16.dp))
                Text(title, style = MaterialTheme.typography.titleLarge, color = titleColor)
                Spacer(Modifier.height(6.dp))
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                actions()
            }
        }
    }
}

@Composable
fun DialogIcon(imageVector: ImageVector, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(48.dp)
    )
}

@Composable
fun DialogImage(@DrawableRes id: Int) {
    Image(
        painter = painterResource(id),
        contentDescription = null,
        modifier = Modifier.size(96.dp)
    )
}

@Composable
fun DialogPrimaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) =
    PrimaryButton(label, onClick, modifier, shape = DialogButtonShape, height = DialogButtonHeight)

@Composable
fun DialogDangerButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    PrimaryButton(
        label = label,
        onClick = onClick,
        modifier = modifier,
        shape = DialogButtonShape,
        height = DialogButtonHeight,
        containerColor = colors.error.copy(alpha = 0.12f),
        contentColor = colors.error,
    )
}

@Composable
fun DialogOutlineButton(label: String, onClick: () -> Unit) =
    OutlineButton(label, onClick, shape = DialogButtonShape, height = DialogButtonHeight)

@Composable
fun DialogTextButton(label: String, color: Color, onClick: () -> Unit) =
    AppTextButton(label, onClick, color = color, style = MaterialTheme.typography.labelLarge)
