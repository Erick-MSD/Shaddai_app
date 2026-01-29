package com.example.shaddai_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import shaddai_app.composeapp.generated.resources.*

/**
 * Carga la familia de fuentes Manrope con todos sus pesos disponibles.
 */
@Composable
fun getManropeFontFamily() = FontFamily(
    Font(Res.font.manropeextrabold, FontWeight.ExtraBold),
    Font(Res.font.manropebold, FontWeight.Bold),
    Font(Res.font.manropesemibold, FontWeight.SemiBold),
    Font(Res.font.manropemedium, FontWeight.Medium),
    Font(Res.font.manroperegular, FontWeight.Normal),
    Font(Res.font.manropelight, FontWeight.Light),
    Font(Res.font.manropeextralight, FontWeight.ExtraLight)
)

/**
 * Genera la configuración de tipografía completa asegurando que Manrope sea la fuente base.
 */
@Composable
fun getTypography(): Typography {
    val manrope = getManropeFontFamily()
    
    // Definimos un estilo base para no repetir fontFamily en cada uno
    val baseStyle = TextStyle(fontFamily = manrope)

    return Typography(
        headlineLarge = baseStyle.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = baseStyle.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        titleLarge = baseStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        bodyLarge = baseStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = baseStyle.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        labelLarge = baseStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    )
}
