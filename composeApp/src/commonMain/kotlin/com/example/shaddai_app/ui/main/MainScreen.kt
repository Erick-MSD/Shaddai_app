package com.example.shaddai_app.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shaddai_app.data.repository.CsvServiciosRepository
import com.example.shaddai_app.ui.calendario.CalendarioScreen
import com.example.shaddai_app.ui.calendario.CalendarioViewModel
import com.example.shaddai_app.ui.components.navigation.GlobalBottomBar
import com.example.shaddai_app.ui.components.navigation.GlobalTopBar
import com.example.shaddai_app.ui.components.navigation.Screen
import com.example.shaddai_app.ui.technician_home.TechnicianColors
import com.example.shaddai_app.ui.technician_home.TechnicianHomeScreen

@Composable
fun TechnicianAppScaffold() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    // Inicializar el repositorio y ViewModel del calendario
    val calendarioRepository = remember { CsvServiciosRepository() }
    val calendarioViewModel = remember { CalendarioViewModel(calendarioRepository) }

    Scaffold(
        topBar = {
            // Mostrar topBar solo cuando NO estemos en la pantalla de calendario
            if (currentScreen != Screen.Calendar) {
                GlobalTopBar(
                    title = "Hola, Técnico",
                    onMenuClick = { /* Lógica para abrir drawer */ }
                )
            }
        },
        bottomBar = {
            // Mostrar bottomBar solo cuando NO estemos en la pantalla de calendario
            if (currentScreen != Screen.Calendar) {
                GlobalBottomBar(
                    currentScreen = currentScreen,
                    onHomeClick = { currentScreen = Screen.Home },
                    onServicesClick = { currentScreen = Screen.Services },
                    onCalendarClick = { currentScreen = Screen.Calendar },
                    onSupportClick = { currentScreen = Screen.Support }
                )
            }
        },
        containerColor = TechnicianColors.Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                Screen.Home -> TechnicianHomeScreen()

                Screen.Calendar -> CalendarioScreen(
                    viewModel = calendarioViewModel,
                    onNavigateHome = { currentScreen = Screen.Home },
                    onNavigateHerramientas = { currentScreen = Screen.Services },
                    onNavigateSoporte = { currentScreen = Screen.Support }
                )

                Screen.Services -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pantalla de Servicios - Próximamente")
                }

                Screen.Support -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pantalla de Soporte - Próximamente")
                }
            }
        }
    }
}