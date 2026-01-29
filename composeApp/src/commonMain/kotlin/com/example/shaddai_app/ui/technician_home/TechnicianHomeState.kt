
package com.example.shaddai_app.ui.technician_home

/**
 * Define la estructura de datos para el estado de la pantalla principal del técnico.
 * Estos datos serán proporcionados por el ViewModel y consumidos por la UI.
 */
data class TechnicianHomeState(
    val currentService: CurrentService? = null,
    val upcomingServices: List<UpcomingService> = emptyList(),
    val isLoading: Boolean = true
)

/**
 * Representa el servicio actual o más próximo.
 * En el futuro, estos datos provendrán de un backend.
 */
data class CurrentService(
    val id: String,
    val serviceType: String, // Ej: "Minisplit"
    val title: String, // Ej: "Limpieza y Mantenimiento"
    val time: String, // Ej: "09:00 AM - 11:00 AM"
    val address: String
)

/**
 * Representa una cita de servicio futura.
 * En el futuro, estos datos provendrán de un backend.
 */
data class UpcomingService(
    val id: String,
    val time: String, // Ej: "12:00 PM"
    val title: String, // Ej: "Instalación"
    val serviceType: String, // Ej: "Aire Acondicionado"
    val address: String
)
