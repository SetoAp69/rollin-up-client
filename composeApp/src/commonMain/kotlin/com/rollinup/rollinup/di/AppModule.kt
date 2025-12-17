package com.rollinup.rollinup.di

import com.rollinup.rollinup.AuthViewModel
import com.rollinup.rollinup.GlobalSettingViewModel
import com.rollinup.rollinup.SecurityViewModel
import com.rollinup.rollinup.UiModeViewModel
import com.rollinup.rollinup.component.di.ComponentModule
import com.rollinup.rollinup.component.export.ExportFileModule
import com.rollinup.rollinup.screen.auth.di.AuthModule
import com.rollinup.rollinup.screen.dashboard.di.DashboardModule
import com.rollinup.rollinup.screen.main.screen.attendance.di.AttendanceModule
import com.rollinup.rollinup.screen.main.screen.globalsetting.di.GlobalSettingModule
import com.rollinup.rollinup.screen.main.screen.permit.di.PermitModule
import com.rollinup.rollinup.screen.main.screen.studentcenter.di.StudentCenterModule
import com.rollinup.rollinup.screen.main.screen.usercenter.di.UserCenterModule
import com.rollinup.rollinup.screen.test.di.TestModule
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {
    operator fun invoke() = listOf(
        ExportFileModule(),
        AuthModule(),
        TestModule(),
        DashboardModule(),
        ComponentModule(),
        UserCenterModule(),
        PermitModule(),
        StudentCenterModule(),
        AttendanceModule(),
        GlobalSettingModule(),
        module {
            viewModelOf(::AuthViewModel)
            viewModelOf(::GlobalSettingViewModel)
            singleOf(::UiModeViewModel)
            viewModelOf(::SecurityViewModel)
        }
    )
}