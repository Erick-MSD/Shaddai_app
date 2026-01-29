package com.example.shaddai_app.ui.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app.data.model.EventoComida
import com.example.shaddai_app.data.model.EventoServicio
import com.example.shaddai_app.data.model.TipoServicio
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.plus

/**
 * Pantalla principal del calendario para técnicos
 */
@Composable
fun CalendarioScreen(
    viewModel: CalendarioViewModel,
    onNavigateHome: () -> Unit = {},
    onNavigateHerramientas: () -> Unit = {},
    onNavigateSoporte: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    CalendarioContent(
        state = uiState,
        onEvent = viewModel::onEvent,
        onNavigateHome = onNavigateHome,
        onNavigateHerramientas = onNavigateHerramientas,
        onNavigateSoporte = onNavigateSoporte
    )
}

@Composable
private fun CalendarioContent(
    state: CalendarioState,
    onEvent: (CalendarioEvent) -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateHerramientas: () -> Unit,
    onNavigateSoporte: () -> Unit
) {
    Scaffold(
        topBar = {
            CalendarioTopBar(
                nombreTecnico = state.tecnicoActual?.nombre ?: "Técnico"
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 2,
                onNavigateHome = onNavigateHome,
                onNavigateHerramientas = onNavigateHerramientas,
                onNavigateCalendario = { },
                onNavigateSoporte = onNavigateSoporte
            )
        },
        containerColor = Color(0xFFF0F9FF)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MesSelectorRow(
                fechaSeleccionada = state.fechaSeleccionada,
                onRetroceder = { onEvent(CalendarioEvent.RetrocederDia) },
                onAvanzar = { onEvent(CalendarioEvent.AvanzarDia) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SemanaSelectorRow(
                fechaSeleccionada = state.fechaSeleccionada,
                onDiaSeleccionado = { fecha ->
                    onEvent(CalendarioEvent.CambiarFecha(fecha))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                EventosDelDia(
                    eventos = state.eventos,
                    onEventoClick = { evento ->
                        onEvent(CalendarioEvent.SeleccionarEvento(evento))
                    }
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun CalendarioTopBar(nombreTecnico: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color(0xFF1565C0),
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Hola, $nombreTecnico",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun MesSelectorRow(
    fechaSeleccionada: LocalDate,
    onRetroceder: () -> Unit,
    onAvanzar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRetroceder) {
            Text("<", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "${obtenerNombreMes(fechaSeleccionada.month)} ${fechaSeleccionada.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        IconButton(onClick = onAvanzar) {
            Text(">", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SemanaSelectorRow(
    fechaSeleccionada: LocalDate,
    onDiaSeleccionado: (LocalDate) -> Unit
) {
    val inicioSemana = obtenerInicioSemana(fechaSeleccionada)
    val diasSemana = (0..6).map { offset ->
        inicioSemana.plus(offset, DateTimeUnit.DAY)
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(diasSemana) { fecha ->
            DiaCard(
                fecha = fecha,
                esSeleccionado = fecha == fechaSeleccionada,
                onClick = { onDiaSeleccionado(fecha) }
            )
        }
    }
}

@Composable
private fun DiaCard(
    fecha: LocalDate,
    esSeleccionado: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (esSeleccionado) Color(0xFF1E88E5) else Color.White
    val textColor = if (esSeleccionado) Color.White else Color.Black

    Column(
        modifier = Modifier
            .width(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (esSeleccionado) 0.dp else 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = obtenerNombreDiaSemana(fecha),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = fecha.dayOfMonth.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun EventosDelDia(
    eventos: List<EventoServicio>,
    onEventoClick: (EventoServicio) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(eventos) { evento ->
            EventoCard(
                evento = evento,
                onClick = { onEventoClick(evento) }
            )
        }

        if (eventos.isNotEmpty()) {
            item {
                HoraComidaCard(
                    eventoComida = EventoComida(
                        horaInicio = LocalTime(13, 0),
                        horaFin = LocalTime(14, 0)
                    )
                )
            }
        }
    }
}

@Composable
private fun EventoCard(
    evento: EventoServicio,
    onClick: () -> Unit
) {
    val backgroundColor = Color(evento.tipoServicio.colorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = evento.obtenerRangoTiempo().split(" - ")[0],
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = evento.obtenerRangoTiempo().split(" - ")[1],
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = evento.subservicio,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = evento.tipoServicio.displayName,
                        fontSize = 12.sp,
                        color = Color(evento.tipoServicio.colorHex),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconoTipoServicio(tipo = evento.tipoServicio)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = evento.ubicacionDireccion,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏱️ ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = evento.obtenerDuracionFormateada(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun HoraComidaCard(eventoComida: EventoComida) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "☕",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hora de comida",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = eventoComida.obtenerRangoTiempo(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun IconoTipoServicio(tipo: TipoServicio) {
    val icono = when (tipo) {
        TipoServicio.PLOMERIA -> "💧"
        TipoServicio.ELECTRICIDAD -> "⚡"
        TipoServicio.AIRE_ACONDICIONADO -> "❄️"
        TipoServicio.REDES -> "🌐"
        TipoServicio.CCTV -> "📹"
        TipoServicio.CARPINTERIA -> "🔨"
        TipoServicio.PINTURA -> "🎨"
        TipoServicio.JARDINERIA -> "🌱"
        TipoServicio.ALBANILERIA -> "🧱"
        TipoServicio.CERRAJERIA -> "🔑"
        TipoServicio.LIMPIEZA_ESPECIALIZADA -> "🧹"
        TipoServicio.TI_SOPORTE -> "💻"
        else -> "🔧"
    }

    Text(text = icono, fontSize = 16.sp)
}

/**
 * Bottom Navigation usando los MISMOS iconos de Material Design que GlobalBottomBar
 */
@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onNavigateHome: () -> Unit,
    onNavigateHerramientas: () -> Unit,
    onNavigateCalendario: () -> Unit,
    onNavigateSoporte: () -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        containerColor = Color(0xFFD7F4F5),
    ) {
        val activeColor = Color(0xFF0E88E6)
        val inactiveColor = Color(0xFFA9A9A9)

        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = onNavigateHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = onNavigateHerramientas,
            icon = { Icon(Icons.Default.Build, contentDescription = "Servicios") },
            label = { Text("Servicios") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = onNavigateCalendario,
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Calendario") },
            label = { Text("Calendario") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = onNavigateSoporte,
            icon = { Icon(Icons.Default.Headset, contentDescription = "Soporte") },
            label = { Text("Soporte") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor,
                selectedTextColor = activeColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
    }
}

private fun obtenerNombreMes(month: Month): String {
    return when (month) {
        Month.JANUARY -> "ENERO"
        Month.FEBRUARY -> "FEBRERO"
        Month.MARCH -> "MARZO"
        Month.APRIL -> "ABRIL"
        Month.MAY -> "MAYO"
        Month.JUNE -> "JUNIO"
        Month.JULY -> "JULIO"
        Month.AUGUST -> "AGOSTO"
        Month.SEPTEMBER -> "SEPTIEMBRE"
        Month.OCTOBER -> "OCTUBRE"
        Month.NOVEMBER -> "NOVIEMBRE"
        Month.DECEMBER -> "DICIEMBRE"
    }
}

private fun obtenerNombreDiaSemana(fecha: LocalDate): String {
    val diaSemana = fecha.dayOfWeek.ordinal + 1
    return when (diaSemana) {
        1 -> "LUN"
        2 -> "MAR"
        3 -> "MIE"
        4 -> "JUE"
        5 -> "VIE"
        6 -> "SAB"
        7 -> "DOM"
        else -> ""
    }
}

private fun obtenerInicioSemana(fecha: LocalDate): LocalDate {
    val diaSemana = fecha.dayOfWeek.ordinal + 1
    val diasHastaLunes = diaSemana - 1
    return fecha.plus(DateTimeUnit.DAY * -diasHastaLunes)
}
