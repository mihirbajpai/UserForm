package com.example.userform.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val background: Color,
    val divider: Color,
    val surface: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val error: Color,
)

val LightColorScheme = CustomColorScheme(
    primary = Color(0xFF2F5BEA),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF2F4F7),
    divider = Color(0xFFE4E7EC),
    surface = Color(0xFFFFFFFF),
    border = Color(0xFFC7CCD6),
    textPrimary = Color(0xFF1B1F24),
    textSecondary = Color(0xFF5F6773),
    error = Color(0xFFC62828),
)
