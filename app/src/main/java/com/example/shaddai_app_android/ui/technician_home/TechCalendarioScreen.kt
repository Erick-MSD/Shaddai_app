package com.example.shaddai_app_android.ui.technician_home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.ui.theme.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Pantalla de Calendario/Agenda del Técnico.
 * Muestra citas reales aceptadas por el técnico desde Firebase RTDB.
 */
@Composable
fun TechCalendarioScreen(
    viewModel: TechCalendarioViewModel,
    onCitaClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val fechaSel = state.fechaSeleccionada

    Column(modifier = modifier.fillMaxSize()) {
        // ── Selector de mes ──
        MesSelector(
            fecha = fechaSel,
            onRetroceder = { viewModel.retrocederDia() },
            onAvanzar = { viewModel.avanzarDia() }
        )

        Spacer(Modifier.height(8.dp))

        // ── Selector de semana ──
        SemanaSelector(
            fechaSeleccionada = fechaSel,
            fechasConCitas = viewModel.fechasConCitas(),
            onDiaSeleccionado = { viewModel.cambiarFecha(it) }
        )

        Spacer(Modifier.height(16.dp))

        // ── Contenido ──
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PrimaryBlue)
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando agenda...", fontFamily = ManropeFontFamily, fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }

            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Default.CloudOff, null, tint = TextHint, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(state.error ?: "", fontFamily = ManropeFontFamily, fontSize = 14.sp, color = TextSecondary, textAlign = TextAlign.Center)
                    }
                }
            }

            state.citasDelDia.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Default.EventBusy, null, tint = TextHint, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Sin citas para este día",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "No tienes servicios programados.",
                            fontFamily = ManropeFontFamily,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            else -> {
                // Header cantidad
                Text(
                    "${state.citasDelDia.size} cita(s) programada(s)",
                    fontFamily = ManropeFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.citasDelDia, key = { it.id }) { cita ->
                        CitaCalendarioCard(cita = cita, onClick = { onCitaClick(cita.id) })
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }
    }
}

// ── Selector de mes con flechas ──
@Composable
private fun MesSelector(fecha: LocalDate, onRetroceder: () -> Unit, onAvanzar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRetroceder) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Anterior", tint = TextPrimary)
        }
        Text(
            text = "${fecha.month.getDisplayName(TextStyle.FULL, Locale("es", "MX")).uppercase()} ${fecha.year}",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary
        )
        IconButton(onClick = onAvanzar) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Siguiente", tint = TextPrimary)
        }
    }
}

// ── Selector de semana (7 días) ──
@Composable
private fun SemanaSelector(
    fechaSeleccionada: LocalDate,
    fechasConCitas: Set<String>,
    onDiaSeleccionado: (LocalDate) -> Unit
) {
    val inicioSemana = fechaSeleccionada.minusDays(fechaSeleccionada.dayOfWeek.value.toLong() - 1) // Lunes
    val diasSemana = (0L..6L).map { inicioSemana.plusDays(it) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(diasSemana) { fecha ->
            val esSeleccionado = fecha == fechaSeleccionada
            val esHoy = fecha == LocalDate.now()
            val tieneCitas = fechasConCitas.any { it.contains(fecha.toString()) }

            DiaCalCard(
                diaSemana = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "MX")).uppercase().take(3),
                diaNumero = fecha.dayOfMonth.toString(),
                esSeleccionado = esSeleccionado,
                esHoy = esHoy,
                tieneCitas = tieneCitas,
                onClick = { onDiaSeleccionado(fecha) }
            )
        }
    }
}

@Composable
private fun DiaCalCard(
    diaSemana: String,
    diaNumero: String,
    esSeleccionado: Boolean,
    esHoy: Boolean,
    tieneCitas: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        esSeleccionado -> PrimaryBlue
        esHoy -> PrimaryBlue.copy(alpha = 0.08f)
        else -> Color.White
    }
    val textColor = if (esSeleccionado) Color.White else TextPrimary
    val borderMod = if (!esSeleccionado) Modifier.border(1.dp, BorderColor, RoundedCornerShape(12.dp)) else Modifier

    Column(
        modifier = Modifier
            .width(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .then(borderMod)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(diaSemana, fontFamily = ManropeFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = textColor.copy(alpha = 0.7f))
        Spacer(Modifier.height(4.dp))
        Text(diaNumero, fontFamily = ManropeFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
        if (tieneCitas && !esSeleccionado) {
            Spacer(Modifier.height(4.dp))
            Box(Modifier.size(6.dp).clip(CircleShape).background(PrimaryBlue))
        }
    }
}

// ── Card de una cita en el calendario ──
@Composable
private fun CitaCalendarioCard(cita: CitaClima, onClick: () -> Unit) {
    val estadoColor = when (cita.estado) {
        "completado" -> SuccessGreen
        "en_proceso" -> Color(0xFFD69E2E)
        "cancelado" -> ErrorColor
        else -> PrimaryBlue
    }
    val estadoTexto = when (cita.estado) {
        "completado" -> "Completado"
        "en_proceso" -> "En Proceso"
        "cancelado" -> "Cancelado"
        else -> "Pendiente"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            // Hora
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(55.dp)) {
                val parts = cita.hora.split(" ")
                Text(parts.getOrElse(0) { cita.hora }, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                if (parts.size > 1) {
                    Text(parts[1], fontFamily = ManropeFontFamily, fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(Modifier.width(10.dp))

            // Barra de color
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(55.dp)
                    .background(estadoColor, RoundedCornerShape(2.dp))
            )

            Spacer(Modifier.width(12.dp))

            // Info
            Column(Modifier.weight(1f)) {
                Text(
                    cita.descripcion.ifBlank { cita.servicio },
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    cita.servicio,
                    fontFamily = ManropeFontFamily,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = TextHint, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        cita.direccion,
                        fontFamily = ManropeFontFamily,
                        fontSize = 11.sp,
                        color = TextHint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // Badge estado
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(estadoColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(estadoTexto, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, color = estadoColor)
            }
        }
    }
}

