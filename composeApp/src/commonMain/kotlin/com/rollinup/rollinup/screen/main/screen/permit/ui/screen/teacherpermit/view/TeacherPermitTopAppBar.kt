package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.rollinup.component.export.ExportAlertDialog
import com.rollinup.rollinup.component.model.Menu
import com.rollinup.rollinup.component.topbar.TopBar
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.paging.TeacherPermitPagingActionSheet
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_permit

@Composable
fun TeacherPermitTopAppBar(
    onNavigateUp: () -> Unit,
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
) {
    var showFilter by remember { mutableStateOf(false) }
    var showAction by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    TopBar(
        onSearch = cb.onSearch,
        onClickMenu = { menu ->
            when (menu) {
                Menu.FILTER -> showFilter = true
                Menu.UNSELECT_ALL, Menu.SELECT_ALL -> {
                    cb.onSelectAll()
                }

                Menu.ACTION -> showAction = true
                Menu.PRINT -> showExportDialog = true


                else -> {}
            }
        },
        menu = getMenu(uiState),
        onNavigateUp = onNavigateUp,
        title = stringResource(Res.string.label_permit),
    )
    TeacherPermitFilterSheet(
        showSheet = showFilter,
        onDismissRequest = { showFilter = it },
        uiState = uiState,
        onApply = cb.onFilter
    )
    TeacherPermitPagingActionSheet(
        showSheet = showAction,
        isActive = uiState.currentTab == PermitTab.ACTIVE,
        onDismissRequest = { showAction = it },
        items = uiState.itemSelected,
        cb = cb
    )
    ExportAlertDialog(
        isShowDialog = showExportDialog,
        fileName = stringResource(Res.string.label_permit),
        onDismissRequest = { showExportDialog = it },
        onConfirm = cb.onExportFile
    )
}


private fun getMenu(uiState: TeacherPermitUiState): List<Menu> {
    return buildList {
        if (uiState.itemSelected.isNotEmpty()) {
            if (uiState.isAllSelected) {
                add(Menu.SELECT_ALL)
            } else {
                add(Menu.UNSELECT_ALL)
            }
        }
        add(Menu.FILTER)
        add(Menu.PRINT)
        if (uiState.itemSelected.isNotEmpty()) {
            add(Menu.ACTION)
        }
    }
}