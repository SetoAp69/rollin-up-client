package com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard.TeacherDashboardFilterData
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.uistate.TeacherDashboardUiState

@Composable
fun TeacherDashboardFilterSheet(
    isShowSheet: Boolean,
    uiState: TeacherDashboardUiState,
    onDismissRequest: (Boolean) -> Unit,
    onApply: (TeacherDashboardFilterData) -> Unit,
) {
    var tempFilterData by remember { mutableStateOf(uiState.filterData) }

    LaunchedEffect(uiState.filterData) {
        tempFilterData = uiState.filterData
    }

    FilterSelectorBottomSheet(
        isShowSheet = isShowSheet,
        onDismissRequest = onDismissRequest,
        onApply = {
            onApply(tempFilterData)
        },
        onReset = {
            tempFilterData = TeacherDashboardFilterData(
                classX = tempFilterData.classX,
                searchQuery = tempFilterData.searchQuery
            )
        },
        showReset = tempFilterData.status.isNotEmpty(),
    ) {
        FilterSelector(
            isLoading = false,
            title = "Status",
            options = uiState.statusOptions,
            value = tempFilterData.status,
            onValueChange = {
                tempFilterData = tempFilterData.copy(status = it)
            }
        )
    }
}