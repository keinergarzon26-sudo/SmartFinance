package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD0BCFF),      // Lavender
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF49454F),
    onPrimaryContainer = Color(0xFFE6E1E5),
    secondary = Color(0xFFCAC4D0),
    onSecondary = Color(0xFF332D41),
    background = Color(0xFF1C1B1F),   // Deep Obsidian
    onBackground = Color(0xFFE6E1E5), // Soft white
    surface = Color(0xFF2B2930),      // Slate surface
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF49454F)       // Sleek borders
  )

private val LightColorScheme = DarkColorScheme // Force elegant dark for a unified custom look

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to true to enforce the Elegant Dark aesthetic
  dynamicColor: Boolean = false, // Disable dynamic colors so our custom theme colors take priority
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
