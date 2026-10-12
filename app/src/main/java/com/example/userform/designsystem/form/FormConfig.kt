package com.example.userform.designsystem.form

import com.example.userform.designsystem.atoms.SelectOption

enum class FieldType { Text, Email, Select, TextArea, Checkbox }

sealed interface Validation {
    data object Required : Validation
    data object Email : Validation
    data class Digits(val count: Int) : Validation
    data class MaxLength(val max: Int) : Validation
}

/** Show the field only while another field holds a given value. */
data class VisibleWhen(val field: String, val equals: String)

/** How much of a row the field takes in the two-column layout. */
enum class FieldSpan { Half, Full }

data class FieldConfig(
    val name: String,
    val type: FieldType,
    val label: String,
    val validations: List<Validation> = emptyList(),
    val options: List<SelectOption> = emptyList(),
    val hint: String? = null,
    val visibleWhen: VisibleWhen? = null,
    val span: FieldSpan = FieldSpan.Full,
)

/** Every value is a string; a checked checkbox is "true", unchecked is "". */
typealias FormValues = Map<String, String>

fun FieldConfig.isVisible(values: FormValues): Boolean =
    visibleWhen == null || values[visibleWhen.field] == visibleWhen.equals
