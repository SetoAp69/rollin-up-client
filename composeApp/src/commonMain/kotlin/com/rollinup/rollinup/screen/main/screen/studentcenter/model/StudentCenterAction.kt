package com.rollinup.rollinup.screen.main.screen.studentcenter.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_document_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24
import rollin_up.composeapp.generated.resources.label_attendance_by_student
import rollin_up.composeapp.generated.resources.label_profile

enum class StudentCenterAction(
    val icon: DrawableResource,
    val label: StringResource,
) {
    PROFILE(
        icon = Res.drawable.ic_user_line_24,
        label = Res.string.label_profile
    ),
    ATTENDANCE(
        icon = Res.drawable.ic_document_line_24,
        label = Res.string.label_attendance_by_student
    )
}