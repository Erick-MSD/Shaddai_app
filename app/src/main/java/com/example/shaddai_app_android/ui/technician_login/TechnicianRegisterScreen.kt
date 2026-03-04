package com.example.shaddai_app_android.ui.technician_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.ManropeFontFamily
import com.example.shaddai_app_android.viewmodel.TechnicianAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianRegisterScreen(
    viewModel: TechnicianAuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var specialtyExpanded by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val specialties = listOf(
        "Aire Acondicionado",
        "Refrigeración",
        "Lavadoras y Secadoras",
        "Electricidad General",
        "Plomería",
        "Calefacción",
        "Electrónica",
        "Otro"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TechLoginColors.BgGradientStart, TechLoginColors.BgGradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = TechLoginColors.TextSecondary
                    )
                }
                Text(
                    text = "Registro de Técnico",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechLoginColors.TextPrimary,
                    fontFamily = ManropeFontFamily
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Únete al equipo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechLoginColors.TextPrimary,
                    fontFamily = ManropeFontFamily
                )

                Text(
                    text = "Completa tus datos para registrarte como técnico",
                    fontSize = 13.sp,
                    color = TechLoginColors.TextSecondary,
                    fontFamily = ManropeFontFamily,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // Nombre completo
                TechTextField(
                    value = name,
                    onValueChange = { if (it.all { c -> c.isLetter() || c.isWhitespace() }) name = it },
                    placeholder = "Nombre completo *",
                    keyboardType = KeyboardType.Text
                )

                // Email
                TechTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Correo electrónico *",
                    keyboardType = KeyboardType.Email
                )

                // Teléfono
                TechTextField(
                    value = phone,
                    onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 10) phone = it },
                    placeholder = "Teléfono *",
                    keyboardType = KeyboardType.Phone
                )

                // Especialidad (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = specialtyExpanded,
                    onExpandedChange = { specialtyExpanded = !specialtyExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    OutlinedTextField(
                        value = specialty,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                "Especialidad *",
                                color = TechLoginColors.TextSecondary,
                                fontFamily = ManropeFontFamily
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = specialtyExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TechLoginColors.Surface,
                            unfocusedContainerColor = TechLoginColors.Surface,
                            focusedBorderColor = TechLoginColors.Accent,
                            unfocusedBorderColor = TechLoginColors.BorderColor,
                            focusedTextColor = TechLoginColors.TextPrimary,
                            unfocusedTextColor = TechLoginColors.TextPrimary,
                            focusedTrailingIconColor = TechLoginColors.Accent,
                            unfocusedTrailingIconColor = TechLoginColors.TextSecondary
                        ),
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = specialtyExpanded,
                        onDismissRequest = { specialtyExpanded = false },
                        modifier = Modifier.background(TechLoginColors.Surface)
                    ) {
                        specialties.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option,
                                        color = TechLoginColors.TextPrimary,
                                        fontFamily = ManropeFontFamily
                                    )
                                },
                                onClick = {
                                    specialty = option
                                    specialtyExpanded = false
                                }
                            )
                        }
                    }
                }

                // Número de licencia (opcional)
                TechTextField(
                    value = licenseNumber,
                    onValueChange = { licenseNumber = it },
                    placeholder = "Número de licencia (opcional)",
                    keyboardType = KeyboardType.Text
                )

                // Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Contraseña *",
                            color = TechLoginColors.TextSecondary,
                            fontFamily = ManropeFontFamily
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                tint = TechLoginColors.TextSecondary
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TechLoginColors.Surface,
                        unfocusedContainerColor = TechLoginColors.Surface,
                        focusedBorderColor = TechLoginColors.Accent,
                        unfocusedBorderColor = TechLoginColors.BorderColor,
                        focusedTextColor = TechLoginColors.TextPrimary,
                        unfocusedTextColor = TechLoginColors.TextPrimary,
                        cursorColor = TechLoginColors.Accent
                    ),
                    singleLine = true
                )

                // Indicador de fuerza de contraseña
                if (password.isNotEmpty()) {
                    val strength = when {
                        password.length < 6 -> "Débil"
                        password.length < 8 -> "Media"
                        password.any { it.isDigit() } && password.any { it.isUpperCase() } -> "Fuerte"
                        else -> "Media"
                    }
                    val strengthColor = when (strength) {
                        "Débil" -> TechLoginColors.ErrorText
                        "Media" -> Color(0xFFFFB347)
                        "Fuerte" -> Color(0xFF38A169)
                        else -> TechLoginColors.TextSecondary
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = {
                                when (strength) {
                                    "Débil" -> 0.33f
                                    "Media" -> 0.66f
                                    "Fuerte" -> 1f
                                    else -> 0f
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp),
                            color = strengthColor,
                            trackColor = TechLoginColors.BorderColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strength,
                            fontSize = 11.sp,
                            color = strengthColor,
                            fontFamily = ManropeFontFamily
                        )
                    }
                }

                // Error message
                if (errorMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = TechLoginColors.ErrorBg)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = TechLoginColors.ErrorText,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(12.dp),
                            fontFamily = ManropeFontFamily
                        )
                    }
                }

                // Register Button
                Button(
                    onClick = {
                        viewModel.register(
                            name = name,
                            email = email,
                            pass = password,
                            phone = phone,
                            specialty = specialty,
                            licenseNumber = licenseNumber,
                            onSuccess = onRegisterSuccess
                        )
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = if (isLoading)
                                        listOf(TechLoginColors.SurfaceLight, TechLoginColors.SurfaceLight)
                                    else
                                        listOf(TechLoginColors.Accent, TechLoginColors.AccentLight)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = TechLoginColors.Accent,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Crear Cuenta de Técnico",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = ManropeFontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Al registrarte, aceptas nuestros términos y condiciones",
                    fontSize = 11.sp,
                    color = TechLoginColors.TextSecondary,
                    fontFamily = ManropeFontFamily
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun TechTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                color = TechLoginColors.TextSecondary,
                fontFamily = ManropeFontFamily
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TechLoginColors.Surface,
            unfocusedContainerColor = TechLoginColors.Surface,
            focusedBorderColor = TechLoginColors.Accent,
            unfocusedBorderColor = TechLoginColors.BorderColor,
            focusedTextColor = TechLoginColors.TextPrimary,
            unfocusedTextColor = TechLoginColors.TextPrimary,
            cursorColor = TechLoginColors.Accent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}



