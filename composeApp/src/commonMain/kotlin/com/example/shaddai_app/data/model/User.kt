package com.example.shaddai_app.data.model

/**
 * Roles soportados por la app.
 *
 * Nota: los valores se guardan como String en la DB para facilitar integración con MongoDB/Realm.
 */
enum class UserRole {
    Admin,
    Tecnico,
    Cliente
}

/**
 * Modelo de usuario autenticado para uso en UI.
 *
 * @param id Identificador del usuario en la fuente de datos.
 * @param email Correo/usuario usado para login.
 * @param displayName Nombre a mostrar.
 * @param role Rol del usuario para enrutar a su módulo correspondiente.
 */
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val role: UserRole
)

