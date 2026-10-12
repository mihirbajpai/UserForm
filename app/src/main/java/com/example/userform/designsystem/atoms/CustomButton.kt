package com.example.userform.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import com.example.userform.designsystem.tokens.CustomSpacing
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

@Composable
fun CustomButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightIn(min = ControlMinHeight)
            .clip(ControlShape)
            .background(CustomTheme.colors.primary)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = CustomSpacing.lg, vertical = CustomSpacing.md),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = label,
            style = CustomTypography.button.copy(color = CustomTheme.colors.onPrimary),
        )
    }
}
