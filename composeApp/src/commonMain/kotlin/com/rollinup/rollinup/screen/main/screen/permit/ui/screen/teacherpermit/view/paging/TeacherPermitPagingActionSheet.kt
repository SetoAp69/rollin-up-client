package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.component.bottomsheet.BottomSheet
import com.rollinup.rollinup.component.button.ActionButton
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitAction
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitCallback
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitapproval.view.PermitApprovalFormBottomSheet
import com.rollinup.rollinup.screen.main.screen.permit.ui.component.permitdetail.view.PermitDetailDialog
import org.jetbrains.compose.resources.stringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.msg_no_action_available

@Composable
fun TeacherPermitPagingActionSheet(
    showSheet: Boolean,
    isActive: Boolean,
    cb: TeacherPermitCallback,
    onDismissRequest: (Boolean) -> Unit,
    items: List<PermitByClassEntity>,
) {
    val actions = TeacherPermitAction.entries.filter { it.show(items, isActive) }
    var showApproval by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }

    BottomSheet(
        isShowSheet = showSheet,
        onDismissRequest = onDismissRequest,
    ) {
        if (actions.isEmpty()) {
            DropDownMenuItem(
                label = stringResource(Res.string.msg_no_action_available),
                onClick = { onDismissRequest(false) }
            )
        } else {
            actions.fastForEach { action ->
                ActionButton(
                    label = action.label,
                    icon = action.icon,
                    onClick = {
                        when (action) {
                            TeacherPermitAction.APPROVAL -> {
                                showApproval = true
                            }

                            TeacherPermitAction.DETAIL -> {
                                showDetail = true
                            }
                        }
                        onDismissRequest(false)
                    }
                )
            }
        }
    }

    PermitApprovalFormBottomSheet(
        showDialog = showApproval,
        selectedId = items.map { it.id },
        onDismissRequest = { showApproval = false },
        onSuccess = cb.onRefresh,
    )
    items.firstOrNull()?.id?.let {
        PermitDetailDialog(
            id = it,
            showDialog = showDetail,
            onDismissRequest = { showDetail = false }
        )
    }
}