package com.example.userform.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import com.example.userform.designsystem.tokens.CustomSpacing
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

/**
 * Label, control, then either the error or the hint underneath.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomFormField(
    label: String?,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    hint: String? = null,
    error: String? = null,
    control: @Composable () -> Unit,
) {
    val bringIntoView = remember { BringIntoViewRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    val imeVisible = WindowInsets.isImeVisible

    // Keep the focused field, error included, on screen: when it gains focus, when the
    // keyboard opens and shrinks the viewport, and when an error appears below it.
    LaunchedEffect(hasFocus, imeVisible, error) {
        if (hasFocus) bringIntoView.bringIntoView()
    }

    Column(
        modifier = modifier
            .bringIntoViewRequester(bringIntoView)
            .onFocusChanged { hasFocus = it.hasFocus },
        verticalArrangement = Arrangement.spacedBy(CustomSpacing.xs),
    ) {
        if (label != null) {
            BasicText(
                text = if (required) "$label *" else label,
                style = CustomTypography.label.copy(color = CustomTheme.colors.textPrimary),
                modifier = Modifier.padding(bottom = CustomSpacing.xs),
            )
        }
        control()
        when {
            error != null -> BasicText(
                error,
                style = CustomTypography.caption.copy(color = CustomTheme.colors.error)
            )

            hint != null -> BasicText(
                hint,
                style = CustomTypography.caption.copy(color = CustomTheme.colors.textSecondary)
            )
        }
    }
}
