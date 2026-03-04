package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = BorderColor,
                ),
                singleLine = true
            )
            
            Text(
                text = "Olvidé mi contraseña",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp)
                    .clickable { /* Handle forgot password */ },
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
    }
}
