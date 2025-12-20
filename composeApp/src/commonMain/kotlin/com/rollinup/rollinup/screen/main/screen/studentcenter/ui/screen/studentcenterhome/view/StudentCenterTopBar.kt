package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterCallback
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_student
import rollin_up.composeapp.generated.resources.label_student_center

@Composable
fun StudentCenterTopBar(
    onNavigateUp: () -> Unit,
    uiState: StudentCenterUiState,
    cb: StudentCenterCallback,
) {
    var showFilter by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    TopBar(
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> {
                    showFilter = true
                }

                Menu.PRINT -> {
                    showExportDialog = true
                }

                else -> {}
            }
        },
        onSearch = cb.onSearch,
        menu = listOf(Menu.PRINT, Menu.FILTER),
        title = stringResource(Res.string.label_student_center),
        onNavigateUp = onNavigateUp
    )

    StudentCenterFilterSheet(
        showFilter = showFilter,
        onDismissRequest = { showFilter = it },
        onApply = cb.onFilter,
        uiState = uiState
    )

    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = stringResource(Res.string.label_student),
        onDismissRequest = { showExportDialog = it },
        onConfirm = cb.onExportFile
    )
}