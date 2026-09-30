package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RachaDarkColorScheme = darkColorScheme(
    primary = NeonLime,
    onPrimary = DarkBg,
    primaryContainer = NeonLimeGlow,
    onPrimaryContainer = NeonLimeBright,
    secondary = NeonCyan,
    onSecondary = DarkBg,
    secondaryContainer = NeonCyanGlow,
    onSecondaryContainer = NeonCyan,
    tertiary = NeonFlameOrange,
    onTertiary = DarkBg,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    outlineVariant = BorderHighlight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Modo oscuro obligatorio como se solicitó
    dynamicColor: Boolean = false, // Mantener colores neón personalizados
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DarkBg.toArgb()
                window.navigationBarColor = DarkBg.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = RachaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
