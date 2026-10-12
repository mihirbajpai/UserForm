package com.example.userform.designsystem.atoms

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    multiline: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusLost: () -> Unit = {},
) {
    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .semantics { contentDescription = label }
            .onFocusChanged { state ->
                if (focused && !state.isFocused) onFocusLost()
                focused = state.isFocused
            },
        singleLine = !multiline,
        minLines = if (multiline) 4 else 1,
        textStyle = CustomTypography.body.copy(color = CustomTheme.colors.textPrimary),
        cursorBrush = SolidColor(CustomTheme.colors.primary),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (multiline) ImeAction.Default else ImeAction.Next,
        ),
        decorationBox = { innerTextField ->
            FieldFrame(focused = focused, isError = isError) { innerTextField() }
        },
    )
}
