package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.filter.FilterSelector
import com.rollinup.rollinup.component.filter.FilterSelectorBottomSheet
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_gender

@Composable
fun StudentCenterFilterSheet(
    showFilter: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onApply: (StudentCenterFilterData) -> Unit,
    uiState: StudentCenterUiState,
) {
    var tempData by remember { mutableStateOf(uiState.filterData) }

    LaunchedEffect(uiState.filterData) {
        if (uiState.filterData != tempData) tempData = uiState.filterData
    }

    FilterSelectorBottomSheet(
        isShowSheet = showFilter,
        onDismissRequest = onDismissRequest,
        onApply = {
            onApply(tempData)
        },
        onReset = { tempData = StudentCenterFilterData() },
        showReset = tempData != StudentCenterFilterData(),
    ) {
        FilterSelector(
            isLoading = uiState.isLoadingFilter,
            title = stringResource(Res.string.label_class),
            options = uiState.classOptions,
            value = tempData.classX,
            onValueChange = {
                tempData = tempData.copy(classX = it)
            }
        )

        FilterSelector(
            isLoading = uiState.isLoading,
            title = stringResource(Res.string.label_gender),
            options = uiState.genderOptions,
            value = tempData.gender,
            onValueChange = {
                tempData = tempData.copy(gender = it)
            }
        )
    }
}