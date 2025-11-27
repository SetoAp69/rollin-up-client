package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.studentpermit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.permit.PermitByStudentEntity
import com.rollinup.common.model.Severity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.permitform.view.PermitForm
import com.rollinup.rollinup.component.theme.theme
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitAction
import com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit.StudentPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view.PermitDetailDialog

@Composable
fun StudentPermitActionSheet(
    isShowSheet: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    item: PermitByStudentEntity,
    cb: StudentPermitCallback,
) {
    val actions = StudentPermitAction
        .entries
        .filter { it.show(item.approvalStatus) }
    var showEdit by remember { mutableStateOf(false) }
    var showCancel by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }

    BottomSheet(
        isShowSheet = isShowSheet,
        onDismissRequest = onDismissRequest
    ) {
        actions.fastForEach { action ->
            ActionButton(
                label = action.label,
                icon = action.icon,
                textColor = if (action.severity == Severity.DANGER) theme.danger else theme.bodyText,
                iconTint = if (action.severity == Severity.DANGER) theme.danger else theme.textPrimary,
                onClick = {
                    when (action) {
                        StudentPermitAction.EDIT -> {
                            showEdit = true
                        }

                        StudentPermitAction.CANCEL -> {
                            showCancel = true
                        }

                        StudentPermitAction.DETAIL -> {
                            showDetail = true
                        }
                    }
                    onDismissRequest(false)
                }
            )
        }
    }

    PermitForm(
        id = item.id,
        showPermitForm = showEdit,
        onDismissRequest = { showEdit = it },
    )
    PermitDetailDialog(
        id = item.id,
        showDialog = showDetail,
        onDismissRequest = { showDetail = it }
    )
    CancelPermitAlertDialog(
        showDialog = showCancel,
        onDismissRequest = { showCancel = it },
        onConfirm = { cb.onCancelPermit(item.id) },
        item = item
    )
}