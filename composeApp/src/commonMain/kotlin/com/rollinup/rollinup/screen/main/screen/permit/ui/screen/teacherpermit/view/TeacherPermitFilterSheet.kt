package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.date.DateRangePickerField
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitFilterData
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState

@Composable
fun TeacherPermitFilterSheet(
    showSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: TeacherPermitUiState,
    onApply: (PermitFilterData) -> Unit,
) {
    var tempData by remember { mutableStateOf(PermitFilterData()) }

    FilterSelectorBottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest,
        onApply = {
            onApply(tempData)
            onDismissRequest(false)
        },
        onReset = {
            tempData = PermitFilterData()
        },
        showReset = tempData != PermitFilterData(),
    ) {
        FilterSelector(
            isLoading = uiState.isLoading,
            title = "Type",
            options = uiState.typeOptions,
            value = tempData.type,
            onValueChange = {
                tempData = tempData.copy(type = it)
            },
        )
        if (uiState.currentTab == PermitTab.ACTIVE) {
            FilterSelector(
                isLoading = uiState.isLoading,
                title = "Status",
                options = uiState.statusOptions,
                value = tempData.status,
                onValueChange = {
                    tempData = tempData.copy(status = it)
                },
            )
        }
        Spacer(12.dp)
        DateRangePickerField(
            title = "",
            placeholder = "Select date range",
            value = tempData.dateRange,
            isAllSelectable = true,
            isDisablePastSelection = false,
        ) {
            tempData = tempData.copy(dateRange = it)
        }
    }
}