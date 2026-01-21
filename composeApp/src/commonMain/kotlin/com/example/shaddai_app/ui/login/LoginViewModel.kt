package com.example.shaddai_app.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de Login
 *
 * Gestiona el estado y la lógica de negocio del login.
 * Preparado para conectar con repositorio/base de datos en el futuro.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    /**
     * Actualiza el email/usuario
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    /**
     * Actualiza la contraseña
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    /**
     * Alterna la visibilidad de la contraseña
     */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /**
     * Maneja el evento de click en el botón de login
     *
     * TODO: Implementar validación y autenticación
     * TODO: Conectar con repositorio/database
     */
    fun onLoginClick() {
        val currentState = _uiState.value

        // Validación básica
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor completa todos los campos") }
            return
        }

        // TODO: Aquí iría la lógica de autenticación
        // _uiState.update { it.copy(isLoading = true) }
        // viewModelScope.launch {
        //     try {
        //         val result = authRepository.login(currentState.email, currentState.password)
        //         // Navegar a la siguiente pantalla
        //     } catch (e: Exception) {
        //         _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
        //     }
        // }

        println("Login attempt - Email: ${currentState.email}")
    }

    /**
     * Maneja el evento de registro
     *
     * TODO: Navegar a pantalla de registro
     */
    fun onRegisterClick() {
        println("Navigate to register screen")
        // TODO: Implementar navegación
    }

    /**
     * Maneja el login con proveedores externos (Google, Facebook, etc.)
     *
     * TODO: Implementar OAuth
     */
    fun onSocialLogin(provider: SocialLoginProvider) {
        println("Social login with: $provider")
        // TODO: Implementar autenticación social
    }
}

enum class SocialLoginProvider {
    GOOGLE,
    FACEBOOK
}
