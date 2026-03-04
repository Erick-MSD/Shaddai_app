package com.example.shaddai_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.ui.screens.*
import com.example.shaddai_app_android.ui.screens.AyudaScreen
import com.example.shaddai_app_android.ui.theme.ShaddaiTheme
import com.example.shaddai_app_android.viewmodel.AuthViewModel
import com.example.shaddai_app_android.viewmodel.CitaViewModel

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

    // Estado temporal para el flujo de nueva cita
    var citaEnProceso by remember { mutableStateOf(CitaData()) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
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
                onVolverAlInicio = {
                    citaEnProceso = CitaData() // Reset
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
    }
}
