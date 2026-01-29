package com.example.shaddai_app.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

/**
 * Enum que representa los diferentes tipos de servicio
 */
enum class TipoServicio(val displayName: String, val colorHex: Long) {
    PLOMERIA("Plomería", 0xFF4CAF50),
    ELECTRICIDAD("Electricidad", 0xFFFFC107),
    AIRE_ACONDICIONADO("Clima", 0xFF2196F3),
    REDES("Redes", 0xFF9C27B0),
    CCTV("CCTV", 0xFFFF5722),
    CARPINTERIA("Carpintería", 0xFF795548),
    PINTURA("Pintura", 0xFFE91E63),
    JARDINERIA("Jardinería", 0xFF8BC34A),
    ALBANILERIA("Albañilería", 0xFF607D8B),
    CERRAJERIA("Cerrajería", 0xFFFFEB3B),
    LIMPIEZA_ESPECIALIZADA("Limpieza", 0xFF00BCD4),
    TI_SOPORTE("TI - Soporte", 0xFF673AB7),
    OTRO("Otro", 0xFF9E9E9E);

    companion object {
        /**
         * Función para obtener el tipo de servicio desde un string
         * @param tipo String con el nombre del tipo de servicio
         * @return TipoServicio correspondiente o OTRO si no se encuentra
         */
        fun fromString(tipo: String?): TipoServicio {
            return entries.find {
                it.name.replace("_", " ").equals(tipo, ignoreCase = true) ||
                it.displayName.equals(tipo, ignoreCase = true)
            } ?: OTRO
        }
    }
}

/**
 * Data class que representa un evento de servicio en el calendario
 * @property serviceId ID del servicio
 * @property ticketNumber Número de ticket
 * @property tipoServicio Tipo de servicio a realizar
 * @property subservicio Subtipo específico del servicio
 * @property descripcion Descripción detallada del servicio
 * @property ubicacionDireccion Dirección donde se realizará el servicio
 * @property fechaProgramada Fecha programada para el servicio
 * @property horaInicio Hora de inicio de la ventana de tiempo
 * @property horaFin Hora de fin de la ventana de tiempo
 * @property duracionMinutos Duración estimada en minutos
 * @property tecnicoId ID del técnico asignado
 * @property tecnicoNombre Nombre del técnico asignado
 */
data class EventoServicio(
    val serviceId: String,
    val ticketNumber: String,
    val tipoServicio: TipoServicio,
    val subservicio: String,
    val descripcion: String,
    val ubicacionDireccion: String,
    val fechaProgramada: LocalDate,
    val horaInicio: LocalTime,
    val horaFin: LocalTime,
    val duracionMinutos: Int,
    val tecnicoId: String,
    val tecnicoNombre: String
) {
    /**
     * Obtiene el rango de tiempo formateado
     * @return String con el formato "HH:mm - HH:mm"
     */
    fun obtenerRangoTiempo(): String {
        return "${formatearHora(horaInicio)} - ${formatearHora(horaFin)}"
    }

    /**
     * Obtiene la duración formateada
     * @return String con la duración en formato legible
     */
    fun obtenerDuracionFormateada(): String {
        val horas = duracionMinutos / 60
        val minutos = duracionMinutos % 60
        return when {
            horas > 0 && minutos > 0 -> "${horas}h ${minutos}m"
            horas > 0 -> "${horas}h"
            else -> "${minutos}m"
        }
    }

    /**
     * Verifica si el evento pertenece a un técnico específico
     * @param tecId ID del técnico a verificar
     * @return true si el evento pertenece al técnico
     */
    fun perteneceATecnico(tecId: String): Boolean = tecnicoId == tecId

    private fun formatearHora(hora: LocalTime): String {
        return String.format("%02d:%02d", hora.hour, hora.minute)
    }
}

/**
 * Data class para representar el evento especial de hora de comida
 */
data class EventoComida(
    val horaInicio: LocalTime,
    val horaFin: LocalTime
) {
    fun obtenerRangoTiempo(): String {
        return "${formatearHora(horaInicio)} - ${formatearHora(horaFin)}"
    }

    private fun formatearHora(hora: LocalTime): String {
        return String.format("%02d:%02d", hora.hour, hora.minute)
    }
}
