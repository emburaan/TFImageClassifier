package com.example.tfimageclassifier.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Palette ──────────────────────────────────────────────────────────────────
private val Purple80   = Color(0xFFD0BCFF)
private val PurpleGrey = Color(0xFFCCC2DC)
private val Pink80     = Color(0xFFEFB8C8)
private val Purple40   = Color(0xFF6650A4)
private val PurpleGrey40 = Color(0xFF625B71)
private val Pink40     = Color(0xFF7D5260)

private val LightColors = lightColorScheme(
    primary         = Purple40,
    secondary       = PurpleGrey40,
    tertiary        = Pink40,
    background      = Color(0xFFFFFBFE),
    surface         = Color(0xFFFFFBFE),
    onPrimary       = Color.White,
    onSecondary     = Color.White,
    onBackground    = Color(0xFF1C1B1F),
    onSurface       = Color(0xFF1C1B1F),
)

private val DarkColors = darkColorScheme(
    primary         = Purple80,
    secondary       = PurpleGrey,
    tertiary        = Pink80,
)

@Composable
fun TFClassifierTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
