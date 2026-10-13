package com.example.fivepages.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

//our Figma indicated a light theme -- so will stick on this rather than adding a dark mode
private val FivePagesColorScheme = lightColorScheme(
    primary = FpGreen,
    onPrimary = FpWhite,
    primaryContainer = FpGoalGreen,
    onPrimaryContainer = FpGoalInk,
    secondaryContainer = FpTint,
    onSecondaryContainer = FpGreen,
    background = FpBackground,
    onBackground = FpInk,
    surface = FpWhite,
    onSurface = FpInk,
    onSurfaceVariant = FpMuted,
    outline = FpLine,
    outlineVariant = FpLine,
    error = FpDanger,
)

@Composable
fun FivePagesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FivePagesColorScheme,
        typography = Typography,
        content = content,
    )
}