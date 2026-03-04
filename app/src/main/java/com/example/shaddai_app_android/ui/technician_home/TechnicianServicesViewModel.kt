package com.example.shaddai_app_android.ui.technician_home

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.CitaClima
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Estado de la pantalla de servicios del técnico.
 */
data class TechnicianServicesState(
    val allServices: List<CitaClima> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedFilter: ServiceFilter = ServiceFilter.PENDIENTES
)

enum class ServiceFilter(val label: String) {
    PENDIENTES("Pendientes"),
    EN_PROCESO("En Proceso"),
    COMPLETADOS("Completados"),
    TODOS("Todos")
}

/**
 * ViewModel que conecta con Firebase Realtime Database para obtener
 * las citas de servicio creadas por los clientes (colección citas_clima).
 */
class TechnicianServicesViewModel : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("citas_clima")

    private val _uiState = MutableStateFlow(TechnicianServicesState())
    val uiState: StateFlow<TechnicianServicesState> = _uiState.asStateFlow()

    private var listener: ValueEventListener? = null

    init {
        loadServices()
    }

    /**
     * Escucha en tiempo real las citas creadas por los clientes.
     */
    private fun loadServices() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val services = snapshot.children.mapNotNull { child ->
                    child.getValue(CitaClima::class.java)?.copy(id = child.key ?: "")
                }.sortedByDescending { it.fechaCreacion }

                _uiState.value = _uiState.value.copy(
                    allServices = services,
                    isLoading = false,
                    error = null
                )
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar servicios: ${error.message}"
                )
            }
        }
        dbRef.addValueEventListener(listener!!)
    }

    /**
     * Cambia el filtro de servicios.
     */
    fun setFilter(filter: ServiceFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    /**
     * Obtiene los servicios filtrados según el filtro seleccionado.
     */
    fun getFilteredServices(): List<CitaClima> {
        val all = _uiState.value.allServices
        return when (_uiState.value.selectedFilter) {
            ServiceFilter.PENDIENTES -> all.filter { it.estado == "pendiente" }
            ServiceFilter.EN_PROCESO -> all.filter { it.estado == "en_proceso" }
            ServiceFilter.COMPLETADOS -> all.filter { it.estado == "completado" }
            ServiceFilter.TODOS -> all
        }
    }

    /**
     * Actualiza el estado de una cita (el técnico la toma, completa, etc.)
     * Cuando se completa, incrementa el contador de servicios completados del técnico.
     */
    fun updateServiceStatus(citaId: String, newStatus: String) {
        dbRef.child(citaId).child("estado").setValue(newStatus)

        if (newStatus == "completado") {
            // Incrementar completedServices del técnico en Firestore
            dbRef.child(citaId).get().addOnSuccessListener { snapshot ->
                val tecnicoUid = snapshot.child("tecnico_uid").getValue(String::class.java)
                if (!tecnicoUid.isNullOrBlank()) {
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("technicians").document(tecnicoUid)
                        .update("completedServices", FieldValue.increment(1))
                }
            }
        }
    }

    /**
     * El técnico se asigna a sí mismo a una cita.
     * Guarda también el UID del técnico para poder actualizar sus estadísticas.
     */
    fun assignToService(citaId: String, technicianName: String) {
        val techUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val updates = mapOf(
            "tecnico_asignado" to technicianName,
            "tecnico_uid" to techUid,
            "estado" to "en_proceso"
        )
        dbRef.child(citaId).updateChildren(updates)
    }

    /**
     * Fuerza recarga de datos.
     */
    fun refresh() {
        listener?.let { dbRef.removeEventListener(it) }
        loadServices()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { dbRef.removeEventListener(it) }
    }
}

