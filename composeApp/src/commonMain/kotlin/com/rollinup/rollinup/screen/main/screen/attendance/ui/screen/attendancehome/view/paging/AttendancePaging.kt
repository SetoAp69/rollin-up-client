package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceCallback
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view.AttendanceHomeActionSheet

@Composable
fun AttendancePaging(
    onClickAction: (AttendanceByClassEntity, AttendanceHomeAction) -> Unit,
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    cb: AttendanceCallback,
) {
    var selectedItem by remember { mutableStateOf<AttendanceByClassEntity?>(null) }
    var showActionSheet by remember { mutableStateOf(false) }

    PagingColumn(
        contentPadding = screenPaddingValues,
        pagingData = pagingData,
        itemContent = { item ->
            AttendancePagingItem(
                item = item,
                onClickAction = {
                    selectedItem = item
                    showActionSheet = true
                }
            )
        },
        loadingContent = {
            AttendancePagingLoading()
        },
        onRefresh = cb.onRefresh,
    )
    selectedItem?.let { item ->
        AttendanceHomeActionSheet(
            showActionSheet = showActionSheet,
            onDismissRequest = { showActionSheet = it },
            onClickAction = { action ->
                onClickAction(item, action)
            }
        )
    }
}