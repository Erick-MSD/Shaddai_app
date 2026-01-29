
package com.example.shaddai_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Services : Screen("services", "Servicios", Icons.Default.Build)
    object Calendar : Screen("calendar", "Calendario", Icons.Default.DateRange)
    object Support : Screen("support", "Soporte", Icons.Default.Phone)
}
