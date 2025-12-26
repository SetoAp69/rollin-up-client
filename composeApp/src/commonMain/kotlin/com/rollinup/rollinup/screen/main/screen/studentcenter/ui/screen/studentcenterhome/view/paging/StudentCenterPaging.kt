package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.StudentCenterActionSheet

@Composable
fun StudentCenterPaging(
    pagingData: LazyPagingItems<UserEntity>,
    cb: StudentCenterCallback,
    onNavigateTo: (String) -> Unit,
) {
    var showAction by remember { mutableStateOf(false) }
    var selectedItem: UserEntity? by remember { mutableStateOf(null) }

    PagingColumn(
        pagingData = pagingData,
        itemContent = { item ->
            StudentCenterPagingItem(
                item = item,
                onAction = {
                    selectedItem = it
                    showAction = true
                }
            )
        },
        contentPadding = screenPaddingValues,
        loadingContent = {
            StudentCenterPagingItemLoading()
        },
        onRefresh = cb.onRefresh
    )
    selectedItem?.let { item ->
        StudentCenterActionSheet(
            showSheet = showAction,
            item = item,
            onDismissRequest = { showAction = it },
            onNavigateTo = onNavigateTo
        )
    }
}