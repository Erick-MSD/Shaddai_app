package com.example.shaddai_app.ui.login

import com.example.shaddai_app.data.model.User

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    // Validación UI
    val emailError: String? = null,
    val passwordError: String? = null,
    val authErrorMessage: String? = null,

    // Resultado
    val loggedInUser: User? = null
)
