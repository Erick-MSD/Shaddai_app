package com.example.shaddai_app.data.repository

import com.example.shaddai_app.data.model.EventoServicio
import com.example.shaddai_app.data.model.Tecnico
import kotlinx.datetime.LocalDate

/**
 * Interfaz que define el contrato para acceder a los datos de servicios y técnicos
 * Esta interfaz permite cambiar fácilmente entre diferentes fuentes de datos (CSV, SQL, NoSQL)
 */
interface ServiciosRepository {
    /**
     * Obtiene todos los eventos de servicio
     * @return Lista de eventos o null si hay error
     */
    suspend fun obtenerEventos(): List<EventoServicio>?

    /**
     * Obtiene eventos filtrados por fecha
     * @param fecha Fecha para filtrar los eventos
     * @return Lista de eventos en la fecha especificada
     */
    suspend fun obtenerEventosPorFecha(fecha: LocalDate): List<EventoServicio>

    /**
     * Obtiene eventos filtrados por técnico
     * @param tecnicoId ID del técnico
     * @return Lista de eventos asignados al técnico
     */
    suspend fun obtenerEventosPorTecnico(tecnicoId: String): List<EventoServicio>

    /**
     * Obtiene la información de un técnico por su ID
     * @param tecnicoId ID del técnico
     * @return Objeto Tecnico o null si no se encuentra
     */
    suspend fun obtenerTecnico(tecnicoId: String): Tecnico?

    /**
     * Obtiene todos los técnicos del sistema
     * @return Mapa con ID como clave y Tecnico como valor
     */
    suspend fun obtenerTodosTecnicos(): Map<String, Tecnico>
}
