package com.example.shaddai_app_android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShaddaiColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = Color.White,
    secondary = AccentBlue,
    onSecondary = Color.White,
    background = BackgroundColor,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    error = ErrorColor,
    onError = Color.White,
    outline = BorderColor,
)

@Composable
fun ShaddaiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ShaddaiColorScheme,
        typography = ManropeTypography,
        content = content
    )
}

