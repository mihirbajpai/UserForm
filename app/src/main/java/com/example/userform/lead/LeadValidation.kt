package com.example.userform.lead

import android.util.Patterns
import com.example.userform.designsystem.form.FieldConfig
import com.example.userform.designsystem.form.FieldType
import com.example.userform.designsystem.form.FormValues
import com.example.userform.designsystem.form.Validation
import com.example.userform.designsystem.form.isVisible

/**
 * Returns field name -> first error message for every visible field that fails.
 * Hidden fields are skipped, so a conditional field is only required while shown.
 */
fun validate(config: List<FieldConfig>, values: FormValues): Map<String, String> =
    config
        .filter { it.isVisible(values) }
        .mapNotNull { field ->
            val value = values[field.name].orEmpty().trim()
            field.validations
                .firstNotNullOfOrNull { rule -> errorFor(rule, field, value) }
                ?.let { field.name to it }
        }
        .toMap()

private fun errorFor(rule: Validation, field: FieldConfig, value: String): String? {
    if (value.isEmpty() && rule != Validation.Required) return null

    return when (rule) {
        Validation.Required -> when {
            value.isNotEmpty() -> null
            field.type == FieldType.Checkbox -> "Please tick this box to continue"
            else -> "${field.label} is required"
        }

        Validation.Email -> {
            if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) null
            else "Enter a valid email address"
        }

        is Validation.Digits -> {
            if (value.length == rule.count && value.all(Char::isDigit)) null
            else "Enter exactly ${rule.count} digits"
        }

        is Validation.MaxLength -> {
            if (value.length <= rule.max) null
            else "Keep it to ${rule.max} characters (currently ${value.length})"
        }
    }
}
