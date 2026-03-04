package com.example.shaddai_app_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shaddai_app_android.model.TechnicianProfile
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TechnicianAuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _technicianName = MutableStateFlow("Técnico")
    val technicianName: StateFlow<String> = _technicianName

    private val _technicianProfile = MutableStateFlow(TechnicianProfile())
    val technicianProfile: StateFlow<TechnicianProfile> = _technicianProfile

    private val _resetPasswordSuccess = MutableStateFlow(false)
    val resetPasswordSuccess: StateFlow<Boolean> = _resetPasswordSuccess

    private val _profileUpdateSuccess = MutableStateFlow(false)
    val profileUpdateSuccess: StateFlow<Boolean> = _profileUpdateSuccess

    init {
        // Verificar si hay sesión activa de técnico
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkIfTechnician(currentUser.uid)
        }
    }

    private fun checkIfTechnician(uid: String) {
        db.collection("technicians").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    _isLoggedIn.value = true
                    val profile = doc.toObject(TechnicianProfile::class.java)
                    if (profile != null) {
                        _technicianProfile.value = profile.copy(
                            uid = uid,
                            email = auth.currentUser?.email ?: profile.email
                        )
                        _technicianName.value = profile.name.ifBlank { "Técnico" }
                    }
                }
            }
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
            // Buscar el email por nombre en la colección de técnicos
            db.collection("technicians")
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
                        _errorMessage.value = "No se encontró un técnico con ese nombre"
                    }
                }
                .addOnFailureListener {
                    _isLoading.value = false
                    _errorMessage.value = "Error al buscar técnico: ${it.message}"
                }
        }
    }

    private fun loginWithEmail(email: String, pass: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    // Verificar que el usuario sea un técnico
                    db.collection("technicians").document(uid).get()
                        .addOnSuccessListener { doc ->
                            _isLoading.value = false
                            if (doc.exists()) {
                                _isLoggedIn.value = true
                                fetchTechnicianProfile()
                                onSuccess()
                            } else {
                                auth.signOut()
                                _errorMessage.value = "Esta cuenta no está registrada como técnico"
                            }
                        }
                        .addOnFailureListener {
                            _isLoading.value = false
                            auth.signOut()
                            _errorMessage.value = "Error al verificar cuenta de técnico"
                        }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Error al iniciar sesión"
                }
            }
    }

    fun register(
        name: String,
        email: String,
        pass: String,
        phone: String,
        specialty: String,
        licenseNumber: String,
        onSuccess: () -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || pass.isBlank() || phone.isBlank() || specialty.isBlank()) {
            _errorMessage.value = "Por favor, completa todos los campos obligatorios"
            return
        }
        if (pass.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val technicianProfile = TechnicianProfile(
                        uid = uid,
                        name = name,
                        email = email,
                        phone = phone,
                        specialty = specialty,
                        licenseNumber = licenseNumber,
                        role = "technician",
                        isActive = true
                    )
                    saveTechnicianProfile(technicianProfile, onSuccess)
                } else {
                    _isLoading.value = false
                    _errorMessage.value = task.exception?.message ?: "Error al registrarse"
                }
            }
    }

    private fun saveTechnicianProfile(profile: TechnicianProfile, onSuccess: () -> Unit) {
        db.collection("technicians").document(profile.uid)
            .set(profile)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    _errorMessage.value = "Error al guardar el perfil: ${task.exception?.message}"
                }
            }
    }

    private fun fetchTechnicianProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("technicians").document(uid).get()
            .addOnSuccessListener { doc ->
                val profile = doc.toObject(TechnicianProfile::class.java)
                if (profile != null) {
                    _technicianProfile.value = profile.copy(
                        uid = uid,
                        email = auth.currentUser?.email ?: profile.email
                    )
                    _technicianName.value = profile.name.ifBlank { "Técnico" }
                }
            }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _errorMessage.value = "Ingresa tu correo electrónico"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _resetPasswordSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Error al enviar correo de recuperación"
                }
            }
    }

    fun clearResetPasswordSuccess() {
        _resetPasswordSuccess.value = false
    }

    fun fetchTechnicianProfilePublic() {
        fetchTechnicianProfile()
    }

    fun updateProfile(name: String, phone: String, specialty: String, licenseNumber: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        _isLoading.value = true
        _errorMessage.value = null

        val updates = mapOf(
            "name" to name,
            "phone" to phone,
            "specialty" to specialty,
            "licenseNumber" to licenseNumber
        )

        db.collection("technicians").document(uid)
            .update(updates)
            .addOnSuccessListener {
                _isLoading.value = false
                _technicianName.value = name.ifBlank { "Técnico" }
                _technicianProfile.value = _technicianProfile.value.copy(
                    name = name,
                    phone = phone,
                    specialty = specialty,
                    licenseNumber = licenseNumber
                )
                _profileUpdateSuccess.value = true
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _errorMessage.value = "Error al actualizar perfil: ${e.message}"
            }
    }

    fun updatePassword(currentPassword: String, newPassword: String, onSuccess: () -> Unit) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return

        _isLoading.value = true
        _errorMessage.value = null

        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        _isLoading.value = false
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

    fun clearError() {
        _errorMessage.value = null
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _technicianName.value = "Técnico"
        _technicianProfile.value = TechnicianProfile()
    }
}


