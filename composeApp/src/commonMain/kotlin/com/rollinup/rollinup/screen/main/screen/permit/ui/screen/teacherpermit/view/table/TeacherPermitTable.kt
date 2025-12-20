package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.date.DateText
import com.rollinup.rollinup.component.tab.TabRow
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.PermitTab
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.view.PermitApprovalFormDialog
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view.PermitDetailDialog
import com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.uistate.TeacherPermitUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_created_at
import rollin_up.composeapp.generated.resources.label_end
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_permit_type
import rollin_up.composeapp.generated.resources.label_reason
import rollin_up.composeapp.generated.resources.label_start
import rollin_up.composeapp.generated.resources.label_status

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
        onRefresh = cb.onRefresh,
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

    idSelected.firstOrNull()?.let {
        PermitDetailDialog(
            id = it,
            showDialog = showDetail,
            onDismissRequest = { showDetail = it }
        )
    }

    PermitApprovalFormDialog(
        showDialog = showApproval,
        selectedId = idSelected,
        onDismissRequest = { showApproval = it },
        onSuccess = cb.onRefresh
    )
}

@Composable
private fun TeacherPermitTableHeader(
    uiState: TeacherPermitUiState,
    onTabChange: (Int) -> Unit,
) {
    TabRow(
        tabList = uiState.tabList,
        currentTab = uiState.currentTabIndex,
        onTabChange = onTabChange,
        modifier = Modifier.widthIn(max = 400.dp)
    )
}


@Composable
private fun getColumn(): List<TableColumn<PermitByClassEntity>> {
    return listOf(
        TableColumn(stringResource(Res.string.label_full_name)) {
            Text(
                text = it.student.name,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn(stringResource(Res.string.label_class),0.5f) {
            Text(
                text = it.student.xClass,
                style = Style.body,
                color = theme.bodyText
            )
        },
        TableColumn(stringResource(Res.string.label_permit_type), 0.5f) {
            Chip(
                text = it.type.label,
                severity = Severity.SECONDARY
            )
        },
        TableColumn(stringResource(Res.string.label_status), 0.8f) {
            Chip(
                text = it.approvalStatus.label,
                severity = it.approvalStatus.severity
            )
        },
        TableColumn(title = stringResource(Res.string.label_reason), weight = 0.5f) {
            Text(
                text = it.reason ?: "-",
                style = Style.body,
                color = theme.bodyText
            )
        },

        TableColumn(stringResource(Res.string.label_start)) {
            DateText(
                dateString = it.startTime,
            )
        },
        TableColumn(stringResource(Res.string.label_end)) {
            DateText(
                dateString = it.endTime,
            )
        },
        TableColumn(stringResource(Res.string.label_created_at)) {
            DateText(
                dateString = it.createdAt,
            )
        }
    )
}