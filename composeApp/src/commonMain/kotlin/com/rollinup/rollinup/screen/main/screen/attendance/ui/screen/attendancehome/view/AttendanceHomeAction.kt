package com.rollinup.rollinup.screen.main.screen.attendance.ui.screen.attendancehome.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.util.fastForEach
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome.AttendanceHomeAction

@Composable
fun AttendanceHomeActionSheet(
    showActionSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickAction: (AttendanceHomeAction) -> Unit,
) {
    val actions = AttendanceHomeAction.entries

    BottomSheet(
        onDismissRequest = onDismissRequest,
        isShowSheet = showActionSheet,
    ) {
        actions.fastForEach { action ->
            ActionButton(
                label = action.label,
                icon = action.icon
            ) {
                onClickAction(action)
                onDismissRequest(false)
            }
        }
    }
}

@Composable
fun AttendanceHomeActionDropdown(
    showDropdown: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    onClickAction: (AttendanceHomeAction) -> Unit,
) {
    val actions = AttendanceHomeAction.entries

    DropDownMenu(
        isShowDropDown = showDropdown,
        onDismissRequest = onDismissRequest
    ) {
        actions.fastForEach { action ->
            DropDownMenuItem(
                label = action.label,
                icon = action.icon
            ) {
                onClickAction(action)
                onDismissRequest(false)
            }
        }
    }
}
