package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.CitaViewModel

data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val tipo: TipoNotificacion,
    val timestamp: Long
)

enum class TipoNotificacion {
    CITA_CREADA,
    CITA_CANCELADA,
    CITA_COMPLETADA,
    RECORDATORIO
}

@Composable
fun NotificacionesScreen(
    citaViewModel: CitaViewModel,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val citas by citaViewModel.citas.collectAsState()

    // Generar notificaciones a partir de las citas reales del usuario
    val notificaciones = remember(citas) {
        citas.mapNotNull { cita ->
            when (cita.estado) {
                "pendiente" -> Notificacion(
                    id = cita.id,
                    titulo = "Cita programada",
                    mensaje = "${cita.servicio} — ${cita.fecha} a las ${cita.hora} hrs",
                    tipo = TipoNotificacion.CITA_CREADA,
                    timestamp = cita.fechaCreacion
                )
                "completado" -> Notificacion(
                    id = cita.id,
                    titulo = "Servicio completado",
                    mensaje = "${cita.servicio} ha sido completado.",
                    tipo = TipoNotificacion.CITA_COMPLETADA,
                    timestamp = cita.fechaCreacion
                )
                "cancelado" -> Notificacion(
                    id = cita.id,
                    titulo = "Cita cancelada",
                    mensaje = "${cita.servicio} del ${cita.fecha} fue cancelada.",
                    tipo = TipoNotificacion.CITA_CANCELADA,
                    timestamp = cita.fechaCreacion
                )
                else -> null
            }
        }.sortedByDescending { it.timestamp }
    }

    Scaffold(
        topBar = {
            ShaddaiTopBar(
                title = "Notificaciones",
                onNavigate = onNavigate,
                onLogout = onLogout
            )
        },
        bottomBar = {
            ShaddaiBottomBar(currentRoute = "notificaciones", onNavigate = onNavigate)
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        if (notificaciones.isEmpty()) {
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
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Sin notificaciones",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Tus notificaciones aparecerán aquí\ncuando tengas actividad.",
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notificaciones, key = { it.id }) { notif ->
                    NotificacionItem(notif)
                }
            }
        }
    }
}

@Composable
private fun NotificacionItem(notificacion: Notificacion) {
    val (icon, iconColor) = when (notificacion.tipo) {
        TipoNotificacion.CITA_CREADA -> Icons.Default.CalendarToday to PrimaryBlue
        TipoNotificacion.CITA_COMPLETADA -> Icons.Default.CheckCircle to SuccessGreen
        TipoNotificacion.CITA_CANCELADA -> Icons.Default.Cancel to ErrorColor
        TipoNotificacion.RECORDATORIO -> Icons.Default.Notifications to Color(0xFFD69E2E)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notificacion.titulo,
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = notificacion.mensaje,
                    fontFamily = ManropeFontFamily,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}



