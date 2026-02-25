package com.example.shaddai_app_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app_android.model.CitaData
import com.example.shaddai_app_android.ui.components.ShaddaiBottomBar
import com.example.shaddai_app_android.ui.components.ShaddaiTopBar
import com.example.shaddai_app_android.ui.components.StepProgressBar
import com.example.shaddai_app_android.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun FechaHoraScreen(
    citaData: CitaData,
    onAtras: () -> Unit,
    onContinuar: (CitaData) -> Unit
) {
    val hoy = remember { LocalDate.now() }
    val maxFecha = remember { hoy.plusDays(7) }

    // Calcular semana actual (los 7 días disponibles)
    var semanaOffset by remember { mutableStateOf(0) }
    val diasDisponibles = remember(semanaOffset) {
        (0..6).map { hoy.plusDays(it.toLong() + semanaOffset * 7) }
            .filter { !it.isAfter(maxFecha) }
    }

    var diaSeleccionado by remember { mutableStateOf<LocalDate?>(
        if (citaData.fecha.isNotBlank())
            try { LocalDate.parse(citaData.fecha) } catch (e: Exception) { null }
        else null
    ) }

    val horasDisponibles = remember {
        (9..17).map { hora ->
            "%02d:00".format(hora)
        } + listOf("18:00")
    }

    var horaSeleccionada by remember { mutableStateOf(
        if (citaData.hora.isNotBlank()) citaData.hora else ""
    ) }

    var intentoContinuar by remember { mutableStateOf(false) }
    val diaError = intentoContinuar && diaSeleccionado == null
    val horaError = intentoContinuar && horaSeleccionada.isBlank()

    val formatoMesAno = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es-MX"))
    val mesActual = diasDisponibles.firstOrNull()?.format(formatoMesAno)?.replaceFirstChar { it.uppercase() }
        ?: hoy.format(formatoMesAno).replaceFirstChar { it.uppercase() }

    Scaffold(
        topBar = { ShaddaiTopBar() },
        bottomBar = { ShaddaiBottomBar() },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            StepProgressBar(currentStep = 3)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fecha y Hora de la Cita",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Selecciona el día y horario que más te convenga.\nDisponible dentro de los próximos 7 días.",
                        fontFamily = ManropeFontFamily,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                // Calendario semanal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, if (diaError) ErrorColor else BorderColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Encabezado mes + navegación
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (semanaOffset > 0) semanaOffset-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = "Semana anterior",
                                    tint = if (semanaOffset > 0) PrimaryBlue else TextHint
                                )
                            }
                            Text(
                                text = mesActual ?: "",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            IconButton(
                                onClick = {
                                    val siguienteSemana = hoy.plusDays(((semanaOffset + 1) * 7).toLong())
                                    if (!siguienteSemana.isAfter(maxFecha)) semanaOffset++
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Siguiente semana",
                                    tint = if (hoy.plusDays(((semanaOffset + 1) * 7).toLong()).isAfter(maxFecha))
                                        TextHint else PrimaryBlue
                                )
                            }
                        }

                        // Días de la semana (encabezados)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach { dia ->
                                Text(
                                    text = dia,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontFamily = ManropeFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Grilla de días
                        val primerDiaSemana = diasDisponibles.firstOrNull() ?: hoy
                        val offsetInicial = primerDiaSemana.dayOfWeek.value % 7

                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Espacios vacíos antes del primer día
                            repeat(offsetInicial) {
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            diasDisponibles.forEachIndexed { idx, dia ->
                                val posicion = (offsetInicial + idx) % 7
                                val isSelected = diaSeleccionado == dia
                                val esDomingo = dia.dayOfWeek == DayOfWeek.SUNDAY
                                val esSabado = dia.dayOfWeek == DayOfWeek.SATURDAY

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isSelected -> SelectedDayBackground
                                                else -> Color.Transparent
                                            }
                                        )
                                        .clickable(enabled = !esDomingo) {
                                            diaSeleccionado = dia
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dia.dayOfMonth.toString(),
                                        fontFamily = ManropeFontFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = when {
                                            isSelected -> Color.White
                                            esDomingo -> TextHint
                                            else -> TextPrimary
                                        }
                                    )
                                }

                                // Salto de fila al llegar al sábado (posición 6)
                                if (posicion == 6 && idx < diasDisponibles.size - 1) {
                                    // fin de fila – en Row normal no se puede, así que dejamos la grilla lineal
                                }
                            }

                            // Relleno al final si hay espacio
                            val totalCeldas = offsetInicial + diasDisponibles.size
                            val restantes = (7 - (totalCeldas % 7)) % 7
                            repeat(restantes) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        if (diaSeleccionado != null) {
                            val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("es-MX"))
                            Text(
                                text = "Seleccionado: ${diaSeleccionado!!.format(formatter).replaceFirstChar { it.uppercase() }}",
                                fontFamily = ManropeFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }

                if (diaError) {
                    Text(
                        text = "Selecciona un día para tu cita",
                        fontFamily = ManropeFontFamily,
                        fontSize = 12.sp,
                        color = ErrorColor
                    )
                }

                // Selección de hora
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, if (horaError) ErrorColor else BorderColor, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Horario disponible",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                        }

                        // Grid de horas 3 columnas
                        val columnas = 3
                        horasDisponibles.chunked(columnas).forEach { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                fila.forEach { hora ->
                                    val isSelected = horaSeleccionada == hora
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) PrimaryBlue else BackgroundColor)
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) PrimaryBlue else BorderColor,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { horaSeleccionada = hora },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = hora,
                                            fontFamily = ManropeFontFamily,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp,
                                            color = if (isSelected) Color.White else TextPrimary
                                        )
                                    }
                                }
                                // Relleno si la fila no está completa
                                repeat(columnas - fila.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                if (horaError) {
                    Text(
                        text = "Selecciona una hora para tu cita",
                        fontFamily = ManropeFontFamily,
                        fontSize = 12.sp,
                        color = ErrorColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onAtras,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = 1.5.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Text(
                            text = "Atrás",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                    Button(
                        onClick = {
                            intentoContinuar = true
                            if (diaSeleccionado != null && horaSeleccionada.isNotBlank()) {
                                onContinuar(
                                    citaData.copy(
                                        fecha = diaSeleccionado.toString(),
                                        hora = horaSeleccionada
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text(
                            text = "Continuar",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}





