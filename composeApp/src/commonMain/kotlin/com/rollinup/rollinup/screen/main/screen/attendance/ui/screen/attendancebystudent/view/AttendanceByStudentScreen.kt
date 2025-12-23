package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.component.model.OnShowSnackBar
import com.rollinup.rollinup.component.model.Platform.Companion.isMobile
import com.rollinup.rollinup.component.utils.getPlatform
import com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancebystudent.viewmodel.AttendanceByStudentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AttendanceByStudentScreen(
    id: String?,
    onNavigateUp: () -> Unit,
    onShowSnackBar: OnShowSnackBar,
) {
    val viewModel: AttendanceByStudentViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val cb = viewModel.getCallback()
    val isMobile = getPlatform().isMobile()

    LaunchedEffect(Unit) {
        id?.let {
            viewModel.init(it, isMobile)
        }
    }

    AttendanceByStudentScreenContent(
        onNavigateUp = onNavigateUp,
        uiState = uiState,
        pagingData = pagingData,
        cb = cb,
        onShowSnackBar = onShowSnackBar
    )
}