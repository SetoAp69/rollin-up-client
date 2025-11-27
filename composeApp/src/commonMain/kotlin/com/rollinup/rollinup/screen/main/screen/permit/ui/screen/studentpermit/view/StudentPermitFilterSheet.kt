package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.date.DatePickerField
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState

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
                title = "Status",
                options = uiState.statusOptions,
                value = tempFilterData.status,
                onValueChange = { tempFilterData = tempFilterData.copy(status = it) }
            )
        }
        FilterSelector(
            isLoading = uiState.isLoading,
            title = "Type",
            options = uiState.typeOptions,
            value = tempFilterData.type,
            onValueChange = { tempFilterData = tempFilterData.copy(type = it) }
        )
        DatePickerField(
            title = "Date",
            placeholder = "Date range",
            value = tempFilterData.dateRange,
            maxSelection = Int.MAX_VALUE,
            isAllSelectable = true,
            isDisablePastSelection = false,
            platform = getPlatform(),
            onValueChange = {
                tempFilterData = tempFilterData.copy(dateRange = it)
            }
        )

    }
}