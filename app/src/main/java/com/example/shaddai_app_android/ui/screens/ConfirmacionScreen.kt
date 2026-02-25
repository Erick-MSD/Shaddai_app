package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.components.StepProgressBar
import com.example.shaddai_app_android.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ConfirmacionScreen(
    citaData: CitaData,
    onVolverAlInicio: () -> Unit
) {
    val fechaFormateada = try {
        val ld = LocalDate.parse(citaData.fecha)
        val fmt = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"))
        ld.format(fmt).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        citaData.fecha
    }

    Scaffold(
        topBar = { ShaddaiTopBar() },
        bottomBar = { ShaddaiBottomBar() },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepProgressBar(currentStep = 4)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Ícono de check
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(SuccessGreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(52.dp)
                    )
                }

                // Título confirmación
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "¡Solicitud Recibida!",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Se te asignará un técnico para tu cita en breve.\nPuedes ver el estado en tu pantalla de inicio.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }

                // Resumen de la cita
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Resumen de tu cita",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )

                        HorizontalDivider(color = BorderColor)

                        // Descripción
                        ResumenItem(
                            icon = Icons.AutoMirrored.Filled.Notes,
                            titulo = "Descripción",
                            valor = citaData.descripcion.ifBlank { "—" }
                        )

                        // Servicios
                        ResumenItem(
                            icon = Icons.Default.Build,
                            titulo = "Tipo de servicio",
                            valor = citaData.tiposServicio.joinToString(", ").ifBlank { "—" }
                        )

                        // Dirección
                        val direccionCompleta = buildString {
                            if (citaData.calle.isNotBlank()) append("${citaData.calle} ${citaData.numeroExterior}, ")
                            if (citaData.colonia.isNotBlank()) append("${citaData.colonia}, ")
                            if (citaData.codigoPostal.isNotBlank()) append("CP ${citaData.codigoPostal}")
                            if (citaData.referencias.isNotBlank()) append("\n${citaData.referencias}")
                        }.trimEnd(',', ' ')

                        ResumenItem(
                            icon = Icons.Default.LocationOn,
                            titulo = "Dirección",
                            valor = direccionCompleta.ifBlank { "—" }
                        )

                        // Fecha
                        ResumenItem(
                            icon = Icons.Default.CalendarToday,
                            titulo = "Fecha",
                            valor = fechaFormateada.ifBlank { "—" }
                        )

                        // Hora
                        ResumenItem(
                            icon = Icons.Default.AccessTime,
                            titulo = "Hora",
                            valor = if (citaData.hora.isNotBlank()) "${citaData.hora} hrs" else "—"
                        )
                    }
                }

                // Botón volver al inicio
                Button(
                    onClick = onVolverAlInicio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "Volver al inicio",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ResumenItem(
    icon: ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = TextSecondary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valor,
                fontFamily = ManropeFontFamily,
                fontSize = 13.sp,
                color = TextPrimary,
                lineHeight = 18.sp
            )
        }
    }
}




