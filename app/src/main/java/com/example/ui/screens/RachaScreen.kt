package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HoySiEstudieButton
import com.example.ui.components.Last7DaysRow
import com.example.ui.components.MotivationalCard
import com.example.ui.components.StreakHeroCard
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFlameOrange
import com.example.ui.theme.NeonFlameYellow
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StreakViewModel

@Composable
fun RachaScreen(
    viewModel: StreakViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedDayToToggle by remember { mutableStateOf<String?>(null) }

    // Feedback banner temporal
    LaunchedEffect(uiState.infoMessage) {
        if (uiState.infoMessage != null) {
            kotlinx.coroutines.delay(3500)
            viewModel.dismissInfoMessage()
        }
    }

    Scaffold(
        containerColor = DarkBg,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header superior: Título de la app + indicador offline
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(NeonFlameOrange.copy(alpha = 0.2f))
                                .border(1.dp, NeonFlameOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = "Racha",
                                tint = NeonFlameYellow,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "RACHA",
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "HÁBITO DE ESTUDIO DIARIO",
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Botón de ayuda/info
                    IconButton(
                        onClick = { showInfoDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceCard)
                            .border(1.dp, Color(0xFF1E293B), CircleShape)
                            .testTag("info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = "Información",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Tarjeta Principal: Contador gigante de racha consecutiva
                StreakHeroCard(
                    currentStreak = uiState.currentStreak,
                    bestStreak = uiState.bestStreak,
                    totalDays = uiState.totalDaysStudied,
                    isStudiedToday = uiState.isStudiedToday
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón principal: «Hoy sí estudié»
                HoySiEstudieButton(
                    isStudiedToday = uiState.isStudiedToday,
                    onPrimaryClick = {
                        viewModel.toggleToday()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sección: Lista de los últimos 7 días
                Last7DaysRow(
                    days = uiState.last7Days,
                    onDayClick = { dateString ->
                        selectedDayToToggle = dateString
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Tarjeta: Mensaje motivador distinto cada vez
                MotivationalCard(
                    quote = uiState.motivationalQuote,
                    onRefreshQuote = {
                        viewModel.nextMotivationalQuote()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Banner flotante de feedback/notificación
            AnimatedVisibility(
                visible = uiState.infoMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                uiState.infoMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = msg,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.dismissInfoMessage() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Cerrar",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para alternar un día seleccionado
    selectedDayToToggle?.let { dateStr ->
        val isToday = (dateStr == com.example.util.DateUtils.getTodayDateString())
        val dayItem = uiState.last7Days.find { it.dateString == dateStr }
        val isStudied = dayItem?.isStudied == true

        AlertDialog(
            onDismissRequest = { selectedDayToToggle = null },
            containerColor = DarkSurfaceCard,
            title = {
                Text(
                    text = if (isToday) "Día de Hoy" else "Día ${dayItem?.dayOfWeek} (${dayItem?.dayAndMonth ?: dateStr})",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (isStudied) {
                        "¿Deseas desmarcar este día como no estudiado?"
                    } else {
                        "¿Deseas marcar este día como estudiado para incluirlo en tu racha?"
                    },
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.toggleDay(dateStr)
                        selectedDayToToggle = null
                    }
                ) {
                    Text(
                        text = if (isStudied) "Desmarcar" else "Marcar como estudiado",
                        color = if (isStudied) NeonFlameOrange else NeonLime,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDayToToggle = null }) {
                    Text(text = "Cancelar", color = TextMuted)
                }
            }
        )
    }

    // Diálogo de información sobre la app RACHA
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            containerColor = DarkSurfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acerca de RACHA",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "• 100% offline: tus datos quedan guardados únicamente en la base de datos de tu teléfono.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Sin publicidad, sin registro de usuarios, sin distracciones.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• El contador registra los días consecutivos que has estudiado.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Toca el botón «Hoy sí estudié» cada día que cumplas con tu sesión de estudio.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text(text = "Entendido", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
