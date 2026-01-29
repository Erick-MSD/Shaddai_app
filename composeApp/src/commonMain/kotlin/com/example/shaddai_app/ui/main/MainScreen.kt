package com.example.shaddai_app.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.shaddai_app.ui.components.navigation.GlobalBottomBar
import com.example.shaddai_app.ui.components.navigation.GlobalTopBar
import com.example.shaddai_app.ui.components.navigation.Screen
import com.example.shaddai_app.ui.technician_home.TechnicianColors
import com.example.shaddai_app.ui.technician_home.TechnicianHomeScreen

@Composable
fun TechnicianAppScaffold() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Scaffold(
        topBar = {
            GlobalTopBar(
                title = "Hola, Técnico",
                onMenuClick = { /* Lógica para abrir drawer */ }
            )
        },
        bottomBar = {
            GlobalBottomBar(
                currentScreen = currentScreen,
                onHomeClick = { currentScreen = Screen.Home },
                onServicesClick = { currentScreen = Screen.Services },
                onCalendarClick = { currentScreen = Screen.Calendar },
                onSupportClick = { currentScreen = Screen.Support }
            )
        },
        containerColor = TechnicianColors.Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.Home -> TechnicianHomeScreen()
                // Agrega placeholders para otras pantallas si es necesario
                // Screen.Services -> Text("Pantalla de Servicios")
                else -> TechnicianHomeScreen() // Default
            }
        }
    }
}