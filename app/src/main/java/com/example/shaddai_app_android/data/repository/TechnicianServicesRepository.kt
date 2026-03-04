package com.example.shaddai_app_android.data.repository

import com.example.shaddai_app_android.data.model.TechnicianService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar

/**
 * Repositorio para gestionar los servicios asignados a técnicos desde Firebase Firestore.
 * Colección: technician_services
 */
class TechnicianServicesRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("technician_services")

    /**
     * Obtiene los servicios de hoy del técnico en tiempo real.
     * Filtra por technicianUid y scheduledDate = hoy.
     */
    fun obtenerServiciosDeHoy(technicianUid: String): Flow<List<TechnicianService>> = callbackFlow {
        val todayStart = getTodayStart()
        val todayEnd = getTodayEnd()

        val listener = collection
            .whereEqualTo("technicianUid", technicianUid)
            .whereGreaterThanOrEqualTo("scheduledDate", todayStart)
            .whereLessThanOrEqualTo("scheduledDate", todayEnd)
            .orderBy("scheduledDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val services = snapshot?.toObjects(TechnicianService::class.java) ?: emptyList()
                trySend(services)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Obtiene todos los servicios pendientes del técnico (hoy y futuros) en tiempo real.
     */
    fun obtenerServiciosPendientes(technicianUid: String): Flow<List<TechnicianService>> = callbackFlow {
        val now = Timestamp.now()

        val listener = collection
            .whereEqualTo("technicianUid", technicianUid)
            .whereIn("status", listOf("pending", "in_progress"))
            .orderBy("scheduledDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val services = snapshot?.toObjects(TechnicianService::class.java) ?: emptyList()
                trySend(services)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Actualiza el estado de un servicio (ej: pending -> in_progress -> completed).
     */
    suspend fun actualizarEstado(serviceId: String, nuevoEstado: String) {
        collection.document(serviceId).update("status", nuevoEstado).await()
    }

    /**
     * Siembra datos de ejemplo en Firestore para un técnico.
     * Solo crea datos si la colección está vacía para ese técnico.
     */
    suspend fun seedDatosEjemplo(technicianUid: String, technicianName: String) {
        val existing = collection
            .whereEqualTo("technicianUid", technicianUid)
            .limit(1)
            .get()
            .await()

        if (!existing.isEmpty) return // Ya hay datos, no crear duplicados

        val calendar = Calendar.getInstance()
        // Hoy a las 00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val todayTimestamp = Timestamp(calendar.time)

        // Mañana
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowTimestamp = Timestamp(calendar.time)

        // Pasado mañana
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayAfterTomorrow = Timestamp(calendar.time)

        val serviciosEjemplo = listOf(
            TechnicianService(
                technicianUid = technicianUid,
                technicianName = technicianName,
                clientName = "María García",
                clientPhone = "5551234567",
                serviceType = "Aire Acondicionado",
                title = "Limpieza y Mantenimiento",
                address = "Av. Siempreviva 742, Springfield",
                scheduledDate = todayTimestamp,
                startTime = "09:00 AM",
                endTime = "11:00 AM",
                status = "pending",
                ticketNumber = "TKT-001",
                notes = "Cliente pide puntualidad"
            ),
            TechnicianService(
                technicianUid = technicianUid,
                technicianName = technicianName,
                clientName = "Carlos López",
                clientPhone = "5559876543",
                serviceType = "Electricidad",
                title = "Revisión de Cableado",
                address = "Calle Falsa 123, Shelbyville",
                scheduledDate = todayTimestamp,
                startTime = "12:00 PM",
                endTime = "01:30 PM",
                status = "pending",
                ticketNumber = "TKT-002",
                notes = ""
            ),
            TechnicianService(
                technicianUid = technicianUid,
                technicianName = technicianName,
                clientName = "Ana Martínez",
                clientPhone = "5555551234",
                serviceType = "Refrigerador",
                title = "Reparación de Compresor",
                address = "Blvd. del Ocaso 45, Capital City",
                scheduledDate = todayTimestamp,
                startTime = "03:00 PM",
                endTime = "05:00 PM",
                status = "pending",
                ticketNumber = "TKT-003",
                notes = "Equipo marca Samsung"
            ),
            TechnicianService(
                technicianUid = technicianUid,
                technicianName = technicianName,
                clientName = "Roberto Sánchez",
                clientPhone = "5558765432",
                serviceType = "Lavadora",
                title = "Instalación Completa",
                address = "Ruta 9, Ogdenville",
                scheduledDate = tomorrowTimestamp,
                startTime = "10:00 AM",
                endTime = "12:00 PM",
                status = "pending",
                ticketNumber = "TKT-004",
                notes = "Traer conectores especiales"
            ),
            TechnicianService(
                technicianUid = technicianUid,
                technicianName = technicianName,
                clientName = "Laura Fernández",
                clientPhone = "5553456789",
                serviceType = "Aire Acondicionado",
                title = "Instalación de Minisplit",
                address = "Paseo de la Reforma 250, CDMX",
                scheduledDate = dayAfterTomorrow,
                startTime = "09:00 AM",
                endTime = "01:00 PM",
                status = "pending",
                ticketNumber = "TKT-005",
                notes = "Piso 3, depto 302"
            )
        )

        val batch = db.batch()
        serviciosEjemplo.forEach { servicio ->
            val docRef = collection.document()
            batch.set(docRef, servicio)
        }
        batch.commit().await()
    }

    private fun getTodayStart(): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return Timestamp(calendar.time)
    }

    private fun getTodayEnd(): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return Timestamp(calendar.time)
    }
}

