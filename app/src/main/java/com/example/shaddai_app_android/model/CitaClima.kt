package com.example.shaddai_app_android.model

data class CitaClima(
    val cliente: String = "",
    val direccion: String = "",
    val fecha: String = "",
    val hora: String = "",
    val servicio: String = "",
    val equipo: String = "",
    val estado: String = "",
    val tecnico_asignado: String = "",
    val tecnico_uid: String = "",
    val notas: String = "",
    val id: String = "",
    val uid: String = "",
    val descripcion: String = "",
    val calle: String = "",
    val numeroExterior: String = "",
    val colonia: String = "",
    val codigoPostal: String = "",
    val referencias: String = "",
    val tiposServicio: List<String> = emptyList(),
    val fechaCreacion: Long = 0L,
    val calificacion: Int = 0,
    val comentarioCalificacion: String = ""
)