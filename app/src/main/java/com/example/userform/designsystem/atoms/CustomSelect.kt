package com.example.userform.designsystem.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.userform.designsystem.tokens.CustomSpacing
import com.example.userform.designsystem.tokens.CustomTheme
import com.example.userform.designsystem.tokens.CustomTypography

data class SelectOption(val value: String, val label: String)

@Composable
fun CustomSelect(
    value: String,
    options: List<SelectOption>,
    onSelect: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "Select",
    isError: Boolean = false,
    onFocusLost: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorWidth by remember { mutableIntStateOf(0) }
    val selected = options.firstOrNull { it.value == value }

    fun close() {
        expanded = false
        onFocusLost()
    }

    FieldFrame(
        focused = expanded,
        isError = isError,
        modifier = modifier
            .onSizeChanged { anchorWidth = it.width }
            .clip(ControlShape)
            .clickable(role = Role.DropdownList) { expanded = true }
            .semantics { contentDescription = label },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(
                text = selected?.label ?: placeholder,
                style = CustomTypography.body.copy(
                    color = if (selected == null) CustomTheme.colors.textSecondary else CustomTheme.colors.textPrimary,
                ),
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(CustomSpacing.sm))
            Chevron(color = CustomTheme.colors.textSecondary, pointsUp = expanded)
        }

        if (expanded) {
            val gap = with(LocalDensity.current) { CustomSpacing.xs.roundToPx() }
            Popup(
                popupPositionProvider = remember(gap) { BelowOrAbove(gap) },
                onDismissRequest = ::close,
                properties = PopupProperties(focusable = true),
            ) {
                val menuShape = RoundedCornerShape(8.dp)
                Column(
                    Modifier
                        .width(with(LocalDensity.current) { anchorWidth.toDp() })
                        .shadow(4.dp, menuShape)
                        .background(CustomTheme.colors.surface, menuShape)
                        .border(1.dp, CustomTheme.colors.border, menuShape),
                ) {
                    options.forEach { option ->
                        val isSelected = option.value == value
                        BasicText(
                            text = option.label,
                            style = CustomTypography.body.copy(
                                color = if (isSelected) CustomTheme.colors.primary else CustomTheme.colors.textPrimary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else null,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = ControlMinHeight)
                                .clickable(role = Role.Button) {
                                    onSelect(option.value)
                                    close()
                                }
                                .padding(
                                    horizontal = CustomSpacing.md,
                                    vertical = CustomSpacing.md
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chevron(color: Color, pointsUp: Boolean) {
    Canvas(
        Modifier
            .size(14.dp)
            .rotate(if (pointsUp) 180f else 0f)
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.35f)
            lineTo(size.width * 0.5f, size.height * 0.7f)
            lineTo(size.width * 0.85f, size.height * 0.35f)
        }
        drawPath(
            path,
            color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

/** Opens under the anchor, or flips above it when there isn't room below. */
private class BelowOrAbove(private val gap: Int) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val below = anchorBounds.bottom + gap
        val y = if (below + popupContentSize.height <= windowSize.height) {
            below
        } else {
            (anchorBounds.top - gap - popupContentSize.height).coerceAtLeast(0)
        }
        return IntOffset(anchorBounds.left, y)
    }
}
