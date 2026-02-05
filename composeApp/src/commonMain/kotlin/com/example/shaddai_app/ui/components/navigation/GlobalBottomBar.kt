package com.example.shaddai_app.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * BottomBar global y reutilizable.
 *
 * @param currentScreen La pantalla actualmente seleccionada.
 * @param onHomeClick Callback para navegar a la pantalla de inicio.
 * @param onServicesClick Callback para navegar a la pantalla de servicios.
 * @param onCalendarClick Callback para navegar a la pantalla de calendario.
 * @param onSupportClick Callback para navegar a la pantalla de soporte.
 */
@Composable
fun GlobalBottomBar(
    currentScreen: Screen,
    onHomeClick: () -> Unit,
    onServicesClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onSupportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = Color(0xFFD7F4F5), // Fondo general
    ) {
        val activeColor = Color(0xFF0E88E6) // Color de acento
        val inactiveColor = Color(0xFFA9A9A9) // Texto secundario

        // Ícono Home
        NavigationBarItem(
            selected = currentScreen == Screen.Home,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor
            )
        )

        // Ícono Servicios
        NavigationBarItem(
            selected = currentScreen == Screen.Services,
            onClick = onServicesClick,
            icon = { Icon(Icons.Default.Build, contentDescription = "Servicios") },
            label = { Text("Servicios") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor
            )
        )

        // Ícono Calendario
        NavigationBarItem(
            selected = currentScreen == Screen.Calendar,
            onClick = onCalendarClick,
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Calendario") },
            label = { Text("Calendario") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor
            )
        )

        // Ícono Soporte
        NavigationBarItem(
            selected = currentScreen == Screen.Support,
            onClick = onSupportClick,
            icon = { Icon(Icons.Default.Headset, contentDescription = "Soporte") },
            label = { Text("Soporte") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor
            )
        )
    }
}

/**
 * Define las pantallas para la navegación.
 */
enum class Screen {
    Home,
    Services,
    Calendar,
    Support
}
