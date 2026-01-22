
package com.example.shaddai_app

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.shaddai_app.ui.home.HomeScreen
import com.example.shaddai_app.ui.login.LoginScreen
import com.example.shaddai_app.ui.login.LoginViewModel
import com.example.shaddai_app.ui.theme.ShaddaiTheme

/**
 * Composable raíz de la aplicación.
 *
 * Gestiona la navegación principal basada en el estado de autenticación del usuario.
 */
@Composable
@Preview
fun App() {
    ShaddaiTheme {
        // Estado que controla si el usuario ha iniciado sesión.
        var isLoggedIn by remember { mutableStateOf(false) }

        // Navegación manual basada en el estado `isLoggedIn`.
        if (isLoggedIn) {
            // Si el usuario está logueado, muestra la pantalla principal.
            HomeScreen()
        } else {
            // Si no, muestra la pantalla de login.
            LoginScreen(
                viewModel = LoginViewModel(),
                // La lambda onLoginSuccess se encarga de cambiar el estado de la navegación.
                onLoginSuccess = { isLoggedIn = true },
                onNavigateToRegister = {
                    // Aquí se implementaría la navegación a la pantalla de registro.
                }
            )
        }
    }
}
