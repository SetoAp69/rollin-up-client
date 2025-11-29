package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.rollinup.component.attendancedetail.AttendanceDetailDialog
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancebystudent.AttendanceByStudentCallback
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.uistate.AttendanceByStudentUiState
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.paging.AttendanceByStudentPaging

@Composable
fun AttendanceByStudentScreenContent(
    onNavigateUp: () -> Unit,
    uiState: AttendanceByStudentUiState,
    pagingData: LazyPagingItems<AttendanceByStudentEntity>,
    cb: AttendanceByStudentCallback,
) {
    Scaffold(
        topBar = {
            AttendanceByStudentTopBar(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                onSelectStatus = cb.onSelectStatus
            )
        }
    ) {
        AttendanceByStudentScreenContent(
            uiState = uiState,
            pagingData = pagingData,
            cb = cb
        )
    }
}

@Composable
fun AttendanceByStudentScreenContent(
    uiState: AttendanceByStudentUiState,
    pagingData: LazyPagingItems<AttendanceByStudentEntity>,
    cb: AttendanceByStudentCallback,
) {
    var showDetail by remember { mutableStateOf(false) }

    Column {
        Column(modifier = Modifier.padding(screenPadding)) {
            AttendanceByStudentProfileMobile(
                user = uiState.student,
                isLoading = uiState.isLoadingProfile
            )
            Spacer(12.dp)
            AttendanceByStudentSummary(
                isLoading = uiState.isLoadingSummary,
                summary = uiState.summary
            )
            Spacer(12.dp)
            HorizontalDivider(thickness = 1.dp, color = theme.lineStroke)
        }
        AttendanceByStudentPaging(
            pagingData = pagingData,
            onClickItem = { item ->
                cb.onGetDetail(item.id)
                showDetail = true
            },
            onRefresh = cb.onRefresh
        )
    }

    AttendanceDetailDialog(
        showDialog = showDetail,
        onDismissRequest = { showDetail = it },
        isLoading = uiState.isLoadingDetail,
        detail = uiState.attendanceDetail
    )
}