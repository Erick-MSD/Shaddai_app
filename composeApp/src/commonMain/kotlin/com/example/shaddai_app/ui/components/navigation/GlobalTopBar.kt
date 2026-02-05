package com.example.shaddai_app.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TopBar global y reutilizable para la aplicación.
 *
 * @param title El texto a mostrar en la TopBar.
 * @param onMenuClick Callback que se invoca al presionar el ícono de menú.
 */
@Composable
fun GlobalTopBar(
    title: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Aplicamos statusBarsPadding() para que el contenido de la TopBar
            // comience debajo de la Status Bar del sistema (hora, batería, señal).
            // Esto evita usar valores fijos o manipular la Status Bar.
            .statusBarsPadding()
            .height(64.dp) // Altura estándar de AppBar
            .background(Color(0xFF1F68BD)) // Color de fondo general
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFFFFFFFF) // Texto principal
        )
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Abrir menú",
                tint = Color(0xFF0E88E6) // Color de acento
            )
        }
    }
}
