package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onTechnicianLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Email / Nombre Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { 
                Text(
                    "Email o Nombre",
                    color = TextHint,
                    fontFamily = ManropeFontFamily
                ) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = TextPrimary,
                unfocusedBorderColor = BorderColor,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { 
                    Text(
                        "Contraseña", 
                        color = TextHint,
                        fontFamily = ManropeFontFamily
                    ) 
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = BorderColor,
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = TextHint
                        )
                    }
                }
            )
            
            Text(
                text = "Olvidé mi contraseña",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp)
                    .clickable { showForgotPasswordDialog = true },
                fontFamily = ManropeFontFamily
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp),
                fontFamily = ManropeFontFamily
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = { viewModel.login(email, password, onLoginSuccess) },
            enabled = !isLoading,
            modifier = Modifier
                .height(48.dp)
                .width(180.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0081D6)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Inicia Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ManropeFontFamily
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Regístrate aquí",
            fontSize = 16.sp,
            color = TextHint,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable { onRegisterClick() }
                .padding(8.dp),
            fontFamily = ManropeFontFamily
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Acceso Técnico
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = BorderColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onTechnicianLoginClick,
            modifier = Modifier
                .height(44.dp)
                .width(220.dp),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextSecondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextSecondary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Soy Técnico",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = ManropeFontFamily
            )
        }
    }

    // Diálogo de Olvidé mi contraseña
    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            viewModel = viewModel,
            onDismiss = {
                showForgotPasswordDialog = false
                viewModel.clearResetPasswordState()
            }
        )
    }
}

@Composable
private fun ForgotPasswordDialog(
    viewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    var resetEmail by remember { mutableStateOf("") }
    val resetSuccess by viewModel.resetPasswordSuccess.collectAsState()
    val resetError by viewModel.resetPasswordError.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = if (resetSuccess) "¡Correo enviado!" else "Recuperar contraseña",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column {
                if (resetSuccess) {
                    Text(
                        text = "Se ha enviado un enlace de recuperación a tu correo electrónico. Revisa tu bandeja de entrada (y spam).",
                        fontFamily = ManropeFontFamily,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                } else {
                    Text(
                        text = "Ingresa tu email o nombre de usuario y te enviaremos un enlace para restablecer tu contraseña.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        placeholder = {
                            Text(
                                "Email o Nombre",
                                color = TextHint,
                                fontFamily = ManropeFontFamily
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = TextPrimary,
                            unfocusedBorderColor = BorderColor,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    if (resetError != null) {
                        Text(
                            text = resetError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = ManropeFontFamily,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (resetSuccess) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0081D6)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Aceptar",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.sendPasswordReset(resetEmail) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0081D6)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Enviar",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            if (!resetSuccess) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancelar",
                        color = TextSecondary,
                        fontFamily = ManropeFontFamily
                    )
                }
            }
        }
    )
}
