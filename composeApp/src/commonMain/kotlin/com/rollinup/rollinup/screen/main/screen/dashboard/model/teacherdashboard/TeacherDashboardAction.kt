package com.rollinup.rollinup.screen.main.screen.dashboard.model.teacherdashboard

import com.rollinup.apiservice.model.attendance.AttendanceStatus
import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_check_line_24
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24

enum class TeacherDashboardAction(
    val show: (List<AttendanceStatus?>) -> Boolean,
    val icon: DrawableResource,
    val label: String,
) {
    EDIT_DATA(
        show = { status ->
            status.size == 1 && status.first() != AttendanceStatus.APPROVAL_PENDING
        },
        label = "Edit data",
        icon = Res.drawable.ic_edit_line_24,
    ),
    APPROVAL(
        show = { status ->
            status.all { it == AttendanceStatus.APPROVAL_PENDING }
        },
        label = "Approval",
        icon = Res.drawable.ic_check_line_24
    ),
    DETAIL(
        show = { status ->
            status.size == 1
        },
        label = "Detail",
        icon = Res.drawable.ic_document_line_24,
    )
}