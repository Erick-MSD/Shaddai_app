package com.example.shaddai_app.domain

import com.example.shaddai_app.data.model.User
import com.example.shaddai_app.data.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Repositorio de autenticación.
 *
 * IMPORTANTE: Implementación temporal sin Realm/MongoDB (porque el plugin Realm no compila con Kotlin 2.3.0/K2 en este proyecto).
 *
 * Cuando se ajuste la compatibilidad de versiones, se vuelve a implementar con:
 * - Realm App Services (Email/Password)
 * - `customData` para role/displayName
 */
class AuthRepository : AuthRepositoryContract {

    // Colección requerida: usuarios temporales.
    private val users: List<UserRecord> = listOf(
        UserRecord("admin@shaddai.com", "1234", UserRole.Admin, "Admin"),
        UserRecord("tecnico@shaddai.com", "1234", UserRole.Tecnico, "Técnico"),
        UserRecord("cliente@shaddai.com", "1234", UserRole.Cliente, "Cliente")
    )

    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.Default) {
        try {
            delay(400) // simula latencia
            val normalizedEmail = email.trim().lowercase()

            // Higher-order: buscamos y mapeamos
            val record = users.firstOrNull { it.email == normalizedEmail && it.password == password }

            if (record == null) {
                Result.failure(IllegalArgumentException("Credenciales inválidas"))
            } else {
                Result.success(
                    User(
                        id = normalizedEmail,
                        email = normalizedEmail,
                        displayName = record.displayName,
                        role = record.role
                    )
                )
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun logout(): Result<Unit> = Result.success(Unit)

    private data class UserRecord(
        val email: String,
        val password: String,
        val role: UserRole,
        val displayName: String
    )
}
