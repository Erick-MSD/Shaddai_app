package com.example.shaddai_app.ui.service_report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shaddai_app.ui.theme.ShaddaiColors

/**
 * Pantalla principal para la creación de un reporte de servicio.
 */
@Composable
fun ServiceReportScreen(viewModel: ServiceReportViewModel) {
    val state by viewModel.uiState.collectAsState()

    if (state.isSignaturePadVisible) {
        SignaturePadDialog(
            onDismiss = { viewModel.onEvent(ServiceReportEvent.OnSignaturePadDismiss) },
            onSave = { viewModel.onEvent(ServiceReportEvent.OnSignatureSave) }
        )
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            EvidenceSection(state, viewModel::onEvent)
            Spacer(modifier = Modifier.height(24.dp))
            TechnicalReportSection(state, viewModel::onEvent)
            Spacer(modifier = Modifier.height(24.dp))
            SignatureSection(state, viewModel::onEvent)
        }
    }
}

@Composable
private fun EvidenceSection(state: ServiceReportState, onEvent: (ServiceReportEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            icon = Icons.Default.CameraAlt,
            title = "EVIDENCIA DEL TRABAJO",
            trailingText = "${state.attachedPhotos.size} Foto(s) adjunta(s)"
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(state.attachedPhotos) { photoUri ->
                // Pasamos el evento de borrado a la tarjeta de la foto
                AttachedPhotoCard(photoUri, onDelete = { onEvent(ServiceReportEvent.OnDeletePhoto(photoUri)) })
            }
            item {
                AddPhotoButton(onClick = { onEvent(ServiceReportEvent.OnAddPhotoClick) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TechnicalReportSection(state: ServiceReportState, onEvent: (ServiceReportEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(icon = Icons.Default.InsertDriveFile, title = "REPORTE TECNICO")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.observations,
            onValueChange = { onEvent(ServiceReportEvent.OnObservationsChange(it)) },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            placeholder = { Text("Describe el trabajo realizado, materiales usados y recomendaciones ...") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ShaddaiColors.White,
                unfocusedContainerColor = ShaddaiColors.White,
                focusedBorderColor = ShaddaiColors.AccentBlue,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun SignatureSection(state: ServiceReportState, onEvent: (ServiceReportEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("FIRMA DE CONFORMIDAD", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ShaddaiColors.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).clickable { onEvent(ServiceReportEvent.OnSignatureClick) },
                contentAlignment = Alignment.Center
            ) {
                if (state.hasSignature) {
                    Text("Firma capturada con éxito", fontWeight = FontWeight.Bold, color = ShaddaiColors.AccentBlue)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = ShaddaiColors.AccentBlue)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Tocar para firmar", color = ShaddaiColors.AccentBlue)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignaturePadDialog(onDismiss: () -> Unit, onSave: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Firmar Documento") },
                    navigationIcon = { IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, "Cerrar") } },
                    actions = { TextButton(onClick = onSave) { Text("Guardar") } }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(it).background(Color.White), contentAlignment = Alignment.Center) {
                Text("Dibuja tu firma aquí", color = Color.LightGray, fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String, trailingText: String? = null) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = title, tint = Color.Gray)
        Spacer(modifier = Modifier.size(8.dp))
        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.weight(1f))
        trailingText?.let { Text(it, color = ShaddaiColors.AccentBlue, fontWeight = FontWeight.SemiBold) }
    }
}

/**
 * Tarjeta para mostrar una foto adjuntada con un botón para eliminarla.
 */
@Composable
private fun AttachedPhotoCard(photoUri: String, onDelete: () -> Unit) {
    Box {
        Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(2.dp)) {
            Box(modifier = Modifier.size(140.dp, 200.dp).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Text(photoUri, fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }
        }
        // Botón de eliminar superpuesto
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape).size(24.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Eliminar foto", tint = Color.White)
        }
    }
}

@Composable
private fun AddPhotoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(140.dp, 200.dp).clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(2.dp, Color.LightGray), RoundedCornerShape(12.dp)).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CameraAlt, "Subir Foto", modifier = Modifier.size(48.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Subir Foto", color = Color.Gray)
        }
    }
}