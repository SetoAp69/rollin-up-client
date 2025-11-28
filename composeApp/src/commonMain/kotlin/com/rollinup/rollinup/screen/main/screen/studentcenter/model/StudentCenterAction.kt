package com.rollinup.rollinup.screen.main.screen.studentcenter.model

import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_user_check_line_24
import rollin_up.composeapp.generated.resources.ic_user_line_24

enum class StudentCenterAction(
    val icon: DrawableResource,
    val label: String,
) {
    PROFILE(
        icon = Res.drawable.ic_user_line_24,
        label = "Profile"
    ),
    ATTENDANCE(
        icon = Res.drawable.ic_user_check_line_24,
        label = "Student Attendance"
    )
}