package com.example.shaddai_app.ui.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Pantalla principal del calendario para técnicos.
 * Esta pantalla ahora solo se encarga de mostrar el contenido, no el Scaffold.
 */
@Composable
fun CalendarioScreen(viewModel: CalendarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp) // Añadimos un padding superior para que no se pegue a la TopBar
    ) {
        MesSelectorRow(
            fechaSeleccionada = uiState.fechaSeleccionada,
            onRetroceder = { viewModel.onEvent(CalendarioEvent.RetrocederDia) },
            onAvanzar = { viewModel.onEvent(CalendarioEvent.AvanzarDia) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SemanaSelectorRow(
            fechaSeleccionada = uiState.fechaSeleccionada,
            onDiaSeleccionado = { fecha ->
                viewModel.onEvent(CalendarioEvent.CambiarFecha(fecha))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            EventosDelDia(
                eventos = uiState.eventos,
                onEventoClick = { evento ->
                    viewModel.onEvent(CalendarioEvent.SeleccionarEvento(evento))
                }
            )
        }

        uiState.error?.let { errorMsg ->
            Text(
                text = errorMsg,
                color = Color.Red, // Corregido: Se usa un color explícito para evitar ambigüedad
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// --- Se han eliminado los componentes CalendarioContent, CalendarioTopBar y BottomNavigationBar ---
// --- ya que la UI global se gestiona desde MainScreen.kt ---

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

                Row(verticalAlignment = Alignment.CenterVertically) {
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📍 ", fontSize = 12.sp)
                    Text(
                        text = evento.ubicacionDireccion,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⏱️ ", fontSize = 12.sp)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "☕", fontSize = 24.sp)
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
    return when (fecha.dayOfWeek) {
        DayOfWeek.MONDAY -> "LUN"
        DayOfWeek.TUESDAY -> "MAR"
        DayOfWeek.WEDNESDAY -> "MIE"
        DayOfWeek.THURSDAY -> "JUE"
        DayOfWeek.FRIDAY -> "VIE"
        DayOfWeek.SATURDAY -> "SAB"
        DayOfWeek.SUNDAY -> "DOM"
    }
}

private fun obtenerInicioSemana(fecha: LocalDate): LocalDate {
    val diasARestar = fecha.dayOfWeek.ordinal
    return fecha.minus(diasARestar, DateTimeUnit.DAY)
}
