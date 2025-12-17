package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.utils.Utils.now
import com.rollinup.common.utils.Utils.toFormattedString
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.date.DateFormatter
import com.rollinup.rollinup.component.date.DateRangePicker
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.filter.TableFilterRow
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.selector.SingleDropDownSelector
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getAttendanceRecordFileName
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardFilterData
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import kotlinx.datetime.LocalDate
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_calendar_fill_24
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun AttendanceTableHeader(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    var showExportDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Dashboard",
            color = theme.textPrimary,
            style = Style.popupTitle
        )
        Spacer(12.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = theme.popUpBg, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(itemGap8)
        ) {
            Box(modifier = Modifier.width(320.dp)) {
                SearchTextField(
                    title = "Search",
                    onValueChange = {},
                    value = uiState.filterData.searchQuery,
                    onSearch = {
                        cb.onUpdateFilter(
                            uiState.filterData.copy(
                                searchQuery = it
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
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
            cb.onUpdateExportDateRanges(value.sorted())
            showExportDialog = true
        },
        title = "Select date ranges",
        isDisablePastSelection = false,
        isAllSelectable = true,
    )

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = getAttendanceRecordFileName(uiState.exportDateRanges, "attendance"),
        onDismissRequest = { showExportDialog = it },
        onConfirm = {
            cb.onExportFile(it)
        }
    )
}

@Composable
fun AttendanceTableFilterRow(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    TableFilterRow(
        onReset = { cb.onUpdateFilter(TeacherDashboardFilterData()) },
        showReset = uiState.filterData != TeacherDashboardFilterData()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                icon = Res.drawable.ic_calendar_fill_24,
            ) {}
            Spacer(itemGap8)
            Text(
                text = LocalDate.now().toFormattedString(),
                style = Style.title,
                color = theme.textPrimary
            )
        }

        SingleDropDownSelector(
            title = "Class",
            value = uiState.user.classKey,
            placeHolder = "Class",
            options = uiState.classOption,
            enable = false,
            onValueChange = {
                cb.onUpdateFilter(
                    uiState.filterData.copy(
                        classX = it
                    )
                )
            }
        )

        MultiDropDownSelector(
            title = "Status",
            value = uiState.filterData.status,
            placeHolder = "Status",
            options = uiState.statusOptions,
            onValueChange = {
                cb.onUpdateFilter(
                    uiState.filterData.copy(
                        status = it
                    )
                )
            }
        )
    }
}
