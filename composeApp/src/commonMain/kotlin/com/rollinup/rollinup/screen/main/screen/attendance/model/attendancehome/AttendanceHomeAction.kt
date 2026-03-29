package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.label_detail
import rollin_up.composeapp.generated.resources.label_attendance_by_student

enum class AttendanceHomeAction(val label: StringResource, val icon: DrawableResource) {
    HISTORY_BY_STUDENT(
        label = Res.string.label_attendance_by_student,
        icon = Res.drawable.ic_document_line_24
    ),
    DETAIL(
        label = Res.string.label_detail,
        icon = Res.drawable.ic_info_line_24
    )
}