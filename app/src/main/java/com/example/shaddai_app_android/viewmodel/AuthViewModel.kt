package com.example.shaddai_app_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.UserProfile
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userName = MutableStateFlow("Cliente")
    val userName: StateFlow<String> = _userName

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _profileUpdateSuccess = MutableStateFlow(false)
    val profileUpdateSuccess: StateFlow<Boolean> = _profileUpdateSuccess

    private val _resetPasswordSuccess = MutableStateFlow(false)
    val resetPasswordSuccess: StateFlow<Boolean> = _resetPasswordSuccess

    private val _resetPasswordError = MutableStateFlow<String?>(null)
    val resetPasswordError: StateFlow<String?> = _resetPasswordError

    init {
        if (auth.currentUser != null) {
            fetchUserName()
            fetchUserProfile()
        }
    }

    private fun fetchUserName() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name")
                if (!name.isNullOrBlank()) {
                    _userName.value = name
                }
            }
    }

    fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val profile = doc.toObject(UserProfile::class.java)
                if (profile != null) {
                    _userProfile.value = profile.copy(
                        uid = uid,
                        email = auth.currentUser?.email ?: profile.email
                    )
                    _userName.value = profile.name.ifBlank { "Cliente" }
                }
            }
    }

    fun updateProfile(name: String, phone: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        if (name.isBlank()) {
            _errorMessage.value = "El nombre no puede estar vacío"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        val updates = mapOf(
            "name" to name,
            "phone" to phone
        )

        db.collection("users").document(uid)
            .update(updates)
            .addOnSuccessListener {
                _isLoading.value = false
                _userName.value = name
                _userProfile.value = _userProfile.value.copy(name = name, phone = phone)
                _profileUpdateSuccess.value = true
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _errorMessage.value = "Error al actualizar: ${e.message}"
            }
    }

    fun updatePassword(currentPassword: String, newPassword: String, onSuccess: () -> Unit) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return

        if (currentPassword.isBlank() || newPassword.isBlank()) {
            _errorMessage.value = "Completa ambos campos de contraseña"
            return
        }
        if (newPassword.length < 6) {
            _errorMessage.value = "La nueva contraseña debe tener al menos 6 caracteres"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        _isLoading.value = false
                        _profileUpdateSuccess.value = true
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        _isLoading.value = false
                        _errorMessage.value = "Error al cambiar contraseña: ${e.message}"
                    }
            }
            .addOnFailureListener {
                _isLoading.value = false
                _errorMessage.value = "Contraseña actual incorrecta"
            }
    }

    fun clearProfileUpdateSuccess() {
        _profileUpdateSuccess.value = false
    }

    fun login(emailOrName: String, pass: String, onSuccess: () -> Unit) {
        if (emailOrName.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos"
            return
        }
        _isLoading.value = true
        _errorMessage.value = null

        val isEmail = emailOrName.contains("@")

        if (isEmail) {
            loginWithEmail(emailOrName, pass, onSuccess)
        } else {
            // Buscar el email por nombre en Firestore
            db.collection("users")
                .whereEqualTo("name", emailOrName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val email = querySnapshot.documents[0].getString("email")
                        if (!email.isNullOrBlank()) {
                            loginWithEmail(email, pass, onSuccess)
                        } else {
                            _isLoading.value = false
                            _errorMessage.value = "No se encontró un correo asociado a ese nombre"
                        }
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = "No se encontró un usuario con ese nombre"
                    }
                }
                .addOnFailureListener {
                    _isLoading.value = false
                    _errorMessage.value = "Error al buscar usuario: ${it.message}"
                }
        }
    }

    private fun loginWithEmail(email: String, pass: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        _isLoading.value = false
                        _errorMessage.value = "Error al obtener la cuenta"
                        return@addOnCompleteListener
                    }
                    // Verificar que NO sea una cuenta de técnico
                    db.collection("technicians").document(uid).get()
                        .addOnSuccessListener { doc ->
                            _isLoading.value = false
                            if (doc.exists()) {
                                // Es técnico → no permitir login como cliente
                                auth.signOut()
                                _errorMessage.value = "Esta cuenta es de técnico. Usa el acceso de técnico para iniciar sesión."
                            } else {
                                // Es cliente → continuar
                                _isLoggedIn.value = true
                                fetchUserName()
                                fetchUserProfile()
                                onSuccess()
                            }
                        }
                        .addOnFailureListener {
                            _isLoading.value = false
                            _errorMessage.value = "Error al verificar la cuenta: ${it.message}"
                            auth.signOut()
                        }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Error al iniciar sesión"
                }
            }
    }

    fun register(name: String, email: String, pass: String, phone: String, onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || pass.isBlank() || phone.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos"
            return
        }
        _isLoading.value = true
        _errorMessage.value = null
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val userProfile = UserProfile(uid, name, email, phone)
                    saveUserProfile(userProfile, onSuccess)
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Error al registrarse"
                }
            }
    }

    private fun saveUserProfile(userProfile: UserProfile, onSuccess: () -> Unit) {
        db.collection("users").document(userProfile.uid)
            .set(userProfile)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    _errorMessage.value = "Error al guardar el perfil: ${task.exception?.message}"
                }
            }
    }

    fun sendPasswordReset(emailOrName: String) {
        if (emailOrName.isBlank()) {
            _resetPasswordError.value = "Ingresa tu email o nombre"
            return
        }

        _resetPasswordSuccess.value = false
        _resetPasswordError.value = null

        val isEmail = emailOrName.contains("@")

        if (isEmail) {
            sendResetEmail(emailOrName)
        } else {
            db.collection("users")
                .whereEqualTo("name", emailOrName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val email = querySnapshot.documents[0].getString("email")
                        if (!email.isNullOrBlank()) {
                            sendResetEmail(email)
                        } else {
                            _resetPasswordError.value = "No se encontró un correo asociado a ese nombre"
                        }
                    } else {
                        _resetPasswordError.value = "No se encontró un usuario con ese nombre"
                    }
                }
                .addOnFailureListener {
                    _resetPasswordError.value = "Error al buscar usuario: ${it.message}"
                }
        }
    }

    private fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _resetPasswordSuccess.value = true
                _resetPasswordError.value = null
            }
            .addOnFailureListener { e ->
                _resetPasswordError.value = when {
                    e.message?.contains("no user record", ignoreCase = true) == true ->
                        "No existe una cuenta con ese correo"
                    e.message?.contains("badly formatted", ignoreCase = true) == true ->
                        "El formato del correo no es válido"
                    else -> "Error al enviar correo: ${e.message}"
                }
            }
    }

    fun clearResetPasswordState() {
        _resetPasswordSuccess.value = false
        _resetPasswordError.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _userName.value = "Cliente"
        _userProfile.value = UserProfile()
    }
}
