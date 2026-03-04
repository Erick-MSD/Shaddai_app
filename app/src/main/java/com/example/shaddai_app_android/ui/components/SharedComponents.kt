package com.example.shaddai_app_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShaddaiTopBar(
    title: String = "Hola, Cliente",
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    containerColor = SurfaceWhite
                ) {
                    DropdownMenuItem(
                        text = { Text("Mi Perfil", fontFamily = ManropeFontFamily, fontSize = 14.sp) },
                        onClick = { menuExpanded = false; onNavigate("perfil") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue) }
                    )
                    DropdownMenuItem(
                        text = { Text("Mis Citas", fontFamily = ManropeFontFamily, fontSize = 14.sp) },
                        onClick = { menuExpanded = false; onNavigate("citas") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryBlue) }
                    )
                    DropdownMenuItem(
                        text = { Text("Notificaciones", fontFamily = ManropeFontFamily, fontSize = 14.sp) },
                        onClick = { menuExpanded = false; onNavigate("notificaciones") },
                        leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryBlue) }
                    )
                    DropdownMenuItem(
                        text = { Text("Ayuda", fontFamily = ManropeFontFamily, fontSize = 14.sp) },
                        onClick = { menuExpanded = false; onNavigate("ayuda") },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = PrimaryBlue) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión", fontFamily = ManropeFontFamily, fontSize = 14.sp, color = ErrorColor) },
                        onClick = { menuExpanded = false; onLogout() },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = ErrorColor) }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryBlue,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun ShaddaiBottomBar(
    currentRoute: String = "",
    onNavigate: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(0.5.dp, Color.LightGray)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            IconButton(onClick = { onNavigate("home") }) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .then(
                            if (currentRoute == "home") Modifier.border(2.dp, TextPrimary, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (currentRoute == "home") Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Inicio",
                        tint = TextPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Add
            IconButton(onClick = { onNavigate("nueva_cita") }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva Cita",
                    tint = TextPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Citas
            IconButton(onClick = { onNavigate("citas") }) {
                Icon(
                    imageVector = Icons.Default.CalendarViewDay,
                    contentDescription = "Mis Citas",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Profile
            IconButton(onClick = { onNavigate("perfil") }) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Perfil",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun StepProgressBar(currentStep: Int, totalSteps: Int = 4) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isActive = index < currentStep
            val isCurrent = index == currentStep - 1
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        when {
                            isActive -> PrimaryBlue
                            isCurrent -> PrimaryBlue
                            else -> BorderColor
                        }
                    )
            )
        }
    }
}

