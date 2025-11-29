package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState

@Composable
fun AttendanceFilterSheet(
    showSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: AttendanceUiState,
    onApply: (AttendanceFilterData) -> Unit,
) {
    var tempFilter by remember { mutableStateOf(uiState.filterData) }
    LaunchedEffect(uiState.filterData) {
        if (tempFilter != uiState.filterData) tempFilter = uiState.filterData
    }

    FilterSelectorBottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest,
        onApply = { onApply(tempFilter) },
        onReset = { tempFilter = AttendanceFilterData() },
        showReset = tempFilter != AttendanceFilterData(),
    ) {
        FilterSelector(
            isLoading = uiState.isLoading,
            title = "Status",
            options = uiState.statusOptions,
            value = tempFilter.status,
            onValueChange = {
                tempFilter = tempFilter.copy(
                    status = it
                )
            }
        )
    }
}