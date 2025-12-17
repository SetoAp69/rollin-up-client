package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.component.pagination.PagingColumn
import com.rollinup.rollinup.component.spacer.screenPaddingValues
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState

@Composable
fun TeacherPermitPaging(
    pagingData: LazyPagingItems<PermitByClassEntity>,
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
) {
    var selectedItem by remember { mutableStateOf<PermitByClassEntity?>(null) }
    var showActionSheet by remember { mutableStateOf(false) }

    PagingColumn(
        pagingData = pagingData,
        itemContent = { item ->
            TeacherPermitPagingItem(
                item = item,
                uiState = uiState,
                cb = cb,
                onClickAction = {
                    selectedItem = it
                    showActionSheet = true
                }
            )
        },
        loadingContent = {
            TeacherPermitPagingItemLoading()
        },
        onRefresh = cb.onRefresh,
        contentPadding = screenPaddingValues,
    )

    selectedItem?.let {
        TeacherPermitPagingActionSheet(
            showSheet = showActionSheet,
            isActive = uiState.currentTab == PermitTab.ACTIVE,
            onDismissRequest = { showActionSheet = it },
            items = listOf(it),
            cb = cb
        )
    }
}