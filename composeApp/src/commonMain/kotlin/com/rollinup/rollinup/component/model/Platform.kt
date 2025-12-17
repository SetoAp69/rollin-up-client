package com.rollinup.rollinup.component.model

import com.rollinup.apiservice.model.common.Role

enum class Platform(val supportedRole: List<Role>) {
    IOS(supportedRole = listOf(Role.TEACHER, Role.STUDENT)),
    ANDROID(supportedRole = listOf(Role.TEACHER, Role.STUDENT)),
    JVM(supportedRole = listOf(Role.TEACHER, Role.ADMIN)),
    NATIVE(emptyList()),
    WASM(emptyList())
    ;

    companion object {
        fun Platform.isMobile() = this in listOf(IOS, ANDROID)
    }
}