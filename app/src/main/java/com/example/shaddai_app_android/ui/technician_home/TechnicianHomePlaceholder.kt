package com.example.shaddai_app_android.ui.technician_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.technician_login.TechLoginColors
import com.example.shaddai_app_android.ui.theme.ManropeFontFamily

/**
 * Pantalla principal del técnico con integración de navegación.
 * Envuelve el TechnicianHomeScreen existente con controles de logout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianHomePlaceholder(
    technicianName: String,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(TechLoginColors.Accent, TechLoginColors.AccentLight)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Hola, $technicianName",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TechLoginColors.TextPrimary,
                                fontFamily = ManropeFontFamily
                            )
                            Text(
                                text = "Panel de Técnico",
                                fontSize = 12.sp,
                                color = TechLoginColors.TextSecondary,
                                fontFamily = ManropeFontFamily
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = TechLoginColors.TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TechLoginColors.BgGradientStart
                )
            )
        },
        containerColor = TechLoginColors.BgGradientEnd
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card informativa
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TechLoginColors.Surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = TechLoginColors.Accent,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Bienvenido al Panel!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TechLoginColors.TextPrimary,
                        fontFamily = ManropeFontFamily
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Has iniciado sesión correctamente como técnico. Aquí verás tus servicios asignados y próximas citas.",
                        fontSize = 14.sp,
                        color = TechLoginColors.TextSecondary,
                        fontFamily = ManropeFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de cerrar sesión
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TechLoginColors.ErrorText
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = ManropeFontFamily
                )
            }
        }
    }
}




