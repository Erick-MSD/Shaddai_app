package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.components.StepProgressBar
import com.example.shaddai_app_android.ui.theme.*

@Composable
fun DireccionScreen(
    citaData: CitaData,
    onAtras: () -> Unit,
    onContinuar: (CitaData) -> Unit
) {
    var calle by remember { mutableStateOf(citaData.calle) }
    var numeroExterior by remember { mutableStateOf(citaData.numeroExterior) }
    var colonia by remember { mutableStateOf(citaData.colonia) }
    var codigoPostal by remember { mutableStateOf(citaData.codigoPostal) }
    var referencias by remember { mutableStateOf(citaData.referencias) }
    var intentoContinuar by remember { mutableStateOf(false) }

    val calleError = intentoContinuar && calle.isBlank()
    val numError = intentoContinuar && numeroExterior.isBlank()
    val coloniaError = intentoContinuar && colonia.isBlank()
    val cpError = intentoContinuar && codigoPostal.isBlank()

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
            StepProgressBar(currentStep = 2)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Ubicación del Servicio",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Ingresa la dirección donde se realizará el trabajo.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                // Card de dirección
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                        // Calle y número
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                FieldLabel("CALLE", calleError)
                                OutlinedTextField(
                                    value = calle,
                                    onValueChange = { calle = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { HintText("Ej. Av. Reforma") },
                                    isError = calleError,
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = fieldColors(calleError)
                                )
                                if (calleError) ErrorText("Campo requerido")
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                FieldLabel("NÚMERO", numError)
                                OutlinedTextField(
                                    value = numeroExterior,
                                    onValueChange = { numeroExterior = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { HintText("222") },
                                    isError = numError,
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = fieldColors(numError)
                                )
                                if (numError) ErrorText("Requerido")
                            }
                        }

                        // Colonia y CP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                FieldLabel("COLONIA", coloniaError)
                                OutlinedTextField(
                                    value = colonia,
                                    onValueChange = { colonia = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { HintText("Colonia") },
                                    isError = coloniaError,
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = fieldColors(coloniaError)
                                )
                                if (coloniaError) ErrorText("Campo requerido")
                            }
                            Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                FieldLabel("CP", cpError)
                                OutlinedTextField(
                                    value = codigoPostal,
                                    onValueChange = {
                                        if (it.length <= 5) codigoPostal = it
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { HintText("00000") },
                                    isError = cpError,
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = fieldColors(cpError)
                                )
                                if (cpError) ErrorText("Requerido")
                            }
                        }

                        // Referencias
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            FieldLabel("REFERENCIAS / SEÑAS", false)
                            OutlinedTextField(
                                value = referencias,
                                onValueChange = { referencias = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { HintText("Ej. Casa azul, con cancel negro") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors(false)
                            )
                        }

                        // Usar ubicación actual
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = PrimaryBlueLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Usar mi ubicación actual",
                                fontFamily = ManropeFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlueLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Botones Atrás / Continuar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onAtras,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            width = 1.5.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextPrimary
                        )
                    ) {
                        Text(
                            text = "Atrás",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                    Button(
                        onClick = {
                            intentoContinuar = true
                            if (calle.isNotBlank() && numeroExterior.isNotBlank() &&
                                colonia.isNotBlank() && codigoPostal.isNotBlank()
                            ) {
                                onContinuar(
                                    citaData.copy(
                                        calle = calle,
                                        numeroExterior = numeroExterior,
                                        colonia = colonia,
                                        codigoPostal = codigoPostal,
                                        referencias = referencias
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(
                            text = "Continuar",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FieldLabel(label: String, isError: Boolean) {
    Text(
        text = label,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        color = if (isError) ErrorColor else TextSecondary,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun HintText(text: String) {
    Text(
        text = text,
        fontFamily = ManropeFontFamily,
        fontSize = 13.sp,
        color = TextHint
    )
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        fontFamily = ManropeFontFamily,
        fontSize = 11.sp,
        color = ErrorColor
    )
}

@Composable
private fun fieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PrimaryBlue,
    unfocusedBorderColor = if (isError) ErrorColor else BorderColor,
    errorBorderColor = ErrorColor,
    focusedContainerColor = SurfaceWhite,
    unfocusedContainerColor = SurfaceWhite,
    errorContainerColor = SurfaceWhite
)


