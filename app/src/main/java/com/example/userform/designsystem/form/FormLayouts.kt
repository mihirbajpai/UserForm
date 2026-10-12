package com.example.userform.designsystem.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.userform.designsystem.tokens.CustomSpacing

/** Compact: one column */
@Composable
internal fun CompactFormLayout(
    fields: List<FieldConfig>,
    field: @Composable (FieldConfig, Modifier) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(CustomSpacing.lg)) {
        fields.forEach { config ->
            key(config.name) { field(config, Modifier.fillMaxWidth()) }
        }
    }
}

/** Expanded: two columns */
@Composable
internal fun ExpandedFormLayout(
    fields: List<FieldConfig>,
    field: @Composable (FieldConfig, Modifier) -> Unit,
) {
    val gap = CustomSpacing.lg
    Column(verticalArrangement = Arrangement.spacedBy(gap)) {
        packIntoRows(fields).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(gap),
                verticalAlignment = Alignment.Top
            ) {
                row.forEach { config ->
                    key(config.name) { field(config, Modifier.weight(1f)) }
                }
                // A half field without a partner keeps its half width.
                if (row.size == 1 && row.single().span == FieldSpan.Half) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

private fun packIntoRows(fields: List<FieldConfig>): List<List<FieldConfig>> {
    val rows = mutableListOf<List<FieldConfig>>()
    var waiting: FieldConfig? = null
    for (field in fields) {
        when {
            field.span == FieldSpan.Full -> {
                waiting?.let { rows += listOf(it) }
                waiting = null
                rows += listOf(field)
            }

            waiting == null -> waiting = field
            else -> {
                rows += listOf(waiting, field)
                waiting = null
            }
        }
    }
    waiting?.let { rows += listOf(it) }
    return rows
}
