package com.example.shaddai_app_android.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Modelo de datos para un servicio asignado a un técnico, mapeado directamente desde Firestore.
 * La colección en Firestore es: technician_services
 */
data class TechnicianService(
    @DocumentId
    val id: String = "",
    val technicianUid: String = "",
    val technicianName: String = "",
    val clientName: String = "",
    val clientPhone: String = "",
    val serviceType: String = "",       // Ej: "Aire Acondicionado", "Plomería"
    val title: String = "",             // Ej: "Limpieza y Mantenimiento"
    val address: String = "",
    val scheduledDate: Timestamp? = null,
    val startTime: String = "",         // Ej: "09:00 AM"
    val endTime: String = "",           // Ej: "11:00 AM"
    val status: String = "pending",     // pending, in_progress, completed, cancelled
    val notes: String = "",
    val ticketNumber: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    /**
     * Obtiene el rango de tiempo formateado.
     */
    fun getTimeRange(): String {
        return if (startTime.isNotBlank() && endTime.isNotBlank()) {
            "$startTime - $endTime"
        } else {
            startTime.ifBlank { "Sin horario" }
        }
    }
}

