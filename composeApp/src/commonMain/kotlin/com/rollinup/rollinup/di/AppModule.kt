package com.rollinup.rollinup.di

import com.rollinup.rollinup.component.di.ComponentModule
import com.rollinup.rollinup.screen.auth.di.AuthModule
import com.rollinup.rollinup.screen.dashboard.di.DashboardModule
import com.rollinup.rollinup.screen.main.screen.permit.di.PermitModule
import com.rollinup.rollinup.screen.main.screen.studentcenter.di.StudentCenterModule
import com.rollinup.rollinup.screen.main.screen.usercenter.di.UserCenterModule
import com.rollinup.rollinup.screen.test.di.TestModule

object AppModule {
    operator fun invoke() = listOf(
        AuthModule(),
        TestModule(),
        DashboardModule(),
        ComponentModule(),
        UserCenterModule(),
        PermitModule(),
        StudentCenterModule
    )
}