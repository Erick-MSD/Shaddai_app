package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.model.tiposDeServicio
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.components.StepProgressBar
import com.example.shaddai_app_android.ui.theme.*

@Composable
fun DescripcionScreen(
    citaData: CitaData,
    onContinuar: (CitaData) -> Unit
) {
    var descripcion by remember { mutableStateOf(citaData.descripcion) }
    var tiposSeleccionados by remember { mutableStateOf(citaData.tiposServicio.toMutableSet()) }
    var intentoContinuar by remember { mutableStateOf(false) }

    val descripcionError = intentoContinuar && descripcion.isBlank()
    val tiposError = intentoContinuar && tiposSeleccionados.isEmpty()

    Scaffold(
        topBar = { ShaddaiTopBar() },
        bottomBar = { ShaddaiBottomBar() },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            StepProgressBar(currentStep = 1)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título de sección
                Text(
                    text = "Describe tu solicitud",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )

                // Campo descripción
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Describe lo que ocupas",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            Text(
                                text = "Ej. Tengo una fuga de agua en el baño, el grifo gotea constantemente...",
                                fontFamily = ManropeFontFamily,
                                fontSize = 13.sp,
                                color = TextHint
                            )
                        },
                        isError = descripcionError,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = if (descripcionError) ErrorColor else BorderColor,
                            errorBorderColor = ErrorColor,
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            errorContainerColor = SurfaceWhite
                        ),
                        maxLines = 5
                    )
                    if (descripcionError) {
                        Text(
                            text = "Por favor describe lo que necesitas",
                            fontFamily = ManropeFontFamily,
                            fontSize = 12.sp,
                            color = ErrorColor
                        )
                    }
                }

                // Tipo de servicio
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Tipo de servicio",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(selecciona los que apliquen)",
                            fontFamily = ManropeFontFamily,
                            fontSize = 12.sp,
                            color = TextHint
                        )
                    }

                    if (tiposError) {
                        Text(
                            text = "Selecciona al menos un tipo de servicio",
                            fontFamily = ManropeFontFamily,
                            fontSize = 12.sp,
                            color = ErrorColor
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceWhite)
                            .border(
                                width = 1.dp,
                                color = if (tiposError) ErrorColor else BorderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column {
                            tiposDeServicio.chunked(2).forEach { fila ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    fila.forEach { tipo ->
                                        val isSelected = tipo in tiposSeleccionados
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    tiposSeleccionados = tiposSeleccionados
                                                        .toMutableSet()
                                                        .apply {
                                                            if (isSelected) remove(tipo) else add(tipo)
                                                        }
                                                }
                                                .padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = { checked ->
                                                    tiposSeleccionados = tiposSeleccionados
                                                        .toMutableSet()
                                                        .apply {
                                                            if (checked) add(tipo) else remove(tipo)
                                                        }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = CheckboxChecked,
                                                    uncheckedColor = BorderColor
                                                )
                                            )
                                            Text(
                                                text = tipo,
                                                fontFamily = ManropeFontFamily,
                                                fontSize = 13.sp,
                                                color = TextPrimary,
                                                modifier = Modifier.padding(end = 4.dp)
                                            )
                                        }
                                    }
                                    // Si la fila tiene solo 1 elemento, rellenar el espacio
                                    if (fila.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Continuar
                Button(
                    onClick = {
                        intentoContinuar = true
                        if (descripcion.isNotBlank() && tiposSeleccionados.isNotEmpty()) {
                            onContinuar(
                                citaData.copy(
                                    descripcion = descripcion,
                                    tiposServicio = tiposSeleccionados
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    )
                ) {
                    Text(
                        text = "Continuar",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

