package com.example.shaddai_app_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.CitaClima
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CitaViewModel: ViewModel() {
        private val dbRef = FirebaseDatabase.getInstance().getReference("citas_clima")

        private val _citas = MutableStateFlow<List<CitaClima>>(emptyList())
        val citas: StateFlow<List<CitaClima>> = _citas

        init {
            obtenerCitas()
        }

        private fun obtenerCitas() {
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lista = snapshot.children.mapNotNull { it.getValue(CitaClima::class.java)?.copy(id = it.key ?: "") }
                    _citas.value = lista
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        fun actualizarEstadoCita(citaId: String, completada: Boolean) {
            val nuevoEstado = if (completada) "completado" else "pendiente"
            dbRef.child(citaId).child("estado").setValue(nuevoEstado)
        }
}