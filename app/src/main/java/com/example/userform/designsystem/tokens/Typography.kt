package com.example.userform.designsystem.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object CustomTypography {
    val title: TextStyle = TextStyle(
        fontSize = 26.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold
    )
    val label: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    )
    val body: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
    val button: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
    val caption: TextStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
}