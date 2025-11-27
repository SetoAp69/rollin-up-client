package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.tab.TabList
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.uistate.StudentPermitUiState
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view.paging.StudentPermitPaging

@Composable
fun StudentPermitContent(
    uiState: StudentPermitUiState,
    pagingData: LazyPagingItems<PermitByStudentEntity>,
    cb: StudentPermitCallback,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            StudentPermitTopBar(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb
            )
        }
    ) {
        val pagerState = rememberPagerState(
            initialPage = PermitTab.entries.indexOf(PermitTab.ACTIVE),
            pageCount = { PermitTab.entries.size })

        LaunchedEffect(pagerState.currentPage) {
            cb.onTabChange(pagerState.currentPage)
        }

        Column {
            Column(
                modifier = Modifier.padding(screenPadding)
            ) {
                TabList(
                    tabList = uiState.tabList,
                    currentIndex = uiState.currentTabIndex,
                    onTabChange = cb.onTabChange
                )
            }

            HorizontalPager(
                state = pagerState,
                pageContent = { page ->
                    StudentPermitPaging(
                        pagingData = pagingData,
                        cb = cb
                    )
                },
            )
        }
    }
}