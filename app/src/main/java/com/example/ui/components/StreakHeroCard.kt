package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceCardElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonFlameGlow
import com.example.ui.theme.NeonFlameOrange
import com.example.ui.theme.NeonFlameYellow
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StreakHeroCard(
    currentStreak: Int,
    bestStreak: Int,
    totalDays: Int,
    isStudiedToday: Boolean,
    modifier: Modifier = Modifier
) {
    // Animación de pulso para la llama cuando hay racha activa
    val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (currentStreak > 0) 1.12f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    val flameGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (currentStreak > 0) 0.65f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_glow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(DarkSurfaceCard)
            .border(
                1.5.dp,
                Brush.verticalGradient(
                    colors = if (currentStreak > 0) listOf(
                        NeonFlameOrange.copy(alpha = 0.8f),
                        NeonCyan.copy(alpha = 0.4f),
                        Color(0xFF1E293B)
                    ) else listOf(
                        NeonCyan.copy(alpha = 0.5f),
                        Color(0xFF1E293B)
                    )
                ),
                RoundedCornerShape(28.dp)
            )
            .padding(22.dp)
            .testTag("streak_hero_card")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Emblema de fuego con pulso neón
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        if (currentStreak > 0) NeonFlameOrange.copy(alpha = flameGlowAlpha)
                        else NeonCyan.copy(alpha = 0.15f)
                    )
                    .border(
                        2.dp,
                        if (currentStreak > 0) NeonFlameOrange else NeonCyan,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocalFireDepartment,
                    contentDescription = "Fuego de racha",
                    tint = if (currentStreak > 0) NeonFlameYellow else NeonCyan,
                    modifier = Modifier
                        .size(44.dp)
                        .scale(flameScale)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Contador grande de días consecutivos
            Text(
                text = "$currentStreak",
                fontSize = 80.sp,
                lineHeight = 84.sp,
                fontWeight = FontWeight.Black,
                color = if (currentStreak > 0) NeonFlameYellow else TextPrimary,
                modifier = Modifier.testTag("streak_number_text")
            )

            Text(
                text = if (currentStreak == 1) "DÍA CONSECUTIVO" else "DÍAS CONSECUTIVOS",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = if (currentStreak > 0) NeonFlameOrange else NeonCyan,
                letterSpacing = 2.sp,
                modifier = Modifier.testTag("streak_label_text")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Estado descriptivo de la racha
            val (statusText, statusBg, statusBorder, statusColor) = when {
                isStudiedToday -> Quadruple(
                    "¡RACHA PROTEGIDA HOY! 🔥",
                    NeonLime.copy(alpha = 0.16f),
                    NeonLime,
                    NeonLime
                )
                currentStreak > 0 -> Quadruple(
                    "¡ESTUDIA HOY PARA MANTENERLA! ⚡",
                    NeonFlameOrange.copy(alpha = 0.16f),
                    NeonFlameOrange,
                    NeonFlameYellow
                )
                else -> Quadruple(
                    "¡SUMA HOY TU PRIMER DÍA! 🚀",
                    NeonCyan.copy(alpha = 0.15f),
                    NeonCyan,
                    NeonCyan
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(statusBg)
                    .border(1.dp, statusBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Fila de estadísticas secundarias: Récord histórico y Total estudiado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceCardElevated)
                    .border(1.dp, Color(0xFF1E2A40), RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Récord
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.EmojiEvents,
                        contentDescription = "Récord",
                        tint = NeonFlameYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "$bestStreak días",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Mejor racha",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                        .background(Color(0xFF26334D))
                )

                // Total de días
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.MenuBook,
                        contentDescription = "Total días",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "$totalDays días",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total estudiados",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
