package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.chip.Chip
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.spacer.itemGap8
import com.rollinup.rollinup.component.table.Table
import com.rollinup.rollinup.component.table.TableColumn
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterAction
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.DeleteAlertDialog
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate.UserCenterUiState
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.label_address
import rollin_up.composeapp.generated.resources.label_class
import rollin_up.composeapp.generated.resources.label_email
import rollin_up.composeapp.generated.resources.label_full_name
import rollin_up.composeapp.generated.resources.label_gender
import rollin_up.composeapp.generated.resources.label_role
import rollin_up.composeapp.generated.resources.label_user_data
import rollin_up.composeapp.generated.resources.label_username

@Composable
fun UserCenterTable(
    uiState: UserCenterUiState,
    cb: UserCenterCallback,
    onShowEdit: (String) -> Unit,
    onShowDetail: (String) -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemSelected by remember { mutableStateOf(emptyList<UserEntity>()) }

    Table(
        items = uiState.itemList,
        headerContent = {
            TableHeaderContent(uiState.itemSelected.size)
        },
        columns = getColumn(),
        isLoading = uiState.isLoadingList,
        itemSelected = uiState.itemSelected,
        onSelectItem = cb.onUpdateSelection,
        onToggleSelectAll = cb.onSelectAll,
        onRefresh = cb.onRefresh
    ) { dropDownState ->
        UserCenterTableDropDown(
            isShowDropdown = dropDownState.expanded,
            onDismissRequest = dropDownState.onDismissRequest,
            item = dropDownState.selectedItem,
            onClickAction = { action ->
                when (action) {
                    UserCenterAction.EDIT_DATA -> {
                        onShowEdit(dropDownState.selectedItem.first().id)
                    }

                    UserCenterAction.DETAIL -> {
                        onShowDetail(dropDownState.selectedItem.first().id)
                    }

                    UserCenterAction.DELETE -> {
                        showDeleteDialog = true
                        itemSelected = dropDownState.selectedItem
                    }
                }
            }
        )
    }

    DeleteAlertDialog(
        items = itemSelected.map { it.userName },
        isShowDialog = showDeleteDialog,
        name = "user",
        onConfirm = {
            cb.onDeleteUser(itemSelected.map { it.id })
            itemSelected = emptyList()
        },
        onCancel = {
            showDeleteDialog = false
            itemSelected = emptyList()
        },
        onDismissRequest = { showDeleteDialog = it },
    )
}

@Composable
private fun TableHeaderContent(itemSelected: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.label_user_data),
            color = theme.bodyText,
            style = Style.title
        )
        Spacer(itemGap8)
        if (itemSelected > 0) {
            Chip(
                text = itemSelected.toString(),
                severity = Severity.SECONDARY
            )
        }
    }
}


@Composable
private fun getColumn(): List<TableColumn<UserEntity>> {
    return listOf(
        TableColumn(stringResource(Res.string.label_username)) {
            Text(
                text = it.userName.ifBlank { "-" },
                color = theme.bodyText,
                style = Style.body
            )
        },
        TableColumn(stringResource(Res.string.label_class), 0.7f) {
            Text(
                text = it.classX.ifBlank { "-" },
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
        TableColumn(stringResource(Res.string.label_gender), 0.5f) {
            Chip(
                text = it.gender.label.ifBlank { "-" },
                severity = Severity.SECONDARY
            )
        },
        TableColumn(stringResource(Res.string.label_role), 0.5f) {
            Chip(
                text = it.role.ifBlank { "-" },
                severity = Severity.SECONDARY
            )
        }
    )
}