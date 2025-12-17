package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.button.IconButton
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.filter.TableFilterRow
import com.rollinup.rollinup.component.selector.MultiDropDownSelector
import com.rollinup.rollinup.component.spacer.popupPadding
import com.rollinup.rollinup.component.textfield.SearchTextField
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterFilterData
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_print_line_24

@Composable
fun StudentCenterTableFilter(
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    var showExportDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = theme.popUpBg, shape = RoundedCornerShape(12.dp))
            .padding(popupPadding),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier.width(320.dp)
        ) {
            SearchTextField(
                title = "Search",
                onValueChange = {},
                value = uiState.searchQuery,
                placeholder = "Search",
                onSearch = cb.onSearch
            )
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                icon = Res.drawable.ic_print_line_24,
                onClick = {
                    showExportDialog = true
                }
            )
            StudentCenterTableFilterRow(
                uiState = uiState,
                cb = cb
            )
        }
    }

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = "Student",
        onDismissRequest = {showExportDialog = it},
        onConfirm = cb.onExportFile
    )
}

@Composable
private fun StudentCenterTableFilterRow(
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    TableFilterRow(
        onReset = { cb.onFilter(StudentCenterFilterData()) },
        showReset = uiState.filterData != StudentCenterFilterData()
    ) {
        MultiDropDownSelector(
            title = "Class",
            value = uiState.filterData.classX,
            options = uiState.classOptions,
            isLoading = uiState.isLoadingFilter,
            onValueChange = {
                cb.onFilter(
                    uiState.filterData.copy(
                        classX = it
                    )
                )
            },
            placeHolder = "All"
        )

        MultiDropDownSelector(
            title = "Gender",
            value = uiState.filterData.gender,
            options = uiState.genderOptions,
            onValueChange = {
                cb.onFilter(
                    uiState.filterData.copy(
                        gender = it
                    )
                )
            },
            placeHolder = "All"
        )
    }
}