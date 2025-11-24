package com.rollinup.rollinup.component.navigationrail

import com.rollinup.apiservice.model.common.Role
import org.jetbrains.compose.resources.DrawableResource

open class NavigationMenu(
    val route: String,
    val icon: DrawableResource,
    val title: String,
) {
    fun withRole(role: Role): String {
        return route.replace("{role}", role.value)
    }

}