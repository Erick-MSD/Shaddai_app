
package com.example.shaddai_app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaddai_app.domain.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login.
 *
 * Se encarga de la lógica de negocio, la gestión del estado y la comunicación
 * con la capa de datos (AuthRepository).
 */
class LoginViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {

    // Flujo de estado privado y mutable que contiene el estado actual de la UI.
    private val _uiState = MutableStateFlow(LoginState())
    // Flujo de estado público e inmutable, expuesto a la UI.
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    /**
     * Función central para manejar todos los eventos de la UI.
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> _uiState.update { it.copy(email = event.email) }
            is LoginEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.password) }
            LoginEvent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginEvent.LoginClicked -> login()
            LoginEvent.RegisterClicked -> {
                // Lógica para navegar a la pantalla de registro
            }
            is LoginEvent.SocialLogin -> {
                // Lógica para el inicio de sesión con redes sociales
            }
        }
    }

    /**
     * Realiza el proceso de inicio de sesión.
     */
    private fun login() {
        viewModelScope.launch {
            // Muestra el indicador de carga
            _uiState.update { it.copy(isLoading = true) }

            // Llama al repositorio para autenticar al usuario.
            val result = authRepository.login(_uiState.value.email, _uiState.value.password)

            // Procesa el resultado de la llamada al repositorio.
            result.fold(
                onSuccess = {
                    // En caso de éxito, actualiza el estado para indicar que el login fue correcto.
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                },
                onFailure = {
                    // En caso de fallo, muestra un mensaje de error (actualmente es un placeholder).
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Credenciales inválidas") }
                }
            )
        }
    }
}
