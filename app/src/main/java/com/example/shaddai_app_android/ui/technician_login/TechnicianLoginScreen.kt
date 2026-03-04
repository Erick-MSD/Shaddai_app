package com.example.shaddai_app_android.ui.technician_login

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

// Colores específicos para el módulo de técnico
object TechLoginColors {
    val BgGradientStart = Color(0xFF0D1B2A)
    val BgGradientEnd = Color(0xFF1B3A4B)
    val Accent = Color(0xFF00B4D8)
    val AccentLight = Color(0xFF48CAE4)
    val Surface = Color(0xFF1B2838)
    val SurfaceLight = Color(0xFF243447)
    val TextPrimary = Color(0xFFEDF2F4)
    val TextSecondary = Color(0xFF8B9DAF)
    val BorderColor = Color(0xFF2D4356)
    val ErrorBg = Color(0xFF3D1A1A)
    val ErrorText = Color(0xFFFF6B6B)
}

@Composable
fun TechnicianLoginScreen(
    viewModel: TechnicianAuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onBackToClientLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
            onClick = onBackToClientLogin,
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
            // Ícono de técnico
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(TechLoginColors.Accent, TechLoginColors.AccentLight)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Técnico",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Portal Técnico",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TechLoginColors.TextPrimary,
                fontFamily = ManropeFontFamily
            )

            Text(
                text = "Accede a tu panel de servicios",
                fontSize = 14.sp,
                color = TechLoginColors.TextSecondary,
                fontFamily = ManropeFontFamily,
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        "Email o nombre de técnico",
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

            // Password Field
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Contraseña",
                            color = TechLoginColors.TextSecondary,
                            fontFamily = ManropeFontFamily
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
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

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 12.sp,
                    color = TechLoginColors.AccentLight,
                    modifier = Modifier
                        .padding(top = 6.dp, start = 4.dp)
                        .clickable { onForgotPasswordClick() },
                    fontFamily = ManropeFontFamily
                )
            }

            // Error message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
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

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button con gradiente
            Button(
                onClick = { viewModel.login(email, password, onLoginSuccess) },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .animateContentSize(),
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
                            text = "Iniciar Sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = ManropeFontFamily
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = TechLoginColors.BorderColor
                )
                Text(
                    text = "  ¿Eres nuevo?  ",
                    color = TechLoginColors.TextSecondary,
                    fontSize = 12.sp,
                    fontFamily = ManropeFontFamily
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = TechLoginColors.BorderColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        colors = listOf(TechLoginColors.Accent, TechLoginColors.AccentLight)
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TechLoginColors.Accent
                )
            ) {
                Text(
                    text = "Registrarme como Técnico",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = ManropeFontFamily
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back to client login
            Text(
                text = "← Volver al inicio de cliente",
                fontSize = 13.sp,
                color = TechLoginColors.TextSecondary,
                fontFamily = ManropeFontFamily,
                modifier = Modifier
                    .clickable { onBackToClientLogin() }
                    .padding(8.dp)
            )
        }
    }
}



