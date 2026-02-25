package com.example.shaddai_app_android.model

data class CitaData(
    // Pantalla 1 - Descripción y tipo de servicio
    val descripcion: String = "",
    val tiposServicio: Set<String> = emptySet(),

    // Pantalla 2 - Dirección
    val calle: String = "",
    val numeroExterior: String = "",
    val colonia: String = "",
    val codigoPostal: String = "",
    val referencias: String = "",

    // Pantalla 3 - Fecha y hora
    val fecha: String = "",
    val hora: String = "",

    // Pantalla 4 - Confirmación (solo lectura)
)

val tiposDeServicio = listOf(
    "Plomería",
    "Electricidad",
    "Carpintería",
    "Pintura",
    "Herrería",
    "Albañilería",
    "Limpieza",
    "Jardinería",
    "Aire acondicionado",
    "Impermeabilización",
    "Instalación de pisos",
    "Otro"
)

