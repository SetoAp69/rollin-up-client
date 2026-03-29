package com.rollinup.rollinup.component.navigationrail

import com.rollinup.apiservice.model.common.Role
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

open class NavigationMenu(
    val route: String,
    val icon: DrawableResource,
    val title: StringResource,
    val filledIcon: DrawableResource,
) {
    fun withRole(role: Role): String {
        return route.replace("{role}", role.value)
    }

}