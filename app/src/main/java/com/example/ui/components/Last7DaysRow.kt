package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonLimeGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.DayItem

@Composable
fun Last7DaysRow(
    days: List<DayItem>,
    onDayClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = days.count { it.isStudied }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceCard)
            .border(1.dp, Color(0xFF1F293D), RoundedCornerShape(20.dp))
            .padding(16.dp)
            .testTag("last_7_days_container")
    ) {
        // Encabezado de la sección
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ÚLTIMOS 7 DÍAS",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Toca un día para editar si olvidaste marcarlo",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            // Badge de progreso semanal
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (completedCount >= 5) NeonLime.copy(alpha = 0.15f)
                        else NeonCyan.copy(alpha = 0.12f)
                    )
                    .border(
                        1.dp,
                        if (completedCount >= 5) NeonLime else NeonCyan,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$completedCount/7 DÍAS",
                    color = if (completedCount >= 5) NeonLime else NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila de 7 días
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEach { day ->
                DayBubble(
                    day = day,
                    onClick = { onDayClick(day.dateString) }
                )
            }
        }
    }
}

@Composable
private fun DayBubble(
    day: DayItem,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            day.isStudied -> NeonLime
            day.isToday -> NeonCyan
            else -> Color(0xFF26334D)
        },
        animationSpec = tween(300),
        label = "bubble_border"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            day.isStudied -> NeonLimeGlow
            day.isToday -> Color(0xFF0F2338)
            else -> Color(0xFF0B101B)
        },
        animationSpec = tween(300),
        label = "bubble_bg"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("day_item_${day.dateString}")
    ) {
        // Nombre del día (LUN, MAR, etc.)
        Text(
            text = day.dayOfWeek,
            color = if (day.isToday) NeonCyan else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (day.isToday) FontWeight.Black else FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Círculo interactivo del día
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(
                    width = if (day.isToday) 2.dp else 1.5.dp,
                    color = borderColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (day.isStudied) {
                // Indicador neón completado
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(NeonLime, Color(0xFF00B359))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Estudiado",
                        tint = DarkBg,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                // Número del día pendiente
                Text(
                    text = day.dayNumber,
                    color = if (day.isToday) NeonCyan else TextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Etiqueta HOY o fecha
        if (day.isToday) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonCyan)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "HOY",
                    color = DarkBg,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
        } else {
            Text(
                text = day.dayNumber,
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
