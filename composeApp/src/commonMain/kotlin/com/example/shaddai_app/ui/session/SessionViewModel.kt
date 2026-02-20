package com.example.shaddai_app.ui.session

import androidx.lifecycle.ViewModel
import com.example.shaddai_app.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun setUser(user: User) {
        _uiState.update { it.copy(user = user) }
    }

    fun clear() {
        _uiState.update { it.copy(user = null) }
    }
}

