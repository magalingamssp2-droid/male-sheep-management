package com.example.malesheep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green800,
    onPrimary = Color.White,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary = Slate500,
    onSecondary = Color.White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate800,
    background = Background,
    onBackground = Slate800,
    surface = Surface,
    onSurface = Slate800,
    error = Red700,
    onError = Color.White,
    outline = Border
)

private val DarkColorScheme = darkColorScheme(
    primary = Green600,
    onPrimary = Color.White,
    primaryContainer = Green900,
    onPrimaryContainer = Green100,
    secondary = Slate500,
    onSecondary = Color.White,
    secondaryContainer = Slate700,
    onSecondaryContainer = Slate100,
    background = Color(0xFF121815),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1B2420),
    onSurface = Color(0xFFF1F5F9),
    error = Color(0xFFEF4444),
    onError = Color.White,
    outline = Slate600
)

@Composable
fun MaleSheepManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
