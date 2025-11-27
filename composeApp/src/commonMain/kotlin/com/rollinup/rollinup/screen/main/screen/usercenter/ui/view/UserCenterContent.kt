package com.rollinup.rollinup.screen.main.screen.usercenter.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rollinup.rollinup.component.profile.profilepopup.view.ProfileDialog
import com.rollinup.rollinup.component.spacer.Spacer
import com.rollinup.rollinup.component.theme.Style
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.usercenter.model.UserCenterCallback
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.component.createedituser.view.CreateEditUserDialog
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.uistate.UserCenterUiState
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table.UserCenterTable
import com.rollinup.rollinup.screen.main.screen.usercenter.ui.view.table.UserCenterTableFilter

@Composable
fun UserCenterContent(
    uiState: UserCenterUiState,
    cb: UserCenterCallback,
) {
    var selectedId by remember { mutableStateOf("") }
    var showEdit by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "User Center",
            style = Style.headerBold,
            color = theme.primary

        )
        Spacer(12.dp)
        UserCenterTableFilter(
            uiState = uiState,
            onSearch = cb.onSearch,
            onFilter = cb.onFilter
        )
        Spacer(12.dp)
        UserCenterTable(
            uiState = uiState,
            cb = cb,
            onShowEdit = {
                selectedId = it
                showEdit = true
            },
            onShowDetail = {
                selectedId = it
                showDetail = true
            },
        )
    }

    ProfileDialog(
        id = selectedId,
        isShowDialog = showDetail,
        onDismissRequest = { showDetail = it }
    )

    CreateEditUserDialog(
        showDialog = showEdit,
        onDismissRequest = { showEdit = it },
        id = selectedId
    )
}