package com.example.shaddai_app.ui.technician_home

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * Carga la fuente Manrope desde los recursos.
 *
 * IMPORTANTE:
 * Para que esto funcione, descarga la fuente Manrope de Google Fonts y coloca los archivos .ttf
 * en la carpeta `composeApp/src/commonMain/resources/font/`.
 * Por ejemplo:
 * - `manrope_regular.ttf`
 * - `manrope_bold.ttf`
 * - `manrope_medium.ttf`
 */
@Composable
fun manropeFontFamily(): FontFamily {
    // TODO: Para usar la fuente Manrope, descomenta las siguientes líneas y asegúrate de que los archivos de fuente estén en la carpeta de recursos.
    /*
    return FontFamily(
        androidx.compose.ui.text.font.Font(org.jetbrains.compose.resources.Font("font/manrope_regular.ttf"), weight = FontWeight.Normal),
        androidx.compose.ui.text.font.Font(org.jetbrains.compose.resources.Font("font/manrope_medium.ttf"), weight = FontWeight.Medium),
        androidx.compose.ui.text.font.Font(org.jetbrains.compose.resources.Font("font/manrope_bold.ttf"), weight = FontWeight.Bold)
    )
    */
    return FontFamily.Default
}

// Definimos un objeto Typography para usar en el tema si fuera necesario.
@Composable
fun getTechnicianTypography(): Typography {
    val manrope = manropeFontFamily()
    // Aquí puedes definir estilos de texto específicos que usen Manrope.
    return Typography()
}
