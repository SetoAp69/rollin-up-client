package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.date.FilterDatePicker
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_status
import rollin_up.composeapp.generated.resources.label_type

@Composable
fun StudentPermitFilterSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onApply: (PermitFilterData) -> Unit,
    uiState: StudentPermitUiState,
) {
    val filterData = uiState.filterData
    var tempFilterData by remember { mutableStateOf(PermitFilterData()) }

    LaunchedEffect(filterData) {
        if (filterData != tempFilterData) tempFilterData = filterData
    }

    FilterSelectorBottomSheet(
        isShowSheet = isShowSheet,
        onDismissRequest = onDismissRequest,
        onApply = { onApply(tempFilterData) },
        onReset = { tempFilterData = filterData },
        showReset = tempFilterData != filterData,
    ) {

        if (uiState.isActive) {
            FilterSelector(
                isLoading = uiState.isLoading,
                title = stringResource(Res.string.label_status),
                options = uiState.statusOptions,
                value = tempFilterData.status,
                onValueChange = { tempFilterData = tempFilterData.copy(status = it) }
            )
        }
        FilterSelector(
            isLoading = uiState.isLoading,
            title = stringResource(Res.string.label_type),
            options = uiState.typeOptions,
            value = tempFilterData.type,
            onValueChange = { tempFilterData = tempFilterData.copy(type = it) }
        )
        Spacer(itemGap8)
        FilterDatePicker(
            title = "Date Range",
            value = tempFilterData.dateRange,
            isLoading = uiState.isLoading,
            onValueChange = {
                tempFilterData = tempFilterData.copy(dateRange = it)
            },
        )

    }
}