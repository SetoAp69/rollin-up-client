package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.view

import GeofenceHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.permitform.view.PermitForm
import com.rollinup.rollinup.component.pullrefresh.PullRefresh
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.component.theme.LocalGlobalSetting
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.screen.dashboard.ui.component.DashboardCalendar
import com.rollinup.rollinup.screen.dashboard.ui.screen.studentdashboard.view.StudentDashboardHeader
import com.rollinup.rollinup.screen.main.navigation.MainRoute
import com.rollinup.rollinup.screen.main.screen.attendance.ui.navigation.AttendanceRoute
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.model.studentdashboard.StudentDashboardQuickAccessCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.uistate.StudentDashboardUiState

@Composable
fun StudentDashboardContent(
    onShowSnackBar: OnShowSnackBar,
    uiState: StudentDashboardUiState,
    cb: StudentDashboardCallback,
    onNavigateTo: (String) -> Unit,
    onRefreshSetting: () -> Unit,
) {
    val generalSetting = LocalGlobalSetting.current

    var showDetail: Boolean by remember { mutableStateOf(false) }
    var showPermitForm: Boolean by remember { mutableStateOf(false) }
    var showCamera: Boolean by remember { mutableStateOf(false) }
    var showConfirmDialog: Boolean by remember { mutableStateOf(false) }

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

    var startTracking by remember { mutableStateOf(false) }

    LaunchedEffect(generalSetting) {
        L.wtf {
            generalSetting.radius.toString()
        }
    }

    LaunchedEffect(Unit) {
        startTracking = true
    }

    GeofenceHandler(
        startTracking = startTracking
    ) { location, isValid ->
        cb.onUpdateLocation(location, isValid)
    }
    Scaffold(
        showLoadingOverlay = uiState.isLoadingOverlay
    ) {
        PullRefresh(
            isRefreshing = uiState.isLoadingRefresh,
            onRefresh = {
                cb.onRefresh()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(screenPaddingValues)
                    .verticalScroll(rememberScrollState()),
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
        onDismissRequest = {
            showCamera = it
            cb.onUpdateTempPhoto(null)
        },
        onCapture = { photo ->
            cb.onUpdateTempPhoto(photo)
            showConfirmDialog = true
            showCamera = false
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

    uiState.tempPhoto?.let {
        CheckInConfirmDialog(
            onConfirm = {
                cb.onCheckIn(it, uiState.currentLocation!!)
            },
            onDismissRequest = { state -> showConfirmDialog = state },
            showDialog = showConfirmDialog
        )
    }
}