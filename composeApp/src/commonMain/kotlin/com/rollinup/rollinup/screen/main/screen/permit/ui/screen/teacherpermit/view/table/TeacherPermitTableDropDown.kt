package com.rollinup.rollinup.screen.main.screen.permit.ui.screen.teacherpermit.view.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.util.fastForEach
import com.rollinup.apiservice.model.permit.PermitByClassEntity
import com.rollinup.rollinup.component.dropdown.DropDownMenu
import com.rollinup.rollinup.component.dropdown.DropDownMenuItem
import com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit.TeacherPermitAction

@Composable
fun TeacherPermitDropDown(
    showDropDown: Boolean,
    isActive: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    items: List<PermitByClassEntity>,
    onDetail: (String) -> Unit,
    onApproval: (List<String>) -> Unit,
) {
    val actions = TeacherPermitAction.entries.filter { it.show(items, isActive) }
    DropDownMenu(
        isShowDropDown = showDropDown,
        onDismissRequest = onDismissRequest,
    ) {
        if (actions.isEmpty()) {
            DropDownMenuItem(
                label = "No action available",
                onClick = {}
            )
        } else {
            actions.fastForEach { action ->
                DropDownMenuItem(
                    label = action.label,
                    icon = action.icon,
                    onClick = {
                        when (action) {
                            TeacherPermitAction.APPROVAL -> {
                                val ids = items.map { it.id }
                                onApproval(ids)
                            }

                            TeacherPermitAction.DETAIL -> {
                                onDetail(items.first().id)
                            }
                        }
                    }
                )
            }
        }
    }
}