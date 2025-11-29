package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.attendance.AttendanceStatus
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.component.model.OptionData

@Composable
fun AttendanceByStudentFilterSheet(
    showFilter: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    options: List<OptionData<AttendanceStatus>>,
    value: List<AttendanceStatus>,
    onValueChange: (List<AttendanceStatus>) -> Unit,
) {
    var tempValue by remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        if (tempValue != value) tempValue = value
    }
    FilterSelectorBottomSheet(
        isShowSheet = showFilter,
        onDismissRequest = onDismissRequest,
        onApply = { onValueChange },
        onReset = { tempValue = emptyList() },
        showReset = tempValue.isNotEmpty(),
    ) {
        FilterSelector(
            title = "Status",
            options = options,
            value = tempValue,
            onValueChange = { tempValue = it }
        )
    }
}