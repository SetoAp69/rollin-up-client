package com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit

import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.common.model.Severity
import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

enum class StudentPermitAction(
    val icon: DrawableResource,
    val label: String,
    val severity: Severity = Severity.PRIMARY,
    val show: (ApprovalStatus) -> Boolean = { false },
) {
    EDIT(
        icon = Res.drawable.ic_edit_line_24,
        label = "Edit permit",
        show = { status -> status == ApprovalStatus.APPROVAL_PENDING }
    ),
    CANCEL(
        icon = Res.drawable.ic_edit_line_24,
        label = "Cancel permit",
        show = { status -> status == ApprovalStatus.APPROVAL_PENDING }
    ),
    DETAIL(
        icon = Res.drawable.ic_info_line_24,
        label = "Detail",
        show = { true }
    ),
}