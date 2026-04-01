package com.rollinup.rollinup.screen.main.screen.permit.model.studentpermit

import com.rollinup.apiservice.model.permit.ApprovalStatus
import com.rollinup.common.model.Severity
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_close_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_cancel_permit
import rollin_up.composeapp.generated.resources.label_detail
import rollin_up.composeapp.generated.resources.label_edit_permit

enum class StudentPermitAction(
    val icon: DrawableResource,
    val label: StringResource,
    val severity: Severity = Severity.PRIMARY,
    val show: (ApprovalStatus) -> Boolean = { false },
) {
    EDIT(
        icon = Res.drawable.ic_edit_line_24,
        label = Res.string.label_edit_permit,
        show = { status -> status == ApprovalStatus.APPROVAL_PENDING }
    ),
    CANCEL(
        icon = Res.drawable.ic_close_line_24,
        label = Res.string.label_cancel_permit,
        show = { status -> status == ApprovalStatus.APPROVAL_PENDING }
    ),
    DETAIL(
        icon = Res.drawable.ic_info_line_24,
        label = Res.string.label_detail,
        show = { true }
    ),
}