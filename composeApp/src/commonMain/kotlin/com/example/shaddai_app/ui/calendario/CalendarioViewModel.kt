package com.example.shaddai_app.ui.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaddai_app.data.model.EventoServicio
import com.example.shaddai_app.data.repository.ServiciosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class CalendarioViewModel(
    private val repository: ServiciosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarioState())
    val uiState: StateFlow<CalendarioState> = _uiState.asStateFlow()

    private val tecnicoIdActual = "TEC-017" // J. Martinez por defecto

    init {
        cargarDatosIniciales()
    }

    fun onEvent(event: CalendarioEvent) {
        when (event) {
            is CalendarioEvent.CambiarFecha -> cambiarFecha(event.nuevaFecha)
            is CalendarioEvent.SeleccionarEvento -> println("Evento seleccionado: ${event.evento.ticketNumber}")
            is CalendarioEvent.RetrocederDia -> cambiarFecha(uiState.value.fechaSeleccionada.plus(-1, DateTimeUnit.DAY))
            is CalendarioEvent.AvanzarDia -> cambiarFecha(uiState.value.fechaSeleccionada.plus(1, DateTimeUnit.DAY))
            else -> {}
        }
    }

    private fun cargarDatosIniciales() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val tecnico = repository.obtenerTecnico(tecnicoIdActual)
                val todosLosEventosDelTecnico = repository.obtenerEventosPorTecnico(tecnicoIdActual)

                // Lógica inteligente: Si hay eventos, ir a la fecha del primero. Si no, quedarse en hoy.
                val fechaAMostrar = todosLosEventosDelTecnico.minOfOrNull { it.fechaProgramada } ?: _uiState.value.fechaSeleccionada
                
                val eventosParaMostrar = todosLosEventosDelTecnico
                    .filter { it.fechaProgramada == fechaAMostrar }
                    .sortedBy { it.horaInicio }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tecnicoActual = tecnico,
                        fechaSeleccionada = fechaAMostrar,
                        eventos = eventosParaMostrar
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar datos: ${e.message}") }
            }
        }
    }

    private fun cambiarFecha(nuevaFecha: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, fechaSeleccionada = nuevaFecha) }
            try {
                val eventosDelDia = repository.obtenerEventosPorTecnico(tecnicoIdActual)
                    .filter { it.fechaProgramada == nuevaFecha }
                    .sortedBy { it.horaInicio }

                _uiState.update { it.copy(isLoading = false, eventos = eventosDelDia) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al cambiar de fecha: ${e.message}") }
            }
        }
    }
}
