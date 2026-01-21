package com.example.shaddai_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ShaddaiTheme(
    content: @Composable () -> Unit
) {
    // Se movió la definición del esquema de colores dentro de la función ShaddaiTheme
    // para evitar errores de NoClassDefFoundError durante la inicialización estática en el Preview.
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
        typography = ShaddaiTypography,
        content = content
    )
}
