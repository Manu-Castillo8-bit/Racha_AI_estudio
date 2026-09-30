package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonLimeBright
import com.example.ui.theme.TextMuted

@Composable
fun HoySiEstudieButton(
    isStudiedToday: Boolean,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    val infiniteTransition = rememberInfiniteTransition(label = "btn_glow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse"
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (!isStudiedToday) glowPulse else 1.0f,
        label = "button_scale"
    )

    val containerBrush = if (isStudiedToday) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF0F261B),
                Color(0xFF132F20)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                NeonLimeBright,
                NeonLime,
                NeonCyan
            )
        )
    }

    val borderColor by animateColorAsState(
        targetValue = if (isStudiedToday) NeonLime else NeonCyan,
        label = "btn_border"
    )

    val contentColor = if (isStudiedToday) NeonLime else DarkBg

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(buttonScale)
            .clip(RoundedCornerShape(22.dp))
            .background(containerBrush)
            .border(
                width = if (isStudiedToday) 1.5.dp else 2.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = if (isStudiedToday) NeonLime else Color.White),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onPrimaryClick()
                }
            )
            .padding(vertical = 18.dp, horizontal = 20.dp)
            .testTag("hoy_si_estudie_button"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isStudiedToday) Icons.Rounded.CheckCircle else Icons.Rounded.ElectricBolt,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = if (isStudiedToday) "¡HOY SÍ ESTUDIÉ! ✓" else "HOY SÍ ESTUDIÉ",
                    color = contentColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )

                Text(
                    text = if (isStudiedToday) "Día registrado · Toca para alternar" else "+1 día a tu racha consecutiva",
                    color = if (isStudiedToday) TextMuted else DarkBg.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
