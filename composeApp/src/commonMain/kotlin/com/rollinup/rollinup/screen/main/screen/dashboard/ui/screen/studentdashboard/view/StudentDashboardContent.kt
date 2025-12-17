package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.lumberjack.core.L
import com.rollinup.apiservice.model.common.Role
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.camera.CameraView
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.location.LocationHandler
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.permitform.view.PermitForm
import com.rollinup.rollinup.component.pullrefresh.PullRefresh
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.LocalGeneralSetting
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.screen.dashboard.ui.component.DashboardCalendar
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardHeader
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation.AttendanceRoute
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardQuickAccessCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState
import dev.jordond.compass.Coordinates

@Composable
fun StudentDashboardContent(
    onShowSnackBar: OnShowSnackBar,
    uiState: StudentDashboardUiState,
    cb: StudentDashboardCallback,
    onNavigateTo: (String) -> Unit,
) {
    val generalSetting = LocalGeneralSetting.current
    var showDetail: Boolean by remember { mutableStateOf(false) }
    var showPermitForm: Boolean by remember { mutableStateOf(false) }
    var showCamera: Boolean by remember { mutableStateOf(false) }
    var isTracking by remember { mutableStateOf(false) }
    val quickAccessCallback = StudentDashboardQuickAccessCallback(
        onCreatePermit = { showPermitForm = true },
        onCheckIn = {
            showCamera = true
        },
        onGoToPermitHistory = {
            uiState.user?.id?.let {
                onNavigateTo(MainRoute.PermitRoute.withRole(Role.STUDENT))
            }
        },
        onGoToAttendanceHistory = {
            uiState.user?.id?.let {
                onNavigateTo(AttendanceRoute.AttendanceByStudentRoute.navigate(it))
            }
        },
    )
    LaunchedEffect(Unit){
        isTracking = true
    }

    LocationHandler(
        onLocationChanges = {
            val target = Coordinates(generalSetting.latitude, generalSetting.longitude)
            val radius = generalSetting.radius
            cb.onUpdateLocation(it, target, radius)
        },
        startTracking = isTracking
    )

    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        PullRefresh(
            isRefreshing = false,
            onRefresh = cb.onRefresh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenPaddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StudentDashboardHeader(uiState = uiState)
                StudentDashboardQuickAccess(
                    uiState = uiState,
                    cb = quickAccessCallback
                )
                DashboardCalendar(
                    attendanceList = uiState.attendanceList,
                    isLoading = uiState.isLoadingCalendar,
                    onClickDay = {
                        it?.let {
                            cb.onShowAttendanceDetail(it.id)
                            showDetail = true
                        }
                    },
                    title = "Attendance"
                )
            }
        }
    }

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = { showDetail = it },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )

    PermitForm(
        showPermitForm = showPermitForm,
        onDismissRequest = { showPermitForm = it },
        onSuccess = {
            onShowSnackBar("Success, Permit data successfully submitted", true)
            showPermitForm = false
        }
    )

    CameraView(
        onDismissRequest = { showCamera = it },
        onCapture = { photo ->
            showCamera = false
            cb.onCheckIn(photo, uiState.currentLocation!!)
        },
        notification = {
            Chip(
                text = "Make sure your face can be identified easily",
                severity = Severity.SECONDARY,
                textStyle = Style.body
            )
        },
        isShowCamera = showCamera,
        onError = {
            onShowSnackBar("Error, failed to take photo please try again", false)
        },
        errorMsg = "Error, failed to take photo please try again",
    )
}