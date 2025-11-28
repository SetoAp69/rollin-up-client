package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.uistate.StudentCenterUiState
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view.paging.StudentCenterPaging

@Composable
fun StudentCenterMobileContent(
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
    onNavigateTo: (String) -> Unit,
    pagingData: LazyPagingItems<UserEntity>,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            StudentCenterTopBar(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb
            )
        }
    ) {
        StudentCenterPaging(
            pagingData = pagingData,
            cb = cb,
            onNavigateTo = onNavigateTo
        )
    }
}