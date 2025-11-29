package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view.paging

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByStudentEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues

@Composable
fun AttendanceByStudentPaging(
    pagingData: LazyPagingItems<AttendanceByStudentEntity>,
    onClickItem: (AttendanceByStudentEntity) -> Unit,
    onRefresh: () -> Unit,
) {
    PagingColumn(
        pagingData = pagingData,
        itemContent = { item ->
            AttendanceByStudentPagingItem(item = item, onClickItem = onClickItem)
        },
        loadingContent = {
            AttendanceByStudentPagingLoading()
        },
        onRefresh = onRefresh,
        contentPadding = screenPaddingValues
    )
}