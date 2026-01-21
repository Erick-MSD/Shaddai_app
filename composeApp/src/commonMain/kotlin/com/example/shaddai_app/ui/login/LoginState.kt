package com.example.shaddai_app.ui.login

/**
 * Estado de la pantalla de Login
 *
 * @property email Email o usuario ingresado
 * @property password Contraseña ingresada
 * @property isPasswordVisible Estado de visibilidad de la contraseña
 * @property isLoading Indica si se está procesando el login
 * @property errorMessage Mensaje de error a mostrar (null si no hay error)
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
