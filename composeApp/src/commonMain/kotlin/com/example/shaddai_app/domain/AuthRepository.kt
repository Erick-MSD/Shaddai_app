
package com.example.shaddai_app.domain

import kotlinx.coroutines.delay

/**
 * Repositorio para gestionar la autenticación.
 *
 * Esta es una implementación simulada (fake) que no realiza una autenticación real.
 * Su propósito es desacoplar la lógica de negocio de la UI y prepararla para una
 * futura integración con un backend real (API, base de datos, etc.).
 */
class AuthRepository {

    /**
     * Simula una llamada de red para iniciar sesión.
     *
     * @param email El email o nombre de usuario.
     * @param password La contraseña del usuario.
     * @return Un [Result] que indica éxito o fallo.
     */
    suspend fun login(email: String, password: String): Result<Unit> {
        // 1. Simula la latencia de una llamada de red.
        delay(1500) // Simula un segundo y medio de espera.

        // 2. Aquí es donde se implementaría la lógica de autenticación real.
        //    - Se haría una llamada a una API (ej. con Ktor).
        //    - Se validarían las credenciales contra una base de datos.
        //    - Se manejarían los diferentes tipos de error (ej. usuario no encontrado, contraseña incorrecta).

        // 3. Por ahora, el login siempre tiene éxito, sin importar las credenciales.
        println("AuthRepository: Simulación de login para '$email' completada con éxito.")
        return Result.success(Unit)
    }
}
