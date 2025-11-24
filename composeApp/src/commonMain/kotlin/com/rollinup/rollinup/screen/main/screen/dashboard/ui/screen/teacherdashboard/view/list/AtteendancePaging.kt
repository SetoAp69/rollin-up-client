package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.list

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun AttendancePaging(
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    cb: TeacherDashboardCallback,
    uiState: TeacherDashboardUiState,
    onClickAction: (AttendanceByClassEntity) -> Unit,
) {
    Column {
        AttendanceListHeader(
            uiState = uiState,
            cb = cb
        )
        PagingColumn(
            pagingData = pagingData,
            itemContent = { item ->
                AttendanceListItem(
                    item = item,
                    cb = cb,
                    uiState = uiState,
                    onClickAction = onClickAction
                )
            },
            loadingContent = {
                AttendanceListItemLoading()
            },
            onRefresh = cb.onRefresh,
            contentPadding = screenPaddingValues,
        )
    }
}