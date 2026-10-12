package com.example.userform.lead

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.userform.designsystem.form.FieldConfig
import com.example.userform.designsystem.form.FormValues
import com.example.userform.designsystem.form.isVisible

/**
 * Holds what the user typed and which fields they've left. Errors are always
 * derived from [validate]; a field only shows its error once it's been touched.
 */
@Stable
class LeadFormState(
    private val config: List<FieldConfig>,
    values: FormValues = emptyMap(),
    touched: Set<String> = emptySet(),
    submitted: FormValues? = null,
) {
    var values by mutableStateOf(values)
        private set

    private var touched by mutableStateOf(touched)

    /** The values from the last successful submit, or null. */
    var submitted by mutableStateOf(submitted)
        private set

    val visibleErrors: Map<String, String>
        get() = validate(config, values).filterKeys { it in touched }

    fun onValueChange(name: String, value: String) {
        values = values + (name to value)
        submitted = null
    }

    fun onFieldBlur(name: String) {
        touched = touched + name
    }

    fun submit() {
        touched = config.mapTo(mutableSetOf()) { it.name }
        submitted = if (validate(config, values).isEmpty()) {
            config.filter { it.isVisible(values) }
                .associate { it.name to values[it.name].orEmpty() }
        } else null
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun saver(config: List<FieldConfig>): Saver<LeadFormState, Any> =
            listSaver<LeadFormState, Any?>(
                save = { state ->
                    listOf(
                        HashMap(state.values),
                        ArrayList(state.touched),
                        state.submitted?.let(::HashMap)
                    )
                },
                restore = { saved ->
                    LeadFormState(
                        config = config,
                        values = saved[0] as FormValues,
                        touched = (saved[1] as List<String>).toSet(),
                        submitted = saved[2] as FormValues?,
                    )
                }
            )
    }
}

@Composable
fun rememberLeadFormState(config: List<FieldConfig>): LeadFormState =
    rememberSaveable(saver = LeadFormState.saver(config)) { LeadFormState(config) }
