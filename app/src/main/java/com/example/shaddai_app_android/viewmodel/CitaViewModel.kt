package com.example.shaddai_app_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.model.CitaData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

    init {
        obtenerCitas()
    }

    private fun obtenerCitas() {
        val uid = auth.currentUser?.uid ?: return

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

    fun refrescarCitas() {
        listener?.let { dbRef.removeEventListener(it) }
        obtenerCitas()
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { dbRef.removeEventListener(it) }
    }
}