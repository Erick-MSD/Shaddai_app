package com.example.shaddai_app_android.model

data class TechnicianProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val specialty: String = "",        // Ej: "Aire Acondicionado", "Refrigeración", etc.
    val licenseNumber: String = "",     // Número de licencia/cédula profesional
    val role: String = "technician",
    val isActive: Boolean = true,
    val rating: Double = 0.0,
    val completedServices: Int = 0
)

