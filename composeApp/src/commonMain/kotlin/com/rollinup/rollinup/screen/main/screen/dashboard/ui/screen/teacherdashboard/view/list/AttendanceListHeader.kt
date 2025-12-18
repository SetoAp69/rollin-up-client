package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateRangePicker
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.searchbar.SearchBarWithMenu
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.component.utils.getAttendanceRecordFileName
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardActionSheet
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardFilterSheet

@Composable
fun AttendanceListHeader(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
) {
    var showFilter by remember { mutableStateOf(false) }
    var showActionSheet by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Attendance",
                style = Style.label,
                color = theme.bodyText
            )

            Chip(
                text = uiState.user.classX ?: "-",
                severity = Severity.SECONDARY
            )
        }
        SearchBarWithMenu(
            searchQuery = uiState.filterData.searchQuery,
            onSearch = {
                cb.onUpdateFilter(
                    uiState.filterData.copy(
                        searchQuery = it
                    )
                )
            },
            menu = getMenu(uiState),
            onClickMenu = { menu ->
                when (menu) {
                    Menu.FILTER -> {
                        showFilter = true
                    }

                    Menu.SELECT_ALL, Menu.UNSELECT_ALL -> {
                        cb.onSelectAll()
                    }

                    Menu.ACTION -> {
                        showActionSheet = true
                    }

                    Menu.PRINT -> {
                        showDatePicker = true
                    }

                    else -> {}
                }
            }
        )
        HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
    }
    TeacherDashboardActionSheet(
        showActionSheet = showActionSheet,
        onDismissRequest = { showActionSheet = it },
        uiState = uiState,
        itemList = uiState.itemSelected,
        cb = cb
    )
    TeacherDashboardFilterSheet(
        isShowSheet = showFilter,
        uiState = uiState,
        onDismissRequest = { showFilter = it },
        onApply = cb.onUpdateFilter
    )
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
            cb.onUpdateExportDateRanges(emptyList())
        }
    )
}


private fun getMenu(uiState: TeacherDashboardUiState): List<Menu> {
    return buildList {
        add(Menu.PRINT)
        val isSelecting = uiState.itemSelected.isNotEmpty()
        val isAllSelected = uiState.isAllSelected
        if (isSelecting) {
            if (isAllSelected) {
                add(Menu.UNSELECT_ALL)
            } else {
                add(Menu.SELECT_ALL)
            }
        }
        add(Menu.FILTER)
        if (isSelecting) {
            add(Menu.ACTION)
        }
    }
}
