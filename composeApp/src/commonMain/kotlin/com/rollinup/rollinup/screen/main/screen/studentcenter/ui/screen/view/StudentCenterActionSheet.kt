package com.rollinup.rollinup.screen.main.screen.studentcenter.ui.screen.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.profile.profilepopup.view.ProfileDialog
import com.rollinup.rollinup.screen.main.screen.studentcenter.model.StudentCenterAction

@Composable
fun StudentCenterActionSheet(
    showSheet: Boolean,
    item: UserEntity,
    onDismissRequest: (Boolean) -> Unit,
    onNavigateTo: (String) -> Unit,
) {
    val actions = StudentCenterAction.entries

    BottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest
    ) {
        actions.fastForEach { action ->
            ActionButton(
                label = action.label,
                icon = action.icon,
                onClick = {
                    when (action) {
                        StudentCenterAction.PROFILE -> {
//                            onNavigateTo(item.id)
                        }

                        StudentCenterAction.ATTENDANCE -> {
//                            onNavigateTo(item.id)
                        }
                    }
                    onDismissRequest(false)
                }
            )
        }
    }
}

@Composable
fun StudentActionDropdown(
    showSheet: Boolean,
    item: UserEntity,
    onDismissRequest: (Boolean) -> Unit,
) {
    val actions = StudentCenterAction.entries
    var showProfile by remember { mutableStateOf(false) }
    var showAttendance by remember { mutableStateOf(false) }

    BottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest
    ) {
        actions.fastForEach { action ->
            ActionButton(
                label = action.label,
                icon = action.icon,
                onClick = {
                    when (action) {
                        StudentCenterAction.PROFILE -> {
                            showProfile = true
                        }

                        StudentCenterAction.ATTENDANCE -> {
                            showAttendance = true
                        }
                    }
                    onDismissRequest(false)
                }
            )
        }
    }

    ProfileDialog(
        id = item.id,
        isShowDialog = showProfile,
        onDismissRequest = { showProfile = it }
    )
}