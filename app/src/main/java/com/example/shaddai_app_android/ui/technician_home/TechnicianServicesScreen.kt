package com.example.shaddai_app_android.ui.technician_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.ui.theme.ManropeFontFamily

/**
 * Pantalla de Servicios del Técnico.
 * Muestra las citas creadas por los clientes desde Firebase Realtime Database.
 */
@Composable
fun TechnicianServicesScreen(
    viewModel: TechnicianServicesViewModel,
    technicianName: String = "Técnico",
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val filteredServices = viewModel.getFilteredServices()
    val manrope = ManropeFontFamily

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Título de la sección
        Text(
            text = "Servicios de Clientes",
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = TechnicianColors.TextPrimary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "Citas registradas por los clientes",
            fontFamily = manrope,
            fontSize = 14.sp,
            color = TechnicianColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        FilterChipsRow(
            selectedFilter = state.selectedFilter,
            onFilterSelected = { viewModel.setFilter(it) },
            fontFamily = manrope
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = TechnicianColors.Accent)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Cargando servicios...",
                            fontFamily = manrope,
                            fontSize = 14.sp,
                            color = TechnicianColors.TextSecondary
                        )
                    }
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = TechnicianColors.TextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.error ?: "Error desconocido",
                            fontFamily = manrope,
                            fontSize = 16.sp,
                            color = TechnicianColors.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.refresh() },
                            colors = ButtonDefaults.buttonColors(containerColor = TechnicianColors.Accent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reintentar", fontFamily = manrope, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            filteredServices.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = null,
                            tint = TechnicianColors.TextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay servicios ${state.selectedFilter.label.lowercase()}",
                            fontFamily = manrope,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TechnicianColors.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Los servicios creados por clientes aparecerán aquí.",
                            fontFamily = manrope,
                            fontSize = 14.sp,
                            color = TechnicianColors.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                // Contador
                Text(
                    text = "${filteredServices.size} servicio(s) encontrado(s)",
                    fontFamily = manrope,
                    fontSize = 12.sp,
                    color = TechnicianColors.TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredServices, key = { it.id }) { cita ->
                        TechnicianServiceCard(
                            cita = cita,
                            technicianName = technicianName,
                            onAssign = { viewModel.assignToService(cita.id, technicianName) },
                            onComplete = { viewModel.updateServiceStatus(cita.id, "completado") },
                            fontFamily = manrope
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: ServiceFilter,
    onFilterSelected: (ServiceFilter) -> Unit,
    fontFamily: androidx.compose.ui.text.font.FontFamily
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(ServiceFilter.entries.toList()) { filter ->
            val isSelected = selectedFilter == filter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter.label,
                        fontFamily = fontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = TechnicianColors.Accent,
                    selectedLabelColor = Color.White,
                    containerColor = TechnicianColors.White,
                    labelColor = TechnicianColors.TextSecondary
                ),
                shape = RoundedCornerShape(20.dp),
                border = if (!isSelected) FilterChipDefaults.filterChipBorder(
                    borderColor = Color(0xFFE2E8F0),
                    enabled = true,
                    selected = false
                ) else null
            )
        }
    }
}

@Composable
private fun TechnicianServiceCard(
    cita: CitaClima,
    technicianName: String,
    onAssign: () -> Unit,
    onComplete: () -> Unit,
    fontFamily: androidx.compose.ui.text.font.FontFamily
) {
    val estadoColor = when (cita.estado) {
        "completado" -> Color(0xFF38A169)
        "en_proceso" -> Color(0xFFD69E2E)
        "cancelado" -> Color(0xFFE53E3E)
        else -> TechnicianColors.Accent
    }

    val estadoTexto = when (cita.estado) {
        "completado" -> "Completado"
        "en_proceso" -> "En Proceso"
        "cancelado" -> "Cancelado"
        else -> "Pendiente"
    }

    val serviceIcon = getServiceIcon(cita.servicio)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TechnicianColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: tipo de servicio + badge estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = serviceIcon,
                        contentDescription = null,
                        tint = TechnicianColors.Accent,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechnicianColors.Accent.copy(alpha = 0.1f))
                            .padding(6.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = cita.servicio.ifBlank { "Servicio" },
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TechnicianColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (cita.cliente.isNotBlank()) {
                            Text(
                                text = cita.cliente,
                                fontFamily = fontFamily,
                                fontSize = 12.sp,
                                color = TechnicianColors.TextSecondary
                            )
                        }
                    }
                }

                // Badge estado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(estadoColor.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = estadoTexto,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = estadoColor
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            // Descripción
            if (cita.descripcion.isNotBlank()) {
                ServiceInfoRow(
                    icon = Icons.Default.Description,
                    text = cita.descripcion,
                    fontFamily = fontFamily
                )
            }

            // Dirección
            if (cita.direccion.isNotBlank()) {
                ServiceInfoRow(
                    icon = Icons.Default.LocationOn,
                    text = cita.direccion,
                    fontFamily = fontFamily
                )
            }

            // Fecha y hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (cita.fecha.isNotBlank()) {
                    ServiceInfoRow(
                        icon = Icons.Default.CalendarToday,
                        text = cita.fecha,
                        fontFamily = fontFamily,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (cita.hora.isNotBlank()) {
                    ServiceInfoRow(
                        icon = Icons.Default.AccessTime,
                        text = "${cita.hora} hrs",
                        fontFamily = fontFamily,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Técnico asignado
            if (cita.tecnico_asignado.isNotBlank()) {
                ServiceInfoRow(
                    icon = Icons.Default.Person,
                    text = "Asignado a: ${cita.tecnico_asignado}",
                    fontFamily = fontFamily
                )
            }

            // Botones de acción
            if (cita.estado != "completado" && cita.estado != "cancelado") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (cita.tecnico_asignado.isBlank() || cita.estado == "pendiente") {
                        // Botón tomar servicio
                        Button(
                            onClick = onAssign,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TechnicianColors.Accent),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Tomar Servicio",
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    if (cita.estado == "en_proceso") {
                        // Botón completar
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38A169)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Completar",
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceInfoRow(
    icon: ImageVector,
    text: String,
    fontFamily: androidx.compose.ui.text.font.FontFamily,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TechnicianColors.Accent,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontFamily = fontFamily,
            fontSize = 13.sp,
            color = TechnicianColors.TextSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Mapea un tipo de servicio a un ícono.
 */
private fun getServiceIcon(servicio: String): ImageVector {
    val lower = servicio.lowercase()
    return when {
        lower.contains("aire") || lower.contains("clima") || lower.contains("minisplit") -> Icons.Default.AcUnit
        lower.contains("electric") -> Icons.Default.ElectricalServices
        lower.contains("plomer") || lower.contains("agua") -> Icons.Default.Plumbing
        lower.contains("refrig") -> Icons.Default.Kitchen
        lower.contains("lavad") -> Icons.Default.LocalLaundryService
        lower.contains("pintura") -> Icons.Default.FormatPaint
        lower.contains("cerraj") -> Icons.Default.Key
        lower.contains("limpieza") -> Icons.Default.CleaningServices
        lower.contains("jardin") -> Icons.Default.Yard
        else -> Icons.Default.Build
    }
}



