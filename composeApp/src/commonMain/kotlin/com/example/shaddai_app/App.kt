package com.example.shaddai_app

import androidx.compose.runtime.*
import com.example.shaddai_app.data.model.User
import com.example.shaddai_app.data.model.UserRole
import com.example.shaddai_app.ui.login.LoginScreen
import com.example.shaddai_app.ui.login.LoginViewModel
import com.example.shaddai_app.ui.main.TechnicianAppScaffold
import com.example.shaddai_app.ui.theme.ShaddaiTheme

/**
 * Composable raíz de la aplicación.
 *
 * Gestiona la navegación principal basada en el estado de autenticación del usuario.
 */
@Composable
fun App() {
    ShaddaiTheme {
        // Estado que representa al usuario actual, null si no hay sesión.
        var user: User? by remember { mutableStateOf(null) }

        // Navegación basada en el estado `user`.
        when (val currentUser = user) {
            null -> {
                // Si no hay usuario, muestra la pantalla de login.
                LoginScreen(
                    viewModel = remember { LoginViewModel() },
                    // Al iniciar sesión con éxito, se establece el usuario.
                    onLoginSuccess = { logged -> user = logged },
                    onNavigateToRegister = {
                        // Aquí se implementaría la navegación a la pantalla de registro.
                    }
                )
            }

            else -> {
                // Si hay usuario, se muestra el scaffold correspondiente a su rol.
                when (currentUser.role) {
                    UserRole.Admin -> {
                        // TODO: Crear scaffold Admin
                        TechnicianAppScaffold()
                    }

                    UserRole.Tecnico -> TechnicianAppScaffold()

                    UserRole.Cliente -> {
                        // TODO: Crear scaffold Cliente
                        TechnicianAppScaffold()
                    }
                }
            }
        }
    }
}
