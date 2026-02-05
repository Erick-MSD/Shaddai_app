package com.example.shaddai_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ShaddaiTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = ShaddaiColors.AccentBlue,
        onPrimary = ShaddaiColors.White,
        primaryContainer = ShaddaiColors.Background,
        onPrimaryContainer = ShaddaiColors.TextPrimary,
        secondary = ShaddaiColors.TextSecondary,
        onSecondary = ShaddaiColors.White,
        background = ShaddaiColors.Background,
        onBackground = ShaddaiColors.TextPrimary,
        surface = ShaddaiColors.White,
        onSurface = ShaddaiColors.TextPrimary,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(), // Usamos la función que carga Manrope
        content = content
    )
}
