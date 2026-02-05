package com.example.shaddai_app.data.model

/**
 * Data class que representa un técnico del sistema
 * @property id Identificador único del técnico
 * @property nombre Nombre completo del técnico
 */
data class Tecnico(
    val id: String,
    val nombre: String
) {
    /**
     * Obtiene el saludo personalizado del técnico
     * @return String formateado con el saludo
     */
    fun obtenerSaludo(): String = "Hola, $nombre"

    /**
     * Verifica si el ID del técnico es válido
     * @return true si el ID no está vacío, false en caso contrario
     */
    fun esIdValido(): Boolean = id.isNotEmpty()
}
