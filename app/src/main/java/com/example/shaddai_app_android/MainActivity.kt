package com.example.shaddai_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.ui.screens.*
import com.example.shaddai_app_android.ui.screens.AyudaScreen
import com.example.shaddai_app_android.ui.technician_home.TechnicianBottomBar
import com.example.shaddai_app_android.ui.technician_home.TechnicianServicesScreen
import com.example.shaddai_app_android.ui.technician_home.TechnicianServicesViewModel
import com.example.shaddai_app_android.ui.technician_home.TechnicianTopBar
import com.example.shaddai_app_android.ui.theme.BackgroundColor
import com.example.shaddai_app_android.ui.technician_login.TechnicianForgotPasswordScreen
import com.example.shaddai_app_android.ui.technician_login.TechnicianLoginScreen
import com.example.shaddai_app_android.ui.technician_login.TechnicianRegisterScreen
import com.example.shaddai_app_android.ui.theme.ShaddaiTheme
import com.example.shaddai_app_android.viewmodel.AuthViewModel
import com.example.shaddai_app_android.viewmodel.CitaViewModel
import com.example.shaddai_app_android.viewmodel.TechnicianAuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShaddaiTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val citaViewModel: CitaViewModel = viewModel()
    val techAuthViewModel: TechnicianAuthViewModel = viewModel()

    // Estado temporal para el flujo de nueva cita
    var citaEnProceso by remember { mutableStateOf(CitaData()) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    citaViewModel.refrescarCitas()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                onTechnicianLoginClick = {
                    navController.navigate("tech_login")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                citaViewModel = citaViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("nueva_cita") {
            DescripcionScreen(
                citaData = citaEnProceso,
                onContinuar = { 
                    citaEnProceso = it
                    navController.navigate("direccion") 
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("direccion") {
            DireccionScreen(
                citaData = citaEnProceso,
                onAtras = { navController.popBackStack() },
                onContinuar = { 
                    citaEnProceso = it
                    navController.navigate("fecha_hora") 
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("fecha_hora") {
            FechaHoraScreen(
                citaData = citaEnProceso,
                onAtras = { navController.popBackStack() },
                onContinuar = { 
                    citaEnProceso = it
                    navController.navigate("confirmacion") 
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("confirmacion") {
            ConfirmacionScreen(
                citaData = citaEnProceso,
                citaViewModel = citaViewModel,
                onAtras = { navController.popBackStack() },
                onVolverAlInicio = {
                    citaEnProceso = CitaData() // Reset
                    citaViewModel.refrescarCitas()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("citas") {
            CitasScreen(
                citaViewModel = citaViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("perfil") {
            PerfilScreen(
                authViewModel = authViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("notificaciones") {
            NotificacionesScreen(
                citaViewModel = citaViewModel,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("ayuda") {
            AyudaScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route: String -> navController.navigate(route) }
            )
        }
        composable(
            route = "detalle_cita/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            DetalleCitaScreen(
                citaId = citaId,
                citaViewModel = citaViewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // ==========================================
        // FLUJO DE TÉCNICO - Login, Registro, Recuperación
        // ==========================================
        composable("tech_login") {
            TechnicianLoginScreen(
                viewModel = techAuthViewModel,
                onLoginSuccess = {
                    navController.navigate("tech_home") {
                        popUpTo("tech_login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("tech_register")
                },
                onForgotPasswordClick = {
                    navController.navigate("tech_forgot_password")
                },
                onBackToClientLogin = {
                    navController.navigate("login") {
                        popUpTo("tech_login") { inclusive = true }
                    }
                }
            )
        }

        composable("tech_register") {
            TechnicianRegisterScreen(
                viewModel = techAuthViewModel,
                onRegisterSuccess = {
                    techAuthViewModel.logout()
                    navController.navigate("tech_login") {
                        popUpTo("tech_register") { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("tech_forgot_password") {
            TechnicianForgotPasswordScreen(
                viewModel = techAuthViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("tech_home") {
            val techUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val techName = techAuthViewModel.technicianName.collectAsState().value
            val homeViewModel = remember(techUid) {
                com.example.shaddai_app_android.ui.technician_home.TechnicianHomeViewModel(
                    technicianUid = techUid,
                    technicianName = techName
                )
            }
            com.example.shaddai_app_android.ui.technician_home.TechnicianHomeScreen(
                viewModel = homeViewModel,
                technicianName = techName,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    techAuthViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("tech_calendario") {
            val techName = techAuthViewModel.technicianName.collectAsState().value
            val calViewModel = remember(techName) {
                com.example.shaddai_app_android.ui.technician_home.TechCalendarioViewModel(technicianName = techName)
            }
            Scaffold(
                topBar = {
                    TechnicianTopBar(
                        technicianName = techName,
                        onNavigate = { route -> navController.navigate(route) },
                        onLogout = {
                            techAuthViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                },
                bottomBar = {
                    TechnicianBottomBar(
                        currentRoute = "tech_calendario",
                        onNavigate = { route -> navController.navigate(route) }
                    )
                },
                containerColor = BackgroundColor
            ) { innerPadding ->
                com.example.shaddai_app_android.ui.technician_home.TechCalendarioScreen(
                    viewModel = calViewModel,
                    onCitaClick = { citaId -> navController.navigate("tech_detalle_cita/$citaId") },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable("tech_servicios") {
            val servicesViewModel: TechnicianServicesViewModel = viewModel()
            val techName = techAuthViewModel.technicianName.collectAsState().value
            Scaffold(
                topBar = {
                    TechnicianTopBar(
                        technicianName = techName,
                        onNavigate = { route -> navController.navigate(route) },
                        onLogout = {
                            techAuthViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                },
                bottomBar = {
                    TechnicianBottomBar(
                        currentRoute = "tech_servicios",
                        onNavigate = { route -> navController.navigate(route) }
                    )
                },
                containerColor = BackgroundColor
            ) { innerPadding ->
                TechnicianServicesScreen(
                    viewModel = servicesViewModel,
                    technicianName = techName,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable("tech_perfil") {
            com.example.shaddai_app_android.ui.technician_home.TechnicianProfileScreen(
                viewModel = techAuthViewModel,
                onLogout = {
                    techAuthViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("tech_ayuda") {
            com.example.shaddai_app_android.ui.technician_home.TechnicianAyudaScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    techAuthViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "tech_detalle_cita/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getString("citaId") ?: ""
            val techName = techAuthViewModel.technicianName.collectAsState().value
            com.example.shaddai_app_android.ui.technician_home.TechnicianServiceDetailScreen(
                citaId = citaId,
                technicianName = techName,
                onBack = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    techAuthViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
