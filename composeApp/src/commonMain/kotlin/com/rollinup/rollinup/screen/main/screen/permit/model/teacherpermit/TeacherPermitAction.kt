package com.rollinup.rollinup.screen.main.screen.permit.model.teacherpermit

import com.rollinup.apiservice.model.permit.PermitByClassEntity
import org.jetbrains.compose.resources.DrawableResource
import rollin_up.composeapp.generated.resources.Res
import rollin_up.composeapp.generated.resources.ic_info_line_24
import rollin_up.composeapp.generated.resources.ic_user_check_line_24

enum class TeacherPermitAction(
    val show: (List<PermitByClassEntity>, Boolean) -> Boolean,
    val label: String,
    val icon: DrawableResource,
) {
    APPROVAL(
        show = { _, isActive -> isActive },
        label = "Approval",
        icon = Res.drawable.ic_user_check_line_24
    ),
    DETAIL(
        show = { list, _ -> list.size == 1 },
        label = "Detail",
        icon = Res.drawable.ic_info_line_24
    )
}