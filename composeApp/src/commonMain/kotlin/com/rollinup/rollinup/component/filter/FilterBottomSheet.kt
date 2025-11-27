package com.rollinup.rollinup.component.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.checkbox.CheckBox
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.loading.ShimmerEffect
import com.rollinup.rollinup.component.model.OptionData
import com.rollinup.rollinup.component.ripple.CustomRipple
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap4
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getScreenHeight
import org.jetbrains.compose.resources.painterResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_filter_line_24
import rollin_up.composeapp.generated.resources.ic_minus_line_24
import rollin_up.composeapp.generated.resources.ic_plus_line_24

@Composable
fun FilterSelectorBottomSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit,
    showReset: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    BottomSheet(
        isShowSheet = isShowSheet,
        onDismissRequest = onDismissRequest,
        btnConfirmText = "Apply",
        onClickConfirm = onApply,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .heightIn(max = getScreenHeight() * 0.8f)
    ) {
        FilterBottomSheetHeader(
            showReset = showReset,
            onReset = onReset
        )
        Spacer(16.dp)
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            content()
        }
    }
}

@Composable
private fun FilterBottomSheetHeader(
    showReset: Boolean,
    onReset: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_filter_line_24),
            tint = theme.primary,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Filter",
            style = Style.popupTitle,
            color = theme.textPrimary,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showReset) {
            Text(
                text = "Reset",
                style = Style.popupTitle,
                color = theme.textPrimary,
                modifier = Modifier.clickable {
                    onReset()
                }
            )
        }

    }
}


@Composable
fun <T> FilterSelector(
    isLoading: Boolean,
    title: String,
    options: List<OptionData<T>>,
    value: List<T>,
    onValueChange: (List<T>) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isLoading) {
            FilterHeaderLoading()
        } else {
            FilterSelectorHeader(
                title = title,
                selected = value.size,
                isExpanded = isExpanded,
                onToggleExpanded = { isExpanded = it }
            )
        }
        if (isExpanded) {
            FilterSelectorContent(
                options = options,
                value = value,
                onValueChange = onValueChange
            )
        }
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
}

@Composable
private fun <T> FilterSelectorContent(
    options: List<OptionData<T>>,
    value: List<T>,
    onValueChange: (List<T>) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(itemGap8),
    ) {
        FilterSelectorItem(
            label = "Select All",
            isSelected = value.size == options.size,
            onSelect = { isSelected ->
                if (isSelected) {
                    onValueChange(emptyList())
                } else {
                    onValueChange(options.map { it.value })
                }
            }
        )
        options.forEach { option ->
            FilterSelectorItem(
                label = option.label,
                isSelected = option.value in value,
                onSelect = { isSelected ->
                    if (isSelected) {
                        onValueChange(value - option.value)
                    } else {
                        onValueChange(value + option.value)
                    }
                }
            )
        }
    }
}

@Composable
fun FilterSelectorItem(
    label: String,
    isSelected: Boolean,
    onSelect: (Boolean) -> Unit,
) {
    CustomRipple(theme.primary) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onSelect(isSelected)
            }
        ) {
            CheckBox(
                checked = isSelected,
                onCheckedChange = { onSelect(isSelected) },
                modifier = Modifier.size(24.dp)
            )
            Spacer(itemGap4)
            Text(
                text = label,
                style = Style.popupBody,
                color = theme.bodyText
            )
        }
    }
}

@Composable
fun FilterSelectorHeader(
    title: String,
    selected: Int,
    isExpanded: Boolean,
    onToggleExpanded: (Boolean) -> Unit,
) {
    val icon =
        if (isExpanded) Res.drawable.ic_minus_line_24
        else Res.drawable.ic_plus_line_24

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = Style.popupTitle,
            color = theme.textPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        Chip(text = selected.toString(), severity = Severity.SECONDARY)
        Spacer(itemGap8)
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = theme.primary,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onToggleExpanded(!isExpanded)
                }
                .size(24.dp)
        )
    }
}

@Composable
fun FilterHeaderLoading() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ShimmerEffect(120.dp)
        Spacer(modifier = Modifier.weight(1f))
        ShimmerEffect(40.dp)
        Spacer(itemGap8)
        Icon(
            painter = painterResource(Res.drawable.ic_plus_line_24),
            contentDescription = null,
            tint = theme.primary,
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
        )
    }
}
