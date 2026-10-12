package com.example.userform.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomSpacing

internal val ControlShape = RoundedCornerShape(8.dp)
internal val ControlMinHeight = 48.dp

/** The bordered box shared by the text field and the select. */
@Composable
internal fun FieldFrame(
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val borderColor = when {
        isError -> CustomTheme.colors.error
        focused -> CustomTheme.colors.primary
        else -> CustomTheme.colors.border
    }
    Box(
        modifier = modifier
            .heightIn(min = ControlMinHeight)
            .background(CustomTheme.colors.surface, ControlShape)
            .border(if (focused) 2.dp else 1.dp, borderColor, ControlShape)
            .padding(horizontal = CustomSpacing.md, vertical = CustomSpacing.md),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}
