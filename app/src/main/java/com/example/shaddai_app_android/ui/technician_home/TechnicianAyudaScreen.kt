package com.example.shaddai_app_android.ui.technician_home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianAyudaScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val faqs = listOf(
        "¿Cómo acepto un servicio?" to "Ve a la pestaña 'Servicios', busca la cita pendiente y presiona 'Tomar Servicio'. La cita pasará a estado 'En Proceso'.",
        "¿Cómo marco un servicio como completado?" to "Cuando termines el trabajo, ve a la cita y presiona 'Completar'. El cliente será notificado.",
        "¿Cómo veo mi agenda del día?" to "Ve a la pestaña 'Agenda' (ícono de calendario) para ver tus citas organizadas por fecha.",
        "¿Cómo llego a la dirección del cliente?" to "En el detalle de la cita, presiona 'Ir al lugar' para abrir Google Maps con la dirección.",
        "¿Cómo cambio mi contraseña?" to "Ve a 'Mi Perfil' > sección 'Seguridad' > 'Cambiar contraseña'.",
        "¿Cómo actualizo mis datos profesionales?" to "En 'Mi Perfil' puedes editar tu nombre, teléfono, especialidad y número de licencia."
    )
    var expandedFaq by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Ayuda y Soporte",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = {
            TechnicianBottomBar(currentRoute = "tech_ayuda", onNavigate = onNavigate)
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // === Contacto rápido ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Contacto rápido", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
                    HorizontalDivider(color = BorderColor)

                    TechAyudaContactoItem(
                        icon = Icons.Default.Phone,
                        label = "Llamar a soporte",
                        value = "+52 123 456 7890",
                        onClick = { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+521234567890"))) }
                    )
                    TechAyudaContactoItem(
                        icon = Icons.Default.Email,
                        label = "Enviar correo",
                        value = "soporte@shaddai.com",
                        onClick = {
                            val i = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:soporte@shaddai.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Ayuda - Técnico Shaddai")
                            }
                            context.startActivity(i)
                        }
                    )
                    TechAyudaContactoItem(
                        icon = Icons.Default.Chat,
                        label = "WhatsApp",
                        value = "+52 123 456 7890",
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/521234567890"))) }
                    )
                }
            }

            // === Preguntas frecuentes ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Preguntas frecuentes", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                    HorizontalDivider(color = BorderColor)

                    faqs.forEachIndexed { index, (pregunta, respuesta) ->
                        val isExpanded = expandedFaq == index
                        Column(
                            modifier = Modifier.fillMaxWidth().clickable { expandedFaq = if (isExpanded) -1 else index }.padding(vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(pregunta, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                                Icon(if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                            }
                            if (isExpanded) {
                                Spacer(Modifier.height(8.dp))
                                Text(respuesta, fontFamily = ManropeFontFamily, fontSize = 13.sp, color = TextSecondary, lineHeight = 19.sp)
                            }
                        }
                        if (index < faqs.lastIndex) {
                            HorizontalDivider(color = BorderColor.copy(alpha = 0.4f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TechAyudaContactoItem(icon: ImageVector, label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(22.dp))
        Column(Modifier.weight(1f)) {
            Text(label, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
            Text(value, fontFamily = ManropeFontFamily, fontSize = 12.sp, color = PrimaryBlue)
        }
    }
}

