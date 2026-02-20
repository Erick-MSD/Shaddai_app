package com.example.shaddai_app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shaddai_app.domain.AuthRepository
import com.example.shaddai_app.domain.AuthRepositoryContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login.
 */
class LoginViewModel(
    private val authRepository: AuthRepositoryContract = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> _uiState.update {
                it.copy(email = event.email, emailError = null, authErrorMessage = null)
            }

            is LoginEvent.OnPasswordChange -> _uiState.update {
                it.copy(password = event.password, passwordError = null, authErrorMessage = null)
            }

            LoginEvent.TogglePasswordVisibility -> _uiState.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }

            LoginEvent.LoginClicked -> login()

            LoginEvent.RegisterClicked -> {
                // Navegación a registro (pendiente)
            }

            is LoginEvent.SocialLogin -> {
                // Social login (pendiente)
            }
        }
    }

    /**
     * Validación local rápida.
     * @return true si todo está OK.
     */
    private fun validateInputs(email: String, password: String): Boolean {
        var ok = true
        if (email.isBlank()) {
            ok = false
            _uiState.update { it.copy(emailError = "Campo requerido") }
        }
        if (password.isBlank()) {
            ok = false
            _uiState.update { it.copy(passwordError = "Campo requerido") }
        }
        return ok
    }

    private fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        // Resetea errores previos
        _uiState.update { it.copy(emailError = null, passwordError = null, authErrorMessage = null) }

        if (!validateInputs(email, password)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.login(email, password)

            result.fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loggedInUser = user,
                            authErrorMessage = null
                        )
                    }
                },
                onFailure = {
                    // Error típico: credenciales inválidas
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            authErrorMessage = "Usuario o contraseña incorrectos",
                            // opcional: marcar ambos campos en rojo en caso de auth fail
                            emailError = it.emailError ?: "",
                            passwordError = it.passwordError ?: ""
                        )
                    }
                }
            )
        }
    }
}
