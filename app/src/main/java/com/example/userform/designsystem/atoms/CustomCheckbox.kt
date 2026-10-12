package com.example.userform.designsystem.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.userform.designsystem.tokens.CustomSpacing
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    val boxShape = RoundedCornerShape(4.dp)
    val boxColor = when {
        isError -> CustomTheme.colors.error
        checked -> CustomTheme.colors.primary
        else -> CustomTheme.colors.border
    }

    Row(
        modifier = modifier
            .heightIn(min = ControlMinHeight)
            .toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(20.dp)
                .background(
                    if (checked) CustomTheme.colors.primary else Color.Transparent,
                    boxShape
                )
                .border(2.dp, boxColor, boxShape),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                val tickColor = CustomTheme.colors.onPrimary
                Canvas(Modifier.size(12.dp)) {
                    val tick = Path().apply {
                        moveTo(size.width * 0.1f, size.height * 0.55f)
                        lineTo(size.width * 0.4f, size.height * 0.85f)
                        lineTo(size.width * 0.9f, size.height * 0.2f)
                    }
                    drawPath(
                        tick,
                        tickColor,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
        Spacer(Modifier.width(CustomSpacing.md))
        BasicText(
            text = label,
            style = CustomTypography.body.copy(color = CustomTheme.colors.textPrimary)
        )
    }
}
