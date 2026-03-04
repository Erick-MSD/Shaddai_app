package com.example.shaddai_app_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.ui.theme.*

@Composable
fun CitaCard(
    cita: CitaClima,
    onCancelar: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val isCancelled = cita.estado == "cancelado"
    val isCompleted = cita.estado == "completado"

    val estadoColor = when (cita.estado) {
        "completado" -> SuccessGreen
        "en_proceso" -> Color(0xFFD69E2E)
        "cancelado" -> ErrorColor
        else -> PrimaryBlue
    }

    val estadoTexto = when (cita.estado) {
        "completado" -> "Completado"
        "en_proceso" -> "En proceso"
        "cancelado" -> "Cancelado"
        else -> "Pendiente"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Servicio + Badge de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cita.servicio.ifBlank { "Servicio" },
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Badge de estado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(estadoColor.copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = estadoTexto,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = estadoColor
                    )
                }
            }

            HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))

            // Descripción
            if (cita.descripcion.isNotBlank()) {
                CitaInfoRow(
                    icon = Icons.Default.Description,
                    text = cita.descripcion
                )
            }

            // Dirección
            if (cita.direccion.isNotBlank()) {
                CitaInfoRow(
                    icon = Icons.Default.LocationOn,
                    text = cita.direccion
                )
            }

            // Fecha y Hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (cita.fecha.isNotBlank()) {
                    CitaInfoRow(
                        icon = Icons.Default.CalendarToday,
                        text = cita.fecha,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (cita.hora.isNotBlank()) {
                    CitaInfoRow(
                        icon = Icons.Default.AccessTime,
                        text = "${cita.hora} hrs",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Botón cancelar cita (solo si no está completada ni cancelada)
            if (!isCompleted && !isCancelled) {
                Button(
                    onClick = onCancelar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorColor
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cancelar cita",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun CitaInfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontFamily = ManropeFontFamily,
            fontSize = 13.sp,
            color = TextSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}