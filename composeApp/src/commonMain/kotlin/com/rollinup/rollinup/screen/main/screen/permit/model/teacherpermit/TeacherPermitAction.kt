package com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit

import com.rollinup.apiservice.model.permit.PermitByClassEntity
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24
import rollin_up.composeapp.generated.resources.label_approval
import rollin_up.composeapp.generated.resources.label_detail

enum class TeacherPermitAction(
    val show: (List<PermitByClassEntity>, Boolean) -> Boolean,
    val label: StringResource,
    val icon: DrawableResource,
) {
    APPROVAL(
        show = { _, isActive -> isActive },
        label = Res.string.label_approval,
        icon = Res.drawable.ic_user_check_line_24
    ),
    DETAIL(
        show = { list, _ -> list.size == 1 },
        label = Res.string.label_detail,
        icon = Res.drawable.ic_info_line_24
    )
}