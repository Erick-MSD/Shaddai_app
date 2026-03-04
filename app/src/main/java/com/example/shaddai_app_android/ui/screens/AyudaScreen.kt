package com.example.shaddai_app_android.ui.screens

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
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyudaScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val faqs = listOf(
        "¿Cómo agendo una cita?" to "Ve a la pantalla de inicio y presiona el botón '+' o el banner '¿Necesitas otro servicio?'. Sigue los 4 pasos: descripción, dirección, fecha/hora y confirmación.",
        "¿Puedo cancelar una cita?" to "Sí, ve a 'Mis Citas', selecciona la cita y presiona 'Cancelar cita'.",
        "¿Cómo cambio mi contraseña?" to "Ve a 'Mi Perfil', sección 'Seguridad', presiona 'Cambiar contraseña'.",
        "¿Puedo modificar mis datos?" to "Sí, en 'Mi Perfil' puedes editar nombre y teléfono. El correo no se puede modificar.",
        "¿Cómo uso mi ubicación actual?" to "En el paso de dirección, presiona 'Usar mi ubicación actual'. Necesitas permisos de ubicación activados."
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        bottomBar = {
            ShaddaiBottomBar(currentRoute = "ayuda", onNavigate = onNavigate)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Contacto rápido",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    HorizontalDivider(color = BorderColor)

                    AyudaContactoItem(
                        icon = Icons.Default.Phone,
                        label = "Llamar a soporte",
                        value = "+52 123 456 7890",
                        onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_DIAL, Uri.parse("tel:+521234567890"))
                            )
                        }
                    )
                    AyudaContactoItem(
                        icon = Icons.Default.Email,
                        label = "Enviar correo",
                        value = "soporte@shaddai.com",
                        onClick = {
                            val i = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:soporte@shaddai.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Ayuda con la app Shaddai")
                            }
                            context.startActivity(i)
                        }
                    )
                    AyudaContactoItem(
                        icon = Icons.Default.Chat,
                        label = "WhatsApp",
                        value = "+52 123 456 7890",
                        onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/521234567890"))
                            )
                        }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Preguntas frecuentes",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    HorizontalDivider(color = BorderColor)

                    faqs.forEachIndexed { index, (pregunta, respuesta) ->
                        val isExpanded = expandedFaq == index
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedFaq = if (isExpanded) -1 else index
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    pregunta,
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    if (isExpanded) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    respuesta,
                                    fontFamily = ManropeFontFamily,
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                        if (index < faqs.lastIndex) {
                            HorizontalDivider(color = BorderColor.copy(alpha = 0.4f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AyudaContactoItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Text(
                value,
                fontFamily = ManropeFontFamily,
                fontSize = 12.sp,
                color = PrimaryBlue
            )
        }
    }
}
