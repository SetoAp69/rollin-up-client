package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.uistate.StudentCenterUiState
import com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.studentcenterhome.view.StudentActionDropdown

@Composable
fun StudentCenterTable(
    uiState: StudentCenterUiState,
    onRefresh: () -> Unit,
) {
    Table(
        items = uiState.itemList,
        columns = getColumn(),
        isLoading = uiState.isLoading,
        showSelection = false,
        headerContent = {
            Text(
                text = "Student list",
                style = Style.popupTitle,
                color = theme.textPrimary
            )
        },
        onRefresh = onRefresh,
        dropDownMenu = { state ->
            StudentActionDropdown(
                showSheet = state.expanded,
                item = state.selectedItem.first(),
                onDismissRequest = state.onDismissRequest,
            )
        },
    )
}

private fun getColumn(): List<TableColumn<UserEntity>> {
    return listOf(
        TableColumn("Student Id") {
            Text(
                text = it.studentId.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Full Name") {
            Text(
                text = it.fullName.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Class") {
            Text(
                text = it.classX.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Email") {
            Text(
                text = it.email.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Address") {
            Text(
                text = it.address.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn("Gender") {
            Chip(
                text = it.gender.label.ifBlank { "-" },
                severity = Severity.SECONDARY
            )
        },
    )
}