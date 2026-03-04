package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.CitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    citaId: String,
    citaViewModel: CitaViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val citas by citaViewModel.citas.collectAsState()
    val cita = citas.find { it.id == citaId }

    val estadoColor = when (cita?.estado) {
        "completado" -> SuccessGreen
        "en_proceso" -> Color(0xFFD69E2E)
        "cancelado" -> ErrorColor
        else -> PrimaryBlue
    }

    val estadoTexto = when (cita?.estado) {
        "completado" -> "Completado"
        "en_proceso" -> "En proceso"
        "cancelado" -> "Cancelado"
        else -> "Pendiente"
    }

    val estadoIcon = when (cita?.estado) {
        "completado" -> Icons.Default.CheckCircle
        "en_proceso" -> Icons.Default.Autorenew
        "cancelado" -> Icons.Default.Cancel
        else -> Icons.Default.Schedule
    }

    val fechaFormateada = try {
        if (cita != null && cita.fecha.isNotBlank()) {
            val ld = LocalDate.parse(cita.fecha)
            val fmt = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"))
            ld.format(fmt).replaceFirstChar { it.uppercase() }
        } else ""
    } catch (_: Exception) {
        cita?.fecha ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de Cita",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            ShaddaiBottomBar(currentRoute = "detalle_cita", onNavigate = onNavigate)
        },
        containerColor = BackgroundColor
    ) { paddingValues ->

        if (cita == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Cita no encontrada",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = TextSecondary
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // === Estado Badge ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = estadoColor.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(estadoColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = estadoIcon,
                            contentDescription = null,
                            tint = estadoColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Estado del servicio",
                            fontFamily = ManropeFontFamily,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = estadoTexto,
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = estadoColor
                        )
                    }
                }
            }

            // === Información del Servicio ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del Servicio",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    HorizontalDivider(color = BorderColor)

                    // Tipo de servicio
                    DetalleItem(
                        icon = Icons.Default.Build,
                        label = "Tipo de servicio",
                        value = cita.servicio.ifBlank { "—" }
                    )

                    // Descripción
                    DetalleItem(
                        icon = Icons.AutoMirrored.Filled.Notes,
                        label = "Descripción",
                        value = cita.descripcion.ifBlank { cita.equipo.ifBlank { "—" } }
                    )

                    // Notas / Referencias
                    if (cita.notas.isNotBlank()) {
                        DetalleItem(
                            icon = Icons.AutoMirrored.Filled.Notes,
                            label = "Notas / Referencias",
                            value = cita.notas
                        )
                    }

                    // Técnico asignado
                    DetalleItem(
                        icon = Icons.Default.Person,
                        label = "Técnico asignado",
                        value = cita.tecnico_asignado.ifBlank { "Por asignar" }
                    )
                }
            }

            // === Ubicación ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Ubicación",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    HorizontalDivider(color = BorderColor)

                    // Dirección completa
                    val direccionDetallada = buildString {
                        if (cita.calle.isNotBlank()) append("${cita.calle} ${cita.numeroExterior}")
                        if (cita.colonia.isNotBlank()) {
                            if (isNotBlank()) append(", ")
                            append(cita.colonia)
                        }
                        if (cita.codigoPostal.isNotBlank()) {
                            if (isNotBlank()) append(", ")
                            append("CP ${cita.codigoPostal}")
                        }
                    }.ifBlank { cita.direccion }

                    DetalleItem(
                        icon = Icons.Default.LocationOn,
                        label = "Dirección",
                        value = direccionDetallada.ifBlank { "—" }
                    )

                    if (cita.referencias.isNotBlank()) {
                        DetalleItem(
                            icon = Icons.Default.NearMe,
                            label = "Referencias",
                            value = cita.referencias
                        )
                    }
                }
            }

            // === Fecha y Hora ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Fecha y Hora",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )

                    HorizontalDivider(color = BorderColor)

                    DetalleItem(
                        icon = Icons.Default.CalendarToday,
                        label = "Fecha",
                        value = fechaFormateada.ifBlank { cita.fecha.ifBlank { "—" } }
                    )

                    DetalleItem(
                        icon = Icons.Default.AccessTime,
                        label = "Hora",
                        value = if (cita.hora.isNotBlank()) "${cita.hora} hrs" else "—"
                    )
                }
            }

            // === Botón de cancelar cita (solo si está pendiente o en proceso) ===
            if (cita.estado == "pendiente" || cita.estado == "en_proceso") {
                Button(
                    onClick = {
                        citaViewModel.cancelarCita(cita.id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancelar cita",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DetalleItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryBlue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontFamily = ManropeFontFamily,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}





