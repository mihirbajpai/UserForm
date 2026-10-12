package com.example.userform.designsystem.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalCustomColors = staticCompositionLocalOf { LightColorScheme }

// Provides the color scheme.
@Composable
fun CustomTheme(
    colors: CustomColorScheme = LightColorScheme,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalCustomColors provides colors, content = content)
}

object CustomTheme {
    val colors: CustomColorScheme @Composable @ReadOnlyComposable get() = LocalCustomColors.current
}
