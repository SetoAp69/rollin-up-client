package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.component.scaffold.Scaffold
import com.rollinup.rollinup.component.spacer.screenPadding
import com.rollinup.rollinup.component.tab.TabRow
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.paging.TeacherPermitPaging

@Composable
fun TeacherPermitMobileContent(
    onNavigateUp: () -> Unit,
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
    pagingData: LazyPagingItems<PermitByClassEntity>,
) {
    Scaffold(
        topBar = {
            TeacherPermitTopAppBar(
                onNavigateUp = onNavigateUp,
                uiState = uiState,
                cb = cb
            )
        },
    ) {
        TeacherPermitMobileContent(
            pagingData = pagingData,
            uiState = uiState,
            cb = cb
        )
    }

}

@Composable
fun TeacherPermitMobileContent(
    uiState: TeacherPermitUiState,
    pagingData: LazyPagingItems<PermitByClassEntity>,
    cb: TeacherPermitCallback,
) {
    val pagerState = rememberPagerState(
        initialPage = PermitTab.entries.indexOf(PermitTab.ACTIVE),
        pageCount = { PermitTab.entries.size })


    LaunchedEffect(pagerState.currentPage) {
        cb.onTabChange(pagerState.currentPage)
    }

    LaunchedEffect(uiState.currentTabIndex){
        pagerState.scrollToPage(uiState.currentTabIndex)
    }

    Column {
        Column(
            modifier = Modifier.padding(screenPadding)
        ) {
            TabRow(
                tabList = uiState.tabList,
                currentTab = uiState.currentTabIndex,
                onTabChange = {
                    cb.onTabChange(it)
                }

            )
        }

        HorizontalPager(
            state = pagerState,
            pageContent = { page ->
                TeacherPermitPaging(
                    pagingData = pagingData,
                    uiState = uiState,
                    cb = cb
                )
            },
        )
    }
}