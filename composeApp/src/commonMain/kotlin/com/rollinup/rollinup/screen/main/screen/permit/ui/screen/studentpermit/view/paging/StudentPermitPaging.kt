package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view.StudentPermitActionSheet

@Composable
fun StudentPermitPaging(
    pagingData: LazyPagingItems<PermitByStudentEntity>,
    cb: StudentPermitCallback,
) {
    var itemSelected: PermitByStudentEntity? by remember { mutableStateOf(null) }
    var showAction by remember { mutableStateOf(false) }

    PagingColumn(
        pagingData = pagingData,
        itemContent = { item ->
            StudentPermitItem(
                item = item,
                onClickAction = {
                    itemSelected = it
                    showAction = true
                }
            )
        },
        loadingContent = {
            StudentPermitItemLoading()
        },
        onRefresh = cb.onRefresh,
        contentPadding = screenPaddingValues,
    )
    itemSelected?.let {
        StudentPermitActionSheet(
            isShowSheet = showAction,
            onDismissRequest = { state -> showAction = state },
            item = it,
            cb = cb,
        )
    }
}