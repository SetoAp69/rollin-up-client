package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.date.DateRangePickerField
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState

@Composable
fun AttendanceByStudentFilterSheet(
    showFilter: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: AttendanceByStudentUiState,
    onUpdateFilter: (AttendanceByStudentFilterData) -> Unit,
) {
    var tempValue by remember { mutableStateOf(uiState.filterData) }
    LaunchedEffect(uiState.filterData) {
        if (tempValue != uiState.filterData) tempValue = uiState.filterData
    }
    FilterSelectorBottomSheet(
        isShowSheet = showFilter,
        onDismissRequest = onDismissRequest,
        onApply = { onUpdateFilter(tempValue) },
        onReset = { tempValue = AttendanceByStudentFilterData() },
        showReset = tempValue != AttendanceByStudentFilterData(),
    ) {
        FilterSelector(
            title = "Status",
            options = uiState.statusOptions,
            value = tempValue.status,
            onValueChange = { tempValue = tempValue.copy(status = it) }
        )
        Spacer(itemGap8)
        DateRangePickerField(
            title = "Date",
            placeholder = "-",
            value = tempValue.dateRange,
            onValueChange = { date ->
                tempValue = tempValue.copy(
                    dateRange = date.sorted()
                )
            },
            isAllSelectable = true
        )
    }
}