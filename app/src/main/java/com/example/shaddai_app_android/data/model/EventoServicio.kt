package com.example.shaddai_app_android.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class EventoServicio(
    val ticketNumber: String = "",
    val tipoServicio: TipoServicio = TipoServicio.PLOMERIA,
    val subservicio: String = "",
    val ubicacionDireccion: String = "",
    val fechaProgramada: LocalDate = LocalDate(2026, 1, 22),
    val horaInicio: LocalTime = LocalTime(9, 0),
    val horaFin: LocalTime = LocalTime(10, 0),
    val tecnicoNombre: String = "",
    val tecnicoId: String = ""
) {
    fun obtenerRangoTiempo(): String {
        return "${formatTime(horaInicio)} - ${formatTime(horaFin)}"
    }

    fun obtenerDuracionFormateada(): String {
        val minutos = (horaFin.hour * 60 + horaFin.minute) - (horaInicio.hour * 60 + horaInicio.minute)
        val horas = minutos / 60
        val mins = minutos % 60
        return when {
            horas > 0 && mins > 0 -> "${horas}h ${mins}min"
            horas > 0 -> "${horas}h"
            else -> "${mins}min"
        }
    }

    private fun formatTime(time: LocalTime): String {
        val hour = if (time.hour > 12) time.hour - 12 else if (time.hour == 0) 12 else time.hour
        val amPm = if (time.hour >= 12) "PM" else "AM"
        return "${hour}:${time.minute.toString().padStart(2, '0')} $amPm"
    }
}

