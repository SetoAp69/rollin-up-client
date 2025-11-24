package com.rollinup.rollinup.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.apiservice.model.attendance.AttendanceByClassEntity
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardCallback
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardContentMobile
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.viwmodel.TeacherDashboardViewModel
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view.TeacherDashboardStateHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeacherDashboardScreen(
    onShowSnackBar: OnShowSnackBar,
    onNavigateTo: (String) -> Unit,
) {
    val viewModel: TeacherDashboardViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val cb = viewModel.getCallback()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    TeacherDashboardContent(
        uiState = uiState,
        cb = cb,
        pagingData = pagingData,
        onShowSnackBar = onShowSnackBar,
        onNavigateTo = onNavigateTo
    )
}

@Composable
fun TeacherDashboardContent(
    uiState: TeacherDashboardUiState,
    cb: TeacherDashboardCallback,
    pagingData: LazyPagingItems<AttendanceByClassEntity>,
    onShowSnackBar: OnShowSnackBar,
    onNavigateTo: (String) -> Unit,
) {
    TeacherDashboardStateHandler(
        uiState = uiState,
        onRefresh = cb.onRefresh,
        onResetMessageState = cb.onResetMessageState,
        onShowSnackBar = onShowSnackBar
    )
    if (getPlatform().isMobile()) {
        TeacherDashboardContentMobile(
            uiState = uiState,
            cb = cb,
            pagingData = pagingData,
            onNavigateTo = onNavigateTo
        )
    } else {
        TeacherDashboardContentDesktop(
            onNavigateTo = onNavigateTo,
            uiState = uiState,
            cb = cb
        )
    }
}

