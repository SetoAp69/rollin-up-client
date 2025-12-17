package com.rollinup.rollinup.screen.dashboard.di

import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.studentdashboard.viewmodel.StudentDashboardViewmodel
import com.rollinup.rollinup.screen.main.screen.dashboard.ui.screen.teacherdashboard.viwmodel.TeacherDashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object DashboardModule {
    operator fun invoke() = module {
        viewModelOf(::StudentDashboardViewmodel)
        viewModelOf(::TeacherDashboardViewModel)
    }
}