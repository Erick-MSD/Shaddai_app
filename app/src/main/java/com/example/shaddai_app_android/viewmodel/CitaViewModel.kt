package com.example.shaddai_app_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.model.CitaData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CitaViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("citas_clima")
    private val auth = FirebaseAuth.getInstance()

    private val _citas = MutableStateFlow<List<CitaClima>>(emptyList())
    val citas: StateFlow<List<CitaClima>> = _citas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var listener: ValueEventListener? = null
    private var currentUid: String? = null

    init {
        obtenerCitas()
    }

    private fun obtenerCitas() {
        val uid = auth.currentUser?.uid ?: return

        // Si ya estamos escuchando para este mismo uid, no re-registrar
        if (uid == currentUid && listener != null) return

        // Limpiar listener anterior si existía
        listener?.let { dbRef.removeEventListener(it) }
        currentUid = uid

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.children.mapNotNull { child ->
                    child.getValue(CitaClima::class.java)?.copy(id = child.key ?: "")
                }.filter { it.uid == uid }
                    .sortedByDescending { it.fechaCreacion }
                _citas.value = lista
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        dbRef.addValueEventListener(listener!!)
    }

    fun guardarCita(citaData: CitaData, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        _isLoading.value = true

        val direccionCompleta = buildString {
            if (citaData.calle.isNotBlank()) append("${citaData.calle} ${citaData.numeroExterior}, ")
            if (citaData.colonia.isNotBlank()) append("${citaData.colonia}, ")
            if (citaData.codigoPostal.isNotBlank()) append("CP ${citaData.codigoPostal}")
        }.trimEnd(',', ' ')

        val nuevaCita = CitaClima(
            cliente = auth.currentUser?.email ?: "",
            direccion = direccionCompleta,
            fecha = citaData.fecha,
            hora = citaData.hora,
            servicio = citaData.tiposServicio.joinToString(", "),
            equipo = citaData.descripcion,
            estado = "pendiente",
            tecnico_asignado = "",
            notas = citaData.referencias,
            uid = uid,
            descripcion = citaData.descripcion,
            calle = citaData.calle,
            numeroExterior = citaData.numeroExterior,
            colonia = citaData.colonia,
            codigoPostal = citaData.codigoPostal,
            referencias = citaData.referencias,
            tiposServicio = citaData.tiposServicio.toList(),
            fechaCreacion = System.currentTimeMillis()
        )

        val newRef = dbRef.push()
        newRef.setValue(nuevaCita)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun getCitaById(citaId: String): CitaClima? {
        return _citas.value.find { it.id == citaId }
    }

    fun cancelarCita(citaId: String) {
        dbRef.child(citaId).child("estado").setValue("cancelado")
    }

    fun actualizarEstadoCita(citaId: String, completada: Boolean) {
        val nuevoEstado = if (completada) "completado" else "pendiente"
        dbRef.child(citaId).child("estado").setValue(nuevoEstado)
    }

    fun calificarCita(citaId: String, calificacion: Int, comentario: String = "") {
        val updates = mutableMapOf<String, Any>(
            "calificacion" to calificacion
        )
        if (comentario.isNotBlank()) {
            updates["comentarioCalificacion"] = comentario
        }
        dbRef.child(citaId).updateChildren(updates).addOnSuccessListener {
            // Recalcular el rating promedio del técnico
            recalcularRatingTecnico(citaId)
        }
    }

    /**
     * Lee el tecnico_uid de la cita, luego busca todas las citas completadas
     * y calificadas de ese técnico para recalcular su rating promedio.
     */
    private fun recalcularRatingTecnico(citaId: String) {
        dbRef.child(citaId).get().addOnSuccessListener { snapshot ->
            val tecnicoUid = snapshot.child("tecnico_uid").getValue(String::class.java)
            if (tecnicoUid.isNullOrBlank()) {
                // Fallback: buscar por nombre del técnico
                val tecnicoNombre = snapshot.child("tecnico_asignado").getValue(String::class.java) ?: return@addOnSuccessListener
                buscarTecnicoYRecalcularPorNombre(tecnicoNombre)
                return@addOnSuccessListener
            }

            // Leer todas las citas para calcular el rating promedio
            dbRef.orderByChild("tecnico_uid").equalTo(tecnicoUid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snap: DataSnapshot) {
                        var totalCalificaciones = 0
                        var sumaCalificaciones = 0
                        snap.children.forEach { child ->
                            val cal = child.child("calificacion").getValue(Int::class.java) ?: 0
                            if (cal > 0) {
                                totalCalificaciones++
                                sumaCalificaciones += cal
                            }
                        }
                        if (totalCalificaciones > 0) {
                            val promedio = sumaCalificaciones.toDouble() / totalCalificaciones
                            FirebaseFirestore.getInstance()
                                .collection("technicians").document(tecnicoUid)
                                .update("rating", promedio)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    /**
     * Fallback: buscar el técnico por nombre en Firestore y recalcular su rating.
     */
    private fun buscarTecnicoYRecalcularPorNombre(nombre: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("technicians")
            .whereEqualTo("name", nombre)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val tecDoc = querySnapshot.documents[0]
                    val tecnicoUid = tecDoc.id

                    dbRef.orderByChild("tecnico_asignado").equalTo(nombre)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snap: DataSnapshot) {
                                var totalCalificaciones = 0
                                var sumaCalificaciones = 0
                                snap.children.forEach { child ->
                                    val cal = child.child("calificacion").getValue(Int::class.java) ?: 0
                                    if (cal > 0) {
                                        totalCalificaciones++
                                        sumaCalificaciones += cal
                                    }
                                }
                                if (totalCalificaciones > 0) {
                                    val promedio = sumaCalificaciones.toDouble() / totalCalificaciones
                                    firestore.collection("technicians").document(tecnicoUid)
                                        .update("rating", promedio)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }
    }

    fun refrescarCitas() {
        listener?.let { dbRef.removeEventListener(it) }
        listener = null
        currentUid = null
        obtenerCitas()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { dbRef.removeEventListener(it) }
    }
}