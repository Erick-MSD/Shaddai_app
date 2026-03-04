package com.example.shaddai_app_android.ui.technician_home

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaClima
import com.example.shaddai_app_android.ui.theme.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Pantalla de detalle de una cita/servicio para el técnico.
 * Muestra toda la información de la cita y acciones como Ir (mapa) y Llamar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianServiceDetailScreen(
    citaId: String,
    technicianName: String = "Técnico",
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var cita by remember { mutableStateOf<CitaClima?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Cargar la cita desde Firebase
    LaunchedEffect(citaId) {
        val dbRef = FirebaseDatabase.getInstance().getReference("citas_clima").child(citaId)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cita = snapshot.getValue(CitaClima::class.java)?.copy(id = snapshot.key ?: "")
                isLoading = false
            }
            override fun onCancelled(error: DatabaseError) {
                isLoading = false
            }
        })
    }

    val estadoColor = when (cita?.estado) {
        "completado" -> SuccessGreen
        "en_proceso" -> Color(0xFFD69E2E)
        "cancelado" -> ErrorColor
        else -> PrimaryBlue
    }
    val estadoTexto = when (cita?.estado) {
        "completado" -> "Completado"
        "en_proceso" -> "En Proceso"
        "cancelado" -> "Cancelado"
        else -> "Pendiente"
    }
    val estadoIcon = when (cita?.estado) {
        "completado" -> Icons.Default.CheckCircle
        "en_proceso" -> Icons.Default.Autorenew
        "cancelado" -> Icons.Default.Cancel
        else -> Icons.Default.Schedule
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Servicio",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            TechnicianBottomBar(currentRoute = "tech_detalle", onNavigate = onNavigate)
        },
        containerColor = BackgroundColor
    ) { paddingValues ->

        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
            return@Scaffold
        }

        if (cita == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ErrorOutline, null, tint = TextHint, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Servicio no encontrado", fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = TextSecondary)
                }
            }
            return@Scaffold
        }

        val c = cita!!

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
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(estadoColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(estadoIcon, null, tint = estadoColor, modifier = Modifier.size(28.dp))
                    }
                    Column {
                        Text("Estado del servicio", fontFamily = ManropeFontFamily, fontSize = 12.sp, color = TextSecondary)
                        Text(estadoTexto, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = estadoColor)
                    }
                }
            }

            // === Información del Servicio ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Información del Servicio", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
                    HorizontalDivider(color = BorderColor)
                    DetailItem(Icons.Default.Build, "Tipo de servicio", c.servicio.ifBlank { "—" })
                    DetailItem(Icons.AutoMirrored.Filled.Notes, "Descripción", c.descripcion.ifBlank { c.equipo.ifBlank { "—" } })
                    if (c.notas.isNotBlank()) DetailItem(Icons.AutoMirrored.Filled.Notes, "Notas", c.notas)
                    DetailItem(Icons.Default.Person, "Cliente", c.cliente.ifBlank { "—" })
                    DetailItem(Icons.Default.Person, "Técnico asignado", c.tecnico_asignado.ifBlank { "Por asignar" })
                }
            }

            // === Ubicación ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Ubicación", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
                    HorizontalDivider(color = BorderColor)

                    val direccionDetallada = buildString {
                        if (c.calle.isNotBlank()) append("${c.calle} ${c.numeroExterior}")
                        if (c.colonia.isNotBlank()) { if (isNotBlank()) append(", "); append(c.colonia) }
                        if (c.codigoPostal.isNotBlank()) { if (isNotBlank()) append(", "); append("CP ${c.codigoPostal}") }
                    }.ifBlank { c.direccion }

                    DetailItem(Icons.Default.LocationOn, "Dirección", direccionDetallada.ifBlank { "—" })
                    if (c.referencias.isNotBlank()) DetailItem(Icons.Default.NearMe, "Referencias", c.referencias)
                }
            }

            // === Fecha y Hora ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Fecha y Hora", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
                    HorizontalDivider(color = BorderColor)
                    DetailItem(Icons.Default.CalendarToday, "Fecha", c.fecha.ifBlank { "—" })
                    DetailItem(Icons.Default.AccessTime, "Hora", if (c.hora.isNotBlank()) "${c.hora} hrs" else "—")
                }
            }

            // === Calificación del cliente ===
            if (c.estado == "completado" && c.calificacion > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Calificación del cliente",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            (1..5).forEach { i ->
                                Icon(
                                    imageVector = if (i <= c.calificacion) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = if (i <= c.calificacion) Color(0xFFFFB300) else TextHint,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Text(
                            text = when (c.calificacion) {
                                1 -> "Malo"; 2 -> "Regular"; 3 -> "Bueno"
                                4 -> "Muy bueno"; 5 -> "Excelente"; else -> ""
                            },
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = when (c.calificacion) {
                                1 -> ErrorColor; 2, 3 -> Color(0xFFD69E2E)
                                4, 5 -> SuccessGreen; else -> TextSecondary
                            }
                        )
                        if (c.comentarioCalificacion.isNotBlank()) {
                            Text(
                                "\"${c.comentarioCalificacion}\"",
                                fontFamily = ManropeFontFamily,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // === Botones de acción ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ir al lugar (Google Maps)
                Button(
                    onClick = {
                        val encoded = Uri.encode(c.direccion)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$encoded"))
                        intent.setPackage("com.google.android.apps.maps")
                        try { context.startActivity(intent) } catch (_: Exception) {
                            // Si no tiene Maps, abrir en navegador
                            val web = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=$encoded"))
                            context.startActivity(web)
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Navigation, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Ir al lugar", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                // Llamar al cliente
                OutlinedButton(
                    onClick = {
                        if (c.cliente.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${c.cliente}"))
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Call, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Llamar", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(PrimaryBlue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(label, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = TextSecondary, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(2.dp))
            Text(value, fontFamily = ManropeFontFamily, fontSize = 14.sp, color = TextPrimary, lineHeight = 20.sp)
        }
    }
}

