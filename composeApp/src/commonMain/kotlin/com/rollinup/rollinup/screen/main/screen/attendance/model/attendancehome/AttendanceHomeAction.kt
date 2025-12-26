package com.rollinup.rollinup.screen.main.screen.attendance.model.attendancehome

import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

enum class AttendanceHomeAction(val label: String, val icon: DrawableResource) {
    HISTORY_BY_STUDENT(
        label = "History by student",
        icon = Res.drawable.ic_document_line_24
    ),
    DETAIL(
        label = "Detail",
        icon = Res.drawable.ic_info_line_24
    )
}