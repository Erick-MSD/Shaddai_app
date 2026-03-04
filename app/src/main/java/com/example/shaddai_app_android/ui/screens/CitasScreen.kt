package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.components.CitaCard
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.CitaViewModel

@Composable
fun CitasScreen(
    citaViewModel: CitaViewModel,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val citas by citaViewModel.citas.collectAsState()
    val isLoading by citaViewModel.isLoading.collectAsState()

    val pendientes = citas.filter { it.estado != "completado" }
    val completadas = citas.filter { it.estado == "completado" }

    Scaffold(
        topBar = { ShaddaiTopBar(title = "Mis Citas", onNavigate = onNavigate, onLogout = onLogout) },
        bottomBar = {
            ShaddaiBottomBar(
                currentRoute = "citas",
                onNavigate = onNavigate
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else if (citas.isEmpty()) {
            // Estado vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarViewDay,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No tienes citas registradas",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Tus citas aparecerán aquí una vez\nque registres una nueva.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 14.sp,
                        color = TextHint,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                // Sección de citas pendientes
                if (pendientes.isNotEmpty()) {
                    item {
                        Text(
                            text = "Pendientes (${pendientes.size})",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    items(pendientes, key = { it.id }) { cita ->
                        CitaCard(
                            cita = cita,
                            onCancelar = { citaViewModel.cancelarCita(cita.id) },
                            onClick = { onNavigate("detalle_cita/${cita.id}") }
                        )
                    }
                }

                // Sección de citas completadas
                if (completadas.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Completadas (${completadas.size})",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    items(completadas, key = { it.id }) { cita ->
                        CitaCard(
                            cita = cita,
                            onClick = { onNavigate("detalle_cita/${cita.id}") }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}