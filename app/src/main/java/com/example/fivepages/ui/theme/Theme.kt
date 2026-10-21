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
    secondary = FpGreen,
    onSecondary = FpWhite,
    secondaryContainer = FpTint,
    onSecondaryContainer = FpGreen,
    tertiary = FpGreen,
    onTertiary = FpWhite,
    background = FpBackground,
    onBackground = FpInk,
    surface = FpWhite,
    onSurface = FpInk,
    surfaceVariant = FpLine,
    onSurfaceVariant = FpMuted,
    outline = FpLine,
    outlineVariant = FpLine,
    error = FpDanger,
    onError = FpWhite,
)

@Composable
fun FivePagesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FivePagesColorScheme,
        typography = Typography,
        content = content,
    )
}