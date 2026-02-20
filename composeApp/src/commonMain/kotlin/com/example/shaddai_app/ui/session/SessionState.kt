package com.example.shaddai_app.ui.session

import com.example.shaddai_app.data.model.User

data class SessionState(
    val user: User? = null
) {
    val isLoggedIn: Boolean get() = user != null
}

