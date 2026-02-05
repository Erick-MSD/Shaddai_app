package com.example.shaddai_app.ui.calendario

import com.example.shaddai_app.data.model.EventoServicio
import com.example.shaddai_app.data.model.Tecnico
import kotlinx.datetime.LocalDate

/**
 * Estado de la UI del calendario
 * @property tecnicoActual Técnico actualmente logueado
 * @property fechaSeleccionada Fecha seleccionada en el calendario
 * @property eventos Lista de eventos del día seleccionado
 * @property vistaCalendario Vista actual del calendario (DIA, SEMANA, MES)
 * @property isLoading Indica si se están cargando datos
 * @property error Mensaje de error si existe alguno
 */
data class CalendarioState(
    val tecnicoActual: Tecnico? = null,
    val fechaSeleccionada: LocalDate = LocalDate(2026, 1, 22),
    val eventos: List<EventoServicio> = emptyList(),
    val vistaCalendario: VistaCalendario = VistaCalendario.DIA,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Enum que representa las diferentes vistas del calendario
 */
enum class VistaCalendario {
    DIA, SEMANA, MES
}

/**
 * Eventos que puede generar la UI del calendario
 */
sealed class CalendarioEvent {
    data class CambiarFecha(val nuevaFecha: LocalDate) : CalendarioEvent()
    data class CambiarVista(val nuevaVista: VistaCalendario) : CalendarioEvent()
    data class SeleccionarEvento(val evento: EventoServicio) : CalendarioEvent()
    data object NavegacionHome : CalendarioEvent()
    data object NavegacionHerramientas : CalendarioEvent()
    data object NavegacionSoporte : CalendarioEvent()
    data object RetrocederDia : CalendarioEvent()
    data object AvanzarDia : CalendarioEvent()
}
