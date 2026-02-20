package com.example.shaddai_app.domain

import com.example.shaddai_app.data.model.User

/**
 * Contrato de autenticación.
 *
 * Nos permite cambiar la fuente de datos (Mongo/Realm, REST, mock local, etc.)
 * sin romper UI/ViewModels.
 */
interface AuthRepositoryContract {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout(): Result<Unit>
}

