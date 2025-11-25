package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.list.AttendanceListHeader

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AttendancePaging(
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    cb: TeacherDashboardCallback,
    uiState: TeacherDashboardUiState,
    onClickAction: (AttendanceByClassEntity) -> Unit,
) {
    if(uiState.itemSelected.isNotEmpty()){
        BackHandler {
            cb.onResetSelection()
        }
    }
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
            contentPadding = PaddingValues(vertical = 12.dp),
            loadingContent = {
                AttendanceListItemLoading()
            },
            onRefresh = cb.onRefresh,
        )
    }
}