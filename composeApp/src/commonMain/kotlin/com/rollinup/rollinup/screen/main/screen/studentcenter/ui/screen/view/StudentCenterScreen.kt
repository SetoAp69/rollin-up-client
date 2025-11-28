package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.viewmodel.StudentCenterViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentCenterScreen(
    onNavigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit,
) {
    val viewModel : StudentCenterViewModel = koinViewModel()
    val cb = viewModel.getCallback()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    if(viewModel.isMobile){
        StudentCenterMobileContent(
            uiState = uiState,
            cb = cb,
            onNavigateTo = onNavigateTo,
            pagingData = pagingData,
            onNavigateUp = onNavigateUp
        )
    }else{
        StudentCenterDesktopContent(
            uiState = uiState,
            cb = cb
        )
    }
}