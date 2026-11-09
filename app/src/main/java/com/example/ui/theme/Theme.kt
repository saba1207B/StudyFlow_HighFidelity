// Copyright (c) 2026 Sabareesh M (saba1207B). All Rights Reserved.
// All rights reserved. No public license is granted.

package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = IndigoDark,
    onPrimaryContainer = IndigoContainer,
    secondary = VioletLight,
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = VioletDark,
    onSecondaryContainer = VioletContainer,
    tertiary = AccentCyanLight,
    onTertiary = Color(0xFF0F172A),
    tertiaryContainer = Color(0xFF075985),
    onTertiaryContainer = AccentCyanContainer,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkSurfaceBorder,
    outlineVariant = Color(0xFF1E293B),
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = OnIndigoContainer,
    secondary = VioletSecondary,
    onSecondary = Color.White,
    secondaryContainer = VioletContainer,
    onSecondaryContainer = OnVioletContainer,
    tertiary = AccentCyan,
    onTertiary = Color.White,
    tertiaryContainer = AccentCyanContainer,
    onTertiaryContainer = Color(0xFF0369A1),
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightSurfaceBorder,
    outlineVariant = Slate200,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun StudyFlowTheme(
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

