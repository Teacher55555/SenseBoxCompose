package com.example.sensebox.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val boxDarkColorScheme = darkColorScheme(
    primary = boxDarkPrimary,
    onPrimary = boxDarkOnPrimary,
    primaryContainer = boxDarkPrimaryContainer,
    onPrimaryContainer = boxDarkOnPrimaryContainer,
    inversePrimary = boxDarkPrimaryInverse,
    secondary = boxDarkSecondary,
    onSecondary = boxDarkOnSecondary,
    secondaryContainer = boxDarkSecondaryContainer,
    onSecondaryContainer = boxDarkOnSecondaryContainer,
    tertiary = boxDarkTertiary,
    onTertiary = boxDarkOnTertiary,
    tertiaryContainer = boxDarkTertiaryContainer,
    onTertiaryContainer = boxDarkOnTertiaryContainer,
    error = boxDarkError,
    onError = boxDarkOnError,
    errorContainer = boxDarkErrorContainer,
    onErrorContainer = boxDarkOnErrorContainer,
    background = boxDarkBackground,
    onBackground = boxDarkOnBackground,
    surface = boxDarkSurface,
    onSurface = boxDarkOnSurface,
    inverseSurface = boxDarkInverseSurface,
    inverseOnSurface = boxDarkInverseOnSurface,
    surfaceVariant = boxDarkSurfaceVariant,
    onSurfaceVariant = boxDarkOnSurfaceVariant,
    outline = boxDarkOutline
)

private val boxLightColorScheme = lightColorScheme(
    primary = boxLightPrimary,
    onPrimary = boxLightOnPrimary,
    primaryContainer = boxLightPrimaryContainer,
    onPrimaryContainer = boxLightOnPrimaryContainer,
    inversePrimary = boxLightPrimaryInverse,
    secondary = boxLightSecondary,
    onSecondary = boxLightOnSecondary,
    secondaryContainer = boxLightSecondaryContainer,
    onSecondaryContainer = boxLightOnSecondaryContainer,
    tertiary = boxLightTertiary,
    onTertiary = boxLightOnTertiary,
    tertiaryContainer = boxLightTertiaryContainer,
    onTertiaryContainer = boxLightOnTertiaryContainer,
    error = boxLightError,
    onError = boxLightOnError,
    errorContainer = boxLightErrorContainer,
    onErrorContainer = boxLightOnErrorContainer,
    background = boxLightBackground,
    onBackground = boxLightOnBackground,
    surface = boxLightSurface,
    onSurface = boxLightOnSurface,
    inverseSurface = boxLightInverseSurface,
    inverseOnSurface = boxLightInverseOnSurface,
    surfaceVariant = boxLightSurfaceVariant,
    onSurfaceVariant = boxLightOnSurfaceVariant,
    outline = boxLightOutline
)

@Composable
fun SenseBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> boxDarkColorScheme
        else -> boxLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = boxTypography,
        content = content
    )
}