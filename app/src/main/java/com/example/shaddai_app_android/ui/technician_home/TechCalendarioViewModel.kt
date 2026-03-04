package com.example.shaddai_app_android.ui.technician_home

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.CitaClima
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

/**
 * Estado del calendario del técnico.
 */
data class TechCalendarioState(
    val fechaSeleccionada: LocalDate = LocalDate.now(),
    val todasLasCitas: List<CitaClima> = emptyList(),
    val citasDelDia: List<CitaClima> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel del Calendario del Técnico.
 * Lee las citas aceptadas por el técnico desde Firebase Realtime Database (citas_clima).
 */
class TechCalendarioViewModel(
    private val technicianName: String = "Técnico"
) : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("citas_clima")

    private val _uiState = MutableStateFlow(TechCalendarioState())
    val uiState: StateFlow<TechCalendarioState> = _uiState.asStateFlow()

    private var listener: ValueEventListener? = null

    init {
        loadCitas()
    }

    private fun loadCitas() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val citas = snapshot.children.mapNotNull { child ->
                    child.getValue(CitaClima::class.java)?.copy(id = child.key ?: "")
                }

                // Solo las citas asignadas a este técnico (en_proceso o pendiente asignadas)
                val misCitas = citas.filter { cita ->
                    cita.tecnico_asignado.equals(technicianName, ignoreCase = true)
                            && cita.estado != "cancelado"
                }.sortedBy { it.fecha + it.hora }

                val fechaSel = _uiState.value.fechaSeleccionada
                val citasDelDia = filtrarPorFecha(misCitas, fechaSel)

                _uiState.value = _uiState.value.copy(
                    todasLasCitas = misCitas,
                    citasDelDia = citasDelDia,
                    isLoading = false,
                    error = null
                )
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar citas: ${error.message}"
                )
            }
        }
        dbRef.addValueEventListener(listener!!)
    }

    fun cambiarFecha(nuevaFecha: LocalDate) {
        val citasDelDia = filtrarPorFecha(_uiState.value.todasLasCitas, nuevaFecha)
        _uiState.value = _uiState.value.copy(
            fechaSeleccionada = nuevaFecha,
            citasDelDia = citasDelDia
        )
    }

    fun retrocederDia() {
        cambiarFecha(_uiState.value.fechaSeleccionada.minusDays(1))
    }

    fun avanzarDia() {
        cambiarFecha(_uiState.value.fechaSeleccionada.plusDays(1))
    }

    /**
     * Filtra citas por fecha. La fecha en CitaClima es un String (ej: "2026-03-04" o "04/03/2026").
     */
    private fun filtrarPorFecha(citas: List<CitaClima>, fecha: LocalDate): List<CitaClima> {
        val fechaStr1 = fecha.toString()  // "2026-03-04"
        val fechaStr2 = "%02d/%02d/%04d".format(fecha.dayOfMonth, fecha.monthValue, fecha.year) // "04/03/2026"
        val fechaStr3 = "%04d-%02d-%02d".format(fecha.year, fecha.monthValue, fecha.dayOfMonth) // "2026-03-04"

        return citas.filter { cita ->
            cita.fecha == fechaStr1 || cita.fecha == fechaStr2 || cita.fecha == fechaStr3
                    || cita.fecha.contains(fechaStr1) || cita.fecha.contains(fechaStr3)
        }.sortedBy { it.hora }
    }

    /**
     * Retorna las fechas que tienen al menos una cita (para marcar en el calendario).
     */
    fun fechasConCitas(): Set<String> {
        return _uiState.value.todasLasCitas.map { it.fecha }.toSet()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { dbRef.removeEventListener(it) }
    }
}

