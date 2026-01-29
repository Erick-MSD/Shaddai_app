package com.example.shaddai_app.ui.technician_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TechnicianHomeScreen(
    viewModel: TechnicianHomeViewModel = TechnicianHomeViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val manrope = manropeFontFamily()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = TechnicianColors.Accent)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Sección de Servicio Actual ---
            item {
                SectionTitle("SERVICIO ACTUAL", manrope)
            }
            state.currentService?.let {
                item {
                    CurrentServiceCard(
                        service = it,
                        onGoNow = { viewModel.onGoNowClicked(it.id) },
                        onCall = { viewModel.onCallClicked(it.id) },
                        fontFamily = manrope
                    )
                }
            }

            // --- Sección de Siguientes Citas ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("SIGUIENTES CITAS", manrope)
            }
            items(state.upcomingServices) {
                UpcomingServiceItem(
                    service = it,
                    onClick = { viewModel.onUpcomingServiceClicked(it.id) },
                    fontFamily = manrope
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, fontFamily: FontFamily) {
    Text(
        text = title,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = TechnicianColors.TextSecondary,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun CurrentServiceCard(
    service: CurrentService,
    onGoNow: () -> Unit,
    onCall: () -> Unit,
    fontFamily: FontFamily
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TechnicianColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = service.serviceType.uppercase(),
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TechnicianColors.Accent
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = service.title,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TechnicianColors.TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = "Horario",
                    tint = TechnicianColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = service.time,
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    color = TechnicianColors.TextSecondary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = service.address,
                fontFamily = fontFamily,
                fontSize = 14.sp,
                color = TechnicianColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onGoNow,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TechnicianColors.Accent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Ir ahora", fontFamily = fontFamily, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onCall,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TechnicianColors.Accent),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(TechnicianColors.Accent)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Llamar", fontFamily = fontFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun UpcomingServiceItem(
    service: UpcomingService,
    onClick: () -> Unit,
    fontFamily: FontFamily
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = TechnicianColors.White),
        onClick = onClick
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = service.serviceType.toIcon(),
                contentDescription = service.serviceType,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TechnicianColors.Background)
                    .padding(8.dp),
                tint = TechnicianColors.Accent
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TechnicianColors.TextPrimary
                )
                Text(
                    text = "${service.serviceType} - ${service.address}",
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    color = TechnicianColors.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = service.time,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TechnicianColors.TextPrimary
            )
        }
    }
}

/**
 * Función de utilidad para mapear un tipo de servicio a un icono.
 * En una app real, esto podría ser más sofisticado.
 */
private fun String.toIcon(): ImageVector {
    return when (this) {
        "Aire Acondicionado" -> Icons.Filled.AcUnit
        "Refrigerador" -> Icons.Filled.Kitchen
        "Lavadora" -> Icons.Filled.LocalLaundryService
        else -> Icons.Filled.Build
    }
}
