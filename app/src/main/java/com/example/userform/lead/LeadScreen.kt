package com.example.userform.lead

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.userform.designsystem.atoms.CustomButton
import com.example.userform.designsystem.form.DynamicForm
import com.example.userform.designsystem.form.FieldConfig
import com.example.userform.designsystem.form.FieldType
import com.example.userform.designsystem.form.FormValues
import com.example.userform.designsystem.tokens.CustomBreakpoints
import com.example.userform.designsystem.tokens.CustomColorScheme
import com.example.userform.designsystem.tokens.CustomSpacing
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

private val CardShape = RoundedCornerShape(12.dp)

@Composable
fun LeadScreen(modifier: Modifier = Modifier) {
    val state = rememberLeadFormState(LeadFormConfig)
    // Used to drop focus on submit, which closes the keyboard.
    val focusManager = LocalFocusManager.current
    // System bars, display cutout and keyboard; split below between the content and the submit bar.
    val safeDrawing = WindowInsets.safeDrawing
    val colors = CustomTheme.colors
    // Caps width on tablets and centres the content; a no-op on phones.
    val contentWidthModifier = Modifier
        .widthIn(max = CustomBreakpoints.MaxContentWidth)
        .fillMaxWidth()

    // Scrolls the submitted values into view after a successful submit.
    val resultInView = remember { BringIntoViewRequester() }
    LaunchedEffect(state.submitted) {
        if (state.submitted != null) resultInView.bringIntoView()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = CustomSpacing.lg, vertical = CustomSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CustomSpacing.lg),
        ) {
            Header(contentWidthModifier, colors)
            DynamicForm(
                config = LeadFormConfig,
                values = state.values,
                errors = state.visibleErrors,
                onValueChange = state::onValueChange,
                onFieldBlur = state::onFieldBlur,
                modifier = contentWidthModifier.card(colors),
            )

            state.submitted?.let { submitted ->
                SubmittedLead(
                    config = LeadFormConfig,
                    values = submitted,
                    modifier = contentWidthModifier.bringIntoViewRequester(resultInView),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface)
                .drawBehind {
                    drawLine(
                        colors.divider,
                        Offset.Zero,
                        Offset(size.width, 0f),
                        1.dp.toPx()
                    )
                }
                .windowInsetsPadding(safeDrawing.only(WindowInsetsSides.Bottom))
                .padding(horizontal = CustomSpacing.lg, vertical = CustomSpacing.md),
            contentAlignment = Alignment.Center,
        ) {
            CustomButton(
                label = "Submit",
                onClick = {
                    focusManager.clearFocus()
                    state.submit()
                },
                modifier = contentWidthModifier,
            )
        }
    }
}

@Composable
private fun Header(modifier: Modifier, colors: CustomColorScheme) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CustomSpacing.xs)
    ) {
        BasicText(
            text = "New lead",
            style = CustomTypography.title.copy(color = colors.textPrimary)
        )
        BasicText(
            text = "Fill in the details and we'll get back to them.",
            style = CustomTypography.body.copy(color = colors.textSecondary),
        )
    }
}

@Composable
private fun SubmittedLead(
    config: List<FieldConfig>,
    values: FormValues,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors
    Column(
        modifier = modifier.card(colors),
        verticalArrangement = Arrangement.spacedBy(CustomSpacing.md)
    ) {
        BasicText(
            text = "Lead submitted",
            style = CustomTypography.label.copy(color = colors.primary)
        )
        config.filter { it.name in values }.forEach { field ->
            Row(horizontalArrangement = Arrangement.spacedBy(CustomSpacing.md)) {
                BasicText(
                    field.label,
                    style = CustomTypography.label.copy(color = colors.textSecondary),
                    modifier = Modifier.weight(0.4f),
                )
                BasicText(
                    displayValue(field, values.getValue(field.name)),
                    style = CustomTypography.body.copy(color = colors.textPrimary),
                    modifier = Modifier.weight(0.6f),
                )
            }
        }
    }
}

private fun Modifier.card(colors: CustomColorScheme): Modifier = this
    .background(colors.surface, CardShape)
    .border(1.dp, colors.divider, CardShape)
    .padding(CustomSpacing.lg)

private fun displayValue(field: FieldConfig, value: String): String = when (field.type) {
    FieldType.Checkbox -> if (value.toBoolean()) "Yes" else "No"
    FieldType.Select -> field.options.firstOrNull { it.value == value }?.label ?: value
    else -> value.ifBlank { "—" }
}
