package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.common.model.Severity
import com.rollinup.common.utils.Utils.toLocalDateTime
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.tab.TabList
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.view.PermitApprovalFormDialog
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view.PermitDetailDialog
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState

@Composable
fun TeacherPermitTableContent(
    uiState: TeacherPermitUiState,
    cb: TeacherPermitCallback,
) {
    var idSelected: List<String> by remember { mutableStateOf(emptyList<String>()) }
    var showDetail: Boolean by remember { mutableStateOf(false) }
    var showApproval: Boolean by remember { mutableStateOf(false) }

    Table(
        items = uiState.itemList,
        columns = getColumn(),
        isLoading = uiState.isLoading,
        onToggleSelectAll = cb.onSelectAll,
        onSelectItem = cb.onUpdateSelection,
        itemSelected = uiState.itemSelected,
        showSelection = uiState.currentTab == PermitTab.ACTIVE,
        headerContent = {
            TeacherPermitTableHeader(
                uiState = uiState,
                onTabChange = cb.onTabChange
            )
        },
        dropDownMenu = { state ->
            TeacherPermitDropDown(
                showDropDown = state.expanded,
                isActive = uiState.currentTab == PermitTab.ACTIVE,
                onDismissRequest = state.onDismissRequest,
                items = state.selectedItem,
                onDetail = {
                    idSelected = listOf(it)
                    showDetail = true
                },
                onApproval = {
                    idSelected = it
                    showApproval = true
                }
            )
        }
    )

    PermitDetailDialog(
        id = idSelected.first(),
        showDialog = showDetail,
        onDismissRequest = { showDetail = it }
    )

    PermitApprovalFormDialog(
        showDialog = showApproval,
        selectedId = idSelected,
        onDismissRequest = { showApproval = it }
    )
}

@Composable
private fun TeacherPermitTableHeader(
    uiState: TeacherPermitUiState,
    onTabChange: (Int) -> Unit,
) {
    TabList(
        tabList = uiState.tabList,
        currentIndex = uiState.currentTabIndex,
        onTabChange = onTabChange
    )
}


private fun getColumn(): List<TableColumn<PermitByClassEntity>> {
    return listOf(
        TableColumn("Full name") {
            Text(
                text = it.student.name,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Class") {
            Text(
                text = it.student.xClass,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Type", 0.5f) {
            Chip(
                text = it.type,
                severity = Severity.SECONDARY
            )
        },
        TableColumn("Status", 0.1f) {
            Chip(
                text = it.approvalStatus.label,
                severity = it.approvalStatus.severity
            )
        },
        TableColumn(title = "Reason", weight = 0.5f) {
            Text(
                text = it.reason ?: "-",
                style = Style.body,
                color = theme.bodyText
            )
        },

        TableColumn("Start") {
            Text(
                text = it.startTime.toLocalDateTime().toString(),
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("End") {
            Text(
                text = it.endTime.toLocalDateTime().toString(),
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn("Requested at") {
            Text(
                text = it.createdAt.toLocalDateTime().toString(),
                style = Style.body,
                color = theme.bodyText
            )
        }
    )
}