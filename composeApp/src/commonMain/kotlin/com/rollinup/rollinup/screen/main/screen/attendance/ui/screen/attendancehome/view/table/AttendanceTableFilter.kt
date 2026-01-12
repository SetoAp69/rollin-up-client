package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.date.DateRangePicker
import com.rollinup.rollinup.component.date.SingleDatePickerField
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.filter.TableFilterRow
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.popupPadding
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getFileName
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceFilterData
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.uistate.AttendanceUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.AttendanceSummary
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun AttendanceTableFilter(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    var showExportDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(12.dp),
                color = theme.popUpBg
            )
            .padding(popupPadding),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.width(320.dp)
            ) {
                SearchTextField(
                    title = "Search",
                    onValueChange = {},
                    value = uiState.searchQuery,
                    onSearch = cb.onSearch,
                    placeholder = "Enter keyword"
                )
            }
            Box(modifier = Modifier.width(320.dp)) {
                AttendanceSummary(
                    isLoading = uiState.isLoadingSummary,
                    summary = uiState.summary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                icon = Res.drawable.ic_print_line_24,
            ) {
                showDatePicker = true
            }
            AttendanceTableFilterRow(
                uiState = uiState,
                cb = cb
            )
        }
    }

    DateRangePicker(
        isShowDatePicker = showDatePicker,
        onDismissRequest = { showDatePicker = it },
        value = uiState.exportDateRanges,
        onSelectDate = { value ->
            cb.onUpdateExportDateRange(value.sorted())
            showExportDialog = true
        },
        title = "Select date ranges",
        isDisablePastSelection = false,
        isAllSelectable = true,
    )

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = getFileName(uiState.exportDateRanges, "Attendance"),
        onDismissRequest = { showExportDialog = it },
        onConfirm = {
            cb.onExportFile(it)
            cb.onUpdateExportDateRange(emptyList())
        }
    )
}

@Composable
private fun AttendanceTableFilterRow(
    uiState: AttendanceUiState,
    cb: AttendanceCallback,
) {
    TableFilterRow(
        onReset = { cb.onFilter(AttendanceFilterData()) },
        showReset = uiState.filterData != AttendanceFilterData(),
    ) {
        Box(modifier = Modifier.width(120.dp)) {
            SingleDatePickerField(
                title = "Date",
                value = uiState.filterData.date,
                onValueChange = { millis ->
                    cb.onFilter(
                        uiState.filterData.copy(
                            date = millis
                        )
                    )
                }
            )
        }
        SingleDropDownSelector(
            title = "Class",
            options = emptyList(),
            value = null,
            enable = false,
            placeHolder = uiState.user.classX ?: "-"
        ) {}
        MultiDropDownSelector(
            title = "Status",
            options = uiState.statusOptions,
            value = uiState.filterData.status,
            placeHolder = "All"
        ) {
            cb.onFilter(
                uiState.filterData.copy(
                    status = it
                )
            )
        }
    }
}