
package com.example.shaddai_app.ui.login

sealed interface LoginEvent {
    data class OnEmailChange(val email: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    object TogglePasswordVisibility : LoginEvent
    object LoginClicked : LoginEvent
    object RegisterClicked : LoginEvent
    data class SocialLogin(val provider: SocialLoginProvider) : LoginEvent
}

enum class SocialLoginProvider {
    GOOGLE,
    FACEBOOK
}
