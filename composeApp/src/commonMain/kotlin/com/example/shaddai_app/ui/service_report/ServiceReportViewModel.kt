package com.example.shaddai_app.ui.service_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaddai_app.data.repository.ServiciosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la interfaz de usuario para la pantalla de reporte de servicio.
 */
data class ServiceReportState(
    val technicianName: String = "",
    val observations: String = "",
    val attachedPhotos: List<String> = emptyList(),
    val isSignaturePadVisible: Boolean = false,
    val hasSignature: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * Define los eventos que la UI puede enviar al ViewModel para interactuar.
 */
sealed interface ServiceReportEvent {
    data class OnObservationsChange(val text: String) : ServiceReportEvent
    object OnAddPhotoClick : ServiceReportEvent
    data class OnDeletePhoto(val photoUri: String) : ServiceReportEvent // Evento para eliminar foto
    object OnSignatureClick : ServiceReportEvent
    object OnSignaturePadDismiss : ServiceReportEvent
    object OnSignatureSave : ServiceReportEvent
}

/**
 * ViewModel para la pantalla de Reporte de Servicio.
 */
class ServiceReportViewModel(private val repository: ServiciosRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceReportState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * Función central para manejar todos los eventos que provienen de la UI.
     */
    fun onEvent(event: ServiceReportEvent) {
        when (event) {
            is ServiceReportEvent.OnObservationsChange -> {
                _uiState.update { it.copy(observations = event.text) }
            }
            ServiceReportEvent.OnAddPhotoClick -> {
                val photoPlaceholder = "foto_${System.currentTimeMillis()}.jpg"
                _uiState.update { it.copy(attachedPhotos = it.attachedPhotos + photoPlaceholder) }
            }
            is ServiceReportEvent.OnDeletePhoto -> {
                // Lógica para eliminar la foto de la lista
                _uiState.update { it.copy(attachedPhotos = it.attachedPhotos - event.photoUri) }
            }
            ServiceReportEvent.OnSignatureClick -> {
                _uiState.update { it.copy(isSignaturePadVisible = true) }
            }
            ServiceReportEvent.OnSignaturePadDismiss -> {
                _uiState.update { it.copy(isSignaturePadVisible = false) }
            }
            ServiceReportEvent.OnSignatureSave -> {
                _uiState.update { it.copy(isSignaturePadVisible = false, hasSignature = true) }
            }
        }
    }

    /**
     * Carga los datos iniciales necesarios para la pantalla.
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val technicianName = repository.obtenerEventos()?.firstOrNull()?.tecnicoNombre
                _uiState.update {
                    it.copy(
                        technicianName = technicianName ?: "Técnico",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error al cargar datos: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}
