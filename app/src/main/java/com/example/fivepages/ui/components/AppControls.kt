package com.example.fivepages.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun LabeledField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
    }
}

@Composable
fun AppTextField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    keyboard: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    errorText: String? = null,
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        readOnly = readOnly,
        singleLine = true,
        keyboardOptions = keyboard,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = colors.onSurfaceVariant.copy(alpha = 0.7f),
            )
        },
        trailingIcon = trailing,
        isError = isError,
        supportingText = if (errorText != null) {
            { Text(errorText) }
        } else null,
        shape = RoundedCornerShape(14.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.onSurface),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colors.surface,
            unfocusedContainerColor = colors.surface,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline,
            cursorColor = colors.primary,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun AppPickerField(
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    Box {
        AppTextField(
            value = value,
            onChange = {},
            placeholder = placeholder,
            readOnly = true,
            trailing = trailing,
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
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
