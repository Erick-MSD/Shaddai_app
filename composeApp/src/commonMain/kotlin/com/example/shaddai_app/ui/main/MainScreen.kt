package com.example.shaddai_app.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.shaddai_app.data.repository.CsvServiciosRepository
import com.example.shaddai_app.navigation.Screen as AppScreen // Alias para la sealed class
import com.example.shaddai_app.ui.calendario.CalendarioScreen
import com.example.shaddai_app.ui.calendario.CalendarioViewModel
import com.example.shaddai_app.ui.components.navigation.GlobalBottomBar
import com.example.shaddai_app.ui.components.navigation.GlobalTopBar
import com.example.shaddai_app.ui.components.navigation.Screen as BottomBarScreen // Alias para el Enum
import com.example.shaddai_app.ui.service_report.ServiceReportScreen
import com.example.shaddai_app.ui.service_report.ServiceReportViewModel
import com.example.shaddai_app.ui.technician_home.TechnicianColors
import com.example.shaddai_app.ui.technician_home.TechnicianHomeScreen

@Composable
fun TechnicianAppScaffold() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }

    // --- Repositorios y ViewModels ---
    val servicesRepository = remember { CsvServiciosRepository() }
    val calendarioViewModel = remember { CalendarioViewModel(servicesRepository) }
    val serviceReportViewModel = remember { ServiceReportViewModel(servicesRepository) }

    Scaffold(
        topBar = {
            GlobalTopBar(
                title = if (currentScreen is AppScreen.Services) "Hola, ${serviceReportViewModel.uiState.collectAsState().value.technicianName}" else currentScreen.label,
                onMenuClick = { /* Lógica para abrir drawer */ }
            )
        },
        bottomBar = {
            val bottomBarScreen = when (currentScreen) {
                is AppScreen.Home -> BottomBarScreen.Home
                is AppScreen.Services -> BottomBarScreen.Services
                is AppScreen.Calendar -> BottomBarScreen.Calendar
                is AppScreen.Support -> BottomBarScreen.Support
            }
            GlobalBottomBar(
                currentScreen = bottomBarScreen,
                onHomeClick = { currentScreen = AppScreen.Home },
                onServicesClick = { currentScreen = AppScreen.Services },
                onCalendarClick = { currentScreen = AppScreen.Calendar },
                onSupportClick = { currentScreen = AppScreen.Support }
            )
        },
        containerColor = TechnicianColors.Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                is AppScreen.Home -> TechnicianHomeScreen()
                is AppScreen.Services -> ServiceReportScreen(viewModel = serviceReportViewModel)
                is AppScreen.Calendar -> CalendarioScreen(viewModel = calendarioViewModel)
                is AppScreen.Support -> {
                    // Mensaje para la pantalla de Soporte
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "La pagina esta en desarrollo se incluira proximamente",
                            textAlign = TextAlign.Center // Corregido: Texto centrado
                        )
                    }
                }
            }
        }
    }
}