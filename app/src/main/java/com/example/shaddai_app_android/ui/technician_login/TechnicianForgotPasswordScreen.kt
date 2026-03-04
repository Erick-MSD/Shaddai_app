package com.example.shaddai_app_android.ui.technician_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.ManropeFontFamily
import com.example.shaddai_app_android.viewmodel.TechnicianAuthViewModel

@Composable
fun TechnicianForgotPasswordScreen(
    viewModel: TechnicianAuthViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val resetSuccess by viewModel.resetPasswordSuccess.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TechLoginColors.BgGradientStart, TechLoginColors.BgGradientEnd)
                )
            )
    ) {
        // Botón de retroceso
        IconButton(
            onClick = {
                viewModel.clearResetPasswordSuccess()
                viewModel.clearError()
                onBackClick()
            },
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = TechLoginColors.TextSecondary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (resetSuccess) {
                // Estado de éxito
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B4332)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Éxito",
                        tint = Color(0xFF38A169),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Correo enviado!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechLoginColors.TextPrimary,
                    fontFamily = ManropeFontFamily
                )

                Text(
                    text = "Revisa tu bandeja de entrada y sigue las instrucciones para restablecer tu contraseña.",
                    fontSize = 14.sp,
                    color = TechLoginColors.TextSecondary,
                    fontFamily = ManropeFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, bottom = 40.dp)
                )

                Button(
                    onClick = {
                        viewModel.clearResetPasswordSuccess()
                        viewModel.clearError()
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(TechLoginColors.Accent, TechLoginColors.AccentLight)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Volver al inicio de sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = ManropeFontFamily
                        )
                    }
                }
            } else {
                // Formulario de recuperación
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(TechLoginColors.Accent.copy(alpha = 0.3f), TechLoginColors.AccentLight.copy(alpha = 0.3f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = TechLoginColors.Accent,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Recuperar Contraseña",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechLoginColors.TextPrimary,
                    fontFamily = ManropeFontFamily
                )

                Text(
                    text = "Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.",
                    fontSize = 14.sp,
                    color = TechLoginColors.TextSecondary,
                    fontFamily = ManropeFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp, bottom = 40.dp)
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Correo electrónico",
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

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

                Spacer(modifier = Modifier.height(16.dp))

                // Send Button
                Button(
                    onClick = { viewModel.sendPasswordReset(email) },
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
                                text = "Enviar enlace de recuperación",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = ManropeFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}



