package com.example.shaddai_app_android.ui.technician_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*
import com.example.shaddai_app_android.viewmodel.TechnicianAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianProfileScreen(
    viewModel: TechnicianAuthViewModel,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val profile by viewModel.technicianProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val profileUpdateSuccess by viewModel.profileUpdateSuccess.collectAsState()

    var nombre by remember(profile.name) { mutableStateOf(profile.name) }
    var telefono by remember(profile.phone) { mutableStateOf(profile.phone) }
    var especialidad by remember(profile.specialty) { mutableStateOf(profile.specialty) }
    var licencia by remember(profile.licenseNumber) { mutableStateOf(profile.licenseNumber) }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    val hasChanges = nombre != profile.name || telefono != profile.phone
            || especialidad != profile.specialty || licencia != profile.licenseNumber

    LaunchedEffect(profileUpdateSuccess) {
        if (profileUpdateSuccess) {
            showSuccessSnackbar = true
            viewModel.clearProfileUpdateSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchTechnicianProfilePublic()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSuccessSnackbar) {
        if (showSuccessSnackbar) {
            snackbarHostState.showSnackbar(
                message = successMessage.ifBlank { "Perfil actualizado correctamente" },
                duration = SnackbarDuration.Short
            )
            showSuccessSnackbar = false
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = errorMessage ?: "",
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TechnicianTopBar(
                technicianName = profile.name.ifBlank { "Técnico" },
                onNavigate = onNavigate,
                onLogout = onLogout
            )
        },
        bottomBar = {
            TechnicianBottomBar(currentRoute = "tech_perfil", onNavigate = onNavigate)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Engineering,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = profile.name.ifBlank { "Técnico" },
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextPrimary
            )

            Text(
                text = profile.email,
                fontFamily = ManropeFontFamily,
                fontSize = 14.sp,
                color = TextSecondary
            )

            // Badge de especialidad
            if (profile.specialty.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(PrimaryBlue.copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = profile.specialty,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = PrimaryBlue
                    )
                }
            }

            // Estadísticas rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Default.CheckCircle,
                    value = "${profile.completedServices}",
                    label = "Servicios",
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Star,
                    value = if (profile.rating > 0) String.format("%.1f", profile.rating) else "—",
                    label = "Calificación",
                    color = Color(0xFFF6AD55),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Circle,
                    value = if (profile.isActive) "Activo" else "Inactivo",
                    label = "Estado",
                    color = if (profile.isActive) SuccessGreen else ErrorColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // === Datos personales ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Datos personales",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    HorizontalDivider(color = BorderColor)

                    TechProfileTextField("Nombre", nombre, { nombre = it }, Icons.Default.Person, true)
                    TechProfileTextField("Correo electrónico", profile.email, {}, Icons.Default.Email, false)
                    TechProfileTextField(
                        "Teléfono", telefono,
                        { if (it.all { c -> c.isDigit() } && it.length <= 10) telefono = it },
                        Icons.Default.Phone, true
                    )
                }
            }

            // === Datos profesionales ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Datos profesionales",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    HorizontalDivider(color = BorderColor)

                    TechProfileTextField("Especialidad", especialidad, { especialidad = it }, Icons.Default.Build, true)
                    TechProfileTextField("Número de licencia", licencia, { licencia = it }, Icons.Default.Badge, true)
                }
            }

            // Botón guardar cambios
            Button(
                onClick = {
                    viewModel.updateProfile(nombre, telefono, especialidad, licencia) {
                        successMessage = "Datos actualizados correctamente"
                        showSuccessSnackbar = true
                    }
                },
                enabled = hasChanges && !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = PrimaryBlue.copy(alpha = 0.4f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    if (isLoading) "Guardando..." else "Guardar cambios",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            // === Seguridad ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Seguridad",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    HorizontalDivider(color = BorderColor)

                    OutlinedButton(
                        onClick = { showPasswordDialog = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                    ) {
                        Icon(Icons.Default.Lock, null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cambiar contraseña", fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = PrimaryBlue)
                    }
                }
            }

            // === Cerrar sesión ===
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
            ) {
                Icon(Icons.Default.ExitToApp, null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    // === Diálogo cambiar contraseña ===
    if (showPasswordDialog) {
        TechChangePasswordDialog(
            isLoading = isLoading,
            onDismiss = { showPasswordDialog = false },
            onConfirm = { currentPass, newPass ->
                viewModel.updatePassword(currentPass, newPass) {
                    showPasswordDialog = false
                    successMessage = "Contraseña actualizada correctamente"
                    showSuccessSnackbar = true
                }
            }
        )
    }
}

// ── Stat card ──
@Composable
private fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            Text(value, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
            Text(label, fontFamily = ManropeFontFamily, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

// ── Text field ──
@Composable
private fun TechProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    enabled: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = TextSecondary, letterSpacing = 0.5.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            leadingIcon = {
                Icon(icon, null, tint = if (enabled) PrimaryBlue else TextHint, modifier = Modifier.size(20.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderColor,
                disabledBorderColor = BorderColor.copy(alpha = 0.5f),
                disabledTextColor = TextSecondary,
                disabledLeadingIconColor = TextHint,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = CardBackground
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = ManropeFontFamily,
                fontSize = 15.sp,
                color = if (enabled) TextPrimary else TextSecondary
            )
        )
    }
}

// ── Change password dialog ──
@Composable
private fun TechChangePasswordDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (currentPassword: String, newPassword: String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfaceWhite,
        title = {
            Text("Cambiar contraseña", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it; error = null },
                    label = { Text("Contraseña actual", fontFamily = ManropeFontFamily, fontSize = 13.sp) },
                    singleLine = true,
                    visualTransformation = if (showCurrent) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showCurrent = !showCurrent }) {
                            Icon(if (showCurrent) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, modifier = Modifier.size(20.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderColor)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it; error = null },
                    label = { Text("Nueva contraseña", fontFamily = ManropeFontFamily, fontSize = 13.sp) },
                    singleLine = true,
                    visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showNew = !showNew }) {
                            Icon(if (showNew) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, modifier = Modifier.size(20.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderColor)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; error = null },
                    label = { Text("Confirmar contraseña", fontFamily = ManropeFontFamily, fontSize = 13.sp) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderColor)
                )
                if (error != null) {
                    Text(error!!, fontFamily = ManropeFontFamily, fontSize = 12.sp, color = ErrorColor)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() -> error = "Ingresa tu contraseña actual"
                        newPassword.length < 6 -> error = "Mínimo 6 caracteres"
                        newPassword != confirmPassword -> error = "Las contraseñas no coinciden"
                        else -> onConfirm(currentPassword, newPassword)
                    }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Text("Cambiar", fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancelar", fontFamily = ManropeFontFamily, color = TextSecondary)
            }
        }
    )
}

