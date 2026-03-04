package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.AuthViewModel
import com.example.shaddai_app_android.viewmodel.CitaViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    citaViewModel: CitaViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit = {}
) {
    val citas by citaViewModel.citas.collectAsState()
    val userName by authViewModel.userName.collectAsState()
    val proximaCita = citas.firstOrNull { it.estado != "completado" }
    val historial = citas.filter { it.estado == "completado" }

    Scaffold(
        topBar = {
            ShaddaiTopBar(
                title = "Hola, $userName",
                onNavigate = onNavigate,
                onLogout = {
                    authViewModel.logout()
                    onLogout()
                }
            )
        },
        bottomBar = {
            ShaddaiBottomBar(currentRoute = "home", onNavigate = onNavigate)
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Línea divisoria sutil debajo del TopBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFB0D4F1))
            )

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Proxima Cita",
                    color = TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = ManropeFontFamily,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Card de Próxima Cita
                if (proximaCita != null) {
                    NextAppointmentCard(
                        cita = proximaCita,
                        onDetallesClick = { onNavigate("detalle_cita/${proximaCita.id}") }
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No tienes citas programadas",
                                color = TextSecondary,
                                fontSize = 15.sp,
                                fontFamily = ManropeFontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Banner "¿Necesitas otro servicio?"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate("nueva_cita") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF60A5FA)
                                    )
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "¿Necesitas otro servicio?",
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = ManropeFontFamily
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Agenda una visita hoy mismo",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = ManropeFontFamily
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Agregar",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Historial reciente",
                    color = TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = ManropeFontFamily,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Lista de historial
                historial.forEach { cita ->
                    HistoryItem(
                        cita = cita,
                        onClick = { onNavigate("detalle_cita/${cita.id}") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (historial.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Sin historial aún",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                fontFamily = ManropeFontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun NextAppointmentCard(cita: CitaClima, onDetallesClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x1A000000)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenido de texto — ocupa el espacio disponible
            Column(modifier = Modifier.weight(1f)) {
                // Badge del tipo de servicio
                Surface(
                    color = Color(0xFF4FC3F7),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = cita.servicio,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ManropeFontFamily,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                    )
                }

                // Nombre del equipo / descripción
                Text(
                    text = cita.equipo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    fontFamily = ManropeFontFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Fecha
                Text(
                    text = cita.fecha,
                    fontSize = 15.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal,
                    fontFamily = ManropeFontFamily
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Botón Detalles
                OutlinedButton(
                    onClick = onDetallesClick,
                    modifier = Modifier
                        .width(140.dp)
                        .height(38.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = borderStroke(1.5.dp, Color(0xFF2D3436)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "Detalles",
                        color = Color(0xFF2D3436),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = ManropeFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Icono decorativo — tamaño fijo, no tapa el texto
            Icon(
                imageVector = Icons.Outlined.Air,
                contentDescription = null,
                tint = Color(0xFF4FC3F7),
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

@Composable
fun HistoryItem(cita: CitaClima, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de check verde
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completado",
                tint = Color(0xFF00C853),
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${cita.servicio} ${cita.equipo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    fontFamily = ManropeFontFamily,
                    modifier = Modifier.padding(bottom = 2.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${cita.fecha}  •  Completado",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal,
                    fontFamily = ManropeFontFamily
                )
                if (cita.calificacion > 0) {
                    // Mostrar estrellas pequeñas
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        (1..5).forEach { i ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i <= cita.calificacion) Color(0xFFFFB300) else Color(0xFFE0E0E0),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                } else {
                    // Indicador de pendiente de calificar
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Calificar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFB300),
                            fontFamily = ManropeFontFamily
                        )
                    }
                }
            }
        }
    }
}

// Función auxiliar para bordes
@Composable
fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
