package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.apiservice.model.common.Role
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardHeader
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.list.AttendancePaging

@Composable
fun TeacherDashboardContentMobile(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    onNavigateTo: (String) -> Unit,
) {
    var selectedAttendance by remember { mutableStateOf(AttendanceByClassEntity()) }
    var showActionSheet by remember { mutableStateOf(false) }
    val selectedList = uiState.itemSelected.ifEmpty { listOf(selectedAttendance) }

    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        Box(
            modifier = Modifier
                .padding(screenPaddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                TeacherDashboardHeader(
                    isLoading = uiState.isLoadingHeader,
                    userLoginEntity = uiState.user
                )
                Spacer(12.dp)
                TeacherDashboardQuickAccess(
                    onPermit = { onNavigateTo(MainRoute.PermitRoute.withRole(Role.TEACHER)) },
                    onStudent = { onNavigateTo(MainRoute.StudentCenterRoute.route) },
                    onStudentAttendance = {
                        onNavigateTo(MainRoute.AttendanceRoute.route)
                    },
                )
                Spacer(12.dp)
                AttendancePaging(
                    pagingData = pagingData,
                    cb = cb,
                    uiState = uiState,
                    onClickAction = {
                        selectedAttendance = it
                        showActionSheet = true
                    }
                )
            }
        }
    }

    TeacherDashboardActionSheet(
        showActionSheet = showActionSheet,
        onDismissRequest = { showActionSheet = it },
        uiState = uiState,
        itemList = selectedList,
        cb = cb
    )
}