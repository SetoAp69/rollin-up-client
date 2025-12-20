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
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_address
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_gender
import rollin_up.composeapp.generated.resources.label_student_id
import rollin_up.composeapp.generated.resources.label_student_list

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
                text = stringResource(Res.string.label_student_list),
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

@Composable
private fun getColumn(): List<TableColumn<UserEntity>> {
    return listOf(
        TableColumn(stringResource(Res.string.label_student_id)) {
            Text(
                text = it.studentId.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_full_name)) {
            Text(
                text = it.fullName.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_class)) {
            Text(
                text = it.classX.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_email)) {
            Text(
                text = it.email.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_address)) {
            Text(
                text = it.address.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_gender)) {
            Chip(
                text = it.gender.label.ifBlank { "-" },
                severity = Severity.SECONDARY
            )
        },
    )
}