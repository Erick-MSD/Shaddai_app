package com.example.shaddai_app.data.repository

import com.example.shaddai_app.data.model.EventoServicio
import com.example.shaddai_app.data.model.Tecnico
import com.example.shaddai_app.data.model.TipoServicio
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shaddai_app.composeapp.generated.resources.Res

/**
 * Implementación del repositorio que lee datos desde un archivo CSV
 * Esta implementación es temporal y será reemplazada por una base de datos
 */
class CsvServiciosRepository : ServiciosRepository {

    private var eventosCache: List<EventoServicio>? = null
    private var tecnicosCache: Map<String, Tecnico>? = null

    @OptIn(ExperimentalResourceApi::class)
    override suspend fun obtenerEventos(): List<EventoServicio>? {
        if (eventosCache != null) {
            return eventosCache
        }

        return try {
            // Leer el archivo CSV desde los recursos
            val csvContent = Res.readBytes("files/services_data.csv").decodeToString()
            val eventos = parsearCSV(csvContent)
            eventosCache = eventos
            eventos
        } catch (e: Exception) {
            println("Error al leer CSV: ${e.message}")
            null
        }
    }

    override suspend fun obtenerEventosPorFecha(fecha: LocalDate): List<EventoServicio> {
        val eventos = obtenerEventos() ?: return emptyList()
        return eventos.filter { it.fechaProgramada == fecha }
    }

    override suspend fun obtenerEventosPorTecnico(tecnicoId: String): List<EventoServicio> {
        val eventos = obtenerEventos() ?: return emptyList()
        return eventos.filter { it.perteneceATecnico(tecnicoId) }
    }

    override suspend fun obtenerTecnico(tecnicoId: String): Tecnico? {
        val tecnicos = obtenerTodosTecnicos()
        return tecnicos[tecnicoId]
    }

    override suspend fun obtenerTodosTecnicos(): Map<String, Tecnico> {
        if (tecnicosCache != null) {
            return tecnicosCache!!
        }

        val eventos = obtenerEventos() ?: return emptyMap()
        val tecnicos = eventos
            .map { Tecnico(it.tecnicoId, it.tecnicoNombre) }
            .distinctBy { it.id }
            .associateBy { it.id }

        tecnicosCache = tecnicos
        return tecnicos
    }

    /**
     * Parsea el contenido del CSV y convierte cada línea en un EventoServicio
     * Incluye manejo de excepciones para líneas mal formadas
     * @param csvContent Contenido completo del archivo CSV
     * @return Lista de eventos parseados exitosamente
     */
    private fun parsearCSV(csvContent: String): List<EventoServicio> {
        val lineas = csvContent.lines()
        if (lineas.isEmpty()) return emptyList()

        // Saltar la primera línea (encabezados)
        return lineas.drop(1)
            .filter { it.isNotBlank() }
            .mapNotNull { linea ->
                try {
                    parsearLineaCSV(linea)
                } catch (e: Exception) {
                    println("Error al parsear línea: ${e.message}")
                    null // Usar null safety para ignorar líneas con errores
                }
            }
    }

    /**
     * Parsea una línea individual del CSV
     * @param linea String con una línea completa del CSV
     * @return EventoServicio parseado o null si hay error
     */
    private fun parsearLineaCSV(linea: String): EventoServicio? {
        val campos = parsearCamposCSV(linea)

        // Verificar que tengamos suficientes campos
        if (campos.size < 27) return null

        // Parsear fecha (formato: 2026-01-28)
        val fechaParts = campos[21].split("-")
        if (fechaParts.size != 3) return null

        val fecha = try {
            LocalDate(
                year = fechaParts[0].toInt(),
                monthNumber = fechaParts[1].toInt(),
                dayOfMonth = fechaParts[2].toInt()
            )
        } catch (e: Exception) {
            return null
        }

        // Parsear horas (formato: 08:00)
        val horaInicio = parsearHora(campos[22]) ?: return null
        val horaFin = parsearHora(campos[23]) ?: return null

        // Obtener duración en minutos
        val duracion = try {
            campos[35].toIntOrNull() ?: 60
        } catch (e: Exception) {
            60
        }

        return EventoServicio(
            serviceId = campos[0],
            ticketNumber = campos[1],
            tipoServicio = TipoServicio.fromString(campos[4]),
            subservicio = campos[5],
            descripcion = campos[6],
            ubicacionDireccion = campos[9],
            fechaProgramada = fecha,
            horaInicio = horaInicio,
            horaFin = horaFin,
            duracionMinutos = duracion,
            tecnicoId = campos[25],
            tecnicoNombre = campos[26]
        )
    }

    /**
     * Parsea los campos de una línea CSV considerando comillas y comas dentro de campos
     * @param linea Línea del CSV a parsear
     * @return Lista de campos extraídos
     */
    private fun parsearCamposCSV(linea: String): List<String> {
        val campos = mutableListOf<String>()
        var campoActual = StringBuilder()
        var dentroDeComillas = false

        for (i in linea.indices) {
            val char = linea[i]
            when {
                char == '"' -> dentroDeComillas = !dentroDeComillas
                char == ',' && !dentroDeComillas -> {
                    campos.add(campoActual.toString().trim())
                    campoActual = StringBuilder()
                }
                else -> campoActual.append(char)
            }
        }
        // Agregar el último campo
        campos.add(campoActual.toString().trim())

        return campos
    }

    /**
     * Parsea una hora en formato HH:mm a LocalTime
     * @param horaStr String con la hora en formato HH:mm
     * @return LocalTime o null si el formato es inválido
     */
    private fun parsearHora(horaStr: String): LocalTime? {
        return try {
            val parts = horaStr.split(":")
            if (parts.size != 2) return null

            LocalTime(
                hour = parts[0].toInt(),
                minute = parts[1].toInt()
            )
        } catch (e: Exception) {
            null
        }
    }
}
