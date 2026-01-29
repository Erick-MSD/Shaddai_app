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

/**
 * ViewModel para la pantalla de calendario
 * Gestiona el estado y la lógica de negocio del calendario de técnicos
 *
 * @property repository Repositorio para acceder a los datos de servicios
 */
class CalendarioViewModel(
    private val repository: ServiciosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarioState())
    val uiState: StateFlow<CalendarioState> = _uiState.asStateFlow()

    // ID del técnico actual (en producción vendría de la sesión)
    private val tecnicoIdActual = "TEC-017" // J. Martinez por defecto

    init {
        cargarDatos()
    }

    /**
     * Maneja los eventos generados por la UI
     * @param event Evento a procesar
     */
    fun onEvent(event: CalendarioEvent) {
        when (event) {
            is CalendarioEvent.CambiarFecha -> cambiarFecha(event.nuevaFecha)
            is CalendarioEvent.CambiarVista -> cambiarVista(event.nuevaVista)
            is CalendarioEvent.SeleccionarEvento -> seleccionarEvento(event.evento)
            is CalendarioEvent.RetrocederDia -> retrocederDia()
            is CalendarioEvent.AvanzarDia -> avanzarDia()
            else -> { /* Otros eventos de navegación */ }
        }
    }

    /**
     * Carga los datos iniciales del técnico y sus eventos
     * Utiliza coroutines para operaciones asíncronas
     */
    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Cargar técnico actual
                val tecnico = repository.obtenerTecnico(tecnicoIdActual)

                if (tecnico == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo cargar la información del técnico"
                        )
                    }
                    return@launch
                }

                // Cargar eventos de la fecha seleccionada
                val eventos = filtrarEventosPorFecha(
                    _uiState.value.fechaSeleccionada
                )

                _uiState.update {
                    it.copy(
                        tecnicoActual = tecnico,
                        eventos = eventos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Cambia la fecha seleccionada y recarga los eventos
     * @param nuevaFecha Nueva fecha a seleccionar
     */
    private fun cambiarFecha(nuevaFecha: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val eventos = filtrarEventosPorFecha(nuevaFecha)

            _uiState.update {
                it.copy(
                    fechaSeleccionada = nuevaFecha,
                    eventos = eventos,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Cambia la vista del calendario (Día, Semana, Mes)
     * @param nuevaVista Nueva vista a mostrar
     */
    private fun cambiarVista(nuevaVista: VistaCalendario) {
        _uiState.update { it.copy(vistaCalendario = nuevaVista) }
    }

    /**
     * Maneja la selección de un evento específico
     * @param evento Evento seleccionado
     */
    private fun seleccionarEvento(evento: EventoServicio) {
        // Por ahora solo imprimimos, en el futuro podría navegar a detalles
        println("Evento seleccionado: ${evento.ticketNumber}")
    }

    /**
     * Retrocede un día en el calendario
     */
    private fun retrocederDia() {
        val fechaActual = _uiState.value.fechaSeleccionada
        val nuevaFecha = fechaActual.plus(-1, DateTimeUnit.DAY)
        cambiarFecha(nuevaFecha)
    }

    /**
     * Avanza un día en el calendario
     */
    private fun avanzarDia() {
        val fechaActual = _uiState.value.fechaSeleccionada
        val nuevaFecha = fechaActual.plus(1, DateTimeUnit.DAY)
        cambiarFecha(nuevaFecha)
    }

    /**
     * Filtra eventos por fecha utilizando funciones de orden superior
     * @param fecha Fecha para filtrar
     * @return Lista de eventos filtrados y ordenados
     */
    private suspend fun filtrarEventosPorFecha(fecha: LocalDate): List<EventoServicio> {
        return try {
            val todosEventos = repository.obtenerEventosPorTecnico(tecnicoIdActual)

            // Usar función de orden superior (filter y sortedBy)
            todosEventos
                .filter { evento -> evento.fechaProgramada == fecha }
                .sortedBy { it.horaInicio }
        } catch (e: Exception) {
            println("Error al filtrar eventos: ${e.message}")
            emptyList()
        }
    }

    /**
     * Obtiene eventos de múltiples días (para vista semanal)
     * @param fechaInicio Fecha de inicio del rango
     * @param dias Cantidad de días a obtener
     * @return Mapa con fecha como clave y lista de eventos como valor
     */
    fun obtenerEventosRango(fechaInicio: LocalDate, dias: Int): Map<LocalDate, List<EventoServicio>> {
        val eventosRango = mutableMapOf<LocalDate, List<EventoServicio>>()

        // Usar range y map (funciones de orden superior)
        (0 until dias).forEach { offset ->
            val fecha = fechaInicio.plus(offset, DateTimeUnit.DAY)
            viewModelScope.launch {
                val eventos = filtrarEventosPorFecha(fecha)
                eventosRango[fecha] = eventos
            }
        }

        return eventosRango
    }
}
