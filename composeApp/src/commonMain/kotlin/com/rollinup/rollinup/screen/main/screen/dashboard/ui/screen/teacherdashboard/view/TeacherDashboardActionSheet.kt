package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardAction
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun TeacherDashboardActionDropDown(
    showDropDown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    itemList: List<AttendanceByClassEntity>,
    onClickAction: (TeacherDashboardAction) -> Unit,
) {
    val actions = TeacherDashboardAction.entries.filter { action ->
        action.show(itemList.map { it.attendance?.status })
    }

    DropDownMenu(
        isShowDropDown = showDropDown,
        onDismissRequest = onDismissRequest,
    ) {
        if (actions.isEmpty()) {
            DropDownMenuItem(
                label = "No action available",
                onClick = { onDismissRequest(false) }
            )
        } else {
            actions.fastForEach { action ->
                DropDownMenuItem(
                    label = action.label,
                    onClick = {
                        onClickAction(action)
                        onDismissRequest(false)
                    },
                    icon = action.icon,
                    iconTint = theme.primary
                )
            }
        }
    }
}

@Composable
fun TeacherDashboardActionSheet(
    showActionSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    uiState: TeacherDashboardUiState,
    itemList: List<AttendanceByClassEntity>,
    cb: TeacherDashboardCallback,
) {
    val actions = TeacherDashboardAction.entries.filter { action ->
        action.show(itemList.map { it.attendance?.status })
    }

    var showEdit by remember { mutableStateOf(false) }
    var showApproval by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }

    BottomSheet(
        isShowSheet = showActionSheet,
        onDismissRequest = onDismissRequest,
    ) {
        if (actions.isEmpty()) {
            ActionButton(
                label = "No action available",
                onClick = { onDismissRequest(false) }
            )
        } else {
            actions.fastForEach { action ->
                ActionButton(
                    label = action.label,
                    onClick = {
                        when (action) {
                            TeacherDashboardAction.EDIT_DATA -> {
                                cb.onGetDetail(itemList.first())
                                showEdit = true
                            }

                            TeacherDashboardAction.APPROVAL -> {
                                showApproval = true
                            }

                            TeacherDashboardAction.DETAIL -> {
                                cb.onGetDetail(itemList.first())
                                showDetail = true
                            }
                        }
                        onDismissRequest(false)
                    },
                    icon = action.icon,
                    iconTint = theme.primary
                )
            }
        }
    }

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = {
            onDismissRequest(it)
            showDetail = it
        },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )

    TeacherDashboardApprovalSheet(
        showSheet = showApproval,
        onDismissRequest = {
            showApproval = it
            onDismissRequest(it)
        },
        uiState = uiState,
        cb = cb
    )

    TeacherDashboardEditAttendance(
        isShowForm = showEdit,
        onDismissRequest = {
            onDismissRequest(it)
            cb.onResetEditForm()
            showEdit = it
        },
        uiState = uiState,
        cb = cb
    )
}