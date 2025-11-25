package com.rollinup.rollinup.screen.main.screen.usercenter.model

import com.rollinup.apiservice.model.user.UserEntity
import com.rollinup.common.model.Severity
import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_delete_line_24
import rollin_up.composeapp.generated.resources.ic_edit_line_24
import rollin_up.composeapp.generated.resources.ic_info_line_24

enum class UserCenterAction(
    val show: (List<UserEntity>) -> Boolean = { false },
    val label: String,
    val icon: DrawableResource,
    val severity: Severity = Severity.PRIMARY,
) {
    EDIT_DATA(
        show = { selected -> selected.size == 1 },
        label = "Eedit data",
        icon = Res.drawable.ic_edit_line_24
    ),
    DETAIL(
        show = { selected -> selected.size == 1 },
        label = "Detail",
        icon = Res.drawable.ic_info_line_24,
    ),
    DELETE(
        show = { true },
        label = "Delete",
        icon = Res.drawable.ic_delete_line_24,
        severity = Severity.DANGER
    )
}