package com.example.userform.designsystem.form

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.userform.designsystem.atoms.CustomCheckbox
import com.example.userform.designsystem.atoms.CustomSelect
import com.example.userform.designsystem.atoms.CustomTextField
import com.example.userform.designsystem.molecules.CustomFormField
import com.example.userform.designsystem.tokens.CustomBreakpoints

/**
 * Renders one field per visible config entry. Values and errors come from the
 * caller; this only draws them and reports edits and focus loss back.
 */
@Composable
fun DynamicForm(
    config: List<FieldConfig>,
    values: FormValues,
    errors: Map<String, String>,
    onValueChange: (name: String, value: String) -> Unit,
    onFieldBlur: (name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val fields = config.filter { it.isVisible(values) }
    val field: @Composable (FieldConfig, Modifier) -> Unit = { entry, fieldModifier ->
        ConfiguredField(
            field = entry,
            value = values[entry.name].orEmpty(),
            error = errors[entry.name],
            onValueChange = { onValueChange(entry.name, it) },
            onBlur = { onFieldBlur(entry.name) },
            modifier = fieldModifier,
        )
    }

    BoxWithConstraints(modifier) {
        if (maxWidth >= CustomBreakpoints.TwoColumn) {
            ExpandedFormLayout(fields, field)
        } else {
            CompactFormLayout(fields, field)
        }
    }
}

@Composable
private fun ConfiguredField(
    field: FieldConfig,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    modifier: Modifier,
) {
    val isError = error != null
    val fill = Modifier.fillMaxWidth()
    CustomFormField(
        // The checkbox carries its own label beside the box.
        label = field.label.takeUnless { field.type == FieldType.Checkbox },
        required = Validation.Required in field.validations,
        hint = field.hint,
        error = error,
        modifier = modifier,
    ) {
        when (field.type) {
            FieldType.Text, FieldType.Email, FieldType.TextArea -> CustomTextField(
                value = value,
                onValueChange = onValueChange,
                label = field.label,
                isError = isError,
                modifier = fill,
                multiline = field.type == FieldType.TextArea,
                keyboardType = field.keyboardType(),
                onFocusLost = onBlur,
            )

            FieldType.Select -> CustomSelect(
                value = value,
                options = field.options,
                onSelect = onValueChange,
                label = field.label,
                isError = isError,
                modifier = fill,
                onFocusLost = onBlur,
            )

            FieldType.Checkbox -> CustomCheckbox(
                checked = value.toBoolean(),
                onCheckedChange = { checked ->
                    onValueChange(if (checked) "true" else "")
                    onBlur()
                },
                label = field.label,
                isError = isError,
                modifier = fill,
            )
        }
    }
}

private fun FieldConfig.keyboardType(): KeyboardType = when {
    type == FieldType.Email -> KeyboardType.Email
    validations.any { it is Validation.Digits } -> KeyboardType.Number
    else -> KeyboardType.Text
}
