package com.example.shaddai_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.ui.screens.*
import com.example.shaddai_app_android.ui.theme.ShaddaiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShaddaiTheme {
                ShaddaiNavHost()
            }
        }
    }
}

@Composable
fun ShaddaiNavHost() {
    val navController = rememberNavController()
    var citaData by remember { mutableStateOf(CitaData()) }

    NavHost(
        navController = navController,
        startDestination = "descripcion"
    ) {
        composable("descripcion") {
            DescripcionScreen(
                citaData = citaData,
                onContinuar = { updated ->
                    citaData = updated
                    navController.navigate("direccion")
                }
            )
        }
        composable("direccion") {
            DireccionScreen(
                citaData = citaData,
                onAtras = { navController.popBackStack() },
                onContinuar = { updated ->
                    citaData = updated
                    navController.navigate("fechahora")
                }
            )
        }
        composable("fechahora") {
            FechaHoraScreen(
                citaData = citaData,
                onAtras = { navController.popBackStack() },
                onContinuar = { updated ->
                    citaData = updated
                    navController.navigate("confirmacion")
                }
            )
        }
        composable("confirmacion") {
            ConfirmacionScreen(
                citaData = citaData,
                onVolverAlInicio = {
                    citaData = CitaData()
                    navController.popBackStack("descripcion", inclusive = false)
                }
            )
        }
    }
}