
package com.example.shaddai_app.ui.technician_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal del técnico.
 * Se encarga de cargar los datos y exponerlos a la UI a través de un StateFlow.
 */
class TechnicianHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TechnicianHomeState(isLoading = true))
    val uiState: StateFlow<TechnicianHomeState> = _uiState.asStateFlow()

    init {
        // En una implementación real, aquí se llamaría a un repositorio
        // para cargar los datos desde una API o base de datos.
        loadFakeData()
    }

    /**
     * Simula la carga de datos desde un backend.
     */
    private fun loadFakeData() {
        viewModelScope.launch {
            // Simula la latencia de red
            kotlinx.coroutines.delay(1000)

            val currentService = CurrentService(
                id = "1",
                serviceType = "Minisplit",
                title = "Limpieza y Mantenimiento",
                time = "09:00 AM - 11:00 AM",
                address = "Av. Siempreviva 742, Springfield"
            )

            val upcomingServices = listOf(
                UpcomingService("2", "12:00 PM", "Instalación", "Aire Acondicionado", "Calle Falsa 123, Shelbyville"),
                UpcomingService("3", "02:00 PM", "Revisión General", "Refrigerador", "Blvd. del Ocaso 45, Capital City"),
                UpcomingService("4", "04:00 PM", "Reparación de Fuga", "Lavadora", "Ruta 9, Ogdenville")
            )

            _uiState.value = TechnicianHomeState(
                currentService = currentService,
                upcomingServices = upcomingServices,
                isLoading = false
            )
        }
    }

    // --- Callbacks para la UI ---

    fun onGoNowClicked(serviceId: String) {
        // Aquí se implementaría la lógica de navegación para iniciar la ruta.
        println("Navegando al servicio con ID: $serviceId")
    }

    fun onCallClicked(serviceId: String) {
        // Aquí se implementaría la lógica para iniciar una llamada.
        println("Llamando al cliente del servicio con ID: $serviceId")
    }

    fun onUpcomingServiceClicked(serviceId: String) {
        // Aquí se implementaría la lógica para ver los detalles de un servicio futuro.
        println("Viendo detalles del servicio futuro con ID: $serviceId")
    }
}
