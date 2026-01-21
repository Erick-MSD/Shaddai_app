package com.example.shaddai_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Tipografía de la aplicación usando Manrope
 *
 * Nota: Para usar Manrope de Google Fonts en KMP:
 * 1. Descarga los archivos .ttf de https://fonts.google.com/specimen/Manrope
 * 2. Colócalos en: composeApp/src/commonMain/composeResources/font/
 * 3. Nombres sugeridos: manrope_regular.ttf, manrope_medium.ttf, manrope_semibold.ttf, manrope_bold.ttf
 *
 * Por ahora se usa la fuente del sistema como fallback.
 */

// Placeholder: En producción, cargar desde resources
// @Composable
// fun getManropeFontFamily(): FontFamily {
//     return FontFamily(
//         Font(Res.font.manrope_regular, FontWeight.Normal),
//         Font(Res.font.manrope_medium, FontWeight.Medium),
//         Font(Res.font.manrope_semibold, FontWeight.SemiBold),
//         Font(Res.font.manrope_bold, FontWeight.Bold)
//     )
// }

val ShaddaiTypography = Typography(
    // Título grande para "Bienvenido"
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default, // Cambiar a Manrope cuando esté disponible
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    // Botones
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Campos de texto
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Texto secundario
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
)
