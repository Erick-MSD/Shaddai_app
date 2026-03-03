package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.viewmodel.CitaViewModel


@Composable
fun CitasScreen(viewModel: CitaViewModel = viewModel()) {
    val citas by viewModel.citas.collectAsState()

    LazyColumn {
        items(citas) { cita ->
            CitaCard(
                cita = cita,
                onStatusChange = { isChecked ->
                    viewModel.actualizarEstadoCita(cita.id, isChecked)
                }
            )
        }
    }
}