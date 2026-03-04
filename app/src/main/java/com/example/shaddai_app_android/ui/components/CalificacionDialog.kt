package com.example.shaddai_app_android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*

@Composable
fun CalificacionDialog(
    technicianName: String,
    onCalificar: (estrellas: Int, comentario: String) -> Unit,
    onDismiss: () -> Unit
) {
    var estrellas by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    val mensajeEstrellas = when (estrellas) {
        1 -> "Malo"
        2 -> "Regular"
        3 -> "Bueno"
        4 -> "Muy bueno"
        5 -> "Excelente"
        else -> ""
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⭐ Califica el servicio",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                if (technicianName.isNotBlank() && technicianName != "Por asignar") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Técnico: $technicianName",
                        fontFamily = ManropeFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¿Cómo fue tu experiencia?",
                    fontFamily = ManropeFontFamily,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                // Estrellas
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    (1..5).forEach { index ->
                        Icon(
                            imageVector = if (index <= estrellas) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "$index estrellas",
                            tint = if (index <= estrellas) Color(0xFFFFB300) else TextHint,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { estrellas = index }
                                .padding(2.dp)
                        )
                    }
                }

                // Texto descriptivo de la calificación
                if (mensajeEstrellas.isNotBlank()) {
                    Text(
                        text = mensajeEstrellas,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = when (estrellas) {
                            1 -> ErrorColor
                            2 -> Color(0xFFD69E2E)
                            3 -> Color(0xFFD69E2E)
                            4 -> SuccessGreen
                            5 -> SuccessGreen
                            else -> TextSecondary
                        }
                    )
                }

                // Comentario opcional
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = {
                        Text(
                            "Deja un comentario (opcional)",
                            fontFamily = ManropeFontFamily,
                            fontSize = 13.sp,
                            color = TextHint
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite
                    ),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (estrellas > 0) {
                        onCalificar(estrellas, comentario)
                    }
                },
                enabled = estrellas > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = PrimaryBlue.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Enviar calificación",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Ahora no",
                    color = TextSecondary,
                    fontFamily = ManropeFontFamily
                )
            }
        }
    )
}

/**
 * Muestra estrellas de calificación (solo lectura).
 */
@Composable
fun EstrellasMostrar(
    calificacion: Int,
    modifier: Modifier = Modifier,
    size: Int = 20
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= calificacion) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (index <= calificacion) Color(0xFFFFB300) else TextHint,
                modifier = Modifier.size(size.dp)
            )
        }
    }
}

