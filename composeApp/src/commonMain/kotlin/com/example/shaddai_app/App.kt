package com.example.shaddai_app

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.shaddai_app.ui.login.LoginScreen
import com.example.shaddai_app.ui.theme.ShaddaiTheme

@Composable
@Preview
fun App() {
    ShaddaiTheme {
        LoginScreen(
            onLoginSuccess = {
                // TODO: Navegar a la pantalla principal
                println("Login exitoso - Navegar a Home")
            },
            onNavigateToRegister = {
                // TODO: Navegar a la pantalla de registro
                println("Navegar a pantalla de registro")
            }
        )
    }
}